package com.a8.zyfc.widget;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a8.zyfc.http.JsonHttpResponseHandler;
import com.a8.zyfc.UserConfig;
import com.a8.zyfc.http.UserAction;
import com.a8.zyfc.model.UserTO;
import com.a8.zyfc.util.JSONUtils;
import com.a8.zyfc.util.TimeCount;
import com.a8.zyfc.util.Util;
import com.a8.zyfc.widget.SdkEditText.onRightIconClickListener;

public class FindPwdLayout extends RelativeLayout {
	public final static int ID_TITLE = 1000;
	public final static int ID_EDIT = 1001;
	public final static int ID_REGISTER = 1002;
	public final static int ID_LEFT_LEFT = 1003;
	public final static int ID_LEFT_CENTER = 1004;
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
	private Button mNextBtn;
	private TextView mRightTv;
	private TextView mLeft_Left_Tv;
	private TextView mLeft_Center_Tv;
	private TextView mLeft_Right_Tv;
	private UserAction mUserCallback;
	private UserTO mUser;
	private TimeCount timer;

	public FindPwdLayout(Context context) {
		super(context);
		mContext = context;
		mUserCallback = new UserAction(mContext);
		initLayout();
		initTitle();
		initEdits();
		initButtons();
		initLink();
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
		mLoginTitle.setTitle(Util.getString(mContext, "a8_findpwd_title"));
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
		mUserNameEdit.setHint(Util.getString(mContext, "a8_findpwd_uedit_hint"));
		mUserNameEdit.setLeftIcon(Util.getDrawableId(mContext, "a8_login_title_user"));
		mUserNameEdit.getEditText().setSingleLine(true);
		mUserNameEdit.getEditText().setEllipsize(TruncateAt.END);
		mUserNameEdit.getEditText().setMaxWidth(20);
		mUserNameEdit.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
		mLoginEditTexts.addSdkEditText(mUserNameEdit);
		
		mPhoneCodeEdit = new SdkEditText(mContext, Util.getInt(mContext, 174), false);
		mPhoneCodeEdit.setHint(Util.getString(mContext, "a8_phone_register_code"));
		mPhoneCodeEdit.setLeftIcon(Util.getDrawableId(mContext, "a8_login_title_phone"));
		mPhoneCodeEdit.setRightIcon(Util.getDrawableId(mContext, "a8_login_code_btn_normal_bg"));
		mPhoneCodeEdit.getEditText().setSingleLine(true);
		mPhoneCodeEdit.getEditText().setEllipsize(TruncateAt.END);
		final Button btn = (Button) mPhoneCodeEdit.getRightIcon();
		btn.setText(Util.getString(mContext, "a8_bindphone_send_btn"));
		btn.setTextSize(Util.getTextSize(mContext, 28));
		btn.setTextColor(Util.getColor(mContext, "a8_bindphone_send_button_text"));
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
		mNextBtn = new Button(mContext);
		mBtnWidth = Util.getInt(mContext, 800);
		mBtnHeight = Util.getInt(mContext, 103);
		LayoutParams elp = new LayoutParams(mBtnWidth, mBtnHeight);
		elp.addRule(BELOW, ID_EDIT);
		elp.addRule(ALIGN_LEFT, ID_EDIT);
		elp.addRule(ALIGN_RIGHT, ID_EDIT);
		elp.topMargin = Util.getInt(mContext, 23);
		mNextBtn.setLayoutParams(elp);
		mNextBtn.setBackgroundResource(Util.getDrawableId(mContext, "a8_login_btn_bg_selector"));
		mNextBtn.setText(Util.getString(mContext, "a8_findpwd_btn"));
		mNextBtn.setTextColor(Util.getColor(mContext, "a8_bindphone_send_button_text"));
		mNextBtn.setTextSize(Util.getTextSize(mContext, 45));
		mNextBtn.setId(ID_REGISTER);
		mLayout.addView(mNextBtn);
	}
	
