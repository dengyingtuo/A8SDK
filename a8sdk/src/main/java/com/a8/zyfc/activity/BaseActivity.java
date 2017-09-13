package com.a8.zyfc.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class BaseActivity extends Activity {
	protected Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
	        return false;
	    }
		return super.onKeyDown(keyCode, event);
	}
	
	public boolean onTouchEvent(MotionEvent event) {  
        if (event.getAction() == MotionEvent.ACTION_DOWN && isOutOfBounds(this, event)) {  
            return true;  
        }
        return super.onTouchEvent(event);  
    }  
	
	private boolean isOutOfBounds(Activity context, MotionEvent event) {  
        final int x = (int) event.getX();  
        final int y = (int) event.getY();  
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();  
        final View decorView = context.getWindow().getDecorView();  
        return (x < -slop) || (y < -slop)|| (x > (decorView.getWidth() + slop))|| (y > (decorView.getHeight() + slop));  
    } 

}
