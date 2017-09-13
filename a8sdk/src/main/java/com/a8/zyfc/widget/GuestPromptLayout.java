package com.a8.zyfc.widget;

import android.content.Context;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a8.zyfc.util.Util;

/**
 * Created by Chooper on 2017/4/24.
 */

public class GuestPromptLayout extends RelativeLayout {
    public final static int ID_TITLE = 1000;
    public final static int ID_MSG = 1001;
    private Context mContext;
    private RelativeLayout mLayout;
    private LoginTitle mLoginTitle;
    private TextView msgTv;
    private Button negativeBtn;
    private Button positiveBtn;

    public GuestPromptLayout(Context context) {
        super(context);
        mContext = context;
        initLayout();
        initTitle();
        initMsgTv();
        initButtons();
    }

    private void initLayout() {
        mLayout = new RelativeLayout(mContext);
        mLayout.setLayoutParams(new LayoutParams(Util.getInt(mContext, 960), LayoutParams.WRAP_CONTENT));
        mLayout.setBackgroundResource(Util.getDrawableId(mContext, "a8_login_dialog_bg"));
        addView(mLayout);
    }

    private void initTitle() {
        mLoginTitle = new LoginTitle(mContext);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(CENTER_HORIZONTAL);
        mLoginTitle.setLayoutParams(lp);
        mLoginTitle.setId(ID_TITLE);
        mLayout.addView(mLoginTitle);
    }

    private  void initMsgTv(){
        msgTv = new TextView(mContext);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(BELOW, ID_TITLE);
        lp.leftMargin = Util.getInt(mContext, 50);
        lp.rightMargin = Util.getInt(mContext, 50);
        lp.bottomMargin = Util.getInt(mContext, 40);
        msgTv.setLayoutParams(lp);
        msgTv.setTextColor(Util.getColor(mContext, "a8_red_text_color"));
        msgTv.setTextSize(Util.getInt(mContext, 20));
        msgTv.setId(ID_MSG);
        mLayout.addView(msgTv);
    }

    private void initButtons(){
        negativeBtn = new Button(mContext);
        LayoutParams nlp = new LayoutParams(Util.getInt(mContext, 300), Util.getInt(mContext, 100));
        nlp.addRule(BELOW, ID_MSG);
        nlp.addRule(ALIGN_PARENT_LEFT);
        nlp.leftMargin = Util.getInt(mContext, 100);
        nlp.bottomMargin = Util.getInt(mContext, 40);
        negativeBtn.setLayoutParams(nlp);
        negativeBtn.setBackgroundResource(Util.getDrawableId(mContext, "a8_negative_btn_bg_selector"));
        negativeBtn.setTextColor(Util.getColor(mContext, "a8_negative_text_color"));
        negativeBtn.setTextSize(Util.getInt(mContext, 16));
        mLayout.addView(negativeBtn);

        positiveBtn = new Button(mContext);
        LayoutParams plp = new LayoutParams(Util.getInt(mContext, 300), Util.getInt(mContext, 100));
        plp.addRule(BELOW, ID_MSG);
        plp.addRule(ALIGN_PARENT_RIGHT);
        plp.rightMargin = Util.getInt(mContext, 100);
        plp.bottomMargin = Util.getInt(mContext, 40);
        positiveBtn.setLayoutParams(plp);
        positiveBtn.setBackgroundResource(Util.getDrawableId(mContext, "a8_login_btn_bg_selector"));
        positiveBtn.setTextColor(Util.getColor(mContext, "a8_red_text_color"));
        positiveBtn.setTextSize(Util.getInt(mContext, 16));
        mLayout.addView(positiveBtn);
    }


    public LoginTitle getTitleView(){
        return mLoginTitle;
    }

    public TextView getMsgTv(){
        return msgTv;
    }

    public Button getNegativeBtn(){
        return negativeBtn;
    }

    public Button getPositiveBtn(){
        return positiveBtn;
    }
}
