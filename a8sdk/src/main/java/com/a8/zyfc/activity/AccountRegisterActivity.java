package com.a8.zyfc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.a8.zyfc.model.UserTO;
import com.a8.zyfc.widget.AccountRegisterLayout;
import com.a8.zyfc.widget.OnLinkClickListener;
import com.a8.zyfc.widget.onButtonClickListener;

public class AccountRegisterActivity extends BaseActivity {
	private AccountRegisterLayout mLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLayout = new AccountRegisterLayout(this);
		setContentView(mLayout);
		mLayout.setOnButtonClickListener(new onButtonClickListener() {
			
			@Override
			public void onButtonClick(View v) {
				new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
					
					@Override
					public void run() {
						UserTO user = mLayout.getRegisterUser();
						Intent intent = new Intent(AccountRegisterActivity.this, BindPhoneNunberActivity.class);
						intent.putExtra("actName", AccountRegisterActivity.class.getSimpleName());
						intent.putExtra("user", user);
						startActivity(intent);
						finish();
					}
				}, 1000);
			}
		});
		mLayout.setOnLinkClickListener(new OnLinkClickListener() {
			
			@Override
			public void onRightClick(View v) {
				startActivity(new Intent(AccountRegisterActivity.this, LoginDialogActivity.class));
				finish();
			}
			
			@Override
			public void onLeftClick(View v) {
				startActivity(new Intent(AccountRegisterActivity.this, PhoneRegisterActivity.class));
				finish();
			}
		});
	}
}
