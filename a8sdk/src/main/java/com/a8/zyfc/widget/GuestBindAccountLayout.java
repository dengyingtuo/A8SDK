package com.a8.zyfc.widget;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a8.zyfc.util.Util;
import com.a8.zyfc.widget.SdkEditText.onRightIconClickListener;
/**
 * 游客绑定A8账号界面布局
 * @author Chooper
 *
 */
public class GuestBindAccountLayout extends RelativeLayout {
	public final static int ID_TITLE = 1000;
	public final static int ID_EDIT = 1001;
	public final static int ID_REGISTER = 1002;
	private Context mContext;
	private int mWidth;
	private int mHeight;
	private int mBtnWidth;
	private int mBtnHeight;
	private RelativeLayout mLayout;
	private LoginTitle mLoginTitle;
	private LoginEditTexts mLoginEditTexts;
	private SdkEditText mUserNameEdit;
	private SdkEditText mPwdEdit;
	private Button mBindBtn;
	private TextView mBindPhoneTv;
	private ImageView mBackView;
	private boolean mIsDisplay = false;

	public GuestBindAccountLayout(Context context) {
		super(context);
		mContext = context;
		initLayout();
		initTitle();
		initEdits();
		initButtons();
		initLinks();
	}
	/**
	 * 绘制整体布局
	 */
	private void initLayout() {
		mWidth = Util.getInt(mContext, 957);
		mHeight = Util.getInt(mContext, 570);
		mLayout = new RelativeLayout(mContext);
		mLayout.setLayoutParams(new FrameLayout.LayoutParams(mWidth, mHeight));
		mLayout.setBackgroundResource(Util.getDrawableId(mContext, "a8_login_dialog_bg"));
		addView(mLayout);
	}
	/**
	 * 绘制title
	 */
	private void initTitle() {
		mLoginTitle = new LoginTitle(mContext);
		mLoginTitle.setTitle(Util.getString(mContext, "a8_guestbindaccount_title"));
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(CENTER_HORIZONTAL);
		mLoginTitle.setLayoutParams(lp);
		mLoginTitle.setId(ID_TITLE);
		mLayout.addView(mLoginTitle);
		
		mBackView = new ImageView(mContext);
		LayoutParams blp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		blp.addRule(ALIGN_PARENT_RIGHT);
		blp.rightMargin = Util.getInt(mContext, 30);
		blp.topMargin = Util.getInt(mContext, 30);
		mBackView.setLayoutParams(blp);
		mBackView.setBackgroundResource(Util.getDrawableId(mContext, "a8_back_img"));
		mLayout.addView(mBackView);
	}
	/**
	 * 绘制用户输入框（账号，密码输入框）
	 */
	private void initEdits() {
		mLoginEditTexts = new LoginEditTexts(mContext);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(BELOW, ID_TITLE);
		lp.addRule(CENTER_HORIZONTAL);
		lp.topMargin = Util.getInt(mContext, 15);
		lp.leftMargin = Util.getInt(mContext, 79);
		mLoginEditTexts.setLayoutParams(lp);
		mLoginEditTexts.setId(ID_EDIT);
		
		mUserNameEdit = new SdkEditText(mContext);
		mUserNameEdit.setHint(Util.getString(mContext, "a8_account_register_uedit_hint"));
		mUserNameEdit.setLeftIcon(Util.getDrawableId(mContext, "a8_login_title_user"));
		mUserNameEdit.getEditText().setMaxWidth(20);
		mLoginEditTexts.addSdkEditText(mUserNameEdit);
		
		mPwdEdit = new SdkEditText(mContext);
		mPwdEdit.setHint(Util.getString(mContext, "a8_login_pwdedit_hint"));
		mPwdEdit.setLeftIcon(Util.getDrawableId(mContext, "a8_login_title_pwd"));
		mPwdEdit.setRightIcon(Util.getDrawableId(mContext, "a8_login_title_hide"));
		mPwdEdit.setPasswordType();
		mPwdEdit.getEditText().setMaxWidth(20);
		mPwdEdit.setRighgIconOnClickListener(new onRightIconClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mIsDisplay) {
					mPwdEdit.setRightIcon(Util.getDrawableId(mContext, "a8_login_title_hide"));
					mPwdEdit.setPasswordType();
				} else {
					mPwdEdit.setRightIcon(Util.getDrawableId(mContext, "a8_login_title_display"));
					mPwdEdit.setVisiblePasswordType();
				}
				mIsDisplay = !mIsDisplay;
			}
		});
		mLoginEditTexts.addSdkEditText(mPwdEdit);
		
		mLoginEditTexts.show();
		mLayout.addView(mLoginEditTexts);
	}
	/**
	 * 绘制绑定按钮
	 */
	private void initButtons() {
		mBindBtn = new Button(mContext);
		mBtnWidth = Util.getInt(mContext, 800);
		mBtnHeight = Util.getInt(mContext, 103);
		LayoutParams elp = new LayoutParams(mBtnWidth, mBtnHeight);
		elp.addRule(BELOW, ID_EDIT);
		elp.addRule(ALIGN_LEFT, ID_EDIT);
		elp.addRule(ALIGN_RIGHT, ID_EDIT);
		elp.topMargin = Util.getInt(mContext, 23);
		mBindBtn.setLayoutParams(elp);
		mBindBtn.setBackgroundResource(Util.getDrawableId(mContext, "a8_login_btn_bg_selector"));
		mBindBtn.setText(Util.getStringId(mContext, "a8_guestbindphone_btn"));
		mBindBtn.setTextColor(Util.getColor(mContext, "a8_bindphone_send_button_text"));
		mBindBtn.setTextSize(Util.getTextSize(mContext, 45));
		mBindBtn.setId(ID_REGISTER);
		mLayout.addView(mBindBtn);
	}
	/**
	 * 绘制前往其他界面的link按钮
	 */
	private void initLinks() {
		mBindPhoneTv = new TextView(mContext);
		LayoutParams rlp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rlp.addRule(BELOW, ID_REGISTER);
		rlp.addRule(ALIGN_RIGHT, ID_REGISTER);
		rlp.addRule(CENTER_VERTICAL);
		rlp.topMargin = Util.getInt(mContext, 20);
		mBindPhoneTv.setLayoutParams(rlp);
		mBindPhoneTv.setPadding(0, 0, 0, 0);
		mBindPhoneTv.setText(Html.fromHtml("<u>" + Util.getString(mContext, "a8_guestbindaccount_link_left") + "</u>"));
		mBindPhoneTv.setTextColor(Util.getColor(mContext, "a8_link_text"));
		mBindPhoneTv.setTextSize(Util.getTextSize(mContext, 30));
		mLayout.addView(mBindPhoneTv);
	}
	
	public String getUserName() {
		return mUserNameEdit.getText();
	}
	
	public String getPwd() {
		return mPwdEdit.getText();
	}
	
	public void setOnLinkClickListener(final OnLinkClickListener l) {
		if(l != null) {
			mBindPhoneTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					l.onLeftClick(v);
				}
			});
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
			mBindBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					l.onButtonClick(v);
				}
			});
		}
	}
}
