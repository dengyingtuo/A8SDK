package com.a8.zyfc.pay.third.order;

import android.app.Activity;
import android.app.ProgressDialog;

import com.a8.zyfc.PayCallback;
import com.a8.zyfc.pay.third.alipay.base.AlixOrder;
import com.a8.zyfc.pay.third.shenzhoufu.ShenPayThread;
import com.a8.zyfc.pay.third.upomp.UpompPayThread;
import com.a8.zyfc.pay.third.yibao.YibaoPayThread;

public class Order {
	// 正式
	public static final String PAY_URL_ALI = "http://a8sdk.3333.cn/pay_sdk/ali/trade.htm";
	public static final String PAY_URL_SZF = "http://a8sdk.3333.cn/pay_sdk/szf/trade.htm";
	public static final String PAY_URL_UPOMP = "http://a8sdk.3333.cn/pay_sdk/upomp/trade.htm";
	public static final String PAY_URL_YIBAO = "http://a8sdk.3333.cn/pay_sdk/yibao/trade.htm";
	public static final String PAY_URL_MO9 = "http://a8sdk.3333.cn/pay_sdk/mo9/getpaymenturl.htm";
	public static final String PAY_URL_WX = "http://a8sdk.3333.cn/pay_sdk/wx/trade.htm";

	/**
	 * 调起支付宝
	 * 
	 * @param context
	 *            上下文
	 * @param subject
	 *            商品标题
	 * @param body
	 *            商品主体说明
	 * @param price
	 *            商品价格（单位元）
	 * @param tradeno
	 *            定单号
	 */
	public static void orderAlipay(Activity context, String subject,
			String body, String price, String tradeno,
			PayCallback payReslutCallBack) {
		AlixOrder ao = new AlixOrder(context);
		ao.order(subject, body, price, tradeno, payReslutCallBack);
	}

	/**
	 * 调起银联支付
	 * 
	 * @param context
	 *            上下文
	 * @param orderid
	 *            定单号
	 * @param price
	 *            价格（单位分）
	 * @param subject
	 *            定单说明
	 */
	public static void orderupomp(Activity context, String orderid,
			String price, String subject, PayCallback payReslutCallBack) {
		ProgressDialog mProgress = ProgressDialog.show(context, "提示",
				"请求支付中...", true, false);
		new Thread(new UpompPayThread(mProgress, context, orderid, price,
				subject, payReslutCallBack)).start();
	}

	/**
	 * 调起神州付
	 * 
	 * @param context
	 *            上下文
	 * @param orderId
	 *            定单号
	 * @param payMoney
	 *            支付金额（单位分）
	 * @param cardMoney
	 *            卡面额（单位元）
	 * @param sn
	 *            卡号
	 * @param password
	 *            卡密码
	 * @param cardTypeCombine
	 *            卡类型 0移动卡1联通卡2电信卡
	 */
	public static void ordershen(Activity context, String orderId,
			String payMoney, String cardMoney, String sn, String password,
			String cardTypeCombine, PayCallback payReslutCallBack) {
		ProgressDialog mProgress = ProgressDialog.show(context, "提示",
				"请求支付中...", true, false);
		new Thread(new ShenPayThread(mProgress, context, orderId, payMoney,
				cardMoney, sn, password, cardTypeCombine, payReslutCallBack))
				.start();
	}

	/**
	 * 调起易宝支付
	 * 
	 * @param context
	 *            上下文
	 * @param p2_Order
	 *            定单号，一定唯一
	 * @param p3_Amt
	 *            定单金额如1.2表示1元2角
	 * @param p4_verifyAmt
	 *            是否检查金额
	 * @param p5_Pid
	 *            产品号（非必填）
	 * @param p6_Pcat
	 *            产品类型（非必填）
	 * @param p7_Pdesc
	 *            产品说明（非必填）
	 * @param pa7_cardAmt
	 *            卡金额（多个卡用,分隔，最多10个)
	 * @param pa8_cardNo
	 *            （多个卡用,分隔，最多10个)
	 * @param pa9_cardPwd
	 *            （多个卡用,分隔，最多10个)
	 * @param pd_FrpId
	 *            JUNNET 骏网一卡通 SNDACARD 盛大卡 SZX 神州行 ZHENGTU 征途卡 QQCARD Q币卡
	 *            UNICOM 联通卡 JIUYOU 久游卡 YPCARD 易宝e卡通 NETEASE 网易卡 WANMEI 完美卡 SOHU
	 *            搜狐卡 TELECOM 电信卡 ZONGYOU 纵游一卡通 TIANXIA 天下一卡通 TIANHONG 天宏一卡通
	 * 
	 * @param pz_userId
	 *            用户ID（非必填）
	 * @param pz1_userRegTime
	 *            用户注册时间 （非必填）
	 */
	public static void orderYibao(Activity context, String p2_Order,
			float p3_Amt, boolean p4_verifyAmt, String p5_Pid, String p6_Pcat,
			String p7_Pdesc, String pa7_cardAmt, String pa8_cardNo,
			String pa9_cardPwd, String pd_FrpId, String pz_userId,
			String pz1_userRegTime, PayCallback payReslutCallBack) {
		ProgressDialog mProgress = ProgressDialog.show(context, "", "请求支付中...",
				true, false);
		new Thread(new YibaoPayThread(mProgress, context, p2_Order, p3_Amt,
				p4_verifyAmt, p5_Pid, p6_Pcat, p7_Pdesc, pa7_cardAmt,
				pa8_cardNo, pa9_cardPwd, pd_FrpId, pz_userId, pz1_userRegTime,
				payReslutCallBack)).start();
	}

}
