package com.a8.zyfc.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.a8.zyfc.UserConfig;
import com.a8.zyfc.db.DatabaseUtil;
import com.a8.zyfc.http.JsonHttpResponseHandler;
import com.a8.zyfc.http.UserAction;
import com.a8.zyfc.model.UserTO;
import com.a8.zyfc.util.Util;
import com.a8.zyfc.widget.AuthenticationLayout;
import com.a8.zyfc.widget.LoginedView;
import com.a8.zyfc.widget.OnLinkClickListener;
import com.a8.zyfc.widget.onButtonClickListener;
/**
 * 实名认证界面
 * @author Chooper
 *
 */
public class AuthenticationActivity extends BaseActivity {
	
	private AuthenticationLayout mLayout;
	private UserTO mUser;
	private UserAction mUserAction;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUser = getIntent().getParcelableExtra("user");
		mUserAction = new UserAction(mContext);
		mLayout = new AuthenticationLayout(this);
		setContentView(mLayout);
		mLayout.setOnButtonClickListener(new onButtonClickListener() {
			
			@Override
			public void onButtonClick(View v) {
				authentication();
			}
		});
	
		mLayout.setOnLinkClickListener(new OnLinkClickListener() {
			
			@Override
			public void onRightClick(View v) {
				onLoginSucceed();
			}
			
			@Override
			public void onLeftClick(View v) {
				
			}
		});
	}
	
	private void authentication(){
		String realName = mLayout.getRealName();
		String idCard = mLayout.getIDCardNumber();
		if (TextUtils.isEmpty(realName)) {
			Util.showToast(mContext, Util.getString(mContext, "a8_real_name_null"));
			return;
		}
		if (idCard.length() != 18) {
			Util.showToast(mContext, Util.getString(mContext, "a8_real_card_nocorrect"));
			return;
		}
		if(TextUtils.isEmpty(idCard)) {
			Util.showToast(mContext, Util.getString(mContext, "a8_real_card_null"));
			return;
		}
		
		mUserAction.authentication(mUser.getUid(), mUser.getToken(), realName, idCard, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					if (response.getInt(UserAction.CODE) == UserAction.RESULT_OK ) {
						onLoginSucceed();
					} else {
						if (!TextUtils.isEmpty(response.getString(UserAction.DESC))) {
							Util.showToast(mContext, response.getString(UserAction.DESC));
						} else {
							Util.showToast(mContext, Util.getString(mContext, "a8_real_name_fail_tips"));
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable,errorResponse);
				Util.showToast(mContext, Util.getString(mContext, "a8_real_name_fail_tips"));
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
		String name = mUser.getNickName();
		if (TextUtils.isEmpty(name)) {
			name = mUser.getUserName();
		}

		Toast toast = new Toast(mContext);
		View view = new LoginedView(mContext, name + Util.getString(mContext, "a8_login_tips_loginsuccess"));
		int t = Util.getInt(mContext, 24);
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setGravity(48, 0, t);
		toast.show();
		Intent intent = new Intent(UserConfig.ACTION);
		intent.putExtra(UserConfig.UID, mUser.getUid());
		intent.putExtra(UserConfig.UNAME, mUser.getUserName());
		intent.putExtra(UserConfig.TOKEN, mUser.getToken());
		LocalBroadcastManager.getInstance(mContext.getApplicationContext()).sendBroadcast(intent);
		finish();
	}
	
}
