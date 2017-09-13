package com.a8.zyfc.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.a8.zyfc.UserConfig;
import com.a8.zyfc.adapter.ChooseGrideAdapter;
import com.a8.zyfc.db.DatabaseUtil;
import com.a8.zyfc.http.HttpUtil;
import com.a8.zyfc.http.JsonHttpResponseHandler;
import com.a8.zyfc.http.UserAction;
import com.a8.zyfc.model.UserTO;
import com.a8.zyfc.util.JSONUtils;
import com.a8.zyfc.util.Util;
import com.a8.zyfc.widget.AutoLoginLayout;
import com.a8.zyfc.widget.GuestPromptDialog;
import com.a8.zyfc.widget.GuestPromptDialog.Builder;
import com.a8.zyfc.widget.LoginChooseLayout;
import com.a8.zyfc.widget.LoginedView;
import com.a8.zyfc.widget.onButtonClickListener;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class LoginChooseActivity extends BaseActivity {

	private static final String TAG = "A8_login";
	private static final String DEFULT_LOGIN_LIST = "{\"10\":\"\"}";
	private static final int TYPE_PHONE_LOGIN = 10;
	private static final int TYPE_QQ_LOGIN = 11;
	private static final int TYPE_WX_LOGIN = 12;
	private static final int TYPE_GUEST_LOGIN = 13;
	private AutoLoginLayout mAutoLoginLayout;
	private RelativeLayout mCurrentLayout;
	private Context mContext;
	private UserAction mUserCallback;
	private String loginListStr;
	private UserTO mUser;
	private UserTO mLastUser;
	private Builder builder;
	private GuestPromptDialog dialog;
	private LoginChooseLayout mChoooseLayout;
	private GridView mChooseGrid;
	private ChooseGrideAdapter mGridAdapter;
	private boolean isAutoLogin = false;

	public static Tencent mTencent;
	private WXLoginBroadcastReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mUserCallback = new UserAction(mContext);
		loginListStr = Util.getFromSharedPreferences("loginList", mContext, DEFULT_LOGIN_LIST);
		receiver = new WXLoginBroadcastReceiver();
		registerReceiver(receiver, new IntentFilter(UserConfig.WXLOGIN_ACTION));
		if (mTencent == null) {
			mTencent = Tencent.createInstance(Util.getApplicationData(mContext, "QQ_APPID"), mContext);
		}
		//获取code,不为0，则是返回登陆选择,则不需要自动登陆，
		int code = getIntent().getIntExtra("code", 0);
		UserTO[] users = DatabaseUtil.getInstance(mContext).getAllUser(10);
		if (code == 0 && (users != null) && (users.length > 0)){
			mLastUser = users[0];
			initAutoLoginLayout();
		} else {
			initLoginChooseLayout();
		}
		setContentView(mCurrentLayout);
	}

	@SuppressWarnings("unchecked")
	private void initLoginChooseLayout(){
		isAutoLogin = false;
		List<Integer> ids = new ArrayList<>();
		Map<String, String> loginTypeMap = JSONUtils.fromJson(loginListStr, Map.class);
		if(loginTypeMap == null){
			Util.showToast(this, Util.getString(this, "a8_login_tips_loginfail"));
			finish();
		}
		if(loginTypeMap.containsKey("10")){
			ids.add(Util.getDrawableId(this, "a8_login_phone_icon"));
		}
		if(!"".equals(Util.getApplicationData(this, "QQ_APPID")) && loginTypeMap.containsKey("11")){
			ids.add(Util.getDrawableId(this, "a8_login_qq_icon"));
		}
		if(!"".equals(Util.getApplicationData(this, "WX_APPID")) && loginTypeMap.containsKey("12")) {
			ids.add(Util.getDrawableId(this, "a8_login_wx_icon"));
		}
		if(loginTypeMap.containsKey("13")){
			ids.add(Util.getDrawableId(this, "a8_login_guest_icon"));
		}
		
		mChoooseLayout = new LoginChooseLayout(mContext);
		mCurrentLayout = mChoooseLayout;
		mChooseGrid = mChoooseLayout.getChooseGrid();
		mChooseGrid.setNumColumns(ids.size());
		mGridAdapter = new ChooseGrideAdapter(mContext, ids);
		mChooseGrid.setAdapter(mGridAdapter);
		mChooseGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				login((int)(view.getTag()));
				mGridAdapter.setSelectIndex(position);
				mGridAdapter.notifyDataSetChanged();
			}
		});
	}

	private void setLoginChooseLayout(){
		if(isAutoLogin){//如果是自动登录，登录异常后需调用此方法，重新加载登录方式选择界面
			if(null == mChoooseLayout) {
				initLoginChooseLayout();
			}else{
				mCurrentLayout = mChoooseLayout;
			}
			setContentView(mCurrentLayout);
		}
	}

	private void login(int type){
		switch (type){
		case TYPE_PHONE_LOGIN:
			if(isAutoLogin){
				requestLogin(mLastUser.getUserName(), mLastUser.getPassword());
			}else{
				startActivity(new Intent(this, LoginDialogActivity.class));
				finish();
			}
			break;
		case TYPE_QQ_LOGIN:
			if(isAutoLogin){
				qqAutoLogin();
			}else{
				qqLogin();
			}
			break;
		case TYPE_WX_LOGIN:
			if(isAutoLogin){
				wxAutoLogin();
			}else{
				wxLogin();
			}
			break;
		case TYPE_GUEST_LOGIN:
			tryPlayReq();
			break;
		//兼容2.3.0版之前老用户的自动登录（2.3.0版之前本地无thirdType字段，故，自动登录会执行此case，
		//	另外，2.3.0之前只有游客和a8账号登录，所以只需判断这两种即可）。
		default:
			if(isAutoLogin){
				if(mLastUser.isFast())
					tryPlayReq();
				else
					requestLogin(mLastUser.getUserName(), mLastUser.getPassword());
			}else{
				setLoginChooseLayout();
			}
			break;
		}
	}

	private void qqAutoLogin(){
		//如果username（openId）不为空，则发起自动登录
		if(null != mLastUser && !"".equals(mLastUser.getUserName())){
			thirdLoginReq(TYPE_QQ_LOGIN, mLastUser.getUserName(), "");
		}else{
			qqLogin();
		}
	}

	private void qqLogin(){
		if (!mTencent.isSessionValid()) {
			mTencent.login(this, UserConfig.QQLOGIN_SCOPE, qqLoginListener);
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
					Log.d(TAG, "QQ_login response null!");
					Util.showToast(mContext, Util.getString(mContext, "qq_login_fail"));
					setLoginChooseLayout();
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
				}else{
					Log.d(TAG, "QQ_login "+jsonResponse.getString("msg"));
					Util.showToast(mContext, Util.getString(mContext, "qq_login_fail"));
					setLoginChooseLayout();
				}

			} catch(Exception e) {
			}
		}

		@Override
		public void onError(UiError uiError) {
			Log.d(TAG, "QQ_login "+uiError.errorMessage);
			Util.showToast(mContext, Util.getString(mContext, "qq_login_fail"));
			setLoginChooseLayout();
		}

		@Override
		public void onCancel() {
			Log.d(TAG, "QQ_login onCancel!");
			Util.showToast(mContext, Util.getString(mContext, "qq_login_cancle"));
			setLoginChooseLayout();
		}
	};

	private void wxAutoLogin(){
		//如果username（openId）不为空，则发起自动登录
		if(null != mLastUser && !"".equals(mLastUser.getUserName())){
			thirdLoginReq(TYPE_WX_LOGIN, mLastUser.getUserName(), "");
		}else{
			wxLogin();
		}
	}

	private void wxLogin(){
		try {
			Intent data = new Intent(this, Class.forName(getApplicationContext().getPackageName()+".wxapi.WXEntryActivity"));
			data.putExtra("actionType", WXBaseActivity.ACTION_TYPE_WXLOGIN);
			startActivity(data);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	//接收微信登录Code
	public class WXLoginBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String wxLoginCode = intent.getStringExtra("wxLoginCode");
			Log.d(TAG, "wxcode="+ wxLoginCode);
			if(null != wxLoginCode && !"".equals(wxLoginCode)){
				thirdLoginReq(TYPE_WX_LOGIN, "", wxLoginCode);
			}else{
				setLoginChooseLayout();
			}
		}
	}

	private void thirdLoginReq(final int type, String openId, String thirdToken){
		Log.d(TAG, "type:"+type+",openId:"+openId+",thirdToken:"+thirdToken);
		mUserCallback.thirdLogin(type, openId, thirdToken, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					if (response.getInt(UserAction.CODE) == UserAction.RESULT_OK ) {
						mUser = JSONUtils.fromJson(response.getString(UserAction.MSG), UserTO.class);
						mUser.setBindMobile(true);
						mUser.setThirdType(type);
						onLoginSucceed(mUser);
						finish();
					} else if(response.getInt(UserAction.CODE) == UserAction.RESULT_99) {
						mUser = JSONUtils.fromJson(response.getString(UserAction.MSG), UserTO.class);
						mUser.setBindMobile(false);
						mUser.setThirdType(type);
						//获取本地存储的用户信息
//						UserTO user = DatabaseUtil.getInstance(mContext).getUserByUid(mUser.getUid());
//						if (user == null || (System.currentTimeMillis() - user.getLastTipTime()) > 7 * 24 * 60 * 60 * 1000) {
//							Intent intent = new Intent(mContext, BindPhoneNunberActivity.class);
//							intent.putExtra("actName", LoginChooseActivity.class.getSimpleName());
//							intent.putExtra("user", mUser);
//							startActivity(intent);
//							finish();
//						} else {
							onLoginSucceed(mUser);
							finish();
//						}
					}else if(response.getInt(UserAction.CODE) == UserAction.RESULT_98){
						if(type == TYPE_QQ_LOGIN){
							qqLogin();
						}else if(type == TYPE_WX_LOGIN){
							wxLogin();
						}
					} else {
						if (!TextUtils.isEmpty(response.getString(UserAction.DESC))) {
							Util.showToast(mContext, response.getString(UserAction.DESC));
						} else {
							Util.showToast(mContext, Util.getString(mContext, "a8_login_tips_loginfail"));
						}
						setLoginChooseLayout();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable,errorResponse);
				Util.showToast(mContext, Util.getString(mContext, "a8_login_tips_loginfail"));
				setLoginChooseLayout();
			}
		});
	}

	private void initAutoLoginLayout() {
		isAutoLogin = true;
		mAutoLoginLayout = new AutoLoginLayout(mContext);
		mCurrentLayout = mAutoLoginLayout;
		final Handler autoLoginHandler = new Handler(new Handler.Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				login(mLastUser.getThirdType());
				return false;
			}
		});

		final Timer timer = new Timer();
		final TimerTask task = new TimerTask(){

			public void run() {
				Message message = new Message();
				message.what = 1;
				autoLoginHandler.sendMessage(message);
			}
		};
		timer.schedule(task, 3000);

		mAutoLoginLayout.setOnSwitchAccountButtonClickListener(new onButtonClickListener() {

			@Override
			public void onButtonClick(View v) {
				HttpUtil.cancel(mContext, true);
				if(timer != null) timer.cancel();
				if(task != null) task.cancel();
				setLoginChooseLayout();
			}
		});

		if(mLastUser.isFast()){//判断是否是游客，true则显示“绑定账号”按钮
			mAutoLoginLayout.getmBindBtn().setVisibility(View.VISIBLE);
			mAutoLoginLayout.getmBindBtn().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					/********* 此处说明：       游客账号的绑定功能是在登录完成后实现，且绑定完成后不重新登录，只是修改本地存储的当前用户信息，
 				故绑定行为不打断游客登录的进行（3s后请求游客登录接口）
 				问题点：           若在3s内完成了账号绑定（包括后台绑定完成）并发出了游客登录请求，那么返回的将是一个新的游客账号，
 				但是上一个已绑定的账号并未丢失，登录界面账号list列表中将显示此账号**********/
					//跳转绑定账号界面
					Intent intent = new Intent(LoginChooseActivity.this, GuestBindPhoneActivity.class);
					intent.putExtra("actName", LoginChooseActivity.class.getSimpleName());
					intent.putExtra("user", mLastUser);
					startActivity(intent);
					finish();
				}
			});
		}
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
						mUser.setThirdType(TYPE_PHONE_LOGIN);
						onLoginSucceed(mUser);
						finish();
					} else if(response.getInt(UserAction.CODE) == UserAction.RESULT_99) {
						mUser = JSONUtils.fromJson(response.getString(UserAction.MSG), UserTO.class);
						mUser.setPassword(pwd);
						mUser.setBindMobile(false);
						mUser.setThirdType(TYPE_PHONE_LOGIN);
						//获取本地存储的用户信息
						UserTO user = DatabaseUtil.getInstance(mContext).getUserByUid(mUser.getUid());
						if(user == null || (System.currentTimeMillis() - user.getLastTipTime()) > 7 * 24 * 60 * 60 * 1000) {
							Intent intent = new Intent(mContext, BindPhoneNunberActivity.class);
							intent.putExtra("actName", LoginChooseActivity.class.getSimpleName());
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
						setLoginChooseLayout();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable,errorResponse);
				Util.showToast(mContext, Util.getString(mContext, "a8_login_tips_loginfail"));
				setLoginChooseLayout();
			}
		});
	}

	private void tryPlayReq(){
		mUserCallback.fastRegist(new JsonHttpResponseHandler() {
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
									Intent intent = new Intent(LoginChooseActivity.this, GuestBindPhoneActivity.class);
									intent.putExtra("actName", LoginChooseActivity.class.getSimpleName());
									intent.putExtra("user", mUser);
									startActivity(intent);
									finish();
								}
							});
							builder.setNegativeButton(mContext.getString(Util.getStringId(mContext, "a8_fast_login_cancle")),
									new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									finish();
								}
							});
							dialog = builder.create();
							dialog.setCancelable(false);
							dialog.setCanceledOnTouchOutside(false);
							dialog.show();
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
						setLoginChooseLayout();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable,errorResponse);
				Util.showToast(mContext, Util.getString(mContext, "a8_login_tips_loginfail"));
				setLoginChooseLayout();
			}
		});
	}

	private void onLoginSucceed(UserTO user) {
		Util.sharedPreferencesSave("a8_lastLoginUser", null, mContext);  //登录成功之后，把这个值清除
		user.setLastLoginTime(System.currentTimeMillis());
		Util.saveUser(mContext, user);
		DatabaseUtil.getInstance(mContext).saveUser(user);
		onLogined();
	}

	private void onLogined() {
		String name = mUser.getNickName();
		//QQ,微信自动登录，取本地nickname
		if(isAutoLogin) name = mLastUser.getNickName();
		//
		if (TextUtils.isEmpty(name)) name = mUser.getUserName();
		//
		if(mUser.isFast()) name = "游客";

		Toast toast = new Toast(mContext);
		View view = new LoginedView(mContext, name + Util.getString(mContext, "a8_login_tips_loginsuccess"));
		int t = Util.getInt(mContext, 24);
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setGravity(48, 0, t);
		toast.show();
		Intent intent = new Intent(UserConfig.ACTION);
		intent.putExtra(UserConfig.UID, mUser.getUid());
		intent.putExtra(UserConfig.UNAME, mUser.getUserName());
		intent.putExtra(UserConfig.TOKEN, mUser.getToken());
		LocalBroadcastManager.getInstance(mContext.getApplicationContext()).sendBroadcast(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Tencent.onActivityResultData(requestCode,resultCode,data,qqLoginListener);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		isAutoLogin = false;
	}
}
