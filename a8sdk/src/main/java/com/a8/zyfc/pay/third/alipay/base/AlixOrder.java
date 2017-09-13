/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

package com.a8.zyfc.pay.third.alipay.base;

import android.app.Activity;
import android.app.ProgressDialog;

import com.a8.zyfc.PayCallback;
import com.a8.zyfc.pay.third.alipay.AliPayThread;

/**
 * 模拟商户应用的商品列表，交易步骤。
 * 
 * 1. 将商户ID，收款帐号，外部订单号，商品名称，商品介绍，价格，通知地址封装成订单信息 2. 对订单信息进行签名 3.
 * 将订单信息，签名，签名方式封装成请求参数 4. 调用pay方法
 * 
 * @version v4_0413 2012-03-02
 */
public class AlixOrder {

	Activity context;

	public AlixOrder(Activity context) {
		this.context = context;
	}

	private ProgressDialog mProgress = null;

	/**
	 * the onItemClick for the list view of the products. 商品列表商品被点击事件
	 */
	public void order(String subject, String body, String price,String tradeno, PayCallback payReslutCallBack) {
		// check to see if the MobileSecurePay is already installed.
		// 检测安全支付服务是否安装
		//MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(context);
		//boolean isMobile_spExist = mspHelper.detectMobile_sp();
		//if (!isMobile_spExist)
			//return;
		// check some info.
		// 检测配置信息
		mProgress = ProgressDialog.show(context, "提示", "请求支付中...", true, false);
		new Thread(new AliPayThread(mProgress, context, subject, body, price,tradeno,payReslutCallBack)).start();
		// start pay for this order.
		// 根据订单信息开始进行支付
	}
}