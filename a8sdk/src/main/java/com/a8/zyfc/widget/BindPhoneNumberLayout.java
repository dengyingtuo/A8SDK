package com.a8.zyfc.widget;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.a8.zyfc.http.JsonHttpResponseHandler;
import com.a8.zyfc.UserConfig;
import com.a8.zyfc.http.UserAction;
import com.a8.zyfc.model.UserTO;
import com.a8.zyfc.util.TimeCount;
import com.a8.zyfc.util.TimeCount.TimeCountListener;
import com.a8.zyfc.util.Util;
import com.a8.zyfc.widget.SdkEditText.onRightIconClickListener;

public class BindPhoneNumberLayout extends RelativeLayout {
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
	private SdkEditText mPhoneCodeEdit;
	private Button mBindPhoneBtn;
	private ImageView mBackView;
	private boolean mIsSending = false;
	private UserAction mUserCallback;
	private UserTO mUser;
	private TimeCount timer;

	public BindPhoneNumberLayout(Context context, UserTO user) {
		super(context);
		mContext = context;
		mUser = user;
		mUserCallback = new UserAction(mContext);
		initLayout();
		initTitle();
		initEdits();
		initButtons();
	}
	
	private void initLayout() {
		mWidth = Util.getInt(mContext, 957);
		mHeight = Util.getInt(mContext, 576);
		mLayout = new RelativeLayout(mContext);
		mLayout.setLayoutParams(new FrameLayout.LayoutParams(mWidth, mHeight));
		mLayout.setBackgroundResource(Util.getDrawableId(mContext, "a8_login_dialog_bg"));
		addView(mLayout);
	}
	
