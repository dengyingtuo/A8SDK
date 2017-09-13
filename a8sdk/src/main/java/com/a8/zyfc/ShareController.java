package com.a8.zyfc;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.a8.zyfc.activity.WXBaseActivity;
import com.a8.zyfc.model.ShareBean;
import com.a8.zyfc.util.Util;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * Created by Chooper on 2017/5/27.
 */

public class ShareController {

	public static final String TAG = "A8_SHARE";

	private Context mContext;
	private Activity mActivity;
	private Tencent mTencent;
	private static ShareController mInstance;
	private ShareBean share;
	private ShareCallBack shareCallBack;
	private WXShareBroadcastReceiver receiver;

	public static ShareController getInstance(Context mContext){
		if(mInstance == null){
			mInstance = new ShareController(mContext);
		}
		return mInstance;
	}

	private ShareController(Context mContext){
		this.mContext = mContext;
		if (mTencent == null) {
			mTencent = Tencent.createInstance(Util.getApplicationData(mContext, "QQ_APPID"), mContext);
		}
		receiver = new WXShareBroadcastReceiver();
		LocalBroadcastManager.getInstance(mContext).registerReceiver(receiver, new IntentFilter(UserConfig.WXSHARE_ACTION));
	}

	public void share(Activity act, ShareBean share, ShareCallBack callBack) {
		mActivity = act;
		this.share = share;
		shareCallBack = callBack;
		switch (share.type){
		case ShareBean.SHARE_TYPE_WX:
		case ShareBean.SHARE_TYPE_WXZONE:
			shareByWX();
			break;
		case ShareBean.SHARE_TYPE_QQ:
		case ShareBean.SHARE_TYPE_QZONE:
			shareByQQ();
			break;
		}
	}

	private void shareByQQ() {
		try {
			Log.d(TAG, "shareInfo:"+share.toString());
			if(!Util.isQQClientAvailable(mActivity)){
				Util.showToast(mActivity, "QQ未安装");
				shareCallBack.onCallBack(ShareCallBack.SHARE_ERR);
			}else{
				final Bundle params = new Bundle();
				switch (share.contentType) {
				case ShareBean.SHARE_CONTENT_IMG:
					params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
					params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, share.imgPath);
					break;
				case ShareBean.SHARE_CONTENT_WEB:
					params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
					params.putString(QQShare.SHARE_TO_QQ_TITLE, share.title);
					params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, share.thumbUrl);
					params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, share.pageUrl);
					break;
				case ShareBean.SHARE_CONTENT_AUDIO:
					params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
					params.putString(QQShare.SHARE_TO_QQ_TITLE, share.title);
					params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, share.audioUrl);
					params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, share.pageUrl);
					break;
				default:
					break;
				}

				params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  share.summary);
				params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  share.appName);

				if(share.type == ShareBean.SHARE_TYPE_QQ){
					params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,  QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
				}else if(share.type == ShareBean.SHARE_TYPE_QZONE){
					params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,  QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
				}

				mTencent.shareToQQ(mActivity, params, myQQShareListener);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private void shareByWX() {
		try {
			Intent intent = new Intent(mActivity, Class.forName(mActivity.getApplicationContext().getPackageName()+".wxapi.WXEntryActivity"));
			intent.putExtra("actionType", WXBaseActivity.ACTION_TYPE_WXSHARE);
			intent.putExtra("shareBean", share);
			mActivity.startActivityForResult(intent, 101);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		if(share.type == ShareBean.SHARE_TYPE_QQ || share.type == ShareBean.SHARE_TYPE_QZONE)
			Tencent.onActivityResultData(requestCode, resultCode, data, myQQShareListener);
	}

	private IUiListener myQQShareListener = new IUiListener() {
		@Override
		public void onComplete(Object o) {
			Log.d(TAG, "QQ分享成功");
			shareCallBack.onCallBack(ShareCallBack.SHARE_OK);
		}

		@Override
		public void onError(UiError uiError) {
			Log.d(TAG, "QQ分享出错:errorDetail=" + uiError.errorDetail + "\nerrorMessage=" + uiError.errorMessage);
			shareCallBack.onCallBack(ShareCallBack.SHARE_ERR);
		}

		@Override
		public void onCancel() {
			Log.d(TAG, "QQ分享取消");
			shareCallBack.onCallBack(ShareCallBack.SHARE_CANCEL);
		}
	};
	
	private class WXShareBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(UserConfig.WXSHARE_ACTION.equals(intent.getAction())){
				shareCallBack.onCallBack(intent.getIntExtra("shareCode", ShareCallBack.SHARE_OK));//0:默认成功
			}else{
				shareCallBack.onCallBack(intent.getIntExtra("shareCode", ShareCallBack.SHARE_ERR));//0:默认成功
			}
		}

	}

	public void onDestroy(){
		if(receiver != null)
			LocalBroadcastManager.getInstance(mContext.getApplicationContext()).unregisterReceiver(receiver);
		share = null;
		mTencent = null;
		if(mInstance != null)
			mInstance = null;
	}

}
