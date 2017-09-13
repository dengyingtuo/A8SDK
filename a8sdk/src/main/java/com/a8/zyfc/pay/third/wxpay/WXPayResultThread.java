package com.a8.zyfc.pay.third.wxpay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.a8.zyfc.pay.util.HttpRequest;

/** 获取微信支付结果信息的的Thread
 * @author Chooper
 */
@SuppressLint("HandlerLeak")
public class WXPayResultThread extends Thread {
	
	public static final String PAY_RESULT_URL_WX = "http://a8sdk.3333.cn/pay_sdk/wx/getResult.htm?orderid=";
	ProgressDialog mProgress;
	Activity context;
	String result = "";
	String orderNumber;
	WXPayCallBack listener;

	public WXPayResultThread(ProgressDialog mProgress, Activity c,String orderNumber, WXPayCallBack listener) {
		this.context = c;
		this.mProgress = mProgress;
		this.orderNumber = orderNumber;
		this.listener = listener;
	}

	UIHandler uh = new UIHandler();

	public void run() {
		Looper.prepare();
		super.run();
		try {
			result = HttpRequest.getData(PAY_RESULT_URL_WX + orderNumber, "5", "UTF-8");
		} catch (Exception e) {
			result = "";
			e.printStackTrace();
		}
		Log.i("a8", "getWXPayResultThread:response="+result);
		Message msg = new Message();
		msg.what = 0;
		msg.obj = result;
		uh.sendMessage(msg);
		Looper.loop();
	}

	class UIHandler extends Handler {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mProgress.dismiss();
			if(msg.what == 0){
				listener.onResult((String)msg.obj);
			}else{
				listener.onCancel();
			}
		}
	}

}
