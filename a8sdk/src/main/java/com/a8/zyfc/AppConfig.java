package com.a8.zyfc;

import android.annotation.SuppressLint;

@SuppressLint("DefaultLocale")
public class AppConfig {

	public static final String VERSION = "Android2.3.2";
	//常量
	public static boolean INISDK = false;
	public static String DEVICEID = "";
	public static String AKEY = "";
	public static String WXAPPID = "";  //微信支付appid
	public static String ACHANNEL = "";
	public static String SESSIONID = "";
	public static String DEVICEMODEL = "unknown";
	public static String SYSRELEASE = "unknown";
	public static String MACADDRESS = "";

    public static final String SAVEPAYINFOUI = "http://a8sdk.3333.cn/a8app/appapay/savepayinfo.htm";
//    public static final String CPUI = "http://a8sdk.3333.cn/a8app/appa8bill/getcode12.htm";
//    public static final String RETUI = "http://a8sdk.3333.cn/a8app/appa8bill/getresult.htm";
//    public static final String MDORETUI = "http://a8sdk.3333.cn/a8app/appa8bill/saveMDOPayResult.htm";
    public static final String PAYLISTUI = "http://a8sdk.3333.cn/a8app/appapay/getpaylist.htm";
//    public static final String PAYORDERINFOUI = "http://a8sdk.3333.cn/a8app/appapay/getpayorderinfo.htm";
//    public static final String SAVEDEBUGUI = "http://a8sdk.3333.cn/a8app/applog/saveDebugInfo.htm";
//    public static final String PAYCONFIGURL = "http://a8sdk.3333.cn/game/config.do?gameKey=";
}
