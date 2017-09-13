package com.a8.zyfc;

public interface ShareCallBack {
	
	static final int SHARE_OK = 0;    //分享成功
	static final int SHARE_CANCEL = 1;//取消分享
	static final int SHARE_ERR = 2;   //分享出错
	
	void onCallBack(int code);
}
