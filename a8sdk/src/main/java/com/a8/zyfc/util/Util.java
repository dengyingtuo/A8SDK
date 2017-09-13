package com.a8.zyfc.util;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.a8.zyfc.model.UserTO;

public class Util {
	public static int SCREEN_WIDTH = -1;
	public static int SCREEN_HEIGHT = -1;
	private static float SCALE_X = -1.0F;
	private static float DENSITY = -1.0F;

	private static TelephonyManager telMgr = null;

	private static void init(Context context) {
		DisplayMetrics display = context.getResources().getDisplayMetrics();
		SCREEN_WIDTH = display.widthPixels; // 屏幕宽度像素
		SCREEN_HEIGHT = display.heightPixels; // 屏幕高度像素

		if (SCREEN_WIDTH > SCREEN_HEIGHT) {
			int flag = SCREEN_WIDTH;
			SCREEN_WIDTH = SCREEN_HEIGHT;
			SCREEN_HEIGHT = flag;
		}
		DENSITY = display.density; // 屏幕密度

		SCALE_X = SCREEN_WIDTH / 1080.0F;
	}

	public static float getSCALE_X(Context context) {
		if (SCALE_X > 0.0F) {
			return SCALE_X;
		}
		init(context);
		return SCALE_X;
	}

	public static int getInt(Context context, int i) {
		return getIntForScalX(context, i);
	}

	private static int getIntForScalX(Context context, int i) {
		return (int) (i * getSCALE_X(context));
	}

	public static int getTextSize(Context context, int i) {
		return (int)(i * getSCALE_X(context) / DENSITY);
	}

	public static int getColor(Context context, String color) {
		return context.getResources().getColor(getColorId(context, color));
	}

	public static String getString(Context context, String string) {
		return context.getString(getStringId(context, string));
	}

	public static boolean isProt(Context context) {
		if (context.getResources().getConfiguration().orientation == 2) {
			return false;
		}
		return true;
	}

	public static void setSavePassword(Context context, boolean b) {
		sharedPreferencesSave("a8_password", b, context);
	}

	public static boolean isSavePassword(Context context) {
		return getFromSharedPreferences(context, "a8_password", true);
	}

	public static void saveUser(Context context, UserTO to) {
		sharedPreferencesSave("a8_uid", to.getUid(), context);
		sharedPreferencesSave("a8_username", to.getUserName(), context);
		sharedPreferencesSave("a8_isfast", to.isFast(), context);
		sharedPreferencesSave("a8_token", to.getToken(), context);
		sharedPreferencesSave("a8_logintype", to.getThirdType(), context);
	}	

	public static UserTO getUserTO(Context context) {
		UserTO to = new UserTO();
		to.setUid(getFromSharedPreferences("a8_uid", context, 0L));
		to.setUserName(getFromSharedPreferences("a8_username", context));
		to.setFast(getFromSharedPreferences(context, "a8_isfast", false));
		to.setToken(getFromSharedPreferences("a8_token", context));
		to.setThirdType(getFromSharedPreferences("a8_logintype", context, -1));
		return to;
	}

	public static void cleanUserTO(Context context) {
		sharedPreferencesSave("a8_uid", null, context);
		sharedPreferencesSave("a8_username", null, context);
		sharedPreferencesSave("a8_isfast", null, context);
		sharedPreferencesSave("a8_token", null, context);
		sharedPreferencesSave("a8_logintype", null, context);
	}

	public static boolean isLogined(Context context) {
		UserTO to = getUserTO(context);
		return (to != null) && (!TextUtils.isEmpty(to.getToken()));
	}

