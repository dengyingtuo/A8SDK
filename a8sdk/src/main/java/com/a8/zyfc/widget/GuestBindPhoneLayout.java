package com.a8.zyfc.widget;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a8.zyfc.http.JsonHttpResponseHandler;
import com.a8.zyfc.UserConfig;
import com.a8.zyfc.http.UserAction;
import com.a8.zyfc.util.TimeCount;
import com.a8.zyfc.util.TimeCount.TimeCountListener;
import com.a8.zyfc.util.Util;
import com.a8.zyfc.widget.SdkEditText.onRightIconClickListener;
/**
 * 游客绑定手机号界面布局
 * @author Chooper
 *
 */
public class GuestBindPhoneLayout extends RelativeLayout {
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
	private SdkEditText mValCodeEdit;
	private Button mBindBtn;
	private boolean mIsDisplay = false;
	private TextView mBindAccountTv;
	private ImageView mBackView;
	private boolean mIsSending = false;
	private UserAction mUserCallback;
	private TimeCount timer;

	public GuestBindPhoneLayout(Context context) {
		super(context);
		mContext = context;
		mUserCallback = new UserAction(mContext);
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
		mHeight = Util.getInt(mContext, 650);
		mLayout = new RelativeLayout(mContext);
		mLayout.setLayoutParams(new FrameLayout.LayoutParams(mWidth, mHeight));
		mLayout.setBackgroundResource(Util.getDrawableId(mContext, "a8_login_dialog_bg"));
		addView(mLayout);
	}
	/**
	 * 绘制界面title
	 */
	private void initTitle() {
		mLoginTitle = new LoginTitle(mContext);
		mLoginTitle.setTitle(Util.getString(mContext, "a8_guestbindphone_title"));
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
	 * 绘制用户输入框（手机号，密码，手机验证码输入框）
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
		mUserNameEdit.setHint(Util.getString(mContext, "a8_phone_register_phone"));
		mUserNameEdit.setLeftIcon(Util.getDrawableId(mContext, "a8_login_title_user"));
		mUserNameEdit.getEditText().setSingleLine(true);
		mUserNameEdit.setMaxLength(11);
		mUserNameEdit.getEditText().setEllipsize(TextUtils.TruncateAt.END);
		mUserNameEdit.getEditText().setImeOptions(EditorInfo.IME_ACTION_NEXT);
		mUserNameEdit.setNumberType();
		mUserNameEdit.getEditText().addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
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
		
		mPwdEdit = new SdkEditText(mContext);
		mPwdEdit.setHint(Util.getString(mContext, "a8_login_pwdedit_hint"));
		mPwdEdit.setLeftIcon(Util.getDrawableId(mContext, "a8_login_title_pwd"));
		mPwdEdit.setRightIcon(Util.getDrawableId(mContext, "a8_login_title_hide"));
		mPwdEdit.getEditText().setSingleLine(true);
		mPwdEdit.setMaxLength(20);
		mPwdEdit.getEditText().setEllipsize(TextUtils.TruncateAt.END);
		mPwdEdit.getEditText().setImeOptions(EditorInfo.IME_ACTION_NEXT);
		mPwdEdit.setPasswordType();
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
		
		
		mValCodeEdit = new SdkEditText(mContext, Util.getInt(mContext, 174), false);
		mValCodeEdit.setHint(Util.getString(mContext, "a8_phone_register_code"));
		mValCodeEdit.setLeftIcon(Util.getDrawableId(mContext, "a8_login_title_phone"));
		mValCodeEdit.getEditText().setSingleLine(true);
		mValCodeEdit.getEditText().setEllipsize(TextUtils.TruncateAt.END);
		mValCodeEdit.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
		mValCodeEdit.setNumberType();
		mValCodeEdit.setRightIcon(Util.getDrawableId(mContext, "a8_login_code_btn_disabled_bg"));
		final Button btn = (Button) mValCodeEdit.getRightIcon();
		btn.setText(Util.getString(mContext, "a8_bindphone_send_btn"));
		btn.setTextSize(Util.getTextSize(mContext, 28));
		btn.setTextColor(Util.getColor(mContext, "a8_login_phoneregister_button"));
		btn.setEnabled(false);
		mValCodeEdit.setNumberType();
		mValCodeEdit.setRighgIconOnClickListener(new onRightIconClickListener() {
			
			@Override
			public void onClick(View v) {
				getCode();
			}
		});
		
		

		mLoginEditTexts.addSdkEditText(mValCodeEdit);
		
		mLoginEditTexts.show();
		mLayout.addView(mLoginEditTexts);
	}
	/**
	 * 绘制绑定按钮
	 */
	private void initButtons() {
		mBindBtn = new Button(mContext);
		mBtnWidth = Util.getInt(mContext, 800);
		mBtnHeight = Util.getInt(mContext, 100);
		LayoutParams elp = new LayoutParams(mBtnWidth, mBtnHeight);
		elp.addRule(BELOW, ID_EDIT);
		elp.addRule(ALIGN_LEFT, ID_EDIT);
		elp.addRule(ALIGN_RIGHT, ID_EDIT);
		elp.topMargin = Util.getInt(mContext, 20);
		mBindBtn.setLayoutParams(elp);
		mBindBtn.setBackgroundResource(Util.getDrawableId(mContext, "a8_login_btn_bg_selector"));
		mBindBtn.setText(Util.getString(mContext, "a8_guestbindphone_btn"));
		mBindBtn.setTextColor(Util.getColor(mContext, "a8_bindphone_send_button_text"));
		mBindBtn.setTextSize(Util.getTextSize(mContext, 45));
		mBindBtn.setId(ID_REGISTER);
		mLayout.addView(mBindBtn);
	}
	/**
	 * 绘制前往其他界面link
	 */
	private void initLinks() {
		mBindAccountTv = new TextView(mContext);
		LayoutParams rlp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rlp.addRule(BELOW, ID_REGISTER);
		rlp.addRule(ALIGN_RIGHT, ID_REGISTER);
		rlp.addRule(CENTER_VERTICAL);
		rlp.topMargin = Util.getInt(mContext, 20);;
		mBindAccountTv.setLayoutParams(rlp);
		mBindAccountTv.setPadding(0, 0, 0, 0);
		mBindAccountTv.setText(Html.fromHtml("<u>" + Util.getString(mContext, "a8_guestbindphone_link_left") + "</u>"));
		mBindAccountTv.setTextColor(Util.getColor(mContext, "a8_link_text"));
		mBindAccountTv.setTextSize(Util.getTextSize(mContext, 30));
		mBindAccountTv.setVisibility(INVISIBLE);/****去掉游客绑定账号功能*****/
		mLayout.addView(mBindAccountTv);
	}
	/**
	 * 设置“发送验证码”按钮点击状态
	 * @param visibility 是否可点击
	 */
	public void setSendCodeButtonVisibility(boolean visibility) {
		Button btn = (Button) mValCodeEdit.getRightIcon();
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
	
	public SdkEditText getUserEditText() {
		return mUserNameEdit;
	}
	
	public SdkEditText getPwdEditText() {
		return mPwdEdit;
	}
	
	public SdkEditText getValCodeEditText() {
		return mValCodeEdit;
	}
	
	public String getUserName() {
		return mUserNameEdit.getText();
	}
	
	public String getPwd() {
		return mPwdEdit.getText();
	}

	public String getValCode() {
		return mValCodeEdit.getText();
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
	
	public void setOnLinkClickListener(final OnLinkClickListener l) {
		if(l != null) {
			mBindAccountTv.setOnClickListener(new OnClickListener() {
				
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
	
	/** 发送验证码（FLAG值同绑定手机） **/
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
		timer = new TimeCount(60 * 1000, 1000, mContext, (Button)mValCodeEdit.getRightIcon(), new TimeCountListener() {
			
			@Override
			public void onTick(long millisUntilFinished) {
				
			}
			
			@Override
			public void onFinish() {
				mIsSending = false;
			}
		});
		timer.start();
		mUserCallback.getCode(phone, UserConfig.FLAG_SEND_CODE_1, new JsonHttpResponseHandler() {

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
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Util.showToast(mContext, Util.getString(mContext, "a8_bindphone_authcode_send_fail"));
				mIsSending = false;
				timer.onCancel();
			}
		});
	}
	
}
