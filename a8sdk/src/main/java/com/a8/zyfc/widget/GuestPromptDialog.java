package com.a8.zyfc.widget;

import com.a8.zyfc.util.Util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;
/**
 * 游客提示信息dialog
 * @author Chooper
 *
 */
public class GuestPromptDialog extends Dialog{
	
	public GuestPromptDialog(Context context) {
		super(context);
	}
	public GuestPromptDialog(Context context, int theme) {
		super(context, theme);
	}
	
	public static class Builder {
		
		private Context mContext;
		private String title;
		private String msg;
		private String positiveButtonText;
		private String negativeButtonText;
		private OnClickListener positiveButtonClickListener;
		private OnClickListener negativeButtonClickListener;

		public Builder(Context context) {  
			this.mContext = context;  
		} 

		/** 
		 * Set the Dialog title
		 *  
		 * @param title 
		 * @return 
		 */  
		public Builder setTitle(int title) {  
			this.title = (String) mContext.getText(title);  
			return this;  
		}  
  
		public Builder setTitle(String title) {  
			this.title = title;  
			return this;  
		}
		
		/** 
		 * Set the Dialog message 
		 *  
		 * @param msg 
		 * @return 
		 */  
		public Builder setMsg(int msg) {  
			this.msg = (String) mContext.getText(msg);  
			return this;  
		}  
  
		public Builder setMsg(String msg) {  
			this.msg = msg;  
			return this;  
		}

		/** 
		 * Set the positive button resource and it's listener 
		 *  
		 * @param positiveButtonText 
		 * @return 
		 */  
		public Builder setPositiveButton(int positiveButtonText, OnClickListener listener) {
			this.positiveButtonText = (String) mContext.getText(positiveButtonText);  
			this.positiveButtonClickListener = listener;  
			return this;  
		}

		public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;  
			this.positiveButtonClickListener = listener;  
			return this;  
		}
		
		/** 
		 * Set the negative button resource and it's listener 
		 *  
		 * @param positiveButtonText 
		 * @return 
		 */  
		public Builder setNegativeButton(int positiveButtonText, OnClickListener listener) {
			this.negativeButtonText = (String) mContext.getText(positiveButtonText);  
			this.negativeButtonClickListener = listener;  
			return this;  
		}

		public Builder setNegativeButton(String positiveButtonText, OnClickListener listener) {
			this.negativeButtonText = positiveButtonText;  
			this.negativeButtonClickListener = listener;  
			return this;  
		}
		
		public GuestPromptDialog create(){
			final GuestPromptDialog dialog = new GuestPromptDialog(mContext,Util.getStyleId(mContext, "a8_dialog_login"));
//			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			View layout = inflater.inflate(Util.getLayoutId(mContext, "prompt_layout"), null);
			GuestPromptLayout layout = new GuestPromptLayout(mContext);
			//添加主布局
			dialog.addContentView(layout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			//设置title
			layout.getTitleView().setTitle(title);
			//设置“ok”按钮
			if(positiveButtonText != null){
				layout.getPositiveBtn().setText(positiveButtonText);
				if(positiveButtonClickListener != null){
					layout.getPositiveBtn().setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
						}
					});
				}
			}else{
				layout.getPositiveBtn().setVisibility(View.GONE);
			}
			//设置“cancle”按钮
			if(negativeButtonText != null){
				layout.getNegativeBtn().setText(negativeButtonText);
				if(negativeButtonClickListener != null){
					layout.getNegativeBtn().setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
						}
					});
				}
			}else{
				layout.getNegativeBtn().setVisibility(View.GONE);
			}
			//设置message
			if(msg != null){
				layout.getMsgTv().setText(msg);
			}
			dialog.setContentView(layout);
			return dialog;
		}
		
	}
}
