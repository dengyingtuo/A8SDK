package com.a8.zyfc.pay.third.wxpay;

public interface WXPayCallBack {
	void onResult(String result);
	void onCancel();
}
