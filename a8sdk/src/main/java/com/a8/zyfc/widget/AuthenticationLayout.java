package com.a8.zyfc.widget;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a8.zyfc.util.Util;
import com.a8.zyfc.widget.SdkEditText.onRightIconClickListener;

public class AuthenticationLayout extends RelativeLayout {
	public final static int ID_TITLE = 1000;
	public final static int ID_TIPS = 1001;
	public final static int ID_EDIT = 1002;
	private Context mContext;
	private int mWidth;
	private int mHeight;
	private int mBtnWidth;
	private int mBtnHeight;
	private RelativeLayout mLayout;
	private LoginTitle mTitle;
	private TextView mTips;
	private LoginEditTexts mEditTexts;
	private SdkEditText mRealNameEdit;
	private SdkEditText mIDCardEdit;
	private Button mBtn;
	private ImageView mBackView;
	private boolean mIsDisplay = true;

	public AuthenticationLayout(Context context) {
		super(context);
		mContext = context;
		initLayout();
		initTitle();
		initTips();
		initEdits();
		initButtons();
	}
	
	private void initLayout() {
		mWidth = Util.getInt(mContext, 957);
		mHeight = Util.getInt(mContext, 600);
		mLayout = new RelativeLayout(mContext);
		mLayout.setLayoutParams(new FrameLayout.LayoutParams(mWidth, mHeight));
		mLayout.setBackgroundResource(Util.getDrawableId(mContext, "a8_login_dialog_bg"));
		addView(mLayout);
	}
	
	private void initTitle() {
		mTitle = new LoginTitle(mContext);
		mTitle.setTitle(Util.getString(mContext, "a8_real_name_title"));
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(CENTER_HORIZONTAL);
		mTitle.setLayoutParams(lp);
		mTitle.setId(ID_TITLE);
		mLayout.addView(mTitle);

		mBackView = new ImageView(mContext);
		LayoutParams blp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		blp.addRule(ALIGN_PARENT_RIGHT);
		blp.rightMargin = Util.getInt(mContext, 30);
		blp.topMargin = Util.getInt(mContext, 30);
		mBackView.setLayoutParams(blp);
		mBackView.setBackgroundResource(Util.getDrawableId(mContext, "a8_back_img"));
		mLayout.addView(mBackView);
	}
	
	private void initTips(){
		mTips = new TextView(mContext);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(BELOW, ID_TITLE);
		lp.addRule(CENTER_HORIZONTAL);
		lp.leftMargin = Util.getInt(mContext, 80);
		lp.rightMargin = Util.getInt(mContext, 80);
		mTips.setLayoutParams(lp);
		mTips.setText(Util.getString(mContext, "a8_real_name_tips"));
		mTips.setTextColor(Util.getColor(mContext, "a8_red_text_color"));
		mTips.setTextSize(Util.getInt(mContext, 14));
		mTips.setId(ID_TIPS);
		mLayout.addView(mTips);
	}
	
	private void initEdits() {
		mEditTexts = new LoginEditTexts(mContext);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(BELOW, ID_TIPS);
		lp.addRule(CENTER_HORIZONTAL);
		lp.topMargin = Util.getInt(mContext, 15);
		lp.leftMargin = Util.getInt(mContext, 79);
		mEditTexts.setLayoutParams(lp);
		mEditTexts.setId(ID_EDIT);
		
		mRealNameEdit = new SdkEditText(mContext);
		mRealNameEdit.setHint(Util.getString(mContext, "a8_real_name_hint"));
		mRealNameEdit.setLeftIcon(Util.getDrawableId(mContext, "a8_login_title_user"));
		mRealNameEdit.getEditText().setSingleLine(true);
		mRealNameEdit.getEditText().setMaxWidth(10);
		mRealNameEdit.getEditText().setEllipsize(TextUtils.TruncateAt.END);
		mRealNameEdit.getEditText().setImeOptions(EditorInfo.IME_ACTION_NEXT);
		mEditTexts.addSdkEditText(mRealNameEdit);
		
		mIDCardEdit = new SdkEditText(mContext);
		mIDCardEdit.setHint(Util.getString(mContext, "a8_id_card_hint"));
		mIDCardEdit.setLeftIcon(Util.getDrawableId(mContext, "a8_login_idcard"));
		mIDCardEdit.setRightIcon(Util.getDrawableId(mContext, "a8_login_title_display"));
		mIDCardEdit.getEditText().setSingleLine(true);
		mIDCardEdit.setMaxLength(18);
		mIDCardEdit.getEditText().setEllipsize(TextUtils.TruncateAt.END);
		mIDCardEdit.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
		mIDCardEdit.setVisiblePasswordType();
		mIDCardEdit.setRighgIconOnClickListener(new onRightIconClickListener() {

			@Override
			public void onClick(View v) {
				if(mIsDisplay) {
					mIDCardEdit.setPasswordType();
					mIDCardEdit.setRightIcon(Util.getDrawableId(mContext, "a8_login_title_hide"));
				} else {
					mIDCardEdit.setRightIcon(Util.getDrawableId(mContext, "a8_login_title_display"));
					mIDCardEdit.setVisiblePasswordType();
				}
				mIsDisplay = !mIsDisplay;
			}
		});
		mEditTexts.addSdkEditText(mIDCardEdit);
		
		mEditTexts.show();
		mLayout.addView(mEditTexts);
	}

	private void initButtons() {
		mBtn = new Button(mContext);
		mBtnWidth = Util.getInt(mContext, 800);
		mBtnHeight = Util.getInt(mContext, 103);
		LayoutParams elp = new LayoutParams(mBtnWidth, mBtnHeight);
		elp.addRule(BELOW, ID_EDIT);
		elp.addRule(ALIGN_LEFT, ID_EDIT);
		elp.addRule(ALIGN_RIGHT, ID_EDIT);
		elp.topMargin = Util.getInt(mContext, 20);
		mBtn.setLayoutParams(elp);
		mBtn.setBackgroundResource(Util.getDrawableId(mContext, "a8_login_btn_bg_selector"));
		mBtn.setText(Util.getStringId(mContext, "a8_real_name_btn_text"));
		mBtn.setTextColor(Util.getColor(mContext, "a8_login_entergame_button"));
		mBtn.setTextSize(Util.getTextSize(mContext, 45));
		mLayout.addView(mBtn);
	}
	
	public void setOnLinkClickListener(final OnLinkClickListener l) {
		if(l != null) {
			mBackView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					l.onRightClick(v);
				}
			});
		}
	}
	
	public void setOnButtonClickListener(final onButtonClickListener l) {
		if(l != null) {
			mBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					l.onButtonClick(v);
				}
			});
		}
	}
	
	public String getRealName(){
		return mRealNameEdit.getText().toString();
	}
	
	public String getIDCardNumber(){
		return mIDCardEdit.getText().toString();
	}
}
