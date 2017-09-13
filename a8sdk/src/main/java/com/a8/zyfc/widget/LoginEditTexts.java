package com.a8.zyfc.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.a8.zyfc.util.Util;

public class LoginEditTexts extends RelativeLayout {
	private Context mContext;
	private List<SdkEditText> mSdkEditTextList;
	protected int mEditWidth = Util.getInt(mContext, 765);
	protected int mEditHeight = Util.getInt(mContext, 79);

	public LoginEditTexts(Context context) {
		super(context);
		mContext = context;
		mSdkEditTextList = new ArrayList<SdkEditText>();
		setBackgroundResource(Util.getDrawableId(mContext, "a8_login_edit_bg"));
		setPadding(Util.getInt(mContext, 15), Util.getInt(mContext, 15), Util.getInt(mContext, 20), Util.getInt(mContext, 15));
	}
	
	public void addSdkEditText(SdkEditText edit) {
		mSdkEditTextList.add(edit);
	}
	
	public void show() {
		LayoutParams lp;
		LayoutParams llp;
		View line;
		for(int i = 1; i <= mSdkEditTextList.size(); i++) {
			lp = new LayoutParams(LayoutParams.MATCH_PARENT, mEditHeight);
			if(i == 1) {
				lp.addRule(ALIGN_PARENT_TOP);
				lp.addRule(ALIGN_PARENT_RIGHT);
			} else{
				line = new View(mContext);
				llp = new LayoutParams(LayoutParams.MATCH_PARENT, 2);
				llp.addRule(BELOW, 1000 + i - 1);
				llp.topMargin = Util.getInt(mContext, 5);
				llp.bottomMargin = llp.topMargin;
				line.setLayoutParams(llp);
				line.setBackgroundColor(Util.getColor(mContext, "a8_login_title_split_line"));
				line.setId(2000 + i);
				addView(line);
				lp.addRule(BELOW, 2000 + i);
				lp.addRule(ALIGN_LEFT, 2000 + i);
				lp.addRule(ALIGN_RIGHT, 2000 + i);
			}
			SdkEditText edit = mSdkEditTextList.get(i - 1);
			edit.setLayoutParams(lp);
			edit.setId(1000 + i);
			addView(edit);
		}
	}
}
