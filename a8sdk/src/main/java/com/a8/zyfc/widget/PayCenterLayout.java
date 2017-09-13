package com.a8.zyfc.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a8.zyfc.util.Util;

/**
 * 支付界面布局
 * @author Chooper
 *
 */
public class PayCenterLayout extends RelativeLayout {
	public final static int ID_TITLE = 1000;
	public final static int ID_CENTER = 1001;
	public final static int ID_GOODSNAME = 1002;
	public final static int ID_SPLIT = 1003;
	public final static int ID_TIPS = 1004;
	public final static int ID_TELTV = 1005;
	public final static int ID_QQTV = 1006;
	private Context mContext;
	private RelativeLayout mLayout;
	private LoginTitle mLoginTitle;
	private ImageView mBackView;
	private RelativeLayout mPayCenter;
	private TextView mGoodsNameTV;
	private TextView mGoodsPriceTV;
	private GridView mPayGridView;
	private TextView mTelTv;
	private TextView mQQTv;
	private TextView mTel;
	private TextView mQQ;

	public PayCenterLayout(Context context) {
		super(context);
		mContext = context;
		initLayout();
		initTitle();
		initGoodsDesc();
		initBottom();
	}
	/**
	 * 绘制整体布局
	 */
	private void initLayout() {
		mLayout = new RelativeLayout(mContext);
		mLayout.setLayoutParams(new FrameLayout.LayoutParams(Util.getInt(mContext, 957), LayoutParams.WRAP_CONTENT));
		mLayout.setBackgroundResource(Util.getDrawableId(mContext, "a8_login_dialog_bg"));
		addView(mLayout);
	}
	/**
	 * 绘制title
	 */
	private void initTitle() {
		mLoginTitle = new LoginTitle(mContext);
		mLoginTitle.setTitle(Util.getString(mContext, "pay_center_title"));
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
	 * 绘制商品展示栏
	 */
	private void initGoodsDesc() {
		mPayCenter = new RelativeLayout(mContext);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(BELOW, ID_TITLE);
		lp.leftMargin = Util.getInt(mContext, 30);
		lp.rightMargin = Util.getInt(mContext, 30);
		mPayCenter.setLayoutParams(lp);
		mPayCenter.setId(ID_CENTER);
		mPayCenter.setBackgroundResource(Util.getDrawableId(mContext, "a8_pay_center_bg"));

		//商品名展示
		mGoodsNameTV = new TextView(mContext);
		LayoutParams gnlp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		gnlp.addRule(ALIGN_PARENT_LEFT);
		gnlp.leftMargin = Util.getInt(mContext, 30);
		mGoodsNameTV.setLayoutParams(gnlp);
		mGoodsNameTV.setTextColor(Util.getColor(mContext, "a8_login_text"));
		mGoodsNameTV.setTextSize(Util.getTextSize(mContext, 68));
		mGoodsNameTV.setId(ID_GOODSNAME);
		mPayCenter.addView(mGoodsNameTV);

		//商品价格展示
		mGoodsPriceTV = new TextView(mContext);
		LayoutParams gplp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		gplp.addRule(ALIGN_PARENT_RIGHT);
		gplp.rightMargin = Util.getInt(mContext, 30);
		mGoodsPriceTV.setLayoutParams(gplp);
		mGoodsPriceTV.setTextColor(Util.getColor(mContext, "a8_red_text_color"));
		mGoodsPriceTV.setTextSize(Util.getTextSize(mContext, 68));
		mPayCenter.addView(mGoodsPriceTV);

		//分割线
		View mGoodsSplit = new View(mContext);
		LayoutParams vlp = new LayoutParams(LayoutParams.MATCH_PARENT, Util.getInt(mContext, 4));
		vlp.addRule(BELOW, ID_GOODSNAME);
		vlp.topMargin = Util.getInt(mContext, 30);
		vlp.leftMargin = Util.getInt(mContext, 30);
		vlp.rightMargin = Util.getInt(mContext, 30);
		vlp.bottomMargin = Util.getInt(mContext, 30);
		mGoodsSplit.setLayoutParams(vlp);
		mGoodsSplit.setBackgroundColor(Util.getColor(mContext, "a8_login_title_split_line"));
		mGoodsSplit.setId(ID_SPLIT);
		mPayCenter.addView(mGoodsSplit);

		//选择提示
		TextView mChooseTipsTV = new TextView(mContext);
		LayoutParams tlp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		tlp.addRule(BELOW, ID_SPLIT);
		tlp.addRule(ALIGN_PARENT_LEFT);
		tlp.leftMargin = Util.getInt(mContext, 30);
		mChooseTipsTV.setLayoutParams(tlp);
		mChooseTipsTV.setText(Util.getString(mContext, "pay_choose_tips"));
		mChooseTipsTV.setTextColor(Util.getColor(mContext, "a8_login_text"));
		mChooseTipsTV.setTextSize(Util.getTextSize(mContext, 50));
		mChooseTipsTV.setId(ID_TIPS);
		mPayCenter.addView(mChooseTipsTV);

		//支付种类列表
		mPayGridView = new GridView(mContext);
		LayoutParams elp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		elp.addRule(BELOW, ID_TIPS);
		elp.addRule(CENTER_HORIZONTAL);
		elp.topMargin = Util.getInt(mContext, 20);
		elp.bottomMargin = Util.getInt(mContext, 20);
		mPayGridView.setLayoutParams(elp);
		mPayGridView.setGravity(Gravity.CENTER);
		mPayGridView.setSelector(new ColorDrawable(Color.parseColor("#33666666")));
		mPayCenter.addView(mPayGridView);

		mLayout.addView(mPayCenter);
	}
	/** 绘制底部栏 */
	private void initBottom(){
		mTelTv = new TextView(mContext);
		LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.addRule(BELOW,ID_CENTER);
		lp.addRule(ALIGN_PARENT_LEFT);
		lp.leftMargin = Util.getInt(mContext, 30);
		lp.topMargin = Util.getInt(mContext, 30);
		lp.bottomMargin = Util.getInt(mContext, 30);
		mTelTv.setLayoutParams(lp);
		mTelTv.setText(Util.getString(mContext, "pay_service_teltv"));
		mTelTv.setTextColor(Util.getColor(mContext, "a8_login_text"));
		mTelTv.setTextSize(Util.getTextSize(mContext, 50));
		mTelTv.setId(ID_TELTV);
		mLayout.addView(mTelTv);

		mTel = new TextView(mContext);
		LayoutParams tlp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		tlp.addRule(RIGHT_OF, ID_TELTV);
		tlp.addRule(ALIGN_BASELINE, ID_TELTV);
		mTel.setLayoutParams(tlp);
		mTel.setText(Html.fromHtml("<u>" + Util.getString(mContext, "pay_service_tel") + "</u>"));
		mTel.setTextColor(Util.getColor(mContext, "a8_login_text"));
		mTel.setTextSize(Util.getTextSize(mContext, 50));
		mTel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Intent dialIntent =  new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Util.getString(mContext, "pay_service_tel")));//直接拨打电话
					mContext.startActivity(dialIntent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		mLayout.addView(mTel);
		
		mQQTv = new TextView(mContext);
		LayoutParams qlp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		qlp.addRule(BELOW,ID_TELTV);
		qlp.addRule(ALIGN_PARENT_LEFT);
		qlp.leftMargin = Util.getInt(mContext, 30);
		qlp.bottomMargin = Util.getInt(mContext, 30);
		mQQTv.setLayoutParams(qlp);
		mQQTv.setText(Util.getString(mContext, "pay_service_qqtv"));
		mQQTv.setTextColor(Util.getColor(mContext, "a8_login_text"));
		mQQTv.setTextSize(Util.getTextSize(mContext, 50));
		mQQTv.setId(ID_QQTV);
		mLayout.addView(mQQTv);
		
		mQQ = new TextView(mContext);
		LayoutParams qtlp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		qtlp.addRule(RIGHT_OF, ID_QQTV);
		qtlp.addRule(ALIGN_BASELINE, ID_QQTV);
		mQQ.setLayoutParams(qtlp);
		mQQ.setText(Html.fromHtml("<u>" + Util.getString(mContext, "pay_service_qq") + "</u>"));
		mQQ.setTextColor(Util.getColor(mContext, "a8_login_text"));
		mQQ.setTextSize(Util.getTextSize(mContext, 50));
		mQQ.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(Util.isQQClientAvailable(mContext)){
					try {
						//直接拉起客服QQ号
//						String url="http://wpa.b.qq.com/cgi/wpa.php?ln=1&key=XzkzODAyODk3MV80MDc0ODRfNDAwMTAxODEwNl8yXw";
						String url="mqqwpa://im/chat?chat_type=wpa&uin=" + Util.getString(mContext, "pay_service_qq");
						mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
						//申请加群
//						joinQQGroup("AbIpIcaPS2f76QFbwQ3Mheh-SGyl3JyU");
					} catch (Exception e) {
						Util.showToast(mContext, "QQ未安装或版本太低，请前往应用市场安装或更新！");
						e.printStackTrace();
					}
				}else{
					Util.showToast(mContext, "未安装QQ，请下载安装！");
					try {
						String url="https://im.qq.com/mobileqq";
						mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		mLayout.addView(mQQ);
	}
	
	/**
	*
	* 发起添加群流程。
	* @param key 由官网生成的key
	* @return 返回true表示呼起手Q成功，返回fals表示呼起失败
	**/
	public boolean joinQQGroup(String key) {
	    Intent intent = new Intent();
	    intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
	    //此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面   
	    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
	    try {
	        mContext.startActivity(intent);
	        return true;
	    } catch (Exception e) {
	        // 未安装手Q或安装的版本不支持
	        return false;
	    }
	}

	public void setGoodsName(String goodsNmae){
		mGoodsNameTV.setText(goodsNmae);
	}

	public void setGoodsPrice(String goodsPrice){
		mGoodsPriceTV.setText(goodsPrice);
	}

	public GridView getmPayGridView(){
		return mPayGridView;
	}

	public void setOnBackClickListener(final onButtonClickListener listener){
		if(null != listener)
			mBackView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onButtonClick(v);
				}
			});
	}

}
