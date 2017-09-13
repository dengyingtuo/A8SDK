package com.a8.zyfc.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.a8.zyfc.http.JsonHttpResponseHandler;
import com.a8.zyfc.db.DatabaseUtil;
import com.a8.zyfc.http.UserAction;
import com.a8.zyfc.model.UserTO;
import com.a8.zyfc.util.Util;
import com.a8.zyfc.widget.GuestBindAccountLayout;
import com.a8.zyfc.widget.OnLinkClickListener;
import com.a8.zyfc.widget.onButtonClickListener;
/**
 * 游客绑定（注册）A8账号Activity
 * @author Chooper
 *
 */
public class GuestBindAccountActivity extends BaseActivity {
	
	private GuestBindAccountLayout mLayout;
	private Context mContext;
	private UserAction mUserCallback;
	private UserTO mUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLayout = new GuestBindAccountLayout(this);
		setContentView(mLayout);
		mContext = this;	
		mUser = getIntent().getParcelableExtra("user");
		mUserCallback = new UserAction(mContext);
		
		mLayout.setOnButtonClickListener(new onButtonClickListener() {
			
			@Override
			public void onButtonClick(View v) {
				bindAccount();
			}
		});
		mLayout.setOnLinkClickListener(new OnLinkClickListener() {
			
			@Override
			public void onRightClick(View v) {
				finish();
			}
			
			@Override
			public void onLeftClick(View v) {
				Intent intent = new Intent(GuestBindAccountActivity.this, GuestBindPhoneActivity.class);
				intent.putExtra("actName", GuestBindAccountActivity.class.getSimpleName());
				intent.putExtra("user", mUser);
				startActivity(intent);
				finish();
			}
		});
	}
	/**
	 * 绑定A8账号
	 */
	private void bindAccount(){
		final String user = mLayout.getUserName();
		final String pwd = mLayout.getPwd();
		if (TextUtils.isEmpty(user)) {
			Util.showToast(mContext, Util.getString(mContext, "a8_login_tips_please_enter_username"));
			return;
		}
		if (user.length() < 6 || user.length() > 20) {
			Util.showToast(mContext, Util.getString(mContext, "a8_register_tips_please_enter_correct_uname"));
			return;
		}
		if (!user.matches("^[A-Za-z0-9_]{2,20}$")) {
			Util.showToastLong(mContext, Util.getString(mContext, "a8_register_tips_please_enter_correct_username"));
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
			Util.showToast(mContext, Util.getString(mContext, "a8_register_tips_please_enter-correct_pwd"));
			return;
		}
		if (!Util.checkNet(mContext)) {
			return;
		}
		//TODO 绑定账号接口
		mUserCallback.guestBindAccount(mUser.getUid(), user, pwd, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					if (response.getInt(UserAction.CODE) == UserAction.RESULT_OK) {
						mUser.setUserName(user);
						mUser.setPassword(pwd);
						mUser.setFast(false);
						mUser.setBindMobile(false);
						mUser.setThirdType(10);
						
						Util.sharedPreferencesSave("a8_lastLoginUser", null, mContext);  //登录成功之后，把这个值清除
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
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Util.showToast(mContext, Util.getString(mContext, "a8_bindphone_binding_fail"));
			}
		});
	}
}
