package com.a8.zyfc.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.a8.zyfc.http.JsonHttpResponseHandler;
import com.a8.zyfc.db.DatabaseUtil;
import com.a8.zyfc.http.UserAction;
import com.a8.zyfc.model.UserTO;
import com.a8.zyfc.util.Util;
import com.a8.zyfc.widget.GuestBindPhoneLayout;
import com.a8.zyfc.widget.OnLinkClickListener;
import com.a8.zyfc.widget.onButtonClickListener;
/**
 * 游客绑定手机号Activity
 * @author Chooper
 *
 */
public class GuestBindPhoneActivity extends BaseActivity {
	private GuestBindPhoneLayout mLayout;
	private Context mContext;
	private UserAction mUserCallback;
	private UserTO mUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUser = getIntent().getParcelableExtra("user");
		mLayout = new GuestBindPhoneLayout(this);
		setContentView(mLayout);
		mContext = this;
		mUserCallback = new UserAction(mContext);
		mLayout.setOnButtonClickListener(new onButtonClickListener() {
			
			@Override
			public void onButtonClick(View v) {
				bindPhone();
			}
		});
		mLayout.setOnLinkClickListener(new OnLinkClickListener() {
			
			@Override
			public void onRightClick(View v) {
				finish();
			}
			
			@Override
			public void onLeftClick(View v) {
				Intent intent = new Intent(GuestBindPhoneActivity.this, GuestBindAccountActivity.class);
				intent.putExtra("actName", GuestBindPhoneActivity.class.getSimpleName());
				intent.putExtra("user", mUser);
				startActivity(intent);
				finish();
			}
		});
	}
	/**
	 * 绑定手机号
	 */
	public void bindPhone() {
		final String phone = mLayout.getUserName();
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
		mUserCallback.guestBindPhone(mUser.getUid(), phone, pwd, valCode, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				Log.i("a8sdk", "Flag=23:"+response.toString());
				try {
					if (response.getInt(UserAction.CODE) == UserAction.RESULT_OK) {
						mUser.setUserName(phone);
						mUser.setPassword(pwd);
						mUser.setFast(false);
						mUser.setBindMobile(true);
						mUser.setMobile(phone);
						mUser.setThirdType(10);
						
//						Util.sharedPreferencesSave("a8_lastLoginUser", null, mContext);  //登录成功之后，把这个值清除
						Util.saveUser(mContext, mUser);
						DatabaseUtil.getInstance(mContext).saveUser(mUser);
						Util.showToast(mContext, Util.getString(mContext, "a8_bindphone_binding_success"));
						finish();
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
}
