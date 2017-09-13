package com.a8.zyfc.pay.third.order;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

public class OrderResult {

	static String URLs[] = new String[] {
		//http://cms.any8.com:8080
		//0支付宝1神州付2银联3易宝4MO9
			"http://a8sdk.3333.cn/pay_sdk/ali/getResult.htm?orderid=${orderid}",
			"http://a8sdk.3333.cn/pay_sdk/szf/getResult.htm?orderid=${orderid}",
			"http://a8sdk.3333.cn/pay_sdk/upomp/getResult.htm?orderid=${orderid}",
			"http://a8sdk.3333.cn/pay_sdk/yibao/getResult.htm?orderid=${orderid}",
			"http://a8sdk.3333.cn/pay_sdk/mo9/getResult.htm?orderid=${orderid}" };

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// System.out.println(getResult(0, "0724145009-2063"));
		// System.out.println(getResult(1, "20130723-159318-b7b1a6a98916"));
		// System.out.println(getResult(2, "528563077160"));
		// System.out.println(getResult(3, "a8881697634c4938-0906180833"));
	}


	/**
	 * 
	 * @param orderid
	 *            定单号
	 * @param type
	 *            0支付宝1神州付2银联3易宝4MO9
	 * @return -2网络异常-1服务器异常0定购失败1定购成功2没有结果
	 */
	private static String getHttpCode(String orderid, int type) {
		HttpURLConnection httpurlconnection = null;
		try {
			String urladd = URLs[type].replace("${orderid}", orderid);
			// System.out.println("urladd=" + urladd);
			URL url = new URL(urladd);
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
			// System.out.println("respCode=" + respCode);
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

	/**
	 * 
	 * @param type
	 *            0支付宝1神州付2银联4MO9
	 * @param orderid
	 *            定单号
	 * @return -2网络异常-1服务器异常0定购失败1定购成功2没有结果
	 */
	public static int getResult(int type, String orderid) {
		String respCode = getHttpCode(orderid, type);
		if (respCode.equals(""))
			return -2;
		return Integer.parseInt(respCode);
	}
}
