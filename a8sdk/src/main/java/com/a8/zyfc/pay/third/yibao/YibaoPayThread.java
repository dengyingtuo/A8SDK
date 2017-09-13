package com.a8.zyfc.pay.third.yibao;

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
public class YibaoPayThread extends Thread {
	ProgressDialog mProgress;
	Activity context;
	String m;

	String p2_Order;
	float p3_Amt;
	boolean p4_verifyAmt;
	String p5_Pid;
	String p6_Pcat;
	String p7_Pdesc;
	String pa7_cardAmt;
	String pa8_cardNo;
	String pa9_cardPwd;
	String pd_FrpId;
	String pz_userId;
	String pz1_userRegTime;
	PayCallback payReslutCallback;

	public YibaoPayThread(ProgressDialog mProgress, Activity c,
			String p2_Order, float p3_Amt, boolean p4_verifyAmt, String p5_Pid,
			String p6_Pcat, String p7_Pdesc, String pa7_cardAmt,
			String pa8_cardNo, String pa9_cardPwd, String pd_FrpId,
			String pz_userId, String pz1_userRegTime,
			PayCallback payReslutCallback) {
		this.context = c;
		this.mProgress = mProgress;
		this.p2_Order = p2_Order;
		this.p3_Amt = p3_Amt;
		this.p4_verifyAmt = p4_verifyAmt;
		this.p5_Pid = p5_Pid;
		this.p6_Pcat = p6_Pcat;
		this.p7_Pdesc = p7_Pdesc;
		this.pa7_cardAmt = pa7_cardAmt;
		this.pa8_cardNo = pa8_cardNo;
		this.pa9_cardPwd = pa9_cardPwd;
		this.pd_FrpId = pd_FrpId;
		this.pz_userId = pz_userId;
		this.pz1_userRegTime = pz1_userRegTime;
		this.payReslutCallback = payReslutCallback;
	}

	UIHandler uh = new UIHandler();

	@Override
	public void run() {
		Looper.prepare();
		super.run();
		m = PayFun.requestPay(p2_Order, p3_Amt, p4_verifyAmt, p5_Pid, p6_Pcat,
				p7_Pdesc, pa7_cardAmt, pa8_cardNo, pa9_cardPwd, pd_FrpId,
				pz_userId, pz1_userRegTime);
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
				if ("1".equals(obj.getString("code"))) {
					payReslutCallback.onFinished(-1, "请求已发送");
				} else {
					payReslutCallback.onFinished(1, info);
				}
			} catch (Exception e) {
				payReslutCallback.onFinished(1, "支付失败");
			}

			return;

		}
	}

}
