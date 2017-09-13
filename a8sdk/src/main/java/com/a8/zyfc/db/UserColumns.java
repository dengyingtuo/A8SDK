package com.a8.zyfc.db;

public class UserColumns {
	
	public static final String MID = "MID";
	public static final String TOKEN = "TOKEN";
	public static final String USERNAME = "USERNAME";
	public static final String NICKNAME = "NICKNAME";
	public static final String PASSWORD = "PASSWORD";
	public static final String LAST_LOGIN_TIME = "LAST_LOGIN_TIME";
	public static final String LAST_TIP_TIME = "LAST_TIP_TIME";
	public static final String IS_FAST = "IS_FAST";
	public static final String LOGIN_TYPE = "LOGIN_TYPE";
  
	public static String[] getAll() {
		return new String[] { "MID", "TOKEN", "USERNAME", "NICKNAME", "PASSWORD", 
				"LAST_LOGIN_TIME", "LAST_TIP_TIME", "IS_FAST", "LOGIN_TYPE"};
	}

}
