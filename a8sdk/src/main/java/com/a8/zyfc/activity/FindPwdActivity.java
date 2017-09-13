package com.a8.zyfc.activity;

import com.a8.zyfc.util.Util;
import com.a8.zyfc.widget.FindPwdLayout;
import com.a8.zyfc.widget.OnLinkClickListener;
import com.a8.zyfc.widget.onButtonClickListener;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class FindPwdActivity extends BaseActivity {
	private FindPwdLayout mLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLayout = new FindPwdLayout(this);
		setContentView(mLayout);
		mLayout.setOnButtonClickListener(new onButtonClickListener() {

			@Override
			public void onButtonClick(View v) {
				Intent intent = new Intent(FindPwdActivity.this, ResetPwdActivity.class);
				intent.putExtra("user", mLayout.getUser());
				startActivity(intent);
				finish();
			}
		});
		mLayout.setOnLinkClickListener(new OnLinkClickListener() {

			@Override
			public void onRightClick(View v) {
				startActivity(new Intent(FindPwdActivity.this, LoginDialogActivity.class));
				finish();
			}

			@Override
			public void onLeftClick(View v) {
				if(Util.isQQClientAvailable(mContext)){
					try {
						//直接拉起客服QQ号
						String url="mqqwpa://im/chat?chat_type=wpa&uin=" + Util.getString(mContext, "pay_service_qq");
						mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
					}catch(Exception e){
						Util.showToast(mContext, "QQ未安装或版本太低，请前往应用市场安装或更新！");
						e.printStackTrace();
					}
				}else{
					Util.showToast(mContext, "QQ未安装，请下载安装！");
					try {
						String url="https://im.qq.com/mobileqq";
						mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}
