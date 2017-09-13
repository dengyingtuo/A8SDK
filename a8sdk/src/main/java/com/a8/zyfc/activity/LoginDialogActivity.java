package com.a8.zyfc.activity;


import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;

import com.a8.zyfc.http.JsonHttpResponseHandler;
import com.a8.zyfc.UserConfig;
import com.a8.zyfc.db.DatabaseUtil;
import com.a8.zyfc.http.UserAction;
import com.a8.zyfc.model.UserTO;
import com.a8.zyfc.util.JSONUtils;
import com.a8.zyfc.util.Util;
import com.a8.zyfc.widget.LoginLayout;
import com.a8.zyfc.widget.LoginLayout.onButtonsClickListener;

public class LoginDialogActivity extends BaseActivity {
	private LoginLayout mLoginLayout;
	private Context mContext;
	private UserAction mUserCallback;
	private UserTO mUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;	
		mUserCallback = new UserAction(mContext);
		initLoginLayout();
		setContentView(mLoginLayout);
	}

	private void initLoginLayout() {
		mLoginLayout = new LoginLayout(this);
		mLoginLayout.setOnButtonsClickListener(new onButtonsClickListener() {

			@Override
			public void onRegisterClick(View v) {
				startActivity(new Intent(LoginDialogActivity.this, PhoneRegisterActivity.class));
				finish();
			}

			@Override
			public void onEnterClick(View v) {
				final String user = mLoginLayout.getUser();
				String pwd = mLoginLayout.getPassword();
				if(TextUtils.isEmpty(user)) {
					Util.showToast(mContext, Util.getString(mContext, "a8_login_tips_please_enter_username"));
					return;
				}
				if(user.length() < 6 || user.length() > 50) {
					Util.showToast(mContext, Util.getString(mContext,"a8_register_tips_please_enter_correct_uname"));
					return;
				}
				if(TextUtils.isEmpty(pwd)) {
					Util.showToast(mContext, Util.getString(mContext, "a8_login_tips_please_enter_pwd"));
					return;
				}
				if ((pwd.length() < 6) || (pwd.length() > 20)) {
					Util.showToast(mContext, Util.getString(mContext, "a8_login_tips_please_enter_correctpwd"));
					return;
				}
				requestLogin(user, pwd);
			}

			@Override
			public void onLinkClick(View v) {
				startActivity(new Intent(LoginDialogActivity.this, FindPwdActivity.class));
				finish();
			}

			@Override
			public void onBackClick(View v) {
				finish();
			}
		});
	}

	private void requestLogin(String user, final String pwd) {
		mUserCallback.login(user, pwd, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					if (response.getInt(UserAction.CODE) == UserAction.RESULT_OK ) {
						mUser = JSONUtils.fromJson(response.getString(UserAction.MSG), UserTO.class);   
						mUser.setPassword(pwd);
						mUser.setBindMobile(true);
						mUser.setThirdType(10);
						onLoginSucceed(mUser);
						finish();
					} else if(response.getInt(UserAction.CODE) == UserAction.RESULT_99) {
						mUser = JSONUtils.fromJson(response.getString(UserAction.MSG), UserTO.class);   
						mUser.setPassword(pwd);
						mUser.setBindMobile(false);
						mUser.setThirdType(10);
						//获取本地存储的用户信息
						UserTO user = DatabaseUtil.getInstance(mContext).getUserByUid(mUser.getUid());
						if(user == null || (System.currentTimeMillis() - user.getLastTipTime()) > 7 * 24 * 60 * 60 * 1000) {
							Intent intent = new Intent(mContext, BindPhoneNunberActivity.class);
							intent.putExtra("actName", LoginDialogActivity.class.getSimpleName());
							intent.putExtra("user", mUser);
							startActivity(intent);
							finish();
						} else {
							onLoginSucceed(mUser);
							finish();
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
//		Util.sharedPreferencesSave("a8_lastLoginUser", null, mContext);  //登录成功之后，把这个值清除
		user.setLastLoginTime(System.currentTimeMillis());
		Util.saveUser(mContext, user);
		DatabaseUtil.getInstance(mContext).saveUser(user);
		onLogined();
	}

	private void onLogined() {
		Intent intent = new Intent(UserConfig.ACTION);
		intent.putExtra(UserConfig.UID, mUser.getUid());
		intent.putExtra(UserConfig.UNAME, mUser.getUserName());
		intent.putExtra(UserConfig.TOKEN, mUser.getToken());
		LocalBroadcastManager.getInstance(mContext.getApplicationContext()).sendBroadcast(intent);
	}
}
