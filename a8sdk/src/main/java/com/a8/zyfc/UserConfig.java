package com.a8.zyfc;

public class UserConfig {
	
	public static final String GET_LOGIN_LIST = "http://a8sdk.3333.cn/a8app/applogin/getloginlist.htm?gamekey=";
	
	/** 注册 **/
	public static final int FLAG_REGISTER = 0;
	
	/** 登录 **/
	public static final int FLAG_LOGIN = 1;
	
	/** 绑定手机 **/
	public static final int FLAG_BIND_MOBILE = 2;
	
	/** 绑定邮箱 **/
	public static final int FLAG_BIND_MAIL = 3;
	
	/** 绑定密保问题 **/
	public static final int FLAG_BIND_QUESION = 4;
	
	/** 重置密码 **/
	public static final int FLAG_RESET_PASSWORD = 5;
	
	/** 用户验证 **/
	public static final int FLAG_VERIFY_USER = 9;
	
	/** 密保答案验证 **/
	public static final int FLAG_VERIFY_ANSWER = 10;
	
	/** 退出 **/
	public static final int FLAG_LOGOUT = 6;
	
	/** 一键试玩 **/
	public static final int FLAG_FAST_REGISTER = 8;
	
	/** 发送验证码(绑定手机) **/
	public static final int FLAG_SEND_CODE_1 = 12;
	
	/** 发送验证码(找回密码) **/
	public static final int FLAG_SEND_CODE_2 = 13;
	
	/** 验证验证码(找回密码) **/
	public static final int FLAG_VERIFY_CODE = 14;
	
	/** 手机注册 **/
	public static final int FLAG_PHONE_REGISTER = 15;
	
	/** 游客绑定手机号 **/
	public static final int FLAG_GUEST_BIND_PHONE = 23;
	
	/** 游客绑定账号 **/
	public static final int FLAG_GUEST_BIND_ACCOUNT = 24;

	/** 第三方登录（QQ、微信） **/
	public static final int FLAG_THIRD_LOGIN = 25;
	
	/** 实名认证 **/
	public static final int FLAG_REAL_AUTHENTICATE = 26;
	
	/** 登录广播接收的ACTION **/
	public final static String ACTION = "com.zhiyou.account.login";
	
	/** 登录广播接收的UID参数名 **/
	public final static String UID = "uid";
	
	/** 登录广播接收的TOKEN参数名 **/
	public final static String TOKEN = "token";

	/** 登录广播接收的UNAME参数名 **/
	public final static String UNAME = "uName";

	/** 应用授权作用域，发起QQ登录的标识，获取用户个人信息 **/
	public static final String QQLOGIN_SCOPE = "all";

	/** 应用授权作用域，发起微信登录的标识，获取用户个人信息 **/
	public static final String WXLOGIN_SCOPE = "snsapi_userinfo";

	/** 用于保持请求和回调的状态，授权请求后原样带回。该参数可用于防止csrf攻击（跨站请求伪造攻击），建议带上该参数，可设置为简单的随机数加session进行校验 **/
	public static final String WXLOGIN_STATE = "wechat_zyfc_game";

	/** 微信activity发送微信登录msg广播的action **/
	public static final String WXLOGIN_ACTION = "com.a8.zyfc.sdk.wxlogin";
	
	/** 微信activity发送微信分享msg广播的action **/
	public static final String WXSHARE_ACTION = "com.a8.zyfc.sdk.wxshare";
}
