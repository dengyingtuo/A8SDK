package com.a8.zyfc.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.a8.zyfc.util.Util;

public class LoginChooseLayout extends RelativeLayout {
	public final static int ID_TITLE = 1000;
	private Context mContext;
	private RelativeLayout mLayout;
	private LoginTitle mLoginTitle;
	private GridView mChooseGrid;


	public LoginChooseLayout(Context context) {
		super(context);
		mContext = context;
		initLayout();
		initTitle();
		initChoose();
	}
	
	private void initLayout() {
		mLayout = new RelativeLayout(mContext);
		mLayout.setLayoutParams(new LayoutParams(Util.getInt(mContext, 960), LayoutParams.WRAP_CONTENT));
		mLayout.setBackgroundResource(Util.getDrawableId(mContext, "a8_login_dialog_bg"));
		addView(mLayout);
	}

	private void initTitle() {
		mLoginTitle = new LoginTitle(mContext);
		mLoginTitle.setTitle(Util.getString(mContext, "a8_login_choose_title"));
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(CENTER_HORIZONTAL);
		mLoginTitle.setLayoutParams(lp);
		mLoginTitle.setId(ID_TITLE);
		mLayout.addView(mLoginTitle);
	}
	
	private void initChoose() {
		mChooseGrid = new GridView(mContext);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(BELOW, ID_TITLE);
		lp.addRule(CENTER_HORIZONTAL);
		lp.topMargin = Util.getInt(mContext, 20);
		lp.bottomMargin = Util.getInt(mContext, 30);
		mChooseGrid.setLayoutParams(lp);
		mChooseGrid.setGravity(Gravity.CENTER_HORIZONTAL);
//		mChooseGrid.setHorizontalSpacing(Util.getInt(mContext, 20));
		mChooseGrid.setSelector(new ColorDrawable(Color.parseColor("#33666666")));
		mLayout.addView(mChooseGrid);
	}

	public GridView getChooseGrid(){
		return mChooseGrid;
	}
	
}
