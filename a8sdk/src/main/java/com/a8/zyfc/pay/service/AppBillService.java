package com.a8.zyfc.pay.service;

import org.json.JSONObject;

import android.content.Context;

import com.a8.zyfc.pay.util.HttpRequest;
import com.a8.zyfc.pay.util.PayUtil;
import com.a8.zyfc.AppConfig;

public class AppBillService {
	private final Context context;

	public AppBillService(Context context) {
		this.context = context;
	}

	/**
	 * 取支付列表信息
	 */
	public String getPayList(String cpParam, String payId, String fee) {

		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("version", AppConfig.VERSION);
			jsonObj.put("userId", AppConfig.DEVICEID);
			jsonObj.put("appKey", AppConfig.AKEY);
			jsonObj.put("subChannel", AppConfig.ACHANNEL);
			jsonObj.put("cpParams", cpParam == null ? "" : cpParam);
			jsonObj.put("goodsId", payId == null ? "" : payId);
			jsonObj.put("fee", fee == null ? "" : fee);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String reqData = jsonObj.toString();
		String resData = HttpRequest.postDataReaperThreed(AppConfig.PAYLISTUI, "5", "UTF-8", reqData);
		return resData;
	}

	/**
	 *
	 * @param sdkOrderId sdk订单号
	 * @param fee        单价
	 * @param payType    支付类型（1：支付宝；2：银联；10：微信）
	 * @param goodsId    商品ID
	 * @param goodsName  商品名称
	 * @param uid        a8账号id
	 * @param uname      a8账号名称
	 * @param roleId     角色id
	 * @param roleName   角色名称
	 * @param cpOrderId  cp订单号
	 * @param cpParam    cp透传参数
	 * @return
	 */
	public String savePayInfo(String sdkOrderId, String fee, String payType, String goodsId, String goodsName,
							  String uid, String uname, String roleId, String roleName, String cpOrderId, String cpParam) {
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("tradeNo", sdkOrderId == null ? "" : sdkOrderId);
			jsonObj.put("fee", fee == null ? "" : fee);
			jsonObj.put("payType", payType == null ? "" : payType);
			jsonObj.put("payId", goodsId == null ? "" : goodsId);
			jsonObj.put("ware", goodsName == null ? "" : goodsName);
			jsonObj.put("userId", uid == null ? "" : uid);
			jsonObj.put("accounts", uname == null ? "" : uname);
			jsonObj.put("roleId", roleId == null ? "" : roleId);
			jsonObj.put("roleName", roleName == null ? "" : roleName);
			jsonObj.put("customInfo", cpOrderId == null ? "" : cpOrderId);
			jsonObj.put("cpParam", cpParam == null ? "" : cpParam);
			jsonObj.put("appKey", AppConfig.AKEY);
			jsonObj.put("channel", AppConfig.ACHANNEL);
			jsonObj.put("version", AppConfig.VERSION);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String reqData = jsonObj.toString();
		String resData = HttpRequest.postDataReaperThreed(AppConfig.SAVEPAYINFOUI, "5", "UTF-8", reqData);
		return resData;
	}

	public void AppPayInit() {

		AppConfig.AKEY = PayUtil.getApplicationData(context, "A_KEY");
		AppConfig.ACHANNEL = PayUtil.getApplicationData(context, "csdkSubChannelId");
		AppConfig.WXAPPID = PayUtil.getApplicationData(context, "WX_APPID");
		AppConfig.DEVICEID = PayUtil.getDeviceId(context);
		AppConfig.MACADDRESS = PayUtil.getMacAddress(context);
		AppConfig.SESSIONID = PayUtil.MD5(PayUtil.createUUID(16));
		AppConfig.DEVICEMODEL = PayUtil.getModel();
		AppConfig.SYSRELEASE = PayUtil.getRelease();
	}

}
