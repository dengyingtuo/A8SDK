package com.a8.zyfc.http;


import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.a8.zyfc.UserConfig;
import com.a8.zyfc.model.UserTO;
import com.a8.zyfc.util.DES;
import com.a8.zyfc.util.KeyUtil;
import com.a8.zyfc.util.Util;

public class UserAction {

	public static final int RESULT_OK = 100;
	public static final int RESULT_99 = 99;//用户未绑定手机，需绑定手机
	public static final int RESULT_98 = 98;//第三方登录（QQ，微信登录）token过期，需重新授权
	public static final int RESULT_97 = 97;//用户未实名认证，需实名认证
	public static final int RESULT_FAIL = 0;
	public static final String CODE = "code";
	public static final String MSG = "msg";
	public static final String DESC = "desc";
	private Context mContext;
	
	public UserAction(Context context){
		mContext = context;
	}
	
	public void getLoginList(JsonHttpResponseHandler handler){
		HttpUtil.get(mContext, UserConfig.GET_LOGIN_LIST + Util.getApplicationData(mContext, "A_KEY"), handler);
	}

	/**
	 * 用户注册
	 * 
	 * @param userName
	 * @param password
	 * @param handler
	 */
	public void regist(String userName, String password, JsonHttpResponseHandler handler) {
		UserTO user = new UserTO();
		user.setUserName(userName);
		user.setPassword(DES.encrypt(password, DES.PASSWORD_CRYPT_KEY));
		StringEntity se = initEntity(user,UserConfig.FLAG_REGISTER);
		HttpUtil.post(mContext, se, handler);
	}
	
	/**
	 * 手机注册
	 * @param phone
	 * @param password
	 * @param authCode
	 * @param handler
	 */
	public void regist(String phone, String password, String authCode, JsonHttpResponseHandler handler) {
		UserTO user = new UserTO();
		user.setUserName(phone);
		user.setPassword(DES.encrypt(password, DES.PASSWORD_CRYPT_KEY));
		user.setAuthCode(authCode);
		StringEntity se = initEntity(user, UserConfig.FLAG_PHONE_REGISTER);
		HttpUtil.post(mContext, se, handler);
	}
	
	/**
	 * 快速注册
	 * @param handler
	 */
	public void fastRegist(JsonHttpResponseHandler handler) {
		UserTO user = new UserTO();
		user.setUserName(KeyUtil.getKey(mContext));
		user.setPassword(DES.encrypt("123456", DES.PASSWORD_CRYPT_KEY));
		StringEntity se = initEntity(user,UserConfig.FLAG_FAST_REGISTER);
		HttpUtil.post(mContext, se, handler);
	}
	
	/**
	 * 登录
	 * @param userName
	 * @param password
	 * @param handler
	 */
	public void login(String userName, String password, JsonHttpResponseHandler handler) {
		UserTO user = new UserTO();
		user.setUserName(userName);
		user.setPassword(DES.encrypt(password, DES.PASSWORD_CRYPT_KEY));
		StringEntity se = initEntity(user,UserConfig.FLAG_LOGIN);
		HttpUtil.post(mContext, se, handler);
	}
	

	
	
	/**
	 * 绑定密保
	 * @param token
	 * @param question
	 * @param answer
	 * @param handler
	 */
	public void bindQuestion(String token, String question, String answer, JsonHttpResponseHandler handler) {
		UserTO user = new UserTO();
		user.setToken(token);
		user.setQuestion(question);
		user.setAnswer(answer);
		StringEntity se = initEntity(user,UserConfig.FLAG_BIND_QUESION);
		HttpUtil.post(mContext, se, handler);
	}
	
	/**
	 * 验证用户
	 * @param uName
	 * @param handler
	 */
	public void verifyUser(String uName, JsonHttpResponseHandler handler){
		UserTO user = new UserTO();
		user.setUserName(uName);	
		StringEntity se = initEntity(user,UserConfig.FLAG_VERIFY_USER);
		HttpUtil.post(mContext, se, handler);
	}
	
	/**
	 * 验证密保答案
	 * @param uid
	 * @param question
	 * @param answer
	 * @param handler
	 */
	public void verifyAnswer(long uid, String question, String answer, JsonHttpResponseHandler handler){
		UserTO user = new UserTO();
		user.setUid(uid);
		user.setQuestion(question);
		user.setAnswer(answer);
		StringEntity se = initEntity(user,UserConfig.FLAG_VERIFY_ANSWER);
		HttpUtil.post(mContext, se, handler);
	}
	
	/** 重置密码 **/
	public void resetPassword(String uname, String password, String authCode, JsonHttpResponseHandler handler){
		UserTO user = new UserTO();
		user.setUserName(uname);
		user.setPassword(DES.encrypt(password, DES.PASSWORD_CRYPT_KEY));
		user.setAuthCode(authCode);
		StringEntity se = initEntity(user,UserConfig.FLAG_RESET_PASSWORD);
		HttpUtil.post(mContext, se, handler);
	}
	
	/** 注销 **/
	public void logout(long uid, String token, JsonHttpResponseHandler handler) {
		UserTO user = new UserTO();
		user.setUid(uid);
		user.setToken(token);
		StringEntity se = initEntity(user,UserConfig.FLAG_LOGOUT);
		HttpUtil.post(mContext, se, handler);
	}
	
