package com.a8.zyfc.pay.third.wxpay;

import org.json.JSONObject;

import com.a8.zyfc.AppConfig;
import com.a8.zyfc.util.Util;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class WXPayBaseActivity extends Activity implements IWXAPIEventHandler{
	
	private String wxAppId;
	public IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("a8", "启动微信");
		super.onCreate(savedInstanceState);
		init();
		wxPay(getIntent().getStringExtra("wxpayInfo"));
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}
	
	public void init(){
		wxAppId = AppConfig.WXAPPID;
		api = WXAPIFactory.createWXAPI(getApplicationContext(), wxAppId);
		api.registerApp(wxAppId);
		api.handleIntent(getIntent(), this);
		if(!api.isWXAppInstalled()){
			Toast.makeText(this, Util.getStringId(this, "wx_app_install"), Toast.LENGTH_SHORT).show();
			finish();
		}
		if(!api.isWXAppSupportAPI() || api.getWXAppSupportAPI()<Build.PAY_SUPPORTED_SDK_INT){
			Toast.makeText(this, Util.getStringId(this, "wx_app_update"), Toast.LENGTH_SHORT).show();
			finish();
		}
	}
	
	public void wxPay(String payInfo){
		Log.i("a8", "wxAct:payInfo="+payInfo);
		try {
			PayReq request = new PayReq();
			JSONObject obj = new JSONObject(payInfo);
			request.appId = wxAppId;
			request.partnerId = obj.getString("partnerid");
			request.prepayId= obj.getString("prepayid");
			request.packageValue = obj.getString("package");
			request.nonceStr= obj.getString("noncestr");
			request.timeStamp= obj.getString("timestamp");
			request.sign= obj.getString("sign");
			api.sendReq(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onReq(BaseReq req) {
		finish();
	}

	@Override
	public void onResp(BaseResp resp) {
		Intent intent = new Intent("com.a8.zyfc.sdk.wxpay");
		intent.putExtra("errCode", resp.errCode);
		Log.i("a8", "wxP_Act:errCode="+String.valueOf(resp.errCode)+";errStr="+resp.errStr);
		sendBroadcast(intent);
		finish();
	}
}
