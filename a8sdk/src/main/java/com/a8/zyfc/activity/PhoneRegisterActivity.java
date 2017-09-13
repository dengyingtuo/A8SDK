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

import com.a8.zyfc.UserConfig;
import com.a8.zyfc.db.DatabaseUtil;
import com.a8.zyfc.http.JsonHttpResponseHandler;
import com.a8.zyfc.http.UserAction;
import com.a8.zyfc.model.UserTO;
import com.a8.zyfc.util.JSONUtils;
import com.a8.zyfc.util.Util;
import com.a8.zyfc.widget.OnLinkClickListener;
import com.a8.zyfc.widget.PhoneRegisterLayout;
import com.a8.zyfc.widget.onButtonClickListener;

public class PhoneRegisterActivity extends BaseActivity {
	private PhoneRegisterLayout mLayout;
	private Context mContext;
	private UserAction mUserCallback;
	private UserTO mUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLayout = new PhoneRegisterLayout(this);
		setContentView(mLayout);
		mContext = this;
		mUserCallback = new UserAction(mContext);
		mLayout.setOnButtonClickListener(new onButtonClickListener() {
			
			@Override
			public void onButtonClick(View v) {
				register();
			}
		});
		mLayout.setOnLinkClickListener(new OnLinkClickListener() {
			
			@Override
			public void onRightClick(View v) {
				startActivity(new Intent(PhoneRegisterActivity.this, LoginDialogActivity.class));
				finish();
			}
			
			@Override
			public void onLeftClick(View v) {
				startActivity(new Intent(PhoneRegisterActivity.this, AccountRegisterActivity.class));
				finish();
			}
		});
	}
	
	private void register() {
		String phone = mLayout.getUserName();
		final String pwd = mLayout.getPwd();
		String valCode = mLayout.getValCode();
		if (TextUtils.isEmpty(phone)) {
			Util.showToast(mContext, Util.getString(mContext, "a8_login_tips_please_enter_phone"));
			return;
		}
		if (!Util.isPhone(phone)) {
			Util.showToast(mContext, Util.getString(mContext, "a8_login_tips_please_enter_correctphone"));
			return;
		}
		if (TextUtils.isEmpty(pwd)) {
			Util.showToast(mContext, Util.getString(mContext, "a8_login_tips_please_enter_pwd"));
			return;
		}
		if ((pwd.length() < 6) || (pwd.length() > 20)) {
			Util.showToast(mContext, Util.getString(mContext, "a8_register_tips_please_enter_correct_pwd"));
			return;
		}
		if (!pwd.matches("^[^\u4e00-\u9fa5]+$")) {
			Util.showToast(mContext, Util.getString(mContext, "a8_register_tips_please_enter_correct_pwd"));
			return;
		}
		if(TextUtils.isEmpty(valCode)) {
			Util.showToast(mContext, Util.getString(mContext, "a8_login_tips_please_enter_valcode"));
			return;
		}
		
		mUserCallback.regist(phone, pwd, valCode, new JsonHttpResponseHandler(){

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					if(response.getInt(UserAction.CODE) == UserAction.RESULT_OK){
						Util.showToast(mContext, Util.getString(mContext, "a8_register_tips_register_success"));
				        mUser = JSONUtils.fromJson(response.getString(UserAction.MSG), UserTO.class);
				        mUser.setPassword(pwd);
				        requestLogin();
					} else {
						if (!TextUtils.isEmpty(response.getString(UserAction.DESC))) {
							 Util.showToast(mContext, response.getString(UserAction.DESC));
						 } else {
							 Util.showToast(mContext, Util.getString(mContext, "a8_register_tips_register_fail"));
						 }
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Util.showToast(mContext, Util.getString(mContext, "a8_register_tips_register_fail"));
			}	
		});
	}
	
	private void requestLogin() {
		mUserCallback.login(mUser.getUserName(), mUser.getPassword(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					String pwd = mUser.getPassword();
					if (response.getInt(UserAction.CODE) == UserAction.RESULT_OK ) {
						mUser = JSONUtils.fromJson(response.getString(UserAction.MSG), UserTO.class);   
				        mUser.setBindMobile(true);
				        mUser.setPassword(pwd);
				        onLoginSucceed();
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
	
	private void onLoginSucceed() {
//		Util.sharedPreferencesSave("a8_lastLoginUser", null, mContext);  //登录成功之后，把这个值清除
		mUser.setLastLoginTime(System.currentTimeMillis());
		mUser.setLastTipTime(System.currentTimeMillis());
		Util.saveUser(mContext, mUser);
		DatabaseUtil.getInstance(mContext).saveUser(mUser);
		onLogined();
	}

	private void onLogined() {
		Intent intent = new Intent(UserConfig.ACTION);
		intent.putExtra(UserConfig.UID, mUser.getUid());
		intent.putExtra(UserConfig.UNAME, mUser.getUserName());
		intent.putExtra(UserConfig.TOKEN, mUser.getToken());
		LocalBroadcastManager.getInstance(mContext.getApplicationContext()).sendBroadcast(intent);
		finish();
	}
}
