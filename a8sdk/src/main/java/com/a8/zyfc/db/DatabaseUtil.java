package com.a8.zyfc.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.a8.zyfc.model.UserTO;
import com.a8.zyfc.util.DES;

public class DatabaseUtil {
	private static DatabaseUtil instance;
	private DBHelper mDbHelper;

	public static synchronized DatabaseUtil getInstance(Context context) {
		if (instance == null) {
			instance = new DatabaseUtil(context);
		}
		return instance;
	}

	private DatabaseUtil(Context context) {
		mDbHelper = new DBHelper(context);
	}

	public static void destory() {
		if (instance != null) {
			instance.onDestory();
		}
	}

	public void onDestory() {
		instance = null;
		if (mDbHelper != null) {
			mDbHelper.close();
			mDbHelper = null;
		}
	}

	/**
	 * 获取全部有登陆记录的用户
	 * @return
	 */
	public UserTO[] getAllUser() {
		return getAllUser(-1);
	}

	/**
	 * 获取有登陆记录的用户，按登陆时间获取最近的max条记录
	 * @param max
	 * @return
	 */
	public UserTO[] getAllUser(int max) {
		Cursor cursor = null;
		try {
			cursor = getUser(UserColumns.getAll(), null, null,"LAST_LOGIN_TIME desc");
			if (cursor != null) {
				int m = max;
				if ((max < 0) || (cursor.getCount() < m)) {
					m = cursor.getCount();
				}
				UserTO[] arr = new UserTO[m];
				int i = 0;
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
					UserTO to = new UserTO();
					to.setUid(cursor.getLong(cursor.getColumnIndex("MID")));
					to.setToken(cursor.getString(cursor.getColumnIndex("TOKEN")));
					to.setUserName(cursor.getString(cursor.getColumnIndex("USERNAME")));
					to.setNickName(cursor.getString(cursor.getColumnIndex("NICKNAME")));
					to.setPassword(cursor.getString(cursor.getColumnIndex("PASSWORD")));
					if (!TextUtils.isEmpty(to.getPassword())) {
						try {
							to.setPassword(DES.decrypt(to.getPassword(),DES.PASSWORD_CRYPT_KEY));
						} catch (Exception localException) {
						}
					}
					to.setLastLoginTime(cursor.getLong(cursor.getColumnIndex("LAST_LOGIN_TIME")));
					to.setFast((cursor.getInt(cursor.getColumnIndex("IS_FAST")) == 1));
					to.setThirdType(cursor.getInt(cursor.getColumnIndex("LOGIN_TYPE")));
					arr[i] = to;
					i++;
					if (i == max) {
						break;
					}
				}
				return arr;
			}
		} catch (Exception localException1) {
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		if (cursor != null) {
			cursor.close();
		}
		return new UserTO[0];
	}

	
	public UserTO getUserByUid(long uid) {
		Cursor cursor = null;
		UserTO to = null;
		cursor = getUser(UserColumns.getAll(), "MID=?", new String[]{Long.toString(uid)}, null);
		if(cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			to = new UserTO();
			to.setUid(cursor.getLong(cursor.getColumnIndex("MID")));
			to.setToken(cursor.getString(cursor.getColumnIndex("TOKEN")));
			to.setUserName(cursor.getString(cursor.getColumnIndex("USERNAME")));
			to.setNickName(cursor.getString(cursor.getColumnIndex("NICKNAME")));
			to.setPassword(cursor.getString(cursor.getColumnIndex("PASSWORD")));
			if (!TextUtils.isEmpty(to.getPassword())) {
				try {
					to.setPassword(DES.decrypt(to.getPassword(),DES.PASSWORD_CRYPT_KEY));
				} catch (Exception localException) {
				}
			}
			to.setLastLoginTime(cursor.getLong(cursor.getColumnIndex("LAST_LOGIN_TIME")));
			to.setLastTipTime(cursor.getLong(cursor.getColumnIndex("LAST_TIP_TIME")));
			to.setFast((cursor.getInt(cursor.getColumnIndex("IS_FAST")) == 1));
			to.setThirdType(cursor.getInt(cursor.getColumnIndex("LOGIN_TYPE")));
		}
		return to;
	}
	/**
	 * 保存登陆用户的记录
	 * @param to
	 */
	public void saveUser(UserTO to) {
		if (to == null) {
			return;
		}
		if (!inserUser(to)) {
			updateUser(to);
		}
	}

