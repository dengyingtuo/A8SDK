package com.a8.zyfc.http;


import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.a8.zyfc.util.Util;

public class ReportAction {

	public static final String QQ = "QQLogin";
	public static final String QQSUC = "QQLoginSuc";
	public static final String QQFAIL = "QQLoginfail";
	public static final String QQERR = "QQLoginError";
	public static final String WX = "WeiXinLogin";
	public static final String WXSUC = "WeiXinLoginSuc";
	public static final String WXFAIL = "WeiXinLoginfail";
	public static final String WXERR = "WeiXinLoginError";
	private Context mContext;
	
	public ReportAction(Context context){
		mContext = context;
	}
	
	public void report(String dataType, String msg, String thirdType) {
		StringEntity se = initEntity(dataType, msg, thirdType);
		HttpUtil.post(mContext, "http://a8sdk.3333.cn:8080/thirdlogin/action.do", se);
	}
	
	private StringEntity initEntity(String dataType, String msg, String thirdType) {
		StringEntity se = null;
		try {
			JSONObject params = new JSONObject();
			params.put("gamekey", Util.getApplicationData(mContext, "A_KEY"));
			params.put("dataType", dataType);
			params.put("deviceId", Util.getDeviceId(mContext));
			params.put("mobiletype", Util.getModel());
			params.put("mobileos", Util.getRelease());
			params.put("msg", msg);
			params.put("thirdType", thirdType);
			params.put("version", Util.getAppVersion(mContext));
			se = new StringEntity(params.toString(), HTTP.UTF_8);
			Log.d("a8", "a8 login report:" + params.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return se;
	}

}