	/** 发送验证码 **/
	public void getCode(String token,long uid, String mobile, int flag, JsonHttpResponseHandler handler) {
		UserTO user = new UserTO();
		user.setToken(token);
		user.setUid(uid);
		user.setMobile(mobile);
		StringEntity se = initEntity(user,flag);
		HttpUtil.post(mContext, se, handler);
		Log.d("a8sdk","token "+ user.getToken().toString());
	}
	
	/** 发送验证码(手机注册) **/
	public void getCode(String mobile, int flag, JsonHttpResponseHandler handler) {
		UserTO user = new UserTO();
		user.setUserName(mobile);
		user.setMobile(mobile);
		StringEntity se = initEntity(user, flag);
		HttpUtil.post(mContext, se, handler);
	}
	
	/** 绑定手机 **/
	public void verifyCode(String token, String mobile, String authCode, int flag, JsonHttpResponseHandler handler) {
		UserTO user = new UserTO();
		user.setToken(token);
		user.setMobile(mobile);
		user.setAuthCode(authCode);
		StringEntity se = initEntity(user,flag);
		HttpUtil.post(mContext, se, handler);
	}
	
	/** 验证验证码（找回密码） **/
	public void verifyBackCode(String uName, String authCode, int flag, JsonHttpResponseHandler handler) {
		UserTO user = new UserTO();
		user.setUserName(uName);
		user.setAuthCode(authCode);
		StringEntity se = initEntity(user,flag);
		HttpUtil.post(mContext, se, handler);
	}
	
	/** 游客绑定手机 **/
	public void guestBindPhone(long uid, String phone, String password, String authCode, JsonHttpResponseHandler handler) {
		UserTO user = new UserTO();
		user.setUid(uid);
		user.setUserName(phone);
		user.setPassword(DES.encrypt(password, DES.PASSWORD_CRYPT_KEY));
		user.setAuthCode(authCode);
		StringEntity se = initEntity(user,UserConfig.FLAG_GUEST_BIND_PHONE);
		HttpUtil.post(mContext, se, handler);
	}
	
	/** 游客绑定账号 **/
	public void guestBindAccount(long uid, String uname, String password, JsonHttpResponseHandler handler) {
		UserTO user = new UserTO();
		user.setUid(uid);
		user.setUserName(uname);
		user.setPassword(DES.encrypt(password, DES.PASSWORD_CRYPT_KEY));
		StringEntity se = initEntity(user,UserConfig.FLAG_GUEST_BIND_ACCOUNT);
		HttpUtil.post(mContext, se, handler);
	}

	/** 第三方登录 **/
	public void thirdLogin(int type,String openId, String thirdToken, JsonHttpResponseHandler handler) {
		UserTO user = new UserTO();
		user.setThirdType(type);
		user.setOpenId(openId);
		user.setThirdToken(thirdToken);
		user.setPassword(DES.encrypt("123456", DES.PASSWORD_CRYPT_KEY));
		StringEntity se = initEntity(user,UserConfig.FLAG_THIRD_LOGIN);
		HttpUtil.post(mContext, se, handler);
	}
	
	/** 实名认证 **/
	public void authentication(long uid, String token, String realName, String card, JsonHttpResponseHandler handler) {
		UserTO user = new UserTO();
		user.setUid(uid);
		user.setToken(token);
		user.setRealName(realName);
		user.setIDCard(card);
		StringEntity se = initEntity(user,UserConfig.FLAG_REAL_AUTHENTICATE);
		HttpUtil.post(mContext, se, handler);
	}
	
	/**
	 * 获取最近在此设备登录过的帐号
	 * @param csdkId
	 * @param deviceId
	 */
	public void getLastLoginAccount(String csdkId,String deviceId,CustomHttpListener handler){
		try {
			String url="http://csdk.3333.cn:8088/index.php/user/getLastLoginUsername";
			JSONObject params=new JSONObject();
			params.put("csdkId", csdkId);
			params.put("sdkId", "android_zyfc");
			params.put("deviceId", deviceId);
			CustomHttpTask task=new CustomHttpTask(mContext);
			task.doPost(handler, params.toString(), url);
			Log.d("a8sdk", "params = "+ params.toString()+"url = "+url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private StringEntity initEntity(UserTO user,int flag) {
		StringEntity se = null;
		try {
			JSONObject params = new JSONObject();
			params.put("uid", user.getUid());
			params.put("token", user.getToken());
			params.put("uName", user.getUserName());
			params.put("nickName", user.getNickName());
			params.put("password", user.getPassword());
			params.put("mobile", user.getMobile());
			params.put("mail", user.getMail());
			params.put("question", user.getQuestion());
			params.put("answer", user.getAnswer());
			params.put("userName", user.getRealName());
			params.put("IDCard", user.getIDCard());
			params.put("version", "Android2.3.0");
			params.put("imei", KeyUtil.getKey(mContext));
			params.put("authCode", user.getAuthCode());
			params.put("isfast", user.isFast()?1:0);
			params.put("gamekey", Util.getApplicationData(mContext, "A_KEY"));
			params.put("thirdType", user.getThirdType());
			params.put("openId", user.getOpenId());
			params.put("thirdToken", user.getThirdToken());
			params.put("flag", flag);
			se = new StringEntity(params.toString(), HTTP.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return se;
	}

}
