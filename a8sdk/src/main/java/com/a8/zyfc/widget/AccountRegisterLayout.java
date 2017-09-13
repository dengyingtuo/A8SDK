package com.a8.zyfc.widget;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a8.zyfc.http.JsonHttpResponseHandler;
import com.a8.zyfc.http.UserAction;
import com.a8.zyfc.model.UserTO;
import com.a8.zyfc.util.JSONUtils;
import com.a8.zyfc.util.Util;
import com.a8.zyfc.widget.SdkEditText.onRightIconClickListener;

public class AccountRegisterLayout extends RelativeLayout {
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
	private Button mRegisterBtn;
	private TextView mPhoneRegisterTv;
	private TextView mEnterGameTv;
	private boolean mIsDisplay = false;
	private UserAction mUserCallback;
	private UserTO mUser;
	private onButtonClickListener mListener;

	public AccountRegisterLayout(Context context) {
		super(context);
		mContext = context;
		mUserCallback = new UserAction(mContext);
		initLayout();
		initTitle();
		initEdits();
		initButtons();
		initLinks();
	}

	private void initLayout() {
		mWidth = Util.getInt(mContext, 957);
		mHeight = Util.getInt(mContext, 571);
		mLayout = new RelativeLayout(mContext);
		mLayout.setLayoutParams(new FrameLayout.LayoutParams(mWidth, mHeight));
		mLayout.setBackgroundResource(Util.getDrawableId(mContext, "a8_login_dialog_bg"));
		addView(mLayout);
	}
	
	private void initTitle() {
		mLoginTitle = new LoginTitle(mContext);
		mLoginTitle.setTitle(Util.getString(mContext, "a8_account_register_title"));
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(CENTER_HORIZONTAL);
		mLoginTitle.setLayoutParams(lp);
		mLoginTitle.setId(ID_TITLE);
		mLayout.addView(mLoginTitle);
	}
	
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

	private void initButtons() {
		mRegisterBtn = new Button(mContext);
		mBtnWidth = Util.getInt(mContext, 800);
		mBtnHeight = Util.getInt(mContext, 103);
		LayoutParams elp = new LayoutParams(mBtnWidth, mBtnHeight);
		elp.addRule(BELOW, ID_EDIT);
		elp.addRule(ALIGN_LEFT, ID_EDIT);
		elp.addRule(ALIGN_RIGHT, ID_EDIT);
		elp.topMargin = Util.getInt(mContext, 23);
		mRegisterBtn.setLayoutParams(elp);
		mRegisterBtn.setBackgroundResource(Util.getDrawableId(mContext, "a8_login_btn_bg_selector"));
		mRegisterBtn.setText(Util.getStringId(mContext, "a8_account_register_btn"));
		mRegisterBtn.setTextColor(Util.getColor(mContext, "a8_bindphone_send_button_text"));
		mRegisterBtn.setTextSize(Util.getTextSize(mContext, 45));
		mRegisterBtn.setId(ID_REGISTER);
		mLayout.addView(mRegisterBtn);
	}
	
	private void initLinks() {
		int topMargin = Util.getInt(mContext, 14);	
		mPhoneRegisterTv = new TextView(mContext);
		LayoutParams rlp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rlp.addRule(BELOW, ID_REGISTER);
		rlp.addRule(ALIGN_LEFT, ID_REGISTER);
		rlp.addRule(CENTER_VERTICAL);
		rlp.topMargin = topMargin;
		mPhoneRegisterTv.setLayoutParams(rlp);
		mPhoneRegisterTv.setPadding(0, 0, 0, 0);
		mPhoneRegisterTv.setText(Html.fromHtml("<u>&lt;&lt;" + Util.getString(mContext, "a8_register_link_phone_register") + "</u>"));
		mPhoneRegisterTv.setTextColor(Util.getColor(mContext, "a8_link_text"));
		mPhoneRegisterTv.setTextSize(Util.getTextSize(mContext, 30));
		mLayout.addView(mPhoneRegisterTv);
		
		mEnterGameTv = new TextView(mContext);
		LayoutParams llp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		llp.addRule(BELOW, ID_REGISTER);
		llp.addRule(ALIGN_RIGHT, ID_REGISTER);
		llp.addRule(CENTER_VERTICAL);
		llp.topMargin = topMargin;
		mEnterGameTv.setLayoutParams(llp);
		mEnterGameTv.setPadding(0, 0, 0, 0);
		mEnterGameTv.setText(Html.fromHtml("<u>" + Util.getString(mContext, "a8_register_link_backtologin") + "</u>"));
		mEnterGameTv.setTextColor(Util.getColor(mContext, "a8_link_text"));
		mEnterGameTv.setTextSize(Util.getTextSize(mContext, 30));
		mLayout.addView(mEnterGameTv);
	}
	
	/*** 用户注册*/
	private void register() {
		String user = mUserNameEdit.getText().toString();
		String password = mPwdEdit.getText().toString();

		if (TextUtils.isEmpty(user)) {
			Util.showToast(mContext, Util.getString(mContext, "a8_login_tips_please_enter_username"));
			return;
		}
		if (user.length() < 6 || user.length() > 20) {
			Util.showToast(mContext, Util.getString(mContext, "a8_register_tips_please_enter_correct_uname"));
			return;
		}
		if (!user.matches("^[A-Za-z0-9_]{2,20}$")) {
			Util.showToastLong(mContext, Util.getString(mContext, "a8_register_tips_please_enter_correct_username"));
			return;
		}
		if (TextUtils.isEmpty(password)) {
			Util.showToast(mContext, Util.getString(mContext, "a8_login_tips_please_enter_pwd"));
			return;
		}
		if ((password.length() < 6) || (password.length() > 20)) {
			Util.showToast(mContext, Util.getString(mContext, "a8_register_tips_please_enter_correct_pwd"));
			return;
		}
		if (!password.matches("^[^\u4e00-\u9fa5]+$")) {
			Util.showToast(mContext, Util.getString(mContext, "a8_register_tips_please_enter_correct_pwd"));
			return;
		}
		if (!Util.checkNet(mContext)) {
			return;
		}
		mUserCallback.regist(user, password, new JsonHttpResponseHandler(){

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					if(response.getInt(UserAction.CODE) == UserAction.RESULT_OK){
						Util.showToast(mContext, Util.getString(mContext, "a8_register_tips_register_success"));
				        mUser = JSONUtils.fromJson(response.getString(UserAction.MSG), UserTO.class);
				        mUser.setPassword(mPwdEdit.getEditText().getText().toString());
						mUser.setThirdType(10);
				        mListener.onButtonClick(mRegisterBtn);
					} else {
						if (!TextUtils.isEmpty(response.getString(UserAction.DESC))) {
							 Util.showToast(mContext, response.getString(UserAction.DESC));
						 } else {
							 Util.showToast(mContext, Util.getString(mContext, "a8_register_tips_register_fail"));
						 }
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Util.showToast(mContext, Util.getString(mContext, "a8_register_tips_register_fail"));
			}	
		});
	
	}
	
	public UserTO getRegisterUser() {
		return mUser;
	}
	
	public void setOnLinkClickListener(final OnLinkClickListener l) {
		if(l != null) {
			mPhoneRegisterTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					l.onLeftClick(v);
				}
			});
			mEnterGameTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					l.onRightClick(v);
				}
			});
		}
	}
	
	public void setOnButtonClickListener(final onButtonClickListener l) {
		mListener = l;
		if(l != null) {
			mRegisterBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					register();
				}
			});
		}
	}
}
