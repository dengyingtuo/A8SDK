package com.a8.zyfc.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a8.zyfc.util.Util;

public class LoginTitle extends RelativeLayout {
	public static final int ID_LOGO = 1001;
	public static final int ID_SPLIT_LINE = 1002;
	private Context mContext;
	private int mWidth = Util.getInt(mContext, 954);
	private int mHeight = Util.getInt(mContext, 136);
	private int mLogoWidth = Util.getInt(mContext, 240);
	private int mLogoHeight = Util.getInt(mContext, 99);
	private View mLogo_View;
	private View mSplitLine_View;
	private TextView mTitle_TextView;
	private RelativeLayout mLayout;

	public LoginTitle(Context context) {
		super(context, null);
		mContext = context;
		initLayout();
		initView();
	}
	
	private void initLayout() {
		mLayout = new RelativeLayout(mContext);
		LayoutParams plp = new LayoutParams(mWidth, mHeight);
		mLayout.setLayoutParams(plp);
		mLayout.setGravity(Gravity.CENTER);
		addView(mLayout);
	}

	private void initView() {
		int lineMargin = Util.getInt(mContext, 9);
		mLogo_View = new View(mContext);
		LayoutParams llp = new LayoutParams(mLogoWidth, mLogoHeight);
		llp.addRule(CENTER_VERTICAL);
		mLogo_View.setLayoutParams(llp);
		mLogo_View.setBackgroundResource(Util.getDrawableId(mContext, "a8_login_title_logo"));
		mLogo_View.setId(ID_LOGO);
//		mLayout.addView(mLogo_View);
		
		mSplitLine_View = new View(mContext);
		LayoutParams slp = new LayoutParams(Util.getInt(mContext, 2), Util.getInt(mContext, 39));
		slp.addRule(CENTER_VERTICAL);
		slp.addRule(RIGHT_OF, ID_LOGO);
		slp.leftMargin = lineMargin;
		slp.rightMargin = lineMargin;
		mSplitLine_View.setLayoutParams(slp);
		mSplitLine_View.setBackgroundColor(Util.getColorId(mContext, "a8_login_title_split_line"));
		mSplitLine_View.setId(ID_SPLIT_LINE);
//		mLayout.addView(mSplitLine_View);
		
		mTitle_TextView = new TextView(mContext);
		LayoutParams tlp = new LayoutParams(LayoutParams.WRAP_CONTENT, mLogoHeight);
		tlp.addRule(CENTER_IN_PARENT);
//		tlp.addRule(RIGHT_OF, ID_SPLIT_LINE);
		mTitle_TextView.setLayoutParams(tlp);
		mTitle_TextView.setTextSize(Util.getTextSize(mContext, 35));
		mTitle_TextView.setGravity(Gravity.CENTER);
		mLayout.addView(mTitle_TextView);
	}
	
	public void setTitle(String title) {
		if(!TextUtils.isEmpty(title) && mTitle_TextView != null) {
			mTitle_TextView.setText(title);
		}
	}
	
	public void setTitleSize(int textSize) {
		if(mTitle_TextView != null && textSize > 0) {
			mTitle_TextView.setTextSize(textSize);
		}
	}
	
	public void setTitleColor(String colorId) {
		if(mTitle_TextView != null) {
			mTitle_TextView.setTextColor(Util.getColor(mContext, colorId));
		}
	}
	
	public void setLogo(String drawableName) {
		if(mLogo_View != null) {
			mLogo_View.setBackgroundResource(Util.getDrawableId(mContext, drawableName));
		}
	}
	
	public TextView getTitleView() {
		return mTitle_TextView;
	}

}
