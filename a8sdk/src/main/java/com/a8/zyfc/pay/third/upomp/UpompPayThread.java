package com.a8.zyfc.pay.third.upomp;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.a8.zyfc.PayCallback;
import com.unionpay.UPPayAssistEx;

// 获取榜单信息的的Thread
@SuppressLint("HandlerLeak")
public class UpompPayThread extends Thread {
	ProgressDialog mProgress;
	Activity context;
	String orderid;
	String price;
	String subject;
	String m;
	PayCallback payReslutCallback;

	public UpompPayThread(ProgressDialog mProgress, Activity c, String orderid,
			String price, String subject, PayCallback payReslutCallback) {
		this.context = c;
		this.orderid = orderid;
		this.price = price;
		this.subject = subject;
		this.mProgress = mProgress;
		this.payReslutCallback = payReslutCallback;
	}

	UIHandler uh = new UIHandler();

	@Override
	public void run() {
		Looper.prepare();
		super.run();
		m = PayFun.getPayInfo(orderid, price, subject, "");
		Message msg = new Message();
		msg.what = 0;
		msg.obj = m;
		uh.sendMessage(msg);
		Looper.loop();
	}

	class UIHandler extends Handler {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			PayFun.closeProgress(mProgress);
			if (null == m || "".equals(m)) {
				PayFun.about(context, "网络错误,请稍后再试！");
				return;
			}
			try {
				JSONObject obj = new JSONObject(m);
				if (obj.getInt("code") == 9000) {
					String info = obj.getString("info");
					// 发起支付
					startPay(context, info, "00");// 正式00测试01
				} else {
					PayFun.about(context, obj.getString("info"));
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
				PayFun.about(context, "参数错误,请联系客服！");
			}
		}
	}

	public void startPay(Activity activity, String TN, String mode) {
		try {
			UPPayAssistEx.startPay(activity, null, null, TN, mode);
		} catch (Exception ex) {
			ex.printStackTrace();
			PayFun.makeToast(context, "银联支付异常");
		}
	}

}
