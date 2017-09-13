package com.a8.zyfc.pay.third.shenzhoufu;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.a8.zyfc.PayCallback;

// 获取榜单信息的的Thread
@SuppressLint("HandlerLeak")
public class ShenPayThread extends Thread {
	ProgressDialog mProgress;
	Activity context;
	String m;
	PayCallback payReslutCallback;
	String orderId, payMoney, cardMoney, sn, password, cardTypeCombine;

	public ShenPayThread(ProgressDialog mProgress, Activity c, String orderId,
			String payMoney, String cardMoney, String sn, String password,
			String cardTypeCombine, PayCallback payReslutCallback) {
		this.context = c;
		this.mProgress = mProgress;
		this.orderId = orderId;
		this.payMoney = payMoney;
		this.cardMoney = cardMoney;
		this.sn = sn;
		this.password = password;
		this.cardTypeCombine = cardTypeCombine;
		this.payReslutCallback = payReslutCallback;
	}

	UIHandler uh = new UIHandler();

	@Override
	public void run() {
		Looper.prepare();
		super.run();
		m = PayFun.getPayInfo(orderId, payMoney, cardMoney, sn, password,
				cardTypeCombine);
		Message msg = new Message();
		msg.what = 0;
		msg.obj = m;
		uh.sendMessage(msg);
		Looper.loop();
	}

	class UIHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mProgress.dismiss();
			if (null == m || "".equals(m)) {
				payReslutCallback.onFinished(1, "网络错误,请稍后再试！");
				return;
			}
			try {
				JSONObject obj = new JSONObject(m);
				String info = obj.getString("info");
				if (obj.getInt("code") == 200) {
					payReslutCallback.onFinished(-1, info);
				} else {
					payReslutCallback.onFinished(1, info);
				}
			} catch (Exception e) {
				payReslutCallback.onFinished(1, "支付失败");
			}

			return;
		}
	}

	// private void saveDebugInfo(String dataType, String data, String ext1,
	// String ext2) {
	// new AppBillService(context).saveDebugInfo(dataType, data, ext1, ext2);
	// }
}
