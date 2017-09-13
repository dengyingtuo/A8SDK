package com.a8.zyfc.widget;


import android.app.Activity;
import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.a8.zyfc.util.Util;

public class SdkEditText extends RelativeLayout {
	protected static final int ID_USER_LOGO = 1001;
	protected static final int ID_RIGHT_ICON = 1002;
	protected Context mContext;
	protected View mLeftIcon;
	protected EditText mEdit;
	protected View mRightIcon;
	protected int mLeftIconWidth = Util.getInt(mContext, 78);
	protected int mRightIconWidth;
	protected int mIconMargin = Util.getInt(mContext, 15);
	private boolean mIsDefault = true;
	
	public SdkEditText(Context context) {
		this(context, Util.getInt(context, 78), true);
	}

	public SdkEditText(Context context, int rightIconWidth, boolean isDefault) {
		super(context);
		mIsDefault = isDefault;
		mContext = context;
		mRightIconWidth = rightIconWidth;
		setGravity(Gravity.CENTER_VERTICAL);
		initLeftIcon();
		initRightIcon();
		initEdit();
	}
	
	private void initLeftIcon() {
		mLeftIcon = new View(mContext);
		mLeftIcon.setId(ID_USER_LOGO);
		LayoutParams uiLP = new LayoutParams(mLeftIconWidth, mLeftIconWidth);
		uiLP.addRule(ALIGN_PARENT_LEFT);
		mLeftIcon.setLayoutParams(uiLP);
		mLeftIcon.setFocusable(true);
		mLeftIcon.setFocusableInTouchMode(true);
		mLeftIcon.requestFocus();
		addView(mLeftIcon);
	}
	
	private void initRightIcon() {
		if(mIsDefault) {
			mRightIcon = new View(mContext);
		} else {
			mRightIcon = new Button(mContext);
		}
		mRightIcon.setId(ID_RIGHT_ICON);
		LayoutParams rlp = new LayoutParams(mRightIconWidth, mLeftIconWidth);
		rlp.addRule(ALIGN_PARENT_RIGHT);
		mRightIcon.setLayoutParams(rlp);
		mRightIcon.setFocusable(true);
		mRightIcon.setFocusableInTouchMode(false);
		mRightIcon.requestFocus();
		addView(mRightIcon);
		mRightIcon.setVisibility(View.GONE);
	}
	
	private void initEdit() {
		mEdit = new EditText(mContext);
		LayoutParams eLP = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		eLP.leftMargin = mIconMargin;
		eLP.addRule(RIGHT_OF, ID_USER_LOGO);
		eLP.addRule(LEFT_OF, ID_RIGHT_ICON);
		eLP.addRule(CENTER_VERTICAL);
		mEdit.setLayoutParams(eLP);
	    mEdit.setSingleLine(true);
	    mEdit.setHintTextColor(Util.getColor(mContext, "a8_hint"));
	    mEdit.setEllipsize(TextUtils.TruncateAt.END);
	    mEdit.setTextSize(Util.getTextSize(mContext, 30));
	    mEdit.setTextColor(Util.getColor(mContext, "a8_title_button_unchoosed"));
	    mEdit.setBackgroundColor(Util.getColor(mContext, "a8_login_edit_bg"));
	    mEdit.setGravity(Gravity.CENTER_VERTICAL);
	    mEdit.setPadding(0, 0, 0, 0);
	    addView(mEdit);
	}

	public void setLeftIcon(int resid) {
		mLeftIcon.setBackgroundResource(resid);
	}
	
	public void setRightIcon(int resid) {
		mRightIcon.setBackgroundResource(resid);
		setRightIconVisibility(true);
	}
	
	public void setHint(String hint) {
		mEdit.setHint(hint);
	}
	
	public EditText getEditText() {
		return mEdit;
	}
	
	public View getRightIcon() {
		return mRightIcon;
	}
	
	public void setRightIconVisibility(boolean visibility) {
		if(visibility) {
			mRightIcon.setVisibility(View.VISIBLE);
		} else {
			mRightIcon.setVisibility(View.GONE);
		}
	}
	
	public String getText() {
		String str = mEdit.getText().toString();
		if (str == null) {
			str = "";
		}
		return str;
	}
	
	public void setText(String text) {
		if (text == null) {
			text = "";
		}
		mEdit.setText(text);
	}
	
	public void setPasswordType() {
		mEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
	}
	
	public void setVisiblePasswordType() {
		mEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
	}
	
	public void setNumberType() {
		mEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
	}
	
	public void setMoneyType() {
		mEdit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
	}
	
	public void setEnglishType() {
		mEdit.setInputType(InputType.TYPE_CLASS_TEXT);
	}
	
	public void setMaxLength(int max) {
		InputFilter[] filters = mEdit.getFilters();
		if (filters == null) {
			filters = new InputFilter[] { new InputFilter.LengthFilter(max) };
			mEdit.setFilters(filters);
		} else 
		{
			InputFilter[] newFilters = new InputFilter[filters.length + 1];
			for (int i = 0, l = newFilters.length; i < l; i++) {
				if (i == filters.length) {
					newFilters[i] = new InputFilter.LengthFilter(max);
				} else {
					newFilters[i] = filters[i];
				}
			}
			mEdit.setFilters(newFilters);
		}
	}
	
	public void setNOSpaces() {
		InputFilter[] ifs = mEdit.getFilters();
		if (ifs == null) {
			ifs = new InputFilter[] { new SpacesEditLinstene() };
			mEdit.setFilters(ifs);
		} else {
			InputFilter[] nifs = new InputFilter[ifs.length + 1];
			for (int i = 0; i < ifs.length + 1; i++) {
				if (i == ifs.length) {
					nifs[i] = new SpacesEditLinstene();
				} else {
					nifs[i] = ifs[i];
				}
			}
			mEdit.setFilters(nifs);
		}
	}

	/**
	 * 隐藏软键盘
	 */
	public void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(((Activity)mContext).getCurrentFocus().getWindowToken(), 0);
	}

	public class SpacesEditLinstene implements InputFilter {
		public SpacesEditLinstene() {
		}

		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			if (source.equals(" ")) {
				return "";
			}
			return source;
		}
	}
	
	public void setRighgIconOnClickListener(final onRightIconClickListener l) {
		if(l != null) {
			mRightIcon.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					hideKeyboard();
					l.onClick(v);
				}
			});
		}
	}
	
	public interface onRightIconClickListener {
		public void onClick(View v);
	}
}
