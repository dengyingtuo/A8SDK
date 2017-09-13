package com.a8.zyfc.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.AsyncTask;

/**
 * 通过http访问应用服务器，获取http返回结果
 */
public class CustomHttpTask extends AsyncTask<String, Void, String> {

	private static final int MAX_RETRY_TIME = 3;

	// 请求失败的情况下，重新请求次数
	private int mRetryCount;

	private CustomHttpListener mListener;

	private boolean mIsHttpPost;

	private String params;

	private Context mContext;


	public CustomHttpTask(Context context) {
		mContext = context;
	}

	public void doPost(CustomHttpListener listener, String params, String url) {
		this.mListener = listener;
		mIsHttpPost = true;
		this.params = params;
		this.mRetryCount = 0;
		execute(url);
	}

	public void doGet(CustomHttpListener listener, String url) {
		this.mListener = listener;
		this.mIsHttpPost = false;
		this.mRetryCount = 0;

		execute(url);
	}

	@Override
	protected String doInBackground(String... params) {

		String response = null;
		while (null == response && mRetryCount < MAX_RETRY_TIME) {

			if (isCancelled())
				return null;
			try {
				String uri = params[0];
				response = executeHttp(mContext, uri);
			} catch (SSLHandshakeException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			mRetryCount++;
		}

		return response;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		if (mListener != null) {
			mListener.onCancelled();
			mListener = null;
		}
	}

	@Override
	protected void onPostExecute(String response) {
		super.onPostExecute(response);
		if (mListener != null && !isCancelled()) {
			mListener.onResponse(response);
			mListener = null;
		}
	}

	private String executeHttp(Context context, String uri)
			throws SSLHandshakeException, ClientProtocolException, IOException {
		return mIsHttpPost ? csdkPost(context, uri) : csdkGet(context, uri);

	}

	private String csdkPost(Context context, String uri) {

		String result = null; // 用来取得返回的String；

		HttpParams httpParams = new BasicHttpParams();
		httpParams.setParameter("charset", "UTF-8");
		HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 1000); // 毫秒
		HttpConnectionParams.setSoTimeout(httpParams, 10 * 1000);
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		// 发送post请求
		HttpPost httpRequest = new HttpPost(uri);
		httpRequest.addHeader("Content-Type", "application/json");
		httpRequest.addHeader("charset", HTTP.UTF_8);
		HttpResponse httpResponse = null;
		try {
			httpRequest.setEntity(new StringEntity(params, "UTF-8"));

			httpResponse = httpClient.execute(httpRequest);
			// 若状态码为200则请求成功，取到返回数据
			if (null != httpResponse
					&& httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取出字符串
				result = EntityUtils
						.toString(httpResponse.getEntity(), "UTF-8");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private String csdkGet(Context context, String uri) {
		String result = null; // 用来取得返回的String；

		HttpParams httpParams = new BasicHttpParams();
		httpParams.setParameter("charset", "UTF-8");
		HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 1000); // 毫秒
		HttpConnectionParams.setSoTimeout(httpParams, 10 * 1000);
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		// 发送post请求
		HttpGet httpRequest = new HttpGet(uri);

		HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpRequest);
			// 若状态码为200则请求成功，取到返回数据
			if (null != httpResponse
					&& httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取出字符串
				result = EntityUtils
						.toString(httpResponse.getEntity(), "UTF-8");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
