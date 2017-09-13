package com.a8.zyfc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.AbsListView.LayoutParams;

import java.util.List;

import com.a8.zyfc.util.Util;

public class ChooseGrideAdapter extends BaseAdapter{
	private Context mContext;
	private List<Integer> mIconIDs;
	private int selectIndex = -1;

	public ChooseGrideAdapter(Context context, List<Integer> ids){
		this.mContext = context;
		this.mIconIDs = ids;
	}
	@Override
	public int getCount() {
		return mIconIDs.size();
	}
	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if(convertView==null){
			convertView = new ImageView(mContext);
		}
        ImageView img = (ImageView)convertView;
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		img.setLayoutParams(lp);
		img.setAdjustViewBounds(true);
		img.setScaleType(ImageView.ScaleType.CENTER_CROP);
		img.setBackgroundResource(mIconIDs.get(position));
		if(mIconIDs.get(position) == Util.getDrawableId(mContext, "a8_login_phone_icon")){
			img.setTag(10);
		}else if(mIconIDs.get(position) == Util.getDrawableId(mContext, "a8_login_qq_icon")){
			img.setTag(11);
		}else if(mIconIDs.get(position) == Util.getDrawableId(mContext, "a8_login_wx_icon")){
			img.setTag(12);
		}else if(mIconIDs.get(position) == Util.getDrawableId(mContext, "a8_login_guest_icon")){
			img.setTag(13);
		}else{
			img.setTag(0);
		}
		if(position == selectIndex){
			img.setSelected(true);
		}else{
			img.setSelected(false);
		}
		return img;
	}

	public void setSelectIndex(int i){
		selectIndex = i;
	}
}
