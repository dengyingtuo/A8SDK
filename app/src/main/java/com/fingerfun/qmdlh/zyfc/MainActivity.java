package com.fingerfun.qmdlh.zyfc;

import java.io.File;
import java.util.UUID;

import com.a8.zyfc.PayCallback;
import com.a8.zyfc.ShareCallBack;
import com.a8.zyfc.model.ShareBean;
import com.fingerfun.qmdlh.zyfc.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	private TextView log;
	private PopupWindow popupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		log = (TextView)findViewById(R.id.log);
	}

	public void onClick(View v){
		switch (v.getId()) {
		case R.id.logout:
			MyApplication.a8sdk.A8Logout();
			log.setText("注销成功！");
			startActivity(new Intent(this, LoginActivity.class));
			finish();
			break;
		case R.id.pay:
			UUID uuid = UUID.randomUUID();
			String gameOrderId = "order_zyfc_" + uuid.toString();// 测试订单号
			MyApplication.a8sdk.A8Pay(this, "11111", gameOrderId, "1", "800004", "100金币", "test_Id", "test_player", "", new PayCallback(){

				@Override
				public void onFinished(int code, String content) {
					switch (code) {
					case PayCallback.PAY_SUCC:
						Log.d("a8sdk", "支付成功！");
						log.setText("支付成功");
						break;
					case PayCallback.PAY_UNSUCC:
						Log.d("a8sdk","支付失败");
						log.setText("支付失败");
						break;
					}
				}
			});
			break;
		case R.id.sharetxt:
			ShareBean share3 = new ShareBean();
			share3.contentType = ShareBean.SHARE_CONTENT_TEXT;
			share3.text = "英雄联盟里的每一个英雄都有着自己独特而富有魅力的个性，与众不同的他们也有着与众不同的故事。"
					+ "热爱着英雄联盟的召唤师们多年来以不同的方式表达着自己对于英雄和故事的喜爱，涌现了一大批令人惊艳惟妙惟肖的COSPLAY作品。"
					+ "为了激励更多的玩家参与，我们即将举办Cosplay正选活动！获选的召唤师们将有机会参与到2017全球总决赛在武汉、广州、上海、北京举行各站的比赛活动中来。";
			showSharePopupWin(v, share3);
			break;
		case R.id.shareimg:
			ShareBean share = new ShareBean();
			share.contentType = ShareBean.SHARE_CONTENT_IMG;
			share.imgPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "1.png";
			showSharePopupWin(v, share);
			break;
		case R.id.shareweb:
			ShareBean share2 = new ShareBean();
			share2.contentType = ShareBean.SHARE_CONTENT_WEB;
			share2.title = "分享测试";
			share2.summary = "分享测试";
			share2.thumbUrl = Environment.getExternalStorageDirectory().getPath() + File.separator + "2.png";
//			share2.thumbUrl = "http://ossweb-img.qq.com/upload/webplat/info/lol/20170712/294881307062540.jpeg";
//			share2.thumbUrl = "http://ossweb-img.qq.com/upload/adw/image/1500210337/1500210337.jpg";
			share2.pageUrl = "http://m.3333.cn";
			showSharePopupWin(v, share2);
			break;
		default:
			break;
		}
	}

	public void showSharePopupWin(View view, final ShareBean share){
		if (popupWindow != null && popupWindow.isShowing()) {
			return;
		}
		LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.share_popupwin_layout, null);
		popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		//点击空白处时，隐藏掉pop窗口
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		//添加弹出、弹入的动画
		popupWindow.setAnimationStyle(R.style.MyBottomPopupwindow);
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		popupWindow.showAtLocation(view, Gravity.LEFT | Gravity.BOTTOM, 0, -location[1]);
		//添加按键事件监听
		RadioGroup shareRG = (RadioGroup) layout.findViewById(R.id.share_rg);
		Button shareCancel = (Button) layout.findViewById(R.id.share_cancel);

		shareRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {
				switch (i){
				case R.id.share_wxfriends:
					share.type = ShareBean.SHARE_TYPE_WX;
					break;
				case R.id.share_wxzone:
					share.type = ShareBean.SHARE_TYPE_WXZONE;
					break;
				case R.id.share_qqfriends:
					share.type = ShareBean.SHARE_TYPE_QQ;
					break;
				case R.id.share_qqzone:
					share.type = ShareBean.SHARE_TYPE_QZONE;
					break;
				}
				MyApplication.a8sdk.A8Share(MainActivity.this, share, shareCallBack);
			}
		});

		shareCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
	}

	private ShareCallBack shareCallBack = new ShareCallBack() {

		@Override
		public void onCallBack(int code) {
			if (popupWindow != null && popupWindow.isShowing()) {
				popupWindow.dismiss();
			}
			switch (code) {
			case ShareCallBack.SHARE_OK:
				Toast.makeText(MainActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
				break;
			case ShareCallBack.SHARE_CANCEL:
				Toast.makeText(MainActivity.this, "分享取消", Toast.LENGTH_SHORT).show();
				break;
			case ShareCallBack.SHARE_ERR:
				Toast.makeText(MainActivity.this, "分享错误", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		MyApplication.a8sdk.onShareActivityResult(requestCode, resultCode, data);
	}

	private long exitTime;
	@Override   
	public boolean onKeyDown(int keyCode, KeyEvent event) {    
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if((System.currentTimeMillis()-exitTime)>3000){
				Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
				exitTime=System.currentTimeMillis();
			}else{
				MyApplication.exit();
			}

		}
		return false;    
	}

}
