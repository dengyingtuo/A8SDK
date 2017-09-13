package com.a8.zyfc.pay.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.a8.zyfc.activity.BaseActivity;
import com.a8.zyfc.adapter.ChooseGrideAdapter;
import com.a8.zyfc.PayCallback;
import com.a8.zyfc.pay.center.PaymentInfo;
import com.a8.zyfc.pay.service.AppBillProxy;
import com.a8.zyfc.pay.service.AppBillService;
import com.a8.zyfc.pay.third.order.Order;
import com.a8.zyfc.pay.third.wxpay.WXPayCallBack;
import com.a8.zyfc.pay.third.wxpay.WXPayInfoThread;
import com.a8.zyfc.pay.third.wxpay.WXPayResultThread;
import com.a8.zyfc.util.Util;
import com.a8.zyfc.widget.PayCenterLayout;
import com.a8.zyfc.widget.onButtonClickListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *  充值界面
 *
 * Created by Chooper on 2017/4/14.
 */

public class AppPayMainActivity extends BaseActivity {

    private static final String TAG = "a8_payAct";

    private Activity mActivity;
    private PayCallback payReslutCallback;
    public static PayCallback payCallback;
    private PaymentInfo payInfo;
    private String payItem;
    private List<Integer> payItemList;
    private List<Integer> menuList;
    private PayCenterLayout mPayCenterLayout;
    private GridView mPayGridView;
    private ChooseGrideAdapter mAdapter;
    private WXPayBroadcastReceiver receiver;

    private int payType;
    private boolean payResult = false;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        receiver = new WXPayBroadcastReceiver();
        registerReceiver(receiver, new IntentFilter("com.a8.zyfc.sdk.wxpay"));

