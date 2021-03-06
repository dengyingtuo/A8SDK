package com.a8.zyfc.pay.third.alipay;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.a8.zyfc.pay.util.HttpRequest;
import com.a8.zyfc.pay.third.order.Order;

//import com.app.pay.third.order.Dom4j;

public class PayFun {
	private static Toast mToast;

	/**
	 *
	 * @return
	 */
	public static String getPayInfo(String orderNumber, String price,
			String subject, String body) {

		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("orderNumber", orderNumber == null ? "" : orderNumber);
			jsonObj.put("price", price == null ? "" : price);
			jsonObj.put("subject", subject == null ? "" : subject);
			jsonObj.put("body", body == null ? "" : body);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String reqData = jsonObj.toString();
		String resData = HttpRequest.postData(Order.PAY_URL_ALI, "5", "UTF-8",
				reqData);
		return resData;
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

	/**
	 * 提示
	 * 
	 * @param content
	 * @param context
	 */
	public static void makeToast(Context context, String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
		} else {
			mToast.setText(msg);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
	}

}
