package com.fingerfun.qmdlh.zyfc;


import com.a8.zyfc.A8SDKApi;
import com.a8.zyfc.UserCallback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends Activity{
	
	private TextView log;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		log = (TextView)findViewById(R.id.log);
	}
	
	private UserCallback mUserCallback = new UserCallback() {
		
		@Override
		public void onSuccess(long uid, String sessionId, String uName) {
			findViewById(R.id.go).setVisibility(View.VISIBLE);
			Log.e("a8sdk", "uid:" + uid + "sessionId:" + sessionId + "uName:" + uName);
			log.setText("登录成功！\nuid:" + uid + "\nsessionId:" + sessionId + "\nuName:" + uName);
		}
		
		@Override
		public void onFail(String msg) {
			log.setText(msg);
		}
	};
	
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.phone:
			MyApplication.a8sdk.A8Login(this, A8SDKApi.TYPE_PHONE_LOGIN, mUserCallback);
			break;
		case R.id.wechat:
			MyApplication.a8sdk.A8Login(this, A8SDKApi.TYPE_WX_LOGIN, mUserCallback);
			break;
		case R.id.qq:
			MyApplication.a8sdk.A8Login(this, A8SDKApi.TYPE_QQ_LOGIN, mUserCallback);
			break;
		case R.id.go:
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		MyApplication.a8sdk.onLoginActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

}
