package com.a8.zyfc.pay.center;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import com.a8.zyfc.PayCallback;
import com.a8.zyfc.AppConfig;
import com.a8.zyfc.pay.util.PayUtil;
import com.a8.zyfc.pay.activity.AppPayMainActivity;
import com.a8.zyfc.pay.service.AppBillProxy;
import com.a8.zyfc.pay.service.AppBillService;

public class AppPayCenter {

	private static AppPayCenter mInstance;
	private Activity mActivity;
	private PaymentInfo payInfo;
	private PayCallback payCallback;
	private String payItem = null;

	public static AppPayCenter init(Activity activity) {
		if (null == mInstance) {
			mInstance = new AppPayCenter(activity);
		}

		return mInstance;
	}

	public AppPayCenter(Activity mActivity) {
		this.mActivity = mActivity;
	}

	/**
	 *
	 * @param payinfo
	 * @param payCallback
	 */
	public void A8Payment(PaymentInfo payinfo, PayCallback payCallback) {
		this.payCallback = payCallback;
		this.payInfo = payinfo;
		this.payInfo.sdkOrderId = makeTradeNo();
		// 检查订单号
		if ("".equals(payInfo.cpOrderId) || payInfo.cpOrderId == null) {
			payInfo.cpOrderId = payInfo.sdkOrderId;
		}
		initPayItem();
	}

	/**
	 * 开始处理
	 */
	private void initPayItem() {
		if (!new AppBillProxy(mActivity).checkNetwork()) {
			AppBillProxy.makeToast("亲!网络不给力啊,信息获取失败.", mActivity);
			payEndAndReturnApp(false);
		} else {
			new getPayItemTask().execute("");
		}
	}

	protected class getPayItemTask extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			payItem = new AppBillService(mActivity).getPayList(payInfo.cpParams, payInfo.goodsId, payInfo.price);
			return payItem;
		}

		@Override
		protected void onPostExecute(String result) {
			if ("[]".equals(result) || !paramNotNull(result)) {
				AppBillProxy.makeToast("暂未开放", mActivity);
				payEndAndReturnApp(false);
			} else {
				startPayByAllThird();
			}
		}
	}

	/**
	 * 显示支付界面
	 */
	private void startPayByAllThird() {

		PayCallback thirdPayCallback;
		thirdPayCallback = new PayCallback() {
			@Override
			public void onFinished(int code, String content) {

				switch (code) {
					case PayCallback.PAY_SUCC: // 支付成功
						AppBillProxy.makeToast("支付成功", mActivity);
						payEndAndReturnApp(true);
						break;
					case PayCallback.PAY_UNSUCC: // 支付失败
					case PayCallback.PAY_NORETURN:
					default:
						AppBillProxy.makeToast("支付失败", mActivity);
						payEndAndReturnApp(false);
						break;
					case PayCallback.PAY_ONCANCEL:
						AppBillProxy.makeToast("已取消支付", mActivity);
						payEndAndReturnApp(false);
						break;
				}
			}
		};

		// 开启支付界面
		AppPayMainActivity.payCallback = thirdPayCallback;
		Intent intent = new Intent(mActivity, AppPayMainActivity.class);
		intent.putExtra("payInfo", payInfo);
		intent.putExtra("payItem", payItem);
		mActivity.startActivity(intent);
	}

	/**
	 * 支付回调方法
	 * @param payResult
	 */
	private void  payEndAndReturnApp(boolean payResult){
		try {
			int code = 1;
			String desc = "";
			String content = "";
			if (payResult) {
				code = 0;
				desc = "支付成功";
			} else {
				code = 1;
				desc = "支付失败";
			}
			JSONObject json = new JSONObject();
			json.put("tradeNo", payInfo.sdkOrderId); // sdk生成订单号
			json.put("amount", Integer.parseInt(payInfo.price)); // 计费金额
			json.put("desc", desc); // 描述信息

			content = json.toString();
			payCallback.onFinished(code, content);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}




	/**
	 * 生成订单
	 */
	private static String makeTradeNo() {
		// tradeNo 22位
		return AppConfig.ACHANNEL + PayUtil.date15() + PayUtil.rand(3);
	}

	private boolean paramNotNull(String param) {
		if (param != null && !"".equals(param)) {
			return true;
		}
		return false;
	}
}
