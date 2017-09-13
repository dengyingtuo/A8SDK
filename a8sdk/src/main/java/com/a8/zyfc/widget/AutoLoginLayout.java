package com.a8.zyfc.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a8.zyfc.util.Util;

public class AutoLoginLayout extends RelativeLayout {
	public final static int ID_TITLE = 1001;
	public final static int ID_LEFT_VIEW = 1002;
	public final static int ID_SWITCH_VIEW = 1003;
	private Context mContext;
	private TextView mSwitchTv;
	private TextView mTitleTv;
	private RelativeLayout mLayout;
	private RelativeLayout mSwitchBtn;
	private Button mBindBtn;

	public AutoLoginLayout(Context context) {
		super(context);
		mContext = context;
		initLayout();
		initTitle();
		initButton();		
	}
	
	private void initLayout() {
		mLayout = new RelativeLayout(mContext);
		mLayout.setLayoutParams(new LayoutParams(Util.getInt(mContext, 525), LayoutParams.WRAP_CONTENT));
		mLayout.setBackgroundResource(Util.getDrawableId(mContext, "a8_login_dialog_bg"));
		addView(mLayout);
	}
	
	private void initTitle() {
		mTitleTv = new TextView(mContext);
		LayoutParams lp = new LayoutParams(Util.getInt(mContext, 329), Util.getInt(mContext, 52));
		lp.addRule(CENTER_HORIZONTAL);
		lp.topMargin = Util.getInt(mContext, 40);
		mTitleTv.setLayoutParams(lp);
		mTitleTv.setId(ID_TITLE);
		mTitleTv.setText(Util.getStringId(mContext, "a8_autologin_title"));
		mTitleTv.setTextSize(Util.getTextSize(mContext, 30));
		mTitleTv.setTextColor(Util.getColor(mContext, "a8_login_text"));
		mLayout.addView(mTitleTv);
	}
	
	private void initButton() {
		mSwitchBtn = new RelativeLayout(mContext);
		LayoutParams lp = new LayoutParams(Util.getInt(mContext, 329), Util.getInt(mContext, 88));
		lp.addRule(CENTER_HORIZONTAL);
		lp.addRule(BELOW, ID_TITLE);
		lp.topMargin = Util.getInt(mContext, 30);
		lp.bottomMargin = Util.getInt(mContext, 30);
		mSwitchBtn.setLayoutParams(lp);
		mSwitchBtn.setBackgroundResource(Util.getDrawableId(mContext, "a8_autologin_btn_normal_bg"));
		mSwitchBtn.setPadding(0, 0, 0, 0);
		mSwitchBtn.setId(ID_SWITCH_VIEW);
		
		View switchView = new View(mContext);
		switchView.setBackgroundResource(Util.getDrawableId(mContext, "a8_auto_login"));
		LayoutParams slp = new LayoutParams(Util.getInt(mContext, 59), Util.getInt(mContext, 88));
		slp.addRule(ALIGN_PARENT_LEFT);
		slp.addRule(CENTER_VERTICAL);
		slp.leftMargin = Util.getInt(mContext, 51);
		switchView.setLayoutParams(slp);
		switchView.setId(ID_LEFT_VIEW);
		mSwitchBtn.addView(switchView);
		
		mSwitchTv = new TextView(mContext);
		mSwitchTv.setText(Util.getStringId(mContext, "a8_autologin_btn"));
		mSwitchTv.setTextSize(Util.getTextSize(mContext, 39));
		mSwitchTv.setTextColor(Util.getColor(mContext, "a8_autologin_btn_text"));
		mSwitchTv.setGravity(Gravity.CENTER_VERTICAL);
		LayoutParams sblp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		sblp.addRule(RIGHT_OF, ID_LEFT_VIEW);
		mSwitchTv.setLayoutParams(sblp);
		mSwitchBtn.addView(mSwitchTv);
		
		mLayout.addView(mSwitchBtn);
		
		mBindBtn = new Button(mContext);
		LayoutParams blp = new LayoutParams(Util.getInt(mContext, 329), Util.getInt(mContext, 88));
		blp.addRule(CENTER_HORIZONTAL);
		blp.addRule(BELOW, ID_SWITCH_VIEW);
		blp.bottomMargin = Util.getInt(mContext, 30);
		mBindBtn.setLayoutParams(blp);
		mBindBtn.setBackgroundResource(Util.getDrawableId(mContext, "a8_register_btn_bg_selector"));
		mBindBtn.setText(Util.getStringId(mContext, "a8_bind_account"));
		mBindBtn.setTextColor(Util.getColor(mContext, "a8_login_phoneregister_button_text"));
		mBindBtn.setTextSize(Util.getTextSize(mContext, 39));
		mBindBtn.setVisibility(GONE);
		mLayout.addView(mBindBtn);
	}
	
	
	
	public Button getmBindBtn() {
		return mBindBtn;
	}

	public void setOnSwitchAccountButtonClickListener(final onButtonClickListener l) {
		if(l != null) {
			mSwitchBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					l.onButtonClick(v);
				}
			});
		}
	}

}
