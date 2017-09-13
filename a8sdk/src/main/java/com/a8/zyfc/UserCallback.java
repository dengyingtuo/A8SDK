package com.a8.zyfc;

public abstract class UserCallback {

	public abstract void onSuccess(long uid, String sessionId ,String uName);

	public abstract void onFail(String msg);
}
