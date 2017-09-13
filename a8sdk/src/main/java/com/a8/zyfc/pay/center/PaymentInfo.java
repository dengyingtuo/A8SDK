package com.a8.zyfc.pay.center;

import android.os.Parcel;
import android.os.Parcelable;

public class PaymentInfo implements Parcelable {
	public String sdkOrderId;
	public String price;
	public String goodsId;
	public String goodsName;
	public String uid;
	public String uname;
	public String roleId;
	public String roleName;
	public String cpOrderId;
	public String cpParams;

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(sdkOrderId);
		dest.writeString(price);
		dest.writeString(goodsId);
		dest.writeString(goodsName);
		dest.writeString(uid);
		dest.writeString(uname);
		dest.writeString(roleId);
		dest.writeString(roleName);
		dest.writeString(cpOrderId);
		dest.writeString(cpParams);

		// TODO Auto-generated method stub

	}

	public static final Creator<PaymentInfo> CREATOR = new Creator<PaymentInfo>() {
		@Override
		public PaymentInfo createFromParcel(Parcel in) {
			return new PaymentInfo(in);
		}

		@Override
		public PaymentInfo[] newArray(int size) {
			return new PaymentInfo[size];
		}
	};

	public PaymentInfo(Parcel in) {
		sdkOrderId = in.readString();
		price = in.readString();
		goodsId = in.readString();
		goodsName = in.readString();
		uid = in.readString();
		uname = in.readString();
		roleId = in.readString();
		roleName = in.readString();
		cpOrderId = in.readString();
		cpParams = in.readString();
	}

	public PaymentInfo() {
	}
}
