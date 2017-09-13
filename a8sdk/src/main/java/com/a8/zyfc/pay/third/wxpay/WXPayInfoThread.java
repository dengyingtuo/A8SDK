package com.a8.zyfc.pay.third.wxpay;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.a8.zyfc.pay.util.HttpRequest;
import com.a8.zyfc.pay.third.order.Order;

/** 获取微信支付预订单等信息的的Thread
 * @author Chooper
 */
@SuppressLint("HandlerLeak")
public class WXPayInfoThread extends Thread {
	ProgressDialog mProgress;
	Activity context;
	String result = "";
	String subject;
	String body;
	String price;
	String orderNumber;
	WXPayCallBack listener;

	public WXPayInfoThread(ProgressDialog mProgress, Activity c, String subject,
			String body, String price, String orderNumber,
			WXPayCallBack listener) {
		this.context = c;
		this.mProgress = mProgress;
		this.subject = subject;
		this.body = body;
		this.price = price;
		this.orderNumber = orderNumber;
		this.listener = listener;
	}

	UIHandler uh = new UIHandler();

	public void run() {
		Looper.prepare();
		super.run();
		try {
			JSONObject obj = new JSONObject();
			obj.put("subject", subject); //系统类型，and、ios、wp
			obj.put("body", body); //商品名称
			obj.put("price", price); //商品价格
			obj.put("orderNumber", orderNumber); //sdk订单号
			result = HttpRequest.postData(Order.PAY_URL_WX, "5", "UTF-8", obj.toString());
		} catch (Exception e) {
			result = "";
			e.printStackTrace();
		}
		Log.i("a8", "getWXPayInfoThread:response="+result);
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