	private void initTitle() {
		mLoginTitle = new LoginTitle(mContext);
		mLoginTitle.setTitle(Util.getString(mContext, "a8_bindphone_title"));
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
		mUserNameEdit.setHint(Util.getString(mContext, "a8_phone_register_phone"));
		mUserNameEdit.setLeftIcon(Util.getDrawableId(mContext, "a8_login_title_user"));
		mUserNameEdit.getEditText().setSingleLine(true);
		mUserNameEdit.getEditText().setEllipsize(TruncateAt.END);
		mUserNameEdit.setNumberType();
		mUserNameEdit.getEditText().setMaxWidth(11);
		mUserNameEdit.getEditText().addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(Util.isPhone(mUserNameEdit.getEditText().getText().toString()) && !mIsSending) {
					setSendCodeButtonVisibility(true);
				} else {
					setSendCodeButtonVisibility(false);
				}
			}
		});
		mLoginEditTexts.addSdkEditText(mUserNameEdit);
		
		mPhoneCodeEdit = new SdkEditText(mContext, Util.getInt(mContext, 174), false);
		mPhoneCodeEdit.setHint(Util.getString(mContext, "a8_phone_register_code"));
		mPhoneCodeEdit.setLeftIcon(Util.getDrawableId(mContext, "a8_login_title_phone"));
		mPhoneCodeEdit.setRightIcon(Util.getDrawableId(mContext, "a8_login_code_btn_bg"));
		mPhoneCodeEdit.getEditText().setSingleLine(true);
		mPhoneCodeEdit.getEditText().setEllipsize(TruncateAt.END);
		mPhoneCodeEdit.setRightIcon(Util.getDrawableId(mContext, "a8_login_code_btn_disabled_bg"));
		mPhoneCodeEdit.setNumberType();
		final Button btn = (Button) mPhoneCodeEdit.getRightIcon();
		btn.setText(Util.getString(mContext, "a8_bindphone_send_btn"));
		btn.setTextSize(Util.getTextSize(mContext, 28));
		btn.setTextColor(Util.getColor(mContext, "a8_login_phoneregister_button"));
		btn.setEnabled(false);
		mPhoneCodeEdit.setNumberType();
		mPhoneCodeEdit.setRighgIconOnClickListener(new onRightIconClickListener() {
			
			@Override
			public void onClick(View v) {
				getCode();
			}
		});
		mLoginEditTexts.addSdkEditText(mPhoneCodeEdit);
		
		mLoginEditTexts.show();
		mLayout.addView(mLoginEditTexts);
	}

	private void initButtons() {
		mBindPhoneBtn = new Button(mContext);
		mBtnWidth = Util.getInt(mContext, 800);
		mBtnHeight = Util.getInt(mContext, 103);
		LayoutParams elp = new LayoutParams(mBtnWidth, mBtnHeight);
		elp.addRule(BELOW, ID_EDIT);
		elp.addRule(ALIGN_LEFT, ID_EDIT);
		elp.addRule(ALIGN_RIGHT, ID_EDIT);
		elp.topMargin = Util.getInt(mContext, 35);
		mBindPhoneBtn.setLayoutParams(elp);
		mBindPhoneBtn.setBackgroundResource(Util.getDrawableId(mContext, "a8_login_btn_bg_selector"));
		mBindPhoneBtn.setText(Util.getStringId(mContext, "a8_bindphone_title"));
		mBindPhoneBtn.setTextColor(Util.getColor(mContext, "a8_bindphone_send_button_text"));
		mBindPhoneBtn.setTextSize(Util.getTextSize(mContext, 45));
		mBindPhoneBtn.setId(ID_REGISTER);
		mLayout.addView(mBindPhoneBtn);
	}
	
	public String getPhone(){
		return mUserNameEdit.getEditText().getText().toString();
	}
	
	public String getValCode(){
		return mPhoneCodeEdit.getText().toString();
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
			mBindPhoneBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					l.onButtonClick(v);
				}
			});
		}
	}

	/** 发送验证码（绑定手机） **/
	private void getCode() {
		final String phone = mUserNameEdit.getEditText().getText().toString();
		if (TextUtils.isEmpty(phone)) {
			Util.showToast(mContext, Util.getString(mContext, "a8_login_tips_please_enter_phone"));
			setSendCodeButtonVisibility(false);
			return;
		}
		if (!Util.isPhone(phone)) {
			Util.showToast(mContext, Util.getString(mContext, "a8_login_tips_please_enter_correctphone"));
			setSendCodeButtonVisibility(false);
			return;
		}
		if(!Util.checkNet(mContext)) {
			return;
		}
		mIsSending = true;
		timer = new TimeCount(60 * 1000, 1000, mContext, (Button)mPhoneCodeEdit.getRightIcon(), new TimeCountListener() {
			
			@Override
			public void onTick(long millisUntilFinished) {
				
			}
			
			@Override
			public void onFinish() {
				mIsSending = false;
			}
		});
		timer.start();
		mUserCallback.getCode(mUser.getToken(), mUser.getUid(), phone, UserConfig.FLAG_SEND_CODE_1, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					if (response.getInt(UserAction.CODE) == UserAction.RESULT_OK) {
						Util.showToast(mContext, Util.getString(mContext, "a8_bindphone_authcode_sended"));
					} else {
						if (!TextUtils.isEmpty(response.getString(UserAction.DESC))) {
							 Util.showToast(mContext, response.getString(UserAction.DESC));
						 } else {
							 Util.showToast(mContext, Util.getString(mContext, "a8_bindphone_authcode_send_fail"));
						 }
						mIsSending = false;
						timer.onCancel();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Util.showToast(mContext, Util.getString(mContext, "a8_bindphone_authcode_send_fail"));
				mIsSending = false;
				timer.onCancel();
			}
		});
	}
	

	private void setSendCodeButtonVisibility(boolean visibility) {
		Button btn = (Button) mPhoneCodeEdit.getRightIcon();
		if(visibility) {
			btn.setEnabled(true);
			btn.setTextColor(Util.getColor(mContext, "a8_bindphone_send_button_text"));
			btn.setBackgroundResource(Util.getDrawableId(mContext, "a8_login_code_btn_normal_bg"));
		} else {
			btn.setEnabled(false);
			btn.setTextColor(Util.getColor(mContext, "a8_login_phoneregister_button"));
			btn.setBackgroundResource(Util.getDrawableId(mContext, "a8_login_code_btn_disabled_bg"));
		}
	}
}
