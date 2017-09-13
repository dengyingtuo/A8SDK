package com.a8.zyfc;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.a8.zyfc.activity.GuestBindPhoneActivity;
import com.a8.zyfc.activity.LoginDialogActivity;
import com.a8.zyfc.activity.WXBaseActivity;
import com.a8.zyfc.db.DatabaseUtil;
import com.a8.zyfc.http.JsonHttpResponseHandler;
import com.a8.zyfc.http.ReportAction;
import com.a8.zyfc.http.UserAction;
import com.a8.zyfc.model.UserTO;
import com.a8.zyfc.util.JSONUtils;
import com.a8.zyfc.util.Util;
import com.a8.zyfc.widget.GuestPromptDialog;
import com.a8.zyfc.widget.GuestPromptDialog.Builder;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class LoginController {

	private static final String TAG = "A8_LOGIN";

	private static final int TYPE_PHONE_LOGIN = 10;
	private static final int TYPE_QQ_LOGIN = 11;
	private static final int TYPE_WX_LOGIN = 12;
	private static final int TYPE_GUEST_LOGIN = 13;

	private Context mContext;
	private Activity mActivity;
	private static LoginController mInstance;
	private Tencent mTencent;
	private UserAction mUserAction;
	private ReportAction mAction;
	private UserTO mUser;
	private UserCallback mCallback;
	private AccountBroadcastReceiver receiver;

	public static LoginController getInstance(Context mContext){
		if (mInstance == null) {
			mInstance = new LoginController(mContext);
		}
		return mInstance;
	}

	private LoginController(Context mContext){
		this.mContext = mContext;
		if (mTencent == null) {
			mTencent = Tencent.createInstance(Util.getApplicationData(mContext, "QQ_APPID"), mContext);
		}
		mUserAction = new UserAction(mContext);
		mAction = new ReportAction(mContext);
		receiver = new AccountBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(UserConfig.ACTION);
		filter.addAction(UserConfig.WXLOGIN_ACTION);
		LocalBroadcastManager.getInstance(mContext).registerReceiver(receiver, filter);
	}

	public void login(Activity act, int type, UserCallback callBack){
		mActivity = act;
		mCallback = callBack;
		switch (type){
		case TYPE_PHONE_LOGIN:
			mActivity.startActivity(new Intent(mActivity, LoginDialogActivity.class));
			break;
		case TYPE_QQ_LOGIN:
			qqLogin();
			break;
		case TYPE_WX_LOGIN:
			wxLogin();
			break;
		case TYPE_GUEST_LOGIN:
			tryPlay();
			break;
		default:
			break;
		}
	}

	private void qqLogin(){
		mAction.report(ReportAction.QQ, "qqlogin ready go", Util.getQQVersion(mActivity));
		if (!mTencent.isSessionValid()) {
			mTencent.login(mActivity, UserConfig.QQLOGIN_SCOPE, qqLoginListener);
		} else {
			thirdLoginReq(TYPE_QQ_LOGIN, mTencent.getOpenId(), mTencent.getAccessToken());
		}
	}

	private IUiListener qqLoginListener = new IUiListener() {
		@Override
		public void onComplete(Object response) {
			try {
				JSONObject jsonResponse = (JSONObject) response;
				if (null == response || (null != jsonResponse && jsonResponse.length() == 0)) {
					mCallback.onFail("QQ_login response null");
					mAction.report(ReportAction.QQFAIL, "QQ_login response null", Util.getQQVersion(mActivity));
					return;
				}

				if(jsonResponse.has("ret") && jsonResponse.getInt("ret")==0){
					String token = jsonResponse.getString(Constants.PARAM_ACCESS_TOKEN);
					String expires = jsonResponse.getString(Constants.PARAM_EXPIRES_IN);
					String openId = jsonResponse.getString(Constants.PARAM_OPEN_ID);
					if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
						mTencent.setAccessToken(token, expires);
						mTencent.setOpenId(openId);
					}
					thirdLoginReq(TYPE_QQ_LOGIN, mTencent.getOpenId(), mTencent.getAccessToken());
					mAction.report(ReportAction.QQSUC, mTencent.getOpenId(), Util.getQQVersion(mActivity));
				}else{
					mCallback.onFail("QQ_login "+jsonResponse.getString("msg"));
					mAction.report(ReportAction.QQFAIL, "QQ_login "+jsonResponse.getString("msg"), Util.getQQVersion(mActivity));
				}

			} catch(Exception e) {
				e.printStackTrace();
				mCallback.onFail("QQ_login " + e.getCause().getMessage());
				mAction.report(ReportAction.QQFAIL, "QQ_login " + e.getLocalizedMessage(), Util.getQQVersion(mActivity));
			}
		}

		@Override
		public void onError(UiError uiError) {
			mCallback.onFail("QQ_login "+uiError.errorMessage);
			mAction.report(ReportAction.QQERR, "QQ_login "+uiError.errorMessage, Util.getQQVersion(mActivity));
		}

		@Override
		public void onCancel() {
			mCallback.onFail("QQ_login onCancel");
			mAction.report(ReportAction.QQFAIL, "QQ_login onCancel", Util.getQQVersion(mActivity));
		}
	};

	private void wxLogin(){
		try {
			Intent intent = new Intent(mActivity, Class.forName(mActivity.getApplicationContext().getPackageName()+".wxapi.WXEntryActivity"));
			intent.putExtra("actionType", WXBaseActivity.ACTION_TYPE_WXLOGIN);
			mActivity.startActivityForResult(intent, 100);
		} catch (ClassNotFoundException e) {
			mCallback.onFail("WX_login " + e.getLocalizedMessage());
			mAction.report(ReportAction.WXERR, "WX_login " + e.getLocalizedMessage(), Util.getWXVersion(mActivity));
			e.printStackTrace();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		try {
//			if(requestCode == 11101 && resultCode == Activity.RESULT_OK)
				//处理返回的数据
//	        	Tencent.handleResultData(intent, qqLoginListener);
			Tencent.onActivityResultData(requestCode, resultCode, intent, qqLoginListener);
		} catch (Exception e) {
			mAction.report(ReportAction.QQERR, "QQ_login " + e.getLocalizedMessage(), Util.getQQVersion(mActivity));
			e.printStackTrace();
		}
		
	}

	private void thirdLoginReq(final int type, String openId, String thirdToken){
		Log.d(TAG, "type:"+type+",openId:"+openId+",thirdToken:"+thirdToken);
		mUserAction.thirdLogin(type, openId, thirdToken, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					if (response.getInt(UserAction.CODE) == UserAction.RESULT_OK ) {
						mUser = JSONUtils.fromJson(response.getString(UserAction.MSG), UserTO.class);
						mUser.setBindMobile(true);
						mUser.setThirdType(type);
						onLoginSucceed(mUser);
					} else if(response.getInt(UserAction.CODE) == UserAction.RESULT_99) {
						mUser = JSONUtils.fromJson(response.getString(UserAction.MSG), UserTO.class);
						mUser.setBindMobile(false);
						mUser.setThirdType(type);
						onLoginSucceed(mUser);
					}else if(response.getInt(UserAction.CODE) == UserAction.RESULT_98){
						if(type == TYPE_QQ_LOGIN){
							qqLogin();
						}else if(type == TYPE_WX_LOGIN){
							wxLogin();
						}
					} else {
						if (!TextUtils.isEmpty(response.getString(UserAction.DESC))) {
							Util.showToast(mActivity, response.getString(UserAction.DESC));
							mCallback.onFail("third_login " + response.getString(UserAction.DESC));
						} else {
							mCallback.onFail("third_login fail");
						}
						mAction.report("third_login fail", "server error", "");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable,errorResponse);
				mCallback.onFail("third_login network error: " + throwable.getMessage());
				mAction.report("third_login fail", "server error", "");
			}
		});
	}
	
	
	private Builder builder;
	private GuestPromptDialog dialog;
	private void tryPlay(){
		mUserAction.fastRegist(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					if (response.getInt(UserAction.CODE) == UserAction.RESULT_OK ) {
						mUser = JSONUtils.fromJson(response.getString(UserAction.MSG), UserTO.class);
						mUser.setFast(true);
						mUser.setThirdType(TYPE_GUEST_LOGIN);
						//获取本地存储的用户信息
						UserTO user = DatabaseUtil.getInstance(mContext).getUserByUid(mUser.getUid());
						if(user == null || (System.currentTimeMillis() - user.getLastTipTime()) > 7 * 24 * 60 * 60 * 1000) {
							mUser.setLastTipTime(System.currentTimeMillis());
							onLoginSucceed(mUser);
							builder = new Builder(mContext);
							builder.setTitle(mContext.getString(Util.getStringId(mContext, "a8_guest_prompt")));
							builder.setMsg(mContext.getString(Util.getStringId(mContext, "a8_fast_login_warn")));
							builder.setPositiveButton(mContext.getString(Util.getStringId(mContext, "a8_fast_login_comfirm")),
									new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									Intent intent = new Intent(mActivity, GuestBindPhoneActivity.class);
									intent.putExtra("user", mUser);
									mActivity.startActivity(intent);
								}
							});
							builder.setNegativeButton(mContext.getString(Util.getStringId(mContext, "a8_fast_login_cancle")),
									new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
							dialog = builder.create();
							dialog.setCancelable(false);
							dialog.setCanceledOnTouchOutside(false);
							dialog.show();
						} else {
							onLoginSucceed(mUser);
						}
					} else {
						if (!TextUtils.isEmpty(response.getString(UserAction.DESC))) {
							Util.showToast(mContext, response.getString(UserAction.DESC));
						} else {
							Util.showToast(mContext, Util.getString(mContext, "a8_login_tips_loginfail"));
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable,errorResponse);
				Util.showToast(mContext, Util.getString(mContext, "a8_login_tips_loginfail"));
			}
		});
	}

	private void onLoginSucceed(UserTO user) {
//		Util.sharedPreferencesSave("a8_lastLoginUser", null, mActivity);  //登录成功之后，把这个值清除
		user.setLastLoginTime(System.currentTimeMillis());
		if(user != null){
			Util.saveUser(mActivity, user);
			DatabaseUtil.getInstance(mActivity).saveUser(user);
		}
		mCallback.onSuccess(mUser.getUid(), mUser.getToken(), mUser.getUserName());
	}

	private class AccountBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(UserConfig.WXLOGIN_ACTION.equals(intent.getAction())){
				String wxLoginCode = intent.getStringExtra("wxLoginCode");
				if(null != wxLoginCode && !"".equals(wxLoginCode)){
					thirdLoginReq(TYPE_WX_LOGIN, "", wxLoginCode);
					mAction.report(ReportAction.WXSUC, wxLoginCode, Util.getWXVersion(mActivity));
				}else{
					mCallback.onFail("WX_login " + intent.getStringExtra("wxLoginMsg"));
				}
			}else if(UserConfig.ACTION.equals(intent.getAction())){
				long uId = intent.getLongExtra(UserConfig.UID, -1);
				String uName = intent.getStringExtra(UserConfig.UNAME);
				String token = intent.getStringExtra(UserConfig.TOKEN);
				mCallback.onSuccess(uId, token, uName);
			}
		}

	}
	public void logout(){
		if (!Util.hasConnectedNetwork(mContext)) {
			Util.showToast(mContext, "网络不可用");
			return;
		}
		UserTO to = Util.getUserTO(mContext);
		if (to.getUid() <= 0L) {
			Util.showToast(mContext, "当前未登录");
			return;
		}
		if(to.getThirdType() == 11){
			mTencent.logout(mContext);
		}
		new UserAction(mContext).logout(to.getUid(), to.getToken(),
				new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				Util.showToast(mContext, "注销成功");
				Util.cleanUserTO(mContext);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

		});
	}

	protected void onDestroy() {
		if(receiver != null)
			LocalBroadcastManager.getInstance(mContext).unregisterReceiver(receiver);
		mTencent = null;
		mUserAction = null;
		mInstance = null;
	}

}
