package com.a8.zyfc.activity;

import com.a8.zyfc.ShareCallBack;
import com.a8.zyfc.UserConfig;
import com.a8.zyfc.http.ReportAction;
import com.a8.zyfc.model.ShareBean;
import com.a8.zyfc.util.Util;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXMusicObject;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXVideoObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

public class WXBaseActivity extends Activity implements IWXAPIEventHandler{

	public static final int ACTION_TYPE_WXLOGIN = 1; //登录行为
	public static final int ACTION_TYPE_WXSHARE = 2; //分享行为

	private int actionType = 0;
	private String wxAppId = "";
	public IWXAPI api;
	private ReportAction mAction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAction = new ReportAction(this);
		init();
		actionType = getIntent().getIntExtra("actionType", 0);
		Util.sharedPreferencesSave("actionType", actionType, this);
		switch (actionType) {
		case ACTION_TYPE_WXLOGIN:
			wxLogin();
			break;
		case ACTION_TYPE_WXSHARE:
			ShareBean share = getIntent().getParcelableExtra("shareBean");
			wxShare(share);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Intent intent = new Intent();
		intent.putExtra("shareCode", BaseResp.ErrCode.ERR_OK);
		this.setResult(101, intent);
		finish();
	}

	public void init(){
		wxAppId = Util.getApplicationData(getApplicationContext(), "WX_APPID");
		api = WXAPIFactory.createWXAPI(getApplicationContext(), wxAppId, true);
		api.registerApp(wxAppId);
		api.handleIntent(getIntent(), this);
		if(!api.isWXAppInstalled()){
			Toast.makeText(this, Util.getStringId(this, "wx_app_install"), Toast.LENGTH_SHORT).show();
			mAction.report(ReportAction.WXERR, "wx_app not install", Util.getWXVersion(this));
			finish();
			return;
		}
		if(!api.isWXAppSupportAPI()){
			Toast.makeText(this, Util.getStringId(this, "wx_app_update"), Toast.LENGTH_SHORT).show();
			mAction.report(ReportAction.WXERR, "wx version too low", Util.getWXVersion(this));
			finish();
			return;
		}
	}

	/**
	 * 微信登录请求
	 */
	public void wxLogin(){
		mAction.report(ReportAction.WX, "wxlogin ready go", Util.getWXVersion(this));
		final SendAuth.Req req = new SendAuth.Req();
		req.scope = UserConfig.WXLOGIN_SCOPE;
		req.state = UserConfig.WXLOGIN_STATE;
		api.sendReq(req);

	}


	/**
	 * 微信分享请求
	 */
	public void wxShare(final ShareBean share){
		try {
			WXMediaMessage msg = new WXMediaMessage();
			switch (share.contentType) {
			case ShareBean.SHARE_CONTENT_TEXT://分享纯文本
				WXTextObject textObj = new WXTextObject();
				textObj.text = share.text;
				msg.mediaObject = textObj;
				msg.description = share.text;
				sendMsgToWX(share, msg);
				break;
				
			case ShareBean.SHARE_CONTENT_IMG://分享图片
				WXImageObject imgObj = new WXImageObject();
				imgObj.setImagePath(share.imgPath);
				msg.mediaObject = imgObj;
				sendMsgToWX(share, msg);
				break;
			case ShareBean.SHARE_CONTENT_WEB://分享网页
				WXWebpageObject webpage = new WXWebpageObject();
				webpage.webpageUrl = share.pageUrl;
				msg.mediaObject = webpage;
				//设置title,summary,thumb并发送请求
				setOtherParams(share, msg);
				break;
				
			case ShareBean.SHARE_CONTENT_AUDIO://分享音乐
				WXMusicObject musicObj = new WXMusicObject();
				musicObj.musicUrl = share.audioUrl;
				msg.mediaObject = musicObj;
				//设置title,summary,thumb并发送请求
				setOtherParams(share, msg);
				break;
				
			case ShareBean.SHARE_CONTENT_VIDEO://分享视频
				WXVideoObject videoObj = new WXVideoObject();
				videoObj.videoUrl = share.videoUrl;
				msg.mediaObject = videoObj;
				//设置title,summary,thumb并发送请求
				setOtherParams(share, msg);
				break;
			default:
				break;
			}
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private void setOtherParams(final ShareBean share, final WXMediaMessage msg) {
		msg.title = share.title;
		msg.description = share.summary;
		Util.byteArrayFromUrl(this, share.thumbUrl, new Handler(){
			@Override
			public void handleMessage(Message message) {
				msg.thumbData = (byte[])message.obj;
				sendMsgToWX(share, msg);
			}
		});
	}

	private void sendMsgToWX(final ShareBean share, final WXMediaMessage msg) {
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());//transaction字段用于唯一标识一个请求，这个必须有，否则会出错
		req.message = msg;

		if(share.type == ShareBean.SHARE_TYPE_WX){//表示分享给朋友
			req.scene = SendMessageToWX.Req.WXSceneSession;
		}else if(share.type == ShareBean.SHARE_TYPE_WXZONE){//表示分享到朋友圈
			req.scene = SendMessageToWX.Req.WXSceneTimeline;
		}
		api.sendReq(req);
	}


	@Override
	public void onReq(BaseReq req) {
		finish();
	}

	/** 
	 * 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	 */
	@Override
	public void onResp(BaseResp resp) {
		Intent intent = new Intent();
		actionType = Util.getFromSharedPreferences("actionType", this, 0);
		if(actionType == ACTION_TYPE_WXLOGIN){
			intent.setAction(UserConfig.WXLOGIN_ACTION);
			//登录回调
			if(resp.getType() == ConstantsAPI.COMMAND_SENDAUTH){
				switch (resp.errCode) {
				case BaseResp.ErrCode.ERR_OK:
					intent.putExtra("wxLoginCode", ((SendAuth.Resp) resp).code);
					break;
				case BaseResp.ErrCode.ERR_USER_CANCEL:
					intent.putExtra("wxLoginMsg", "onCancel");
					mAction.report(ReportAction.WXFAIL, "wxlogin onCancel", Util.getWXVersion(this));
					break;
				case BaseResp.ErrCode.ERR_AUTH_DENIED:
					intent.putExtra("wxLoginMsg", "onRefuse");
					mAction.report(ReportAction.WXERR, "wxlogin onRefuse", Util.getWXVersion(this));
					break;
				default:
					intent.putExtra("wxLoginMsg", "onFail");
					mAction.report(ReportAction.WXFAIL, "wxlogin other error", Util.getWXVersion(this));
					break;
				}
			}else{
				intent.putExtra("wxLoginMsg", "wx action type not COMMAND_SENDAUTH");
				mAction.report(ReportAction.WXFAIL, "wx action type not COMMAND_SENDAUTH", Util.getWXVersion(this));
			}
		}else if(actionType == ACTION_TYPE_WXSHARE){
			intent.setAction(UserConfig.WXSHARE_ACTION);
			switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				intent.putExtra("shareCode", ShareCallBack.SHARE_OK);
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				intent.putExtra("shareCode", ShareCallBack.SHARE_CANCEL);
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
			default:
				intent.putExtra("shareCode", ShareCallBack.SHARE_ERR);
				break;
			}
		}
		LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
		finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Util.sharedPreferencesSave("actionType", 0, this);
	}
}
