package com.a8.zyfc.model;

import android.os.Parcel;
import android.os.Parcelable;


public class ShareBean implements Parcelable {
	
	/********** 分享渠道类型 **************/
	public static final int SHARE_TYPE_WX = 1;      //分享给微信好友
	public static final int SHARE_TYPE_WXZONE = 2;  //分享到微信朋友圈
	public static final int SHARE_TYPE_QQ = 3;      //分享给QQ好友
	public static final int SHARE_TYPE_QZONE = 4;   //分享到QQ空间
	
	/********** 分享内容类型 **************/
	public static final int SHARE_CONTENT_TEXT = 1; //分享纯文本
	public static final int SHARE_CONTENT_IMG = 2;  //分享本地图片
	public static final int SHARE_CONTENT_WEB = 3;  //分享网页
	public static final int SHARE_CONTENT_AUDIO = 4;//分享音乐
	public static final int SHARE_CONTENT_VIDEO = 5;//分享音乐

	public int type = -1;
	public int contentType = -1;
	
	public String title = "";
	public String summary = "";
	public String thumbUrl = "";
	public String appName = "";
	
	public String text = "";
	public String imgPath = "";
	public String pageUrl = "";
	public String audioUrl = "";
	public String videoUrl = "";

	

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getContentType() {
		return contentType;
	}

	public void setContentType(int contentType) {
		this.contentType = contentType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getThumbUrl() {
		return thumbUrl;
	}

	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public String getAudioUrl() {
		return audioUrl;
	}

	public void setAudioUrl(String audioUrl) {
		this.audioUrl = audioUrl;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(type);
		dest.writeInt(contentType);
		dest.writeString(title);
		dest.writeString(summary);
		dest.writeString(thumbUrl);
		dest.writeString(appName);
		dest.writeString(text);
		dest.writeString(imgPath);
		dest.writeString(pageUrl);
		dest.writeString(audioUrl);
		dest.writeString(videoUrl);
	}

	public static final Creator<ShareBean> CREATOR = new Creator<ShareBean>() {

		@Override
		public ShareBean createFromParcel(Parcel source) {
			ShareBean share = new ShareBean();
			share.type = source.readInt();
			share.contentType = source.readInt();
			share.title = source.readString();
			share.summary = source.readString();
			share.thumbUrl = source.readString();
			share.appName = source.readString();
			share.text = source.readString();
			share.imgPath = source.readString();
			share.pageUrl = source.readString();
			share.audioUrl = source.readString();
			share.videoUrl = source.readString();
			return share;
		}

		@Override
		public ShareBean[] newArray(int size) {
			return new ShareBean[size];
		}
	};

}
