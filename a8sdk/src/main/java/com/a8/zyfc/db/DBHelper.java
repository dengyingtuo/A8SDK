package com.a8.zyfc.db;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper{
	

	  public static final String MGMT_DB_NAME = "A8sdk.db";
	  public static final int DB_VERSION = 1;
	  public static final String TABLE_USERS_NAME = "user";
	  private SQLiteDatabase sqlite;
	  private File path;

	  public DBHelper(Context context) {
		  path = context.getFilesDir();			  
		  File file = new File(path, MGMT_DB_NAME);
		  if(!file.exists()) {
			  try {
				file.createNewFile();
				sqlite = SQLiteDatabase.openOrCreateDatabase(file, null);
				createUserTable(sqlite);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("database file create on " + file.getParent() + " fail, cause by " + e.getMessage());
			}
		  } else {
			  sqlite = SQLiteDatabase.openOrCreateDatabase(file, null);
			  if(!checkColumnExist(sqlite, TABLE_USERS_NAME, "LOGIN_TYPE"))
			  	sqlite.execSQL("alter table user add column LOGIN_TYPE integer;");
		  }
		  
	  }
	  
	  private void createUserTable(SQLiteDatabase db) {
	    StringBuilder appStr = new StringBuilder();
	    appStr.append("create table ").append(TABLE_USERS_NAME).append(" ( ");
	    appStr.append("MID").append(" Long primary key,");
	    appStr.append("TOKEN").append(" varchar(100),");
	    appStr.append("USERNAME").append(" varchar(100),");
	    appStr.append("NICKNAME").append(" varchar(100),");
	    appStr.append("LAST_LOGIN_TIME").append(" Long,");
	    appStr.append("LAST_TIP_TIME").append(" Long,");
	    appStr.append("IS_FAST").append(" integer,");
	    appStr.append("PASSWORD").append(" varchar(100),");
		appStr.append("LOGIN_TYPE").append(" integer);");
	    db.execSQL(appStr.toString());
	  }
	  
	  public void close() {
		  sqlite.close();
	  }
	  
	  public SQLiteDatabase getWritableDatabase() {
		  return sqlite;
	  }
	  
	  public SQLiteDatabase getReadableDatabase() {
		  return sqlite;
	  }

	  private boolean checkColumnExist(SQLiteDatabase db, String table, String columnName){
		  boolean result = false ;
		  Cursor cursor = null ;
		  try{
			  //查询一行
			  cursor = db.rawQuery( "SELECT * FROM " + table + " LIMIT 0", null );
			  result = cursor != null && cursor.getColumnIndex(columnName) != -1 ;
		  }catch (Exception e){
			 e.printStackTrace();
		  }finally{
			  if(null != cursor && !cursor.isClosed()){
				  cursor.close() ;
			  }
		  }

		  return result ;
	  }

}