	public static void sharedPreferencesSave(String key, String value,
			Context context) {
		SharedPreferences pref = context.getSharedPreferences("PREFS_NAME", 1);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void sharedPreferencesSave(String key, long value,
			Context context) {
		SharedPreferences pref = context.getSharedPreferences("PREFS_NAME",
				1);
		SharedPreferences.Editor editor = pref.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static void sharedPreferencesSave(String key, boolean value,
			Context context) {
		SharedPreferences pref = context.getSharedPreferences("PREFS_NAME",
				1);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static void sharedPreferencesSave(String key, int value,
			Context context) {
		SharedPreferences pref = context.getSharedPreferences("PREFS_NAME",
				1);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static int getFromSharedPreferences(String key, Context context,
			int dafault) {
		SharedPreferences uiState = context.getSharedPreferences("PREFS_NAME",
				1);
		if (uiState == null) {
			return dafault;
		}
		return uiState.getInt(key, dafault);
	}

	public static long getFromSharedPreferences(String key, Context context,
			long dafault) {
		SharedPreferences uiState = context.getSharedPreferences("PREFS_NAME",
				1);
		if (uiState == null) {
			return dafault;
		}
		Long l = Long.valueOf(uiState.getLong(key, dafault));
		if (l == null) {
			l = Long.valueOf(0L);
		}
		return l.longValue();
	}

	public static String getFromSharedPreferences(String key, Context context,
			String dafault) {
		SharedPreferences uiState = context.getSharedPreferences("PREFS_NAME",
				1);
		if (uiState == null) {
			return dafault;
		}
		return uiState.getString(key, dafault);
	}

	public static boolean getFromSharedPreferences(Context context, String key,
			boolean dafault) {
		SharedPreferences uiState = context.getSharedPreferences("PREFS_NAME",
				1);
		if (uiState == null) {
			return dafault;
		}
		return uiState.getBoolean(key, dafault);
	}

	public static String getFromSharedPreferences(String key, Context context) {
		SharedPreferences uiState = context.getSharedPreferences("PREFS_NAME",
				1);
		if (uiState == null) {
			return null;
		}
		return uiState.getString(key, "");
	}

	/**
	 * 检查是否为正确的手机号码格式
	 * @param phone
	 * @return
	 */
	public static boolean isPhone(String phone) {
		if (!TextUtils.isEmpty(phone)) {
			return phone.matches("1+[0-9]+[0-9]{9}");
		}
		return false;
	}

	/**
	 * 检查网络   
	 * @param context
	 * @return 有网络返回true，无网络弹出Toast提示并返回false
	 */
	public static boolean checkNet(Context context) {
		if (hasConnectedNetwork(context)) {
			return true;
		}
		showToast(context, Util.getString(context, "a8_login_tips_not_network"));
		return false;
	}

	/**
	 * 获取手机的Mac地址
	 */
	public static String getMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);

		WifiInfo info = wifi.getConnectionInfo();
		String macAddress = info.getMacAddress();

		if (null == macAddress || macAddress.equals("")) {
			macAddress = "unknown";
		}
		return macAddress;

	}

	/**
	 * 检查网络
	 * @param context
	 * @return 有网络返回true，无网络返回false
	 */
	public static boolean hasConnectedNetwork(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		}
		return connectivity.getActiveNetworkInfo() != null;
	}

	/**
	 * Toast length_short
	 * @param context
	 * @param s
	 */
	public static void showToast(Context context, String s) {
		Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Toast length_long
	 * @param context
	 * @param s
	 */
	public static void showToastLong(Context context, String s) {
		Toast.makeText(context, s, Toast.LENGTH_LONG).show();
	}

	/**
	 * 获取布局资源id
	 * @param paramContext  上下文
	 * @param paramString   布局名字
	 * @return
	 */
	public static int getLayoutId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "layout",
				paramContext.getPackageName());
	}

