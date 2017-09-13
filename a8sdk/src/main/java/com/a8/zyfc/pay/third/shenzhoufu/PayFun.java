package com.a8.zyfc.pay.third.shenzhoufu;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import com.a8.zyfc.pay.third.order.Order;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class PayFun {

	public static String getPayInfo(String orderId, String payMoney,
			String cardMoney, String sn, String password, String cardTypeCombine // 0：移动
																					// 1：联通
																					// 2：电信
	) {
		return getHttpCode(orderId, payMoney, cardMoney, sn, password, cardTypeCombine);
//		return postHttpCode(orderId, payMoney, cardMoney, sn, password, cardTypeCombine);
	}
	
//	private static String postHttpCode(String orderId, String payMoney,
//			String cardMoney, String sn, String password, String cardTypeCombine ){
//		JSONObject jsonObj = new JSONObject();
//		try {
//			jsonObj.put("orderId", orderId == null ? "" : orderId);
//			jsonObj.put("payMoney", payMoney == null ? "" : payMoney);
//			jsonObj.put("cardMoney", cardMoney == null ? "" : cardMoney);
//			jsonObj.put("sn", sn == null ? "" : sn);
//			jsonObj.put("password", password == null ? "" : password);
//			jsonObj.put("cardTypeCombine", cardTypeCombine == null ? "" : cardTypeCombine);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		String reqData = jsonObj.toString();
//		String resData = HttpRequest.postData(Order.PAY_URL_SZF, "5","UTF-8", reqData);
//		return resData;
//	}

	private static String getHttpCode(String orderId, String payMoney,
			String cardMoney, String sn, String password, String cardTypeCombine) {
		HttpURLConnection httpurlconnection = null;
		try {
			URL url = new URL(Order.PAY_URL_SZF + "?orderId=" + orderId
					+ "&payMoney=" + payMoney + "&cardMoney=" + cardMoney
					+ "&sn=" + sn + "&password=" + password
					+ "&cardTypeCombine=" + cardTypeCombine);
			httpurlconnection = (HttpURLConnection) url.openConnection();
			httpurlconnection.setRequestMethod("POST");
			httpurlconnection.setRequestProperty("content-type", "text/plain");
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

	public static void closeProgress(ProgressDialog mProgress) {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
