package com.a8.zyfc.pay.util;

import java.io.IOException;
import java.io.StringReader;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

public class PayUtil {

	private static TelephonyManager telMgr = null;
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
    /**
     * 获取手机的Mac地址
     */
    public static String getMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        WifiInfo info = wifi.getConnectionInfo();
        String macAddress = info.getMacAddress();

        if (null == macAddress || macAddress.equals("")) {
            macAddress = "unknown";
        }
        return macAddress;

    }
	// private static ConnectivityManager connMgr = null;

	/**
	 * 判断是否有网络
	 * 
	 * @return true/false
	 */
	public static boolean checkNetwork1(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.getState() == NetworkInfo.State.CONNECTED)
			return true;
		return false;
	}

	/**
	 * 检测用户网络连接
	 * 
	 * @param cxt
	 *            Context实例
	 * @return 0有2G/3G/wifi; 1无网络
	 */
	public static String checkNetwork2(Context context) {
		if (checkNetwork1(context))
			return "0";
		else
			return "1";
	}

	/**
	 * 取运营商
	 */
	public static boolean checkSimOperator(String simOperator) {
		if ("00".equals(simOperator) || "01".equals(simOperator)
				|| "03".equals(simOperator)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取运营商getSimOperator()
	 * 
	 * @param cxt
	 *            Context实例
	 * @return "00"中国移动; "01"中国联通; "03"中国电信
	 */
	public static String getSimOperator(Context context) {
		String simOperator = "";
		telMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		String operator = telMgr.getSimOperator();
		if (null != operator) {
			if ("46000".equals(operator) || "46002".equals(operator)
					|| "46007".equals(operator)) {
				// 中国移动
				simOperator = "00";
			} else if ("46001".equals(operator)) {
				// 中国联通
				simOperator = "01";
			} else if ("46003".equals(operator)) {
				// 中国电信
				simOperator = "03";
			}
		}
		return simOperator;
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
			String msg = appInfo.metaData.getString(dataName);
			return msg;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 判断GPS是否开启，GPS开启一个就认为是开启的
	 * 
	 * @param context
	 * @return true 表示开启
	 */
	public static boolean isOPen(Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		boolean gps = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (gps) {
			return true;
		}
		return false;
	}

	/**
	 * 随机数（数字） Math.random()
	 * 
	 * @param len
	 * @return
	 */
	public static String rand(int len) {
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i < len; i++) {
			sbf.append(Integer.toString((int) (Math.random() * 10)));
		}
		return sbf.toString();
	}

	/**
	 * 时间戳
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String date15() {
		return  new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()).substring(2);
	}

	/**
	 * 随机数（ 数字+字母） UUID.randomUUID()
	 * 
	 * @param len
	 * @return
	 */
	public static String createUUID(int len) {
		String uuid = java.util.UUID.randomUUID().toString()
				.replaceAll("-", "");
		return uuid.substring(0, len);
	}

	/**
	 * 取经纬度
	 * 
	 * @param context
	 * @return
	 */
	public static String getLocal(Context context) {
		String loc = "";
		LocationManager loctionManager;
		loctionManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		String provider = LocationManager.NETWORK_PROVIDER;

		Location location = loctionManager.getLastKnownLocation(provider);

		if (null != location) {
			loc = location.getLongitude() + "," + location.getLatitude();
		} else {

		}
		return loc;
	}

	/**
	 * 
	 * @param longitude
	 * @param latitude
	 * @return 省市
	 */
	public static String getProvinceCity(String longitude, String latitude) {
		return getAreaByGeocoding(longitude, latitude);
	}

	public static String getAreaByGeocoding(String longitude, String latitude) {
		// http://api.map.baidu.com/geocoder/v2/?ak=B2a189f2751a73c9b0adf73af546f483&location=22.542046,113.949185&output=xml&pois=0
		if (longitude == null || latitude == null) {
			return null;
		}
		StringBuffer bf = new StringBuffer();
		bf.append("http://api.map.baidu.com/geocoder/v2/").append("?ak=")
				.append("B2a189f2751a73c9b0adf73af546f483")
				.append("&location=").append(latitude).append(",")
				.append(longitude).append("&output=xml&pois=0");

		String url = bf.toString();
		String province = null;
		String city = null;
		try {
			String resData = HttpRequest.getData(url, "10", "UTF-8");
			Document doc = stringToDoc(resData);
			province = doc.getElementsByTagName("province").item(0)
					.getFirstChild().getNodeValue();
			city = doc.getElementsByTagName("city").item(0).getFirstChild()
					.getNodeValue();
		} catch (Exception e) {
		}
		if (province == null) {
			return null;
		}
		return province + "," + city;
	}

	/**
	 * String 转 XML org.w3c.dom.Document
	 */
	public static Document stringToDoc(String xmlStr) {
		// 字符串转XML
		Document doc = null;
		try {
			xmlStr = new String(xmlStr.getBytes(), "UTF-8");
			StringReader sr = new StringReader(xmlStr);
			InputSource is = new InputSource(sr);
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			doc = builder.parse(is);

		} catch (ParserConfigurationException e) {
			System.err.println(xmlStr);
			e.printStackTrace();
		} catch (SAXException e) {
			System.err.println(xmlStr);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println(xmlStr);
			e.printStackTrace();
		}
		return doc;
	}


	public static List<Map<String, Object>> getJsonListMap(String jsonArrayString) {
		JSONArray jsonList;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		try {
			jsonList = new JSONArray(jsonArrayString);
			JSONObject obj;
			for (int i = 0, j = jsonList.length(); i < j; i++) {
				try {
					obj = jsonList.getJSONObject(i);
					map = new HashMap<String, Object>();
					map.put("orders", obj.getString("orders"));
					map.put("result", obj.getString("result"));
					map.put("goodsName", obj.getString("goodsName"));
					map.put("tm", obj.getString("tm"));
					list.add(map);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return list;
	}
	
    /**
     * 构成session
     *
     * @param s
     * @return
     */
    public static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
