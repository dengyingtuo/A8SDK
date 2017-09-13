package com.a8.zyfc.pay.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class HttpRequest {



	/***
	 * 提供循環發送請求 3次
	 * @param uri
	 * @param scd
	 * @param charSet
	 * @param postData
	 * @return
	 */

	public static String postDataReaperThreed(String uri, String scd, String charSet, String postData) {
		return postData(uri, scd, charSet, postData, 3);
	}
	/***
	 * 提供循環發送請求
	 * @param uri
	 * @param scd
	 * @param charSet
	 * @param postData
	 * @param repeatTimes
	 * @return
	 */

	public static String postData(String uri, String scd, String charSet, String postData,int repeatTimes) {
		String strreturn = "";
		for (int i = 0; i < repeatTimes; i++) {
			strreturn = postData(uri, scd, charSet, postData);
			if (!"".equals(strreturn)) {
				break;
			}
		}
		return strreturn;
	}


	/**
	 * HttpPost方式请求
	 *
	 * @param uri
	 *            请求地址
	 * @param scd
	 *            请求超时设置(单位:秒 填0时默认为20)
	 * @param charSet
	 *            编码类型
	 * @param postData
	 *            请求数据
	 * @return 返回数据
	 */
	public static String postData(String uri, String scd, String charSet, String postData) {
		String retStr = "";
		int tmout = 20;
		try {
			tmout = Integer.parseInt(scd);
		} catch (Exception e) {
			// e.printStackTrace();
		}

		if (tmout <= 0)
			tmout = 20;

		try {
			HttpParams httpParams = new BasicHttpParams();
			httpParams.setParameter("charset", charSet);
			HttpConnectionParams.setConnectionTimeout(httpParams, tmout * 1000); // 毫秒
			HttpConnectionParams.setSoTimeout(httpParams, tmout * 1000);
			HttpClient httpClient = new DefaultHttpClient(httpParams);

			HttpPost httpPost = new HttpPost(uri);
			httpPost.setEntity(new StringEntity(postData.toString(), charSet));
			HttpResponse response;
			response = httpClient.execute(httpPost);
			// 检验状态码，如果成功接收数据
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				retStr = EntityUtils.toString(response.getEntity(), charSet);
			}
		} catch (Exception e) {
			retStr = "";
		}
		return retStr;
	}

	/**
	 * HttpGet方式请求
	 * 
	 * @param uri
	 *            请求地址
	 * @param scd
	 *            请求超时设置(单位:秒 填0时默认为20)
	 * @param charSet
	 *            编码类型
	 * @return 返回数据
	 */
	public static String getData(String uri, String scd, String charSet) {
		String retStr = "";
		int tmout = 20;
		try {
			tmout = Integer.parseInt(scd);
		} catch (Exception e) {
			// e.printStackTrace();
		}

		if (tmout <= 0)
			tmout = 20;
		try {
			HttpParams httpParams = new BasicHttpParams();
			httpParams.setParameter("charset", charSet);
			HttpConnectionParams.setConnectionTimeout(httpParams, tmout * 1000); // 毫秒
			HttpConnectionParams.setSoTimeout(httpParams, tmout * 1000);
			HttpClient httpClient = new DefaultHttpClient(httpParams);

			HttpGet httpGet = new HttpGet(uri);
			HttpResponse response;
			response = httpClient.execute(httpGet);
			// 检验状态码，如果成功接收数据
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				retStr = EntityUtils.toString(response.getEntity(), charSet);
			}
		} catch (Exception e) {
			retStr = "";
		}
		return retStr;
	}
}