	/**
	 * 插入用户记录，如果用户已经存在表中则返回false，反之返回true
	 * @param to
	 * @return
	 */
	public boolean inserUser(UserTO to) {
		if (to == null) {
			return false;
		}
		boolean has = false;
		String where = "MID=?";
		String[] whereArgs = { Long.toString(to.getUid()) };

		Cursor c = null;
		try {
			c = getUser(new String[] { "MID" }, where, whereArgs, null);
			has = (c != null) && (c.getCount() > 0);
		} catch (Exception e) {
			deleteUser(to.getUid());
		} finally {
			if (c != null) {
				c.close();
			}
		}
		if (has) {
			return false;
		}
		ContentValues values = new ContentValues();
		values.put("MID", Long.valueOf(to.getUid()));
		values.put("TOKEN", to.getToken());
		values.put("USERNAME", to.getUserName());
		values.put("NICKNAME", to.getNickName());
		values.put("LAST_TIP_TIME", to.getLastTipTime());
		try {
			values.put("PASSWORD", DES.encrypt(to.getPassword(),DES.PASSWORD_CRYPT_KEY));
		} catch (Exception localException2) {
		}
		values.put("LAST_LOGIN_TIME", Long.valueOf(to.getLastLoginTime()));
		values.put("IS_FAST", Integer.valueOf(to.isFast() ? 1 : 0));
		values.put("LOGIN_TYPE", Integer.valueOf(to.getThirdType()));
		return insertUser(values) > 0L;
	}

	/**
	 * 用户记录查询
	 * @param projection
	 * @param where
	 * @param whereArgs
	 * @param orderBy
	 * @return
	 */
	public Cursor getUser(String[] projection, String where,String[] whereArgs, String orderBy) {
		if (mDbHelper == null) {
			return null;
		}
		try {
			return mDbHelper.getReadableDatabase().query("user",projection, where, whereArgs, null, null, orderBy);
		} catch (Exception localException) {
			Log.i("a8", "exception:query");
			localException.printStackTrace();
		}
		return null;
	}

	/**
	 * 用户记录插入
	 * @param values
	 * @return
	 */
	public long insertUser(ContentValues values) {
		if (mDbHelper == null) {
			return 0L;
		}
		try {
			return mDbHelper.getWritableDatabase().insert("user", null,values);
		} catch (Exception localException) {
		}
		return 0L;
	}

	/**
	 * 用户记录更新
	 * @param to
	 * @return
	 */
	public int updateUser(UserTO to) {
		String where = "MID=?";
		String[] whereArgs = { Long.toString(to.getUid()) };
		ContentValues values = new ContentValues();
		if (!TextUtils.isEmpty(to.getPassword())) {
			values.put("TOKEN", to.getToken());
		}
		if (!TextUtils.isEmpty(to.getPassword())) {
			values.put("USERNAME", to.getUserName());
		}
		if (!TextUtils.isEmpty(to.getPassword())) {
			values.put("NICKNAME", to.getNickName());
		}
		if (!TextUtils.isEmpty(to.getPassword())) {
			try {
				values.put("PASSWORD", DES.encrypt(to.getPassword(),DES.PASSWORD_CRYPT_KEY));
			} catch (Exception localException) {
			}
		}
		if (to.getLastLoginTime() > 0L) {
			values.put("LAST_LOGIN_TIME", Long.valueOf(to.getLastLoginTime()));
		}
		if (to.getLastTipTime() > 0L) {
			values.put("LAST_TIP_TIME", Long.valueOf(to.getLastTipTime()));
		}
		if(to.isFast()){
			values.put("IS_FAST", 1);
		}else{
			values.put("IS_FAST", 0);
		}
		if (to.getThirdType() >-1) {
			values.put("LOGIN_TYPE", Integer.valueOf(to.getThirdType()));
		}
		return updateUser(values, where, whereArgs);
	}

	/**
	 * 用户密码更新
	 * @param mid
	 * @param password
	 */
	public void updateUserPassword(long mid, String password) {
		String where = "MID=?";
		String[] whereArg = { Long.toString(mid) };
		ContentValues values = new ContentValues();
		try {
			values.put("PASSWORD",DES.encrypt(password,DES.PASSWORD_CRYPT_KEY));
		} catch (Exception localException) {
		}
		updateUser(values, where, whereArg);
	}
	
	/**
	 * 用户密码更新
	 * @param uName
	 * @param password
	 */
	public void updateUserPassword(String uName, String password) {
		String where = "USERNAME=?";
		String[] whereArg = { uName };
		ContentValues values = new ContentValues();
		try {
			values.put("PASSWORD",DES.encrypt(password,DES.PASSWORD_CRYPT_KEY));
		} catch (Exception localException) {
		}
		updateUser(values, where, whereArg);
	}

	/**
	 * 用户记录更新
	 * @param values
	 * @param where
	 * @param whereArg
	 * @return
	 */
	public int updateUser(ContentValues values, String where, String[] whereArg) {
		if (mDbHelper == null) {
			return 0;
		}
		try {
			mDbHelper.getWritableDatabase().update("user", values, where,whereArg);
		} catch (Exception localException) {
		}
		return 0;
	}

	/**
	 * 用户记录删除，根据Id删除
	 * @param mid
	 * @return
	 */
	public int deleteUser(long mid) {
		try {
			String[] whereArgs = { Long.toString(mid) };
			return deleteUser("MID=?", whereArgs);
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * 用户记录删除，根据条件删除
	 * @param where
	 * @param whereArgs
	 * @return
	 */
	public int deleteUser(String where, String[] whereArgs) {
		if (mDbHelper == null) {
			return 0;
		}
		try {
			return mDbHelper.getWritableDatabase().delete("user", where,whereArgs);
		} catch (Exception localException) {
		}
		return 0;
	}
}
