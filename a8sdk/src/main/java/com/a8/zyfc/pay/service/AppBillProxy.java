package com.a8.zyfc.pay.service;

import android.content.Context;
import android.widget.Toast;

import com.a8.zyfc.pay.util.PayUtil;

public class AppBillProxy {
	//private static Toast mToast;
	private final Context context;

	public AppBillProxy(Context context) {
		this.context = context;
	}

	/**
	 * @param len
	 * @return
	 */
	public static String createUUID(int len) {
		return PayUtil.createUUID(len);
	}

	public String getChannel() {
		return PayUtil.getApplicationData(context, "csdkSubChannelId");
	}

	public String getKey() {
		return PayUtil.getApplicationData(context, "A_KEY");
	}
	
	/**
	 * 判断是否有网络
	 * 
	 * @return true/false
	 */
	public boolean checkNetwork() {
		return PayUtil.checkNetwork1(context);
	}

	/**
	 * 取运营商
	 */
	public boolean checkSimOperator(String simOperator) {
		return PayUtil.checkSimOperator(simOperator);
	}
	
	/**
	 * 提示
	 * @param content
	 * @param context
	 */
	public static void makeToast(String content, Context context){  
//	       // if(mToast == null) {  
//	            mToast = Toast.makeText(context, content, Toast.LENGTH_SHORT);  
//	       // } else {  
//	        	mToast = Toast.makeText(context, content, Toast.LENGTH_SHORT); 
//	           // mToast.setText(content);    
//	           // mToast.setDuration(Toast.LENGTH_SHORT);  
//	       // }  
//	        mToast.show();

		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}
	
}