	/**
	 * 获取字符串资源id
	 * @param paramContext  上下文
	 * @param paramString   字符串名字
	 * @return
	 */
	public static int getStringId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "string",
				paramContext.getPackageName());
	}

	/**
	 * 获取图片资源id
	 * @param paramContext  上下文
	 * @param paramString   图片名字
	 * @return
	 */
	public static int getDrawableId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString,
				"drawable", paramContext.getPackageName());
	}

	/**
	 * 获取样式资源id
	 * @param paramContext  上下文
	 * @param paramString   样式名字
	 * @return
	 */
	public static int getStyleId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "style",
				paramContext.getPackageName());
	}

	/**
	 * 获取id资源id
	 * @param paramContext  上下文
	 * @param paramString   id名字
	 * @return
	 */
	public static int getId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "id",
				paramContext.getPackageName());
	}

	/**
	 * 获取颜色资源id
	 * @param paramContext  上下文
	 * @param paramString   颜色名字
	 * @return
	 */
	public static int getColorId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "color",
				paramContext.getPackageName());
	}

	/**
	 * 获取原始文件资源id
	 * @param paramContext  上下文
	 * @param paramString   原始文件名字
	 * @return
	 */
	public static int getRawId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "raw",
				paramContext.getPackageName());
	}

	/**
	 * 
	 * @param context
	 * @param dataName
	 * @return manifest<metadata>
	 */
	public static String getApplicationData(Context context, String dataName) {
		ApplicationInfo appInfo;
		try {
			appInfo = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			String msg = String.valueOf(appInfo.metaData.get(dataName));
			return msg;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 唯一的设备ID 所有设备都有此ID,如果是GSM网络，返回IMEI；如果是CDMA网络，返回MEID
	 * 需要权限：android.permission.READ_PHONE_STATE
	 * 
	 * @return null if device ID is not available.
	 */
	public static String getDeviceId(Context context) {
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

	public static String getAppVersion(Context ctx){
		// 请求失败的情况下，重新请求次数
		int mRetryCount = 0;
		String versionName = "";
		PackageInfo pi = null;
		// 版本号获取失败会影响到更新，所以再失败的情况下要重试3次
		while ((null == versionName || versionName.equals(""))
				&& mRetryCount < 3) {
			try {
				pi = ctx.getPackageManager().getPackageInfo(
						ctx.getPackageName(), 0);
				versionName = pi.versionName;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			mRetryCount++;
		}

		return versionName;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWeixinAvilible(Context context) {  
		final PackageManager packageManager = context.getPackageManager();// 获取packagemanager  
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息  
		if (pinfo != null) {  
			for (int i = 0; i < pinfo.size(); i++) {  
				String pn = pinfo.get(i).packageName;  
				if (pn.equals("com.tencent.mm")) {  
					return true;  
				}  
			}  
		}  
		return false;  
	}  

	/**  
	 * 判断qq是否可用  
	 *   
	 * @param context  
	 * @return  
	 */  
	public static boolean isQQClientAvailable(Context context) {  
		final PackageManager packageManager = context.getPackageManager();  
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);  
		if (pinfo != null) {  
			for (int i = 0; i < pinfo.size(); i++) {  
				String pn = pinfo.get(i).packageName;  
				if (pn.equals("com.tencent.mobileqq")) {  
					return true;  
				}  
			}  
		}  
		return false;  
	}
	
	/**
	 * 获取wx版本号
	 * @param context
	 * @return
	 */
	public static String getWXVersion(Context context) {  
		final PackageManager packageManager = context.getPackageManager();// 获取packagemanager  
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息  
		if (pinfo != null) {  
			for (int i = 0; i < pinfo.size(); i++) {  
				String pn = pinfo.get(i).packageName;  
				if (pn.equals("com.tencent.mm")) {  
					return pinfo.get(i).versionName;  
				}  
			}  
		}  
		return "";  
	}  

	/**  
	 * 获取QQ版本号 
	 *   
	 * @param context  
	 * @return  
	 */  
	public static String getQQVersion(Context context) {  
		final PackageManager packageManager = context.getPackageManager();  
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);  
		if (pinfo != null) {  
			for (int i = 0; i < pinfo.size(); i++) {  
				String pn = pinfo.get(i).packageName;  
				if (pn.equals("com.tencent.mobileqq")) {  
					return pinfo.get(i).versionName;  
				}  
			}  
		}  
		return "";  
	}

	public static void byteArrayFromUrl(Context mContext, String imgUrl, Handler handler){
		new LoadPicThread(mContext, imgUrl, handler).start();
	}  


	public static final int IMAGE_SIZE=32*1024;//微信分享缩略图大小限制  32kb
	private static class LoadPicThread extends Thread{
		private Context cxt;
		private String url;  
		private Handler handler;  
		public LoadPicThread(Context mContext, String url,Handler handler){
			this.cxt = mContext;
			this.url = url;  
			this.handler = handler;  
		}  

		@Override  
		public void run(){  
			Message message = handler.obtainMessage();
			Bitmap bmp;
			try {
				if(!"".equals(url) && !url.startsWith("http")){
					bmp = BitmapFactory.decodeFile(url);
					message.obj = getZoomImage(bmp, IMAGE_SIZE);
				}else if(!"".equals(url) && url.startsWith("http")){
					URL picurl = new URL(url);   
					HttpURLConnection conn = (HttpURLConnection)picurl.openConnection(); // 获得连接   
					conn.setConnectTimeout(10000);//设置超时   
					conn.setDoInput(true);   
					conn.setUseCaches(false);//不缓存   
					conn.connect();  
					bmp = BitmapFactory.decodeStream(conn.getInputStream());  
					message.obj = getZoomImage(bmp, IMAGE_SIZE);
				}else{
					bmp = BitmapFactory.decodeResource(cxt.getResources(), cxt.getApplicationInfo().icon);
					message.obj = getZoomImage(bmp, IMAGE_SIZE);
				}

			} catch (Exception e) {
				bmp = BitmapFactory.decodeResource(cxt.getResources(), cxt.getApplicationInfo().icon);
				message.obj = getZoomImage(bmp, IMAGE_SIZE);
			}  
			message.sendToTarget(); 
		}  
	}

	/**
	 * 图片的缩放方法
	 *
	 * @param bitmap  ：源图片资源
	 * @param maxSize ：图片允许最大空间  单位:Byte
	 * @return
	 */
	public static byte[] getZoomImage(Bitmap bitmap, int maxSize) {
		if (null == bitmap) {
			return null;
		}
		if (bitmap.isRecycled()) {
			return null;
		}

		// 单位：从 Byte 换算成 KB
		int currentSize = bitmapToByteArray(bitmap, false).length;
		// 判断bitmap占用空间是否大于允许最大空间,如果大于则压缩,小于则不压缩
		while (currentSize > maxSize) {
			// 计算bitmap的大小是maxSize的多少倍
			double multiple = (double)currentSize / maxSize;
			// 开始压缩：将宽带和高度压缩掉对应的平方根倍
			// 1.保持新的宽度和高度，与bitmap原来的宽高比率一致
			// 2.压缩后达到了最大大小对应的新bitmap，显示效果最好
			bitmap = getZoomImage(bitmap, bitmap.getWidth() / Math.sqrt(multiple), bitmap.getHeight() / Math.sqrt(multiple));
			currentSize = bitmapToByteArray(bitmap, false).length;
		}
		return bitmapToByteArray(bitmap, true);
	}

	/**
	 * 图片的缩放方法
	 *
	 * @param orgBitmap ：源图片资源
	 * @param newWidth  ：缩放后宽度
	 * @param newHeight ：缩放后高度
	 * @return
	 */
	public static Bitmap getZoomImage(Bitmap orgBitmap, double newWidth, double newHeight) {
		if (null == orgBitmap) {
			return null;
		}
		if (orgBitmap.isRecycled()) {
			return null;
		}
		if (newWidth <= 0 || newHeight <= 0) {
			return null;
		}

		// 获取图片的宽和高
		float width = orgBitmap.getWidth();
		float height = orgBitmap.getHeight();
		// 创建操作图片的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(orgBitmap, 0, 0, (int) width, (int) height, matrix, true);
		return bitmap;
	}

	/**
	 * bitmap转换成byte数组
	 *
	 * @param bitmap
	 * @param needRecycle
	 * @return
	 */
	public static byte[] bitmapToByteArray(Bitmap bitmap, boolean needRecycle) {
		if (null == bitmap) {
			return null;
		}
		if (bitmap.isRecycled()) {
			return null;
		}

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bitmap.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取手机型号
	 *
	 * @return model
	 */
	public static String getModel() {
		String model = android.os.Build.MODEL; // 手机型号
		if (null == model) {
			model = "unknown";
		}
		return model;
	}

	/**
	 * 获取系统版本
	 *
	 * @return release
	 */
	public static String getRelease() {
		String release = android.os.Build.VERSION.RELEASE;
		if (null == release) {
			release = "unknown";
		}
		return release;
	}

}
