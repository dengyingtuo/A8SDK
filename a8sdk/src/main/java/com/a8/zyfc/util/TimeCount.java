package com.a8.zyfc.util;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Button;

public class TimeCount extends CountDownTimer {
	private Button mBtn;
	private Context mContext;
	private TimeCountListener mListener;

	public TimeCount(long millisInFuture, long countDownInterval, Context context, Button btn, TimeCountListener l) {
		super(millisInFuture, countDownInterval);
		mBtn = btn;
		mContext = context;
		mListener = l;
	}

	@Override
	public void onTick(long millisUntilFinished) {
		mBtn.setEnabled(false);
		mBtn.setText(millisUntilFinished / 1000 + "s");
		mBtn.setTextColor(Util.getColor(mContext, "a8_login_phoneregister_button"));
		mBtn.setBackgroundResource(Util.getDrawableId(mContext, "a8_login_code_btn_disabled_bg"));
		if(mListener != null) {
			mListener.onTick(millisUntilFinished);
		}
	}
	
	@Override
	public void onFinish() {
		mBtn.setEnabled(true);
		mBtn.setText(Util.getStringId(mContext, "a8_bindphone_resend_btn"));
		mBtn.setTextColor(Util.getColor(mContext, "a8_bindphone_send_button_text"));
		mBtn.setBackgroundResource(Util.getDrawableId(mContext, "a8_login_code_btn_normal_bg"));
		if(mListener != null) {
			mListener.onFinish();
		}
	}
	
	public void onCancel(){
		mBtn.setEnabled(true);
		mBtn.setText(Util.getStringId(mContext, "a8_bindphone_send_btn"));
		mBtn.setTextColor(Util.getColor(mContext, "a8_bindphone_send_button_text"));
		mBtn.setBackgroundResource(Util.getDrawableId(mContext, "a8_login_code_btn_normal_bg"));
		this.cancel();
	}
	
	public interface TimeCountListener {
		public void onTick(long millisUntilFinished);
		public void onFinish();
	}

}
