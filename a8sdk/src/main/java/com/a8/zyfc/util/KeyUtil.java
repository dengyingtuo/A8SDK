package com.a8.zyfc.util;

import android.content.Context;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

public class KeyUtil {
	private static TelephonyManager telMgr = null;

	public static String getKey(Context context) {

		telMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String id = telMgr.getDeviceId();
		if (id == null) {
			id = telMgr.getSubscriberId();
		}
		if (id == null) {
			id = Secure.getString(context.getContentResolver(),
					Secure.ANDROID_ID);
		}
		return id;
	}
}
