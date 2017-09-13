package com.a8.zyfc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.a8.zyfc.pay.center.AppPayCenter;
import com.a8.zyfc.pay.center.PaymentInfo;
import com.a8.zyfc.http.ReportAction;
import com.a8.zyfc.model.ShareBean;
import com.a8.zyfc.pay.service.AppBillService;
import com.a8.zyfc.util.Util;

public class A8SDKApi {

	public static final int TYPE_PHONE_LOGIN = 10;
	public static final int TYPE_QQ_LOGIN = 11;
	public static final int TYPE_WX_LOGIN = 12;
	public static final int TYPE_GUEST_LOGIN = 13;
	
	private static Context mContext;
	private Activity mActivity;
	private static A8SDKApi mInstance;
	
	/**
	 * A8SDK  初始化
	 * @param activity
	 * @return
	 */
	public static A8SDKApi init(Context cxt) {
		if (mInstance == null) {
			mInstance = new A8SDKApi(cxt);
		}
		return mInstance;
	}

	private A8SDKApi(Context cxt) {
		mContext = cxt.getApplicationContext();
		if (!AppConfig.INISDK) {
			new AppBillService(mContext).AppPayInit();
			AppConfig.INISDK = true;
		}
	}

	/**
	 * A8SDK  登录接口
	 * @param act
	 * @param type 
	 * @param callback
	 */
	public void A8Login(Activity act, int type, UserCallback callBack){
		mActivity = act;
		if(mContext == null){
			mContext = act.getApplicationContext();
		}
		LoginController.getInstance(mContext).login(act, type, callBack);
	}
	
	/**
	 * A8SDK  登出接口
	 */
	public void A8Logout() {
		if(mContext == null){
			mContext = mActivity.getApplicationContext();
		}
		LoginController.getInstance(mContext).logout();
	}
	
	/**
	 * A8SDK  支付接口
	 * @param mAct         当前Activity实例
	 * @param uid		         当前登录的uid
	 * @param orderNumber  cp订单号
	 * @param price        商品价格（单位：分）
	 * @param goodsId      商品Id
	 * @param goodsName    商品名称
	 * @param roleId       角色Id
	 * @param roleName     角色名称
	 * @param cpParams     cp透传参数
	 * @param payCallback  支付结果回调接口
	 */
	public void A8Pay(final Activity act, String uid, String orderNumber, String price, String goodsId,
			String goodsName, String roleId, String roleName, String cpParams,
			final PayCallback payCallback){
		mActivity = act;
		if (!Util.hasConnectedNetwork(act)) {
			Util.showToast(act, "网络不可用");
			return;
		}
//		final UserTO to = Util.getUserTO(act);
//		if (to.getUid() <= 0L) {
//			Util.showToast(act, "当前未登录");
//			return;
//		}
		PaymentInfo payInfo = new PaymentInfo();
		payInfo.price = price;
		payInfo.goodsId = goodsId;
		payInfo.goodsName = goodsName;
//		payInfo.uid = Long.toString(to.getUid());
//		payInfo.uname = to.getUserName();
		payInfo.uid = uid;
		payInfo.uname = uid;
		payInfo.roleId = roleId;
		payInfo.roleName = roleName;
		payInfo.cpOrderId = orderNumber;
		payInfo.cpParams = cpParams;
		AppPayCenter.init(act).A8Payment(payInfo, payCallback);
	}

	/**
	 * QQ好友、QZone、微信好友、微信朋友圈分享接口
	 * @param act
	 * @param share
	 * @param callBack
	 */
	public void A8Share(Activity act, ShareBean share, ShareCallBack callBack){
		mActivity = act;
		if(mContext == null){
			mContext = act.getApplicationContext();
		}
		ShareController.getInstance(mContext).share(act, share, callBack);
	}

	/**
	 * onActivityResult生命周期方法
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void onLoginActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if(mContext == null){
				mContext = mActivity.getApplicationContext();
			}
			LoginController.getInstance(mContext).onActivityResult(requestCode, resultCode, data);
		} catch (Exception e) {
			new ReportAction(mContext).report(ReportAction.QQERR, "QQ_login " + e.getLocalizedMessage(), Util.getQQVersion(mActivity));
		}
		
	}
	
	/**
	 * onActivityResult生命周期方法
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void onShareActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if(mContext == null){
				mContext = mActivity.getApplicationContext();
			}
			ShareController.getInstance(mContext).onActivityResult(requestCode, resultCode, data);
		} catch (Exception e) {
		}
		
	}


	/**
	 * onDestroy生命周期方法 
	 */
	public void onDestroy() {
		try {
			LoginController.getInstance(mContext).onDestroy();
			ShareController.getInstance(mContext).onDestroy();
			mContext = null;
			mActivity = null;
			mInstance = null;
		} catch (Exception e) {
		}
	}
}
