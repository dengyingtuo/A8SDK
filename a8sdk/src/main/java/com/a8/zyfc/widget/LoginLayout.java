package com.a8.zyfc.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a8.zyfc.adapter.AccountAdapter;
import com.a8.zyfc.db.DatabaseUtil;
import com.a8.zyfc.model.UserTO;
import com.a8.zyfc.util.Util;
import com.a8.zyfc.widget.SdkEditText.onRightIconClickListener;

public class LoginLayout extends RelativeLayout {
	public final static int ID_TITLE = 1000;
	public final static int ID_EDIT = 1001;
	public final static int ID_LOGIN = 1002;
	public final static int ID_REGISTER = 1003;
	private Context mContext;
	private int mWidth;
	private int mHight;
	private int mBtnWidth;
	private int mBtnHeight;
	private RelativeLayout mLayout;
	private LoginTitle mLoginTitle;
	private LoginEditTexts mLoginEditTexts;
	private SdkEditText mUserNameEdit;
	private SdkEditText mPwdEdit;
	private Button mEnterBtn;
	private Button mRegisterBtn;
	private TextView mRightTv;
	private PopupWindow popupWindow;
	private boolean mIsDisplay = false;
	private List<UserTO> mUsers;
	private ImageView mBackView;

	public LoginLayout(Context context) {
		super(context);
		mContext = context;
		initData();
		initLayout();
		initTitle();
		initEdits();
		initButtons();
		initLink();
	}

	private void initData(){
		//只取手机账号，剔除游客及第三方账号
		mUsers = new ArrayList<UserTO>();
		for(UserTO user : DatabaseUtil.getInstance(mContext).getAllUser(10)){
			if(user.getThirdType() == 10) mUsers.add(user);
		}
	}

	private void initLayout() {
		mWidth = Util.getInt(mContext, 960);
		mHight = Util.getInt(mContext, 670);
		mLayout = new RelativeLayout(mContext);
		mLayout.setLayoutParams(new FrameLayout.LayoutParams(mWidth, mHight));
		mLayout.setBackgroundResource(Util.getDrawableId(mContext, "a8_login_dialog_bg"));
		addView(mLayout);
	}

	private void initTitle() {
		mLoginTitle = new LoginTitle(mContext);
		mLoginTitle.setTitle(Util.getString(mContext, "a8_login_title"));
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
		mUserNameEdit.setHint(Util.getString(mContext, "a8_login_uedit_hint"));
		mUserNameEdit.setLeftIcon(Util.getDrawableId(mContext, "a8_login_title_user"));
		mUserNameEdit.getEditText().setSingleLine(true);
		mUserNameEdit.getEditText().setMaxWidth(20);
		mUserNameEdit.getEditText().setEllipsize(TextUtils.TruncateAt.END);
		mUserNameEdit.getEditText().setImeOptions(EditorInfo.IME_ACTION_NEXT);

		if(mUsers != null && mUsers.size() > 0) {
			mUserNameEdit.setRightIcon(Util.getDrawableId(mContext, "a8_login_account_select_down"));
			mUserNameEdit.setRighgIconOnClickListener(new onRightIconClickListener() {

				@Override
				public void onClick(View v) {
					if(popupWindow == null) {
						initAccountListView();
					} else if(popupWindow.isShowing()) {
						popupWindow.dismiss();
					} else {
						mUserNameEdit.setRightIcon(Util.getDrawableId(mContext, "a8_login_account_select_up"));
						popupWindow.showAsDropDown(mUserNameEdit.getEditText(), -Util.getInt(mContext, 10), Util.getInt(mContext, 8));
					}
				}
			});
		}

		mLoginEditTexts.addSdkEditText(mUserNameEdit);

		mPwdEdit = new SdkEditText(mContext);
		mPwdEdit.setHint(Util.getString(mContext, "a8_login_pwdedit_hint"));
		mPwdEdit.setLeftIcon(Util.getDrawableId(mContext, "a8_login_title_pwd"));
		mPwdEdit.setRightIcon(Util.getDrawableId(mContext, "a8_login_title_hide"));
		mPwdEdit.getEditText().setSingleLine(true);
		mPwdEdit.setMaxLength(20);
		mPwdEdit.getEditText().setEllipsize(TextUtils.TruncateAt.END);
		mPwdEdit.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
		mPwdEdit.setPasswordType();
		mPwdEdit.setRighgIconOnClickListener(new onRightIconClickListener() {

			@Override
			public void onClick(View v) {
				if(mIsDisplay) {
					mPwdEdit.setPasswordType();
					mPwdEdit.setRightIcon(Util.getDrawableId(mContext, "a8_login_title_hide"));
				} else {
					mPwdEdit.setRightIcon(Util.getDrawableId(mContext, "a8_login_title_display"));
					mPwdEdit.setVisiblePasswordType();
				}
				mIsDisplay = !mIsDisplay;
			}
		});
		mLoginEditTexts.addSdkEditText(mPwdEdit);

		if(mUsers != null && mUsers.size() > 0) {
			mUserNameEdit.getEditText().setText(mUsers.get(0).getUserName());
			mPwdEdit.getEditText().setText(mUsers.get(0).getPassword());
		}else if(null != Util.getFromSharedPreferences("a8_lastLoginUser", mContext, null)){
			Log.d("a8sdk", "LoginView---"+Util.getFromSharedPreferences("a8_lastLoginUser", mContext));
			setUser(Util.getFromSharedPreferences("a8_lastLoginUser", mContext));
		}

		mLoginEditTexts.show();
		mLayout.addView(mLoginEditTexts);
	}

