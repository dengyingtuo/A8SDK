package com.fingerfun.qmdlh.zyfc;

import com.a8.zyfc.A8SDKApi;
import com.squareup.leakcanary.LeakCanary;

import android.app.Application;

public class MyApplication extends Application {

	public static A8SDKApi a8sdk;
	
	@Override
	public void onCreate() {
		super.onCreate();
		if (LeakCanary.isInAnalyzerProcess(this)) {
			// This process is dedicated to LeakCanary for heap analysis.
			// You should not init your app in this process.
			return;
		}
		LeakCanary.install(this);
		a8sdk = A8SDKApi.init(this);
	}
	
	public static void exit(){
		a8sdk.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
}
