package com.a8.zyfc.pay.third.alipay;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.alipay.sdk.app.PayTask;
import com.a8.zyfc.PayCallback;
import com.a8.zyfc.pay.third.alipay.base.PayResult;
import com.a8.zyfc.pay.third.alipay.base.Res;

// 获取榜单信息的的Thread
@SuppressLint("HandlerLeak")
public class AliPayThread extends Thread {
	static String TAG = "AliPay";
	private static final int SDK_PAY_FLAG = 1;
	ProgressDialog mProgress;
	Activity context;
	// Map<String, String> m;
	String m;
	String subject;
	String body;
	String price;
	String tradeno;
	PayCallback payReslutCallback;

	public AliPayThread(ProgressDialog mProgress, Activity c, String subject,
			String body, String price, String tradeno,
			PayCallback payReslutCallback) {
		this.context = c;
		this.mProgress = mProgress;
		this.subject = subject;
		this.body = body;
		this.price = price;
		this.tradeno = tradeno;// PayFun.getOutTradeNo();
		this.payReslutCallback = payReslutCallback;
	}

	UIHandler uh = new UIHandler();

	public void run() {
		Looper.prepare();
		super.run();
		m = PayFun.getPayInfo(tradeno, price, subject, body);
		Log.d(TAG, "getPayInfo"+m);
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
					startPay(info);
				} else {
					PayFun.about(context, obj.getString("info"));
					return;
				}
			} catch (Exception e) {
				PayFun.about(context, "参数错误,请联系客服！");
			}
		}
	}

	public void startPay(final String info) {
		try {
			// 调用pay方法进行支付
//			MobileSecurePayer msp = new MobileSecurePayer();
//			boolean bRet = msp.pay(info, mHandler, AlixId.RQF_PAY, context);
			Runnable payRunnable = new Runnable() {

				@Override
				public void run() {
					// 构造PayTask 对象
					PayTask alipay = new PayTask(context);
					// 调用支付接口，获取支付结果
					String result = alipay.pay(info);
					Message msg = new Message();
					msg.what = SDK_PAY_FLAG;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			};
			// 必须异步调用
			Thread payThread = new Thread(payRunnable);
			payThread.start();
//					
//			if (bRet) {
//				// 显示“正在支付”进度条
//				mProgress = BaseHelper.showProgress(context, null, "正在支付",
//						false, true);
//			}
		} catch (Exception ex) {
			ex.printStackTrace();
			PayFun.makeToast(context, Res.Title.remote_call_failed);
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {				
				switch (msg.what) {
				case SDK_PAY_FLAG: {
					try {
						PayResult payResult = new PayResult((String) msg.obj);

						// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
						String resultInfo = payResult.getResult();

						String resultStatus = payResult.getResultStatus();

						// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
						if (TextUtils.equals(resultStatus, "9000")) {
							payReslutCallback.onFinished(0, "支付宝支付成功");
						} else {
							// 判断resultStatus 为非“9000”则代表可能支付失败
							// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
							if (TextUtils.equals(resultStatus, "8000") ) {

							} else if (TextUtils.equals(resultStatus, "6001")){
								payReslutCallback.onFinished(1, "支付宝支付已取消");
							}else {
								payReslutCallback.onFinished(1, "支付宝支付失败："+payResult.toString());
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						payReslutCallback.onFinished(1, (String)msg.obj);
					}
					break;
				}

				default:
					break;
				}
				super.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

}
