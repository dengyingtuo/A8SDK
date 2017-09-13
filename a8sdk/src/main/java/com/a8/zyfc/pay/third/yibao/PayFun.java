package com.a8.zyfc.pay.third.yibao;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.a8.zyfc.pay.third.order.Order;

public class PayFun {

	/**
	 * -100 交易签名被篡改 -1：签名较验失败或未知错误 2：卡密成功处理过或者提交卡号过于频繁 5：卡数量过多，目前最多支持10张卡
	 * 11：订单号重复 66：支付金额有误 95：支付方式未开通 112：业务状态不可用，未开通此类卡业务 8001：卡面额组填写错误
	 * 8002：卡号密码为空或者数量不相等（使用组合支付时）
	 */
	static final Map<String, String> ERRORMSG = new HashMap<String, String>();
	static {
		ERRORMSG.put("-100", "交易签名被篡改!");
		ERRORMSG.put("-1", "签名较验失败或未知错误!");
		ERRORMSG.put("2", "卡密成功处理过或者提交卡号过于频繁!");
		ERRORMSG.put("5", "卡数量过多，目前最多支持10张卡!");
		ERRORMSG.put("11", "此订单已提交过!");
		ERRORMSG.put("66", "支付金额有误，请正确填写!");
		ERRORMSG.put("95", " 交易签名被篡改!");
		ERRORMSG.put("112", "暂不支付些支付方式!");
		ERRORMSG.put("8001", "卡面额组填写错误!");
		ERRORMSG.put("8002", "卡号密码为空或者数量不相等!");
	}

	public static String requestPay(String p2_Order, float p3_Amt,
			boolean p4_verifyAmt, String p5_Pid, String p6_Pcat,
			String p7_Pdesc, String pa7_cardAmt, String pa8_cardNo,
			String pa9_cardPwd, String pd_FrpId, String pz_userId,
			String pz1_userRegTime) {
		return getHttpCode(p2_Order, p3_Amt, p4_verifyAmt, p5_Pid, p6_Pcat,
				p7_Pdesc, pa7_cardAmt, pa8_cardNo, pa9_cardPwd, pd_FrpId,
				pz_userId, pz1_userRegTime);
	}

	private static String getHttpCode(String p2_Order, float p3_Amt,
			boolean p4_verifyAmt, String p5_Pid, String p6_Pcat,
			String p7_Pdesc, String pa7_cardAmt, String pa8_cardNo,
			String pa9_cardPwd, String pd_FrpId, String pz_userId,
			String pz1_userRegTime) {
		HttpURLConnection httpurlconnection = null;
		try {
			URL url = new URL(Order.PAY_URL_YIBAO + "?p2_Order=" + p2_Order
					+ "&p3_Amt=" + p3_Amt + "&p4_verifyAmt=" + p4_verifyAmt
					+ "&p5_Pid=" + p5_Pid + "&p6_Pcat=" + p6_Pcat
					+ "&p7_Pdesc=" + p7_Pdesc + "&pa7_cardAmt=" + pa7_cardAmt
					+ "&pa8_cardNo=" + pa8_cardNo + "&pa9_cardPwd="
					+ pa9_cardPwd + "&pd_FrpId=" + pd_FrpId + "&pz_userId="
					+ pz_userId + "&pz1_userRegTime=" + pz1_userRegTime);
			httpurlconnection = (HttpURLConnection) url.openConnection();
			httpurlconnection.setRequestMethod("POST");
			// httpurlconnection.setRequestProperty("content-type",
			// "text/plain");
			httpurlconnection.setDoOutput(true);
			httpurlconnection.setDoInput(true);
		} catch (Exception e) {
			return "";
		}
		try {
			int respCode = httpurlconnection.getResponseCode();
			if (200 != respCode) {
				return "";
			}
			InputStream is = httpurlconnection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"));
			StringBuilder responseBuilder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				// Log.v("resquest130", line);
				if (line.trim().length() > 0)
					responseBuilder.append(line);
			}
			return URLDecoder.decode(responseBuilder.toString(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpurlconnection != null)
				httpurlconnection.disconnect();
		}
		return "";
	}

	public static void alert(Context context, String text) {
		Toast msg = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
				msg.getYOffset() / 2);
		msg.show();
	}

	public static void about(final Context context, String text) {
		new AlertDialog.Builder(context).setTitle("提示").setMessage(text)
				.setPositiveButton("确认", null).show();
	}
}