        initPayItem();
        initView();
        setContentView(mPayCenterLayout);
    }

    private void initPayItem() {
        payInfo = getIntent().getParcelableExtra("payInfo");
        payItem = getIntent().getStringExtra("payItem");
        // 支付结果回调
        payReslutCallback = new PayCallback() {

            @Override
            public void onFinished(int code, String content) {
                switch (code) {
                    case PayCallback.PAY_SUCC:
                        // 支付成功
                        payResult = true;
                        savePayInfo();
                        payResultReturnApp(true);
                        break;
                    case PayCallback.PAY_UNSUCC:
                    case PayCallback.PAY_NORETURN:
                        AppBillProxy.makeToast(content, mActivity);
//                        payEndAndReturnApp();
                        break;
                }
            }
        };
        // 初始化支付菜单
        try {
            Log.i(TAG, "payItem:" + payItem);
            payItemList = new ArrayList<>();
            menuList = new ArrayList<>();
            JSONArray list = new JSONArray(payItem);
            for (int i = 0; i < list.length(); i++) {
                if(Integer.parseInt(list.getJSONObject(i).getString("menuId")) == 10){
                    payItemList.add(Util.getDrawableId(this, "a8_wxpay_icon"));
                    menuList.add(10);
                }

                if(Integer.parseInt(list.getJSONObject(i).getString("menuId")) == 1) {
                    payItemList.add(Util.getDrawableId(this, "a8_alipay_icon"));
                    menuList.add(1);
                }
                if(Integer.parseInt(list.getJSONObject(i).getString("menuId")) == 2) {
                    payItemList.add(Util.getDrawableId(this, "a8_unionpay_icon"));
                    menuList.add(2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initView(){
        mPayCenterLayout = new PayCenterLayout(this);
        mPayCenterLayout.setGoodsName(payInfo.goodsName);
        mPayCenterLayout.setGoodsPrice("￥" + Float.valueOf(payInfo.price)/100);
        mPayCenterLayout.setOnBackClickListener(new onButtonClickListener() {
            @Override
            public void onButtonClick(View v) {
            	payCallback.onFinished(-2, "");
            	finish();
            }
        });

        mPayGridView = mPayCenterLayout.getmPayGridView();
        mPayGridView.setNumColumns(payItemList.size());
        mAdapter = new ChooseGrideAdapter(this, payItemList);
        mPayGridView.setAdapter(mAdapter);
        mPayGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(isFastClick()){
                    return ;
                }
                payType = menuList.get(position);
                savePayInfo();
                mAdapter.setSelectIndex(position);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 防止控件被重复点击
     */
    private static long lastClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 2000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    private void savePayInfo(){
        new savePayInfoTask().start();
    }

    private class savePayInfoTask extends Thread {
        UIHandler uh = new UIHandler();

        @Override
        public void run() {
            Looper.prepare();
            super.run();

            String result = new AppBillService(mActivity).savePayInfo(payInfo.sdkOrderId, payInfo.price,
                    Integer.toString(payType), payInfo.goodsId, payInfo.goodsName, payInfo.uid, payInfo.uname,
                    payInfo.roleId, payInfo.roleName, payInfo.cpOrderId, payInfo.cpParams);

            Message msg = new Message();
            msg.what = 0;
            msg.obj = result;
            uh.sendMessage(msg);
            Looper.loop();
        }

        @SuppressLint("HandlerLeak")
        class UIHandler extends Handler {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    if (null == msg.obj || "".equals((String) msg.obj)) {
                        Log.i(TAG, "savePayInfo:result="+msg.obj);
                        AppBillProxy.makeToast("网络连接失败，请检查网络并重试！", mActivity);
//                        payEndAndReturnApp();
                    } else {
                        //判断当前支付状态（支付前上报成功后拉起支付界面，支付成功后上报直接往下执行）
                        if(!payResult){
                            startPay();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
        }
    }

    private void startPay(){
        switch (payType){
            case 1:
                Order.orderAlipay(mActivity, payInfo.goodsName, payInfo.uid, Float.toString(Float.parseFloat(payInfo.price)/100),
                        payInfo.sdkOrderId, payReslutCallback);
                break;
            case 2:
                Order.orderupomp(mActivity, payInfo.sdkOrderId, payInfo.price, payInfo.goodsName, payReslutCallback);
                break;
            case 10:
                startWXPay(mActivity, "and", payInfo.uid, Float.toString(Float.parseFloat(payInfo.price)/100), payInfo.sdkOrderId);
                break;
        }
    }

    private void startWXPay(Activity act, String subject, String body, String price, String orderNumber){
        Log.i("a8", "startWXPay");
        ProgressDialog mPd = new ProgressDialog(mActivity);
        mPd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mPd.setMessage("请求支付中...");
        mPd.setCanceledOnTouchOutside(false);
        mPd.setCancelable(true);
        mPd.show();
        new WXPayInfoThread(mPd, mActivity, subject, body, price, orderNumber, new WXPayCallBack() {

            @Override
            public void onResult(String result) {
                if(!("").equals(result) && !("-1").equals(result)){
                    try {
                        Intent data = new Intent(AppPayMainActivity.this, Class.forName(getApplicationContext().getPackageName()+".wxapi.WXPayEntryActivity"));
                        data.putExtra("wxpayInfo", result);
                        startActivity(data);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }else{
                    AppBillProxy.makeToast("请求失败，请重试！", mActivity);
                }
            }

            @Override
            public void onCancel() {

            }
        }).start();
    }

    /**
     * 支付回调方法
     * @param payResult
     */
    private void  payResultReturnApp(boolean payResult){
        try {
            int code = 1;
            String desc = "";
            String content = "";
            if (payResult) {
                code = 0;
                desc = "支付成功";
            } else {
                code = 1;
                desc = "支付已取消";
            }
            JSONObject json = new JSONObject();
            json.put("tradeNo", payInfo.sdkOrderId); // sdk生成订单号
            json.put("amount", Integer.parseInt(payInfo.price)); // 计费金额
            json.put("desc", desc); // 描述信息

            content = json.toString();

            payCallback.onFinished(code, content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finish();
        }

    }

    /*************************************************
     * 处理微信支付结果
     ************************************************/
    public class WXPayBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int errCode = intent.getIntExtra("errCode", -10);
            Log.i("a8", "a8payAct:errCode="+errCode);
            switch (errCode) {
                case 0:
                    ProgressDialog mPd = new ProgressDialog(mActivity);
                    mPd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mPd.setMessage("查询支付结果...");
                    mPd.setCanceledOnTouchOutside(false);
                    mPd.setCancelable(true);
                    mPd.show();
                    new WXPayResultThread(mPd, mActivity, payInfo.sdkOrderId, new WXPayCallBack() {

                        @Override
                        public void onResult(String result) {
                            Log.i("a8", "mAct:wxPayResult="+result);
                            if("1".equals(result)){
                                payReslutCallback.onFinished(0, "微信支付成功");
                            }else if("-1".equals(result)){
                                payReslutCallback.onFinished(1, "订单号为空");
                            }else{
                                payReslutCallback.onFinished(1, "微信支付失败");
                            }
                        }

                        @Override
                        public void onCancel() {
                            payReslutCallback.onFinished(1, "微信支付失败");

                        }
                    }).start();
                    break;
                case -1:
                    payReslutCallback.onFinished(1, "微信支付失败");
                    break;
                case -2:
                default:
                    payReslutCallback.onFinished(1, "微信支付已取消");
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            /*************************************************
             * 处理银联手机支付控件返回的支付结果
             ************************************************/
            case 10: {
                if (data == null) {
                    break;
                }
			/*
			 * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
			 */
                String str = data.getExtras().getString("pay_result");
                if (str.equalsIgnoreCase("success")) {
                    payReslutCallback.onFinished(0, "银联支付成功");
                } else if (str.equalsIgnoreCase("fail")) {
                    payReslutCallback.onFinished(1, "银联支付失败");
                } else if (str.equalsIgnoreCase("cancel")) {
                    payReslutCallback.onFinished(1, "银联支付已取消");
                }
            }
            break;
            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
        }
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
