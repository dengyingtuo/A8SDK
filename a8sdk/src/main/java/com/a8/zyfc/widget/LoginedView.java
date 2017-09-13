package com.a8.zyfc.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a8.zyfc.util.Util;

/**
 * 登录成功后欢迎回来的提示View
 * @author Sandy.Shen
 *
 */
public class LoginedView extends LinearLayout {
	

	public LoginedView(Context context) {
		super(context);
	}

	public LoginedView(Context context, String text) {
		super(context);

		RelativeLayout layout = new RelativeLayout(context);
		int mWidth = Util.getInt(context, 972);

		int mHeight = Util.getInt(context, 144);
		layout.setLayoutParams(new LayoutParams(mWidth, mHeight));
		layout.setBackgroundResource(Util.getDrawableId(context,"a8_logined_bg"));
		addView(layout);
		layout.setGravity(Gravity.CENTER_VERTICAL);

		TextView textView = new TextView(context);
		RelativeLayout.LayoutParams tlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		tlp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		tlp.bottomMargin = Util.getInt(context, -9);
		textView.setLayoutParams(tlp);

		textView.setText(text);
		textView.setTextSize(Util.getTextSize(context, 45));
		textView.setTextColor(Color.WHITE);
		textView.setSingleLine();
		textView.setEllipsize(TextUtils.TruncateAt.END);
		textView.setGravity(Gravity.BOTTOM);
		layout.addView(textView);
	}
}
