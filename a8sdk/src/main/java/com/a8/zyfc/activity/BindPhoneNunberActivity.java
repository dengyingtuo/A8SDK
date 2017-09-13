package com.a8.zyfc.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.a8.zyfc.widget.BindPhoneNumberLayout;
import com.a8.zyfc.widget.OnLinkClickListener;
import com.a8.zyfc.widget.onButtonClickListener;

public class BindPhoneNunberActivity extends BaseActivity {
	
	private BindPhoneNumberLayout mLayout;
	private UserTO mUser;
	private UserAction mUserCallback;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUser = getIntent().getParcelableExtra("user");
		mUserCallback = new UserAction(mContext);
		mLayout = new BindPhoneNumberLayout(this, mUser);
		setContentView(mLayout);
		
		mLayout.setOnButtonClickListener(new onButtonClickListener() {
			
			@Override
			public void onButtonClick(View v) {
				bindPhone();
			}
		});
	
		mLayout.setOnLinkClickListener(new OnLinkClickListener() {
			
			@Override
			public void onRightClick(View v) {
				String actName = BindPhoneNunberActivity.this.getIntent().getStringExtra("actName");
				if(actName.equals(AccountRegisterActivity.class.getSimpleName())){
					requestLogin();
				}else{
					onLoginSucceed();
				}
			}
			
			@Override
			public void onLeftClick(View v) {
				
			}
		});
	}
	
	private void bindPhone() {
		final String phone = mLayout.getPhone();
		String valCode = mLayout.getValCode();
		if (TextUtils.isEmpty(phone)) {
			Util.showToast(mContext, Util.getString(mContext, "a8_login_tips_please_enter_phone"));
			return;
		}
		if (!Util.isPhone(phone)) {
			Util.showToast(mContext, Util.getString(mContext, "a8_login_tips_please_enter_correctphone"));
			return;
		}
		if(TextUtils.isEmpty(valCode)) {
			Util.showToast(mContext, Util.getString(mContext, "a8_login_tips_please_enter_valcode"));
			return;
		}
		mUserCallback.verifyCode(mUser.getToken(), phone, valCode, UserConfig.FLAG_BIND_MOBILE, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					if (response.getInt(UserAction.CODE) == UserAction.RESULT_OK) {
						Util.showToast(mContext, Util.getString(mContext, "a8_bindphone_binding_success"));
						mUser.setMobile(phone);
						mUser.setBindMobile(true);
						onLoginSucceed();
					} else {
						if (!TextUtils.isEmpty(response.getString(UserAction.DESC))) {
							 Util.showToast(mContext, response.getString(UserAction.DESC));
						 } else {
							 Util.showToast(mContext, Util.getString(mContext, "a8_bindphone_binding_fail"));
						 }
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Util.showToast(mContext, Util.getString(mContext, "a8_bindphone_binding_fail"));
			}
		});
	}
	
	public void requestLogin() {
		mUserCallback.login(mUser.getUserName(), mUser.getPassword(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					String pwd = mUser.getPassword();
					if (response.getInt(UserAction.CODE) != UserAction.RESULT_FAIL ) {
						mUser = JSONUtils.fromJson(response.getString(UserAction.MSG), UserTO.class);   
						mUser.setBindMobile(false);
				        mUser.setPassword(pwd);
				        mUser.setThirdType(10);
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
