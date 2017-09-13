package com.a8.zyfc.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.a8.zyfc.http.JsonHttpResponseHandler;
import com.a8.zyfc.http.UserAction;
import com.a8.zyfc.model.UserTO;
import com.a8.zyfc.util.Util;
import com.a8.zyfc.widget.ResetPwdLayout;
import com.a8.zyfc.widget.onButtonClickListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

public class ResetPwdActivity extends BaseActivity {
	private ResetPwdLayout mLayout;
	private UserTO mUser;
	private UserAction mUserCallback;
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLayout = new ResetPwdLayout(this);
		mUserCallback = new UserAction(this);
		mContext = this;
		setContentView(mLayout);
		mUser = getIntent().getParcelableExtra("user");
		mLayout.setOnButtonClickListener(new onButtonClickListener() {
			
			@Override
			public void onButtonClick(View v) {
				final String password = mLayout.getNewPassword();
				if (TextUtils.isEmpty(password)) {
					Util.showToast(mContext, Util.getString(mContext, "a8_login_tips_please_enter_pwd"));
					return;
				}
				if ((password.length() < 6) || (password.length() > 20)) {
					Util.showToast(mContext, Util.getString(mContext, "a8_register_tips_please_enter_correct_pwd"));
					return;
				}
				if (!password.matches("^[^\u4e00-\u9fa5]+$")) {
					Util.showToast(mContext, Util.getString(mContext, "a8_register_tips_please_enter-correct_pwd"));
					return;
				}
				mUserCallback.resetPassword(mUser.getUserName(), password, mUser.getAuthCode(), new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
						super.onSuccess(statusCode, headers, response);
						try {
							if(response.getInt(UserAction.CODE) == UserAction.RESULT_OK){
								Util.showToast(mContext, Util.getString(mContext, "a8_resetpwd_tips_reset_success"));
								mUser.setPassword(password);
								startActivity(new Intent(mContext, LoginDialogActivity.class));
								finish();
							} else {
								if (!TextUtils.isEmpty(response.getString(UserAction.DESC))) {
									 Util.showToast(mContext, response.getString(UserAction.DESC));
								 } else {
									 Util.showToast(mContext, Util.getString(mContext, "a8_resetpwd_tips_reset_fail"));
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
						 Util.showToast(mContext, Util.getString(mContext, "a8_resetpwd_tips_reset_fail"));
					}	
				});
			}
		});
	}
	
}
