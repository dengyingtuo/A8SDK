package com.a8.zyfc.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.params.ClientPNames;

import android.content.Context;

public class HttpUtil {
	//private static final String BASE_URL = "http://cms.any8.com:8080/auser/action/do.htm";//旧地址
	private static final String BASE_URL = "http://a8sdk.3333.cn/auser2/action/do.htm";//新地址

	private static AsyncHttpClient client = new AsyncHttpClient();    

	static {
		/*// 设置cookie存储
		PersistentCookieStore cookieStore = new PersistentCookieStore();
		client.setCookieStore(cookieStore);*/

		// 并发最大连接数
		client.setMaxConnections(10);

		// 重试次数和超时时间
		client.setMaxRetriesAndTimeout(3, 8000);

		// 允许多次重定向
		client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		
	}

	// 防止实例化
	private HttpUtil() {}
	
	/**
	 * post请求(传递对象)
	 * @param context
	 * @param url
	 * @param mEntity
	 */
	public static void post(Context context, String url, HttpEntity mEntity) {
		client.post(context, url, mEntity, "application/json", new JsonHttpResponseHandler());
	}

	/**
	 * post请求(传递对象)
	 * @param context
	 * @param url
	 * @param mEntity
	 * @param responseHandler
	 */
	public static void post(Context context, String url, HttpEntity mEntity, AsyncHttpResponseHandler responseHandler) {
		client.post(context, getAbsoluteUrl(context, url), mEntity, "application/json", responseHandler);
	}
	
	/**
	 * post请求(传递对象)
	 * @param context
	 * @param mEntity
	 * @param responseHandler
	 */
	public static void post(Context context, HttpEntity mEntity, AsyncHttpResponseHandler responseHandler) {
		client.post(context, getAbsoluteUrl(context, ""), mEntity, "application/json", responseHandler);
	}
	
	/**
	 * get请求
	 * @param context
	 * @param url
	 * @param responseHandler
	 */
	public static void get(Context context, String url, AsyncHttpResponseHandler responseHandler){
		client.get(context, url, responseHandler);
	}
	
	/**
	 * 取消请求
	 * @param context
	 *          上下文对象
	 * @param mayInterruptIfRunning
	 *            是否打断当前正在运行的请求
	 */
	public static void cancel(Context context, boolean mayInterruptIfRunning) {
		client.cancelRequests(context, mayInterruptIfRunning);

	}

	public static String getAbsoluteUrl(Context context, String relativeUrl) {
		return BASE_URL + relativeUrl;
	}

}