	private void initLink() {
		int topMargin = Util.getInt(mContext, 10);		
		mRightTv = new TextView(mContext);
		LayoutParams rlp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		rlp.addRule(BELOW, ID_REGISTER);
		rlp.addRule(ALIGN_RIGHT, ID_REGISTER);
		rlp.addRule(CENTER_VERTICAL);
		rlp.topMargin = topMargin;
		mRightTv.setLayoutParams(rlp);
		mRightTv.setPadding(0, 0, 0, 0);
		mRightTv.setText(Html.fromHtml("<u>" + Util.getString(mContext, "a8_register_link_backtologin") + "</u>"));
		mRightTv.setTextColor(Util.getColor(mContext, "a8_link_text"));
		mRightTv.setTextSize(Util.getTextSize(mContext, 30));
		mLayout.addView(mRightTv);
		
		mLeft_Left_Tv = new TextView(mContext);
		LayoutParams lllp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		lllp.addRule(BELOW, ID_REGISTER);
		lllp.addRule(ALIGN_LEFT, ID_REGISTER);
		lllp.addRule(CENTER_VERTICAL);
		lllp.topMargin = topMargin;
		mLeft_Left_Tv.setLayoutParams(lllp);
		mLeft_Left_Tv.setPadding(0, 0, 0, 0);
		mLeft_Left_Tv.setText(Util.getString(mContext, "a8_findpwd_link_tip1"));
		mLeft_Left_Tv.setTextColor(Util.getColor(mContext, "a8_login_title_split_line"));
		mLeft_Left_Tv.setTextSize(Util.getTextSize(mContext, 30));
		mLeft_Left_Tv.setId(ID_LEFT_LEFT);
		mLayout.addView(mLeft_Left_Tv);
		
		mLeft_Center_Tv = new TextView(mContext);
		LayoutParams lclp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		lclp.addRule(RIGHT_OF, ID_LEFT_LEFT);
		lclp.addRule(ALIGN_BOTTOM, ID_LEFT_LEFT);
		lclp.addRule(ALIGN_TOP, ID_LEFT_LEFT);
		lclp.addRule(CENTER_VERTICAL);
		mLeft_Center_Tv.setLayoutParams(lclp);
		mLeft_Center_Tv.setPadding(0, 0, 0, 0);
		mLeft_Center_Tv.setText(Html.fromHtml("<u>" + Util.getString(mContext, "a8_findpwd_link_tip2") + "</u>"));
		mLeft_Center_Tv.setTextColor(Util.getColor(mContext, "a8_link_text"));
		mLeft_Center_Tv.setTextSize(Util.getTextSize(mContext, 30));
		mLeft_Center_Tv.setId(ID_LEFT_CENTER);
		mLayout.addView(mLeft_Center_Tv);
		
		mLeft_Right_Tv = new TextView(mContext);
		LayoutParams lrlp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		lrlp.addRule(RIGHT_OF, ID_LEFT_CENTER);
		lrlp.addRule(ALIGN_BOTTOM, ID_LEFT_LEFT);
		lrlp.addRule(ALIGN_TOP, ID_LEFT_LEFT);
		lrlp.addRule(CENTER_VERTICAL);
		mLeft_Right_Tv.setLayoutParams(lrlp);
		mLeft_Right_Tv.setPadding(0, 0, 0, 0);
		mLeft_Right_Tv.setText(Util.getString(mContext, "a8_findpwd_link_tip3"));
		mLeft_Right_Tv.setTextColor(Util.getColor(mContext, "a8_login_title_split_line"));
		mLeft_Right_Tv.setTextSize(Util.getTextSize(mContext, 30));
		mLayout.addView(mLeft_Right_Tv);
	}
	
	public UserTO getUser() {
		return mUser;
	}
	
	public void setSendCodeButtonVisibility(boolean visibility) {
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
	
	public void setOnButtonClickListener(final onButtonClickListener l) {
		if(l != null) {
			mNextBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(final View v) {
					String uName = mUserNameEdit.getText();
					String valCode = mPhoneCodeEdit.getText();
					if(TextUtils.isEmpty(uName)) {
						Util.showToast(mContext, Util.getString(mContext, "a8_findpwd_uedit_hint"));
						return;
					}
					if(uName.length() < 6 || uName.length() > 20) {
						Util.showToast(mContext, Util.getString(mContext, "a8_findpwd_tips_please_enter_correctuname"));
						return;
					}
					if(TextUtils.isEmpty(valCode)) {
						Util.showToast(mContext, Util.getString(mContext, "a8_login_tips_please_enter_valcode"));
						return;
					}
					if(mUser == null){
						mUser = new UserTO();
						mUser.setUserName(uName);
						mUser.setAuthCode(valCode);
					}else{
						mUser.setAuthCode(valCode);
					}
					mUserCallback.verifyBackCode(uName, valCode, UserConfig.FLAG_VERIFY_CODE, new JsonHttpResponseHandler(){
						@Override
						public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
							super.onSuccess(statusCode, headers, response);
							try {
								if (response.getInt(UserAction.CODE) == UserAction.RESULT_OK) {
									Util.showToast(mContext, Util.getString(mContext, "a8_findpwd_tips_auth_success"));
									l.onButtonClick(v);
								} else {
									if (!TextUtils.isEmpty(response.getString(UserAction.DESC))) {
										 Util.showToast(mContext, response.getString(UserAction.DESC));
									 } else {
										 Util.showToast(mContext, Util.getString(mContext, "a8_findpwd_tips_send_fail"));
									 }
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);
							Util.showToast(mContext, Util.getString(mContext, "a8_findpwd_tips_send_fail"));
						}
					});
				}
			});
		}
	}
	
	public void setOnLinkClickListener(final OnLinkClickListener l) {
		if(l != null) {
			mLeft_Center_Tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					l.onLeftClick(v);
				}
			});
			
			mRightTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					l.onRightClick(v);
				}
			});
		}
	}

	/** 发送验证码（修改密码） **/
	private void getCode() {
		String phone = mUserNameEdit.getEditText().getText().toString();
		if (TextUtils.isEmpty(phone)) {
			Util.showToast(mContext, Util.getString(mContext, "a8_login_tips_please_enter_phone"));
			return;
		}
		if(!Util.checkNet(mContext)) {
			return;
		}
		timer = new TimeCount(60 * 1000, 1000, mContext, (Button)mPhoneCodeEdit.getRightIcon(), null);
		timer.start();
		mUserCallback.getCode(phone, UserConfig.FLAG_SEND_CODE_2, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				Log.e("A8SDK", "getCode:" + response.toString());
				try {
					if (response.getInt(UserAction.CODE) == UserAction.RESULT_OK) {
						Util.showToast(mContext, Util.getString(mContext, "a8_bindphone_authcode_sended"));
						mUser = JSONUtils.fromJson(response.getString(UserAction.MSG), UserTO.class);
						mUserNameEdit.setText(mUser.getMobile());
					} else {
						if (!TextUtils.isEmpty(response.getString(UserAction.DESC))) {
							 Util.showToast(mContext, response.getString(UserAction.DESC));
						 } else {
							 Util.showToast(mContext, Util.getString(mContext, "a8_bindphone_authcode_send_fail"));
						 }
						timer.onCancel();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				 Util.showToast(mContext, Util.getString(mContext, "a8_bindphone_authcode_send_fail"));
				timer.onCancel();
			}
		});
	}
}
