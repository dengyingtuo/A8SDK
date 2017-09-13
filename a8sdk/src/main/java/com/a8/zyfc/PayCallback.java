package com.a8.zyfc;

public interface PayCallback {

	//支付结果
	int PAY_SUCC = 0;
	int PAY_UNSUCC = 1;
	int PAY_NORETURN = -1;
	int PAY_ONCANCEL = -2;
	/**
	 * 处理完成,回调结果
	 * 
	 * @param code
	 * @param content
	 */
	void onFinished(int code, String content);

}
