package com.a8.zyfc.adapter;

import java.util.List;

import com.a8.zyfc.model.UserTO;
import com.a8.zyfc.util.Util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AccountAdapter extends BaseAdapter {
	private List<UserTO> mList;
	private Context mContext;
	
	public AccountAdapter(Context context, List<UserTO> list) {
		mContext = context;
		mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = new TextView(mContext);
		}
		TextView tv = (TextView) convertView;
		tv.setText(mList.get(position).getUserName());
		tv.setTextColor(Util.getColor(mContext, "a8_login_account_select_text"));
		tv.setTextSize(Util.getTextSize(mContext, 38));
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, Util.getInt(mContext, 86));
		tv.setLayoutParams(lp);
		tv.setPadding(Util.getInt(mContext, 22), Util.getInt(mContext, 20), 0, Util.getInt(mContext, 20));
		return tv;
	}

}