	private void initButtons() {
		mEnterBtn = new Button(mContext);
		mBtnWidth = Util.getInt(mContext, 800);
		mBtnHeight = Util.getInt(mContext, 103);
		LayoutParams elp = new LayoutParams(mBtnWidth, mBtnHeight);
		elp.addRule(BELOW, ID_EDIT);
		elp.addRule(ALIGN_LEFT, ID_EDIT);
		elp.addRule(ALIGN_RIGHT, ID_EDIT);
		elp.topMargin = Util.getInt(mContext, 13);
		mEnterBtn.setLayoutParams(elp);
		mEnterBtn.setBackgroundResource(Util.getDrawableId(mContext, "a8_login_btn_bg_selector"));
		mEnterBtn.setText(Util.getStringId(mContext, "a8_login_entergame"));
		mEnterBtn.setTextColor(Util.getColor(mContext, "a8_login_entergame_button"));
		mEnterBtn.setTextSize(Util.getTextSize(mContext, 45));
		mEnterBtn.setId(ID_LOGIN);
		mLayout.addView(mEnterBtn);

		mRegisterBtn = new Button(mContext);
		LayoutParams rlp = new LayoutParams(Util.getInt(mContext, 420), mBtnHeight);
		rlp.addRule(BELOW, ID_LOGIN);
		rlp.addRule(ALIGN_LEFT, ID_EDIT);
		rlp.addRule(ALIGN_RIGHT, ID_EDIT);
		rlp.topMargin = Util.getInt(mContext, 13);
		mRegisterBtn.setLayoutParams(rlp);
		mRegisterBtn.setBackgroundResource(Util.getDrawableId(mContext, "a8_register_btn_bg_selector"));
		mRegisterBtn.setText(Util.getStringId(mContext, "a8_login_phoneregister"));
		mRegisterBtn.setTextColor(Util.getColor(mContext, "a8_login_phoneregister_button_text"));
		mRegisterBtn.setTextSize(Util.getTextSize(mContext, 45));
		mRegisterBtn.setId(ID_REGISTER);
		mLayout.addView(mRegisterBtn);
	}

	private void initLink() {
		int topMargin = Util.getInt(mContext, 15);
		mRightTv = new TextView(mContext);
		LayoutParams rlp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rlp.addRule(BELOW, ID_REGISTER);
		rlp.addRule(ALIGN_RIGHT, ID_LOGIN);
		rlp.addRule(CENTER_VERTICAL);
		rlp.topMargin = topMargin;
		mRightTv.setLayoutParams(rlp);
		mRightTv.setPadding(0, 0, 0, 0);
		mRightTv.setText(Html.fromHtml("<u>" + Util.getString(mContext, "a8_login_link_forget_pwd") + "</u>"));
		mRightTv.setTextColor(Util.getColor(mContext, "a8_link_text"));
		mRightTv.setTextSize(Util.getTextSize(mContext, 30));
		mLayout.addView(mRightTv);
	}

	private void initAccountListView() {

		mUserNameEdit.setRightIcon(Util.getDrawableId(mContext, "a8_login_account_select_up"));
		LinearLayout layout = new LinearLayout(mContext);
		layout.setVerticalGravity(LinearLayout.VERTICAL);
		layout.setBackgroundResource(Util.getDrawableId(mContext, "a8_login_account_select_bg"));
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(lp);

		popupWindow = new PopupWindow(mLayout, Util.getInt(mContext, 680), LayoutParams.WRAP_CONTENT);
		popupWindow.setContentView(layout);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		popupWindow.setAnimationStyle(android.R.style.Animation_Toast);
		popupWindow.showAsDropDown(mUserNameEdit.getEditText(), -Util.getInt(mContext, 10), Util.getInt(mContext, 8));
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				mUserNameEdit.setRightIcon(Util.getDrawableId(mContext, "a8_login_account_select_down"));
			}
		});

		ListView listView = new ListView(mContext);
		LayoutParams lvlp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		listView.setLayoutParams(lvlp);
		listView.setAdapter(new AccountAdapter(mContext, mUsers));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mUserNameEdit.getEditText().setText(mUsers.get(position).getUserName());
				mPwdEdit.getEditText().setText(mUsers.get(position).getPassword());
				popupWindow.dismiss();
			}
		});
		layout.addView(listView);

	}

	public void setOnButtonsClickListener(final onButtonsClickListener l) {
		if(l != null) {
			mEnterBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					l.onEnterClick(v);
				}
			});

			mRegisterBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					l.onRegisterClick(v);
				}
			});

			mRightTv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					l.onLinkClick(v);
				}
			});

			mBackView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					l.onBackClick(v);
				}
			});
		}
	}

	public interface onButtonsClickListener {
		void onEnterClick(View v);
		void onRegisterClick(View v);
		void onLinkClick(View v);
		void onBackClick(View v);
	}

	/**
	 * 获取用户名
	 * @return 用户名字符串
	 */
	public String getUser() {
		return mUserNameEdit.getText();
	}

	/**
	 * 获取密码
	 * @return 用户名字符串
	 */
	public String getPassword() {
		return mPwdEdit.getText();
	}

	/**
	 * 设置用户名
	 * @param user
	 */
	public void setUser(String user) {
		mUserNameEdit.setText(user);
		mUserNameEdit.getEditText().setSelection(mUserNameEdit.getText().length());
	}

	/**
	 * 设置密码
	 * @param password
	 */
	public void setPassword(String password) {
		mPwdEdit.setText(password);
	}

}
