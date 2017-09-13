package com.a8.zyfc.model;

import android.os.Parcel;
import android.os.Parcelable;


public class UserTO implements Parcelable {

	
	
	private long uid;
	private long lastLoginTime = -1L;
	private String lastLoginTimeStr;
	private String memberNum;
	private boolean isBindMobile = false;
	private long createdDate;
	private boolean isFast = false;
	private long lastTipTime;
	// --------------------------------------接口包含参数--------------------------------------- //
	private String token;
	private String uName;
	private String nickName;
	private String pwd;
	private String mobile;
	private String mail;
	private String question;
	private String answer;
	private String version;
	private String imei;
	private String authCode;
	private int flag;
	private String gameKey;
	private String realName; //用户真实姓名（用于实名认证）
	private String IDCard;
	// ------------------------------------------第三方登录参数-------------------------------- //
	private int thirdType; // 11：QQ登录；  12：微信登录。
	private String openId; //QQ登录获取的openId
	private String thirdToken;//QQ登录获取的accessToken，或微信登录获取的authCode。
	
	
	
	/**
	 * 用户id
	 */
	public long getUid() {
		return uid;
	}
	/**
	 * 用户id
	 */
	public void setUid(long uid) {
		this.uid = uid;
	}
	
	/**
	 * 最后一次登录的时间
	 */
	public long getLastLoginTime() {
		return lastLoginTime;
	}
	/**
	 * 最后一次登录的时间
	 */
	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	
	/**
	 * 最后一次登录的时间（字符串）
	 */
	public String getLastLoginTimeStr() {
		return lastLoginTimeStr;
	}
	/**
	 * 最后一次登录的时间（字符串）
	 */
	public void setLastLoginTimeStr(String lastLoginTimeStr) {
		this.lastLoginTimeStr = lastLoginTimeStr;
	}
	
	/**
	 * 安全措施数目（电话、邮箱、密保）
	 */
	public String getMemberNum() {
		return memberNum;
	}
	/**
	 * 安全措施数目（电话、邮箱、密保）
	 */
	public void setMemberNum(String memberNum) {
		this.memberNum = memberNum;
	}
	
	/**
	 * 是否绑定手机
	 */
	public boolean isBindMobile() {
		return isBindMobile;
	}
	/**
	 * 是否绑定手机
	 */
	public void setBindMobile(boolean isBindMobile) {
		this.isBindMobile = isBindMobile;
	}
	
	/**
	 * 账号创建日期
	 */
	public long getCreatedDate() {
		return createdDate;
	}
	/**
	 * 账号创建日期
	 */
	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}
	
	/**
	 * 是否快速登录
	 */
	public boolean isFast() {
		return isFast;
	}
	/**
	 * 是否快速登录
	 */
	public void setFast(boolean isFast) {
		this.isFast = isFast;
	}
	
	/**
	 * @return 最后提醒时间
	 */
	public long getLastTipTime() {
		return lastTipTime;
	}
	
	/**
	 * 设置最后提醒时间
	 * @param lastTipTime
	 */
	public void setLastTipTime(long lastTipTime) {
		this.lastTipTime = lastTipTime;
	}
	

	
	/**
	 * session
	 */
	public String getToken() {
		return token == null ? "" : token;
	}
	/**
	 * session
	 */
	public void setToken(String token) {
		this.token = token;
	}
	
	/**
	 * 用户名
	 */
	public String getUserName() {
		return uName == null ? "" : uName;
	}
	/**
	 * 用户名
	 */
	public void setUserName(String uName) {
		this.uName = uName;
	}
	
	/**
	 * 用户别名
	 */
	public String getNickName() {
		return nickName == null ? "" : nickName;
	}
	/**
	 * 用户别名
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	/**
	 * 用户密码
	 */
	public String getPassword() {
		return pwd == null ? "" : pwd;
	}
	/**
	 * 用户密码
	 */
	public void setPassword(String password) {
		this.pwd = password;
	}
	
	/**
	 * 手机号码
	 */
	public String getMobile() {
		return mobile == null ? "" : mobile;
	}
	/**
	 * 手机号码
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	/**
	 * 邮箱
	 */
	public String getMail() {
		return mail == null ? "" : mail;
	}
	/**
	 * 邮箱
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	/**
	 * 密保问题
	 */
	public String getQuestion() {
		return question == null ? "" : question;
	}
	/**
	 * 密保问题
	 */
	public void setQuestion(String question) {
		this.question = question;
	}
	
	/**
	 * 问题答案
	 */
	public String getAnswer() {
		return answer == null ? "" : answer;
	}
	/**
	 * 问题答案
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	/**
	 * 版本
	 */
	public String getVersion() {
		return version == null ? "" : version;
	}
	/**
	 * 版本
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	/**
	 * imei
	 * @return
	 */
	public String getImei() {
		return imei == null ? "" : imei;
	}
	/**
	 * imei
	 * @return
	 */
	public void setImei(String imei) {
		this.imei = imei;
	}
	
	/** 验证码 **/
	public String getAuthCode() {
		return authCode == null ? "" : authCode;
	}
	
	/** 验证码 **/
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	
	/**
	 * 数据类型
	 */
	public int getFlag() {
		return flag;
	}
	/**
	 * 数据类型
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getGameKey() {
		return gameKey;
	}

	public void setGameKey(String gameKey) {
		this.gameKey = gameKey;
	}

	public int getThirdType() {
		return thirdType;
	}

	public void setThirdType(int thirdType) {
		this.thirdType = thirdType;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getThirdToken() {
		return thirdToken;
	}

	public void setThirdToken(String thirdToken) {
		this.thirdToken = thirdToken;
	}

	public String getIDCard() {
		return IDCard;
	}
	public void setIDCard(String iDCard) {
		this.IDCard = iDCard;
	}
	
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}


	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(uid);
		dest.writeLong(lastLoginTime);
		dest.writeString(lastLoginTimeStr);
		dest.writeString(memberNum);
		dest.writeByte((byte) (isBindMobile?1:0));
		dest.writeLong(createdDate);
		dest.writeByte((byte) (isFast?1:0));
		dest.writeString(token);
		dest.writeString(uName);
		dest.writeString(nickName);
		dest.writeString(pwd);
		dest.writeString(mobile);
		dest.writeString(mail);
		dest.writeString(question);
		dest.writeString(answer);
		dest.writeString(version);
		dest.writeString(imei);
		dest.writeString(authCode);
		dest.writeString(gameKey);
		dest.writeInt(thirdType);
		dest.writeString(openId);
		dest.writeString(thirdToken);
		dest.writeInt(flag);
		dest.writeString(IDCard);
		dest.writeString(realName);
	}

	public static final Creator<UserTO> CREATOR = new Creator<UserTO>() {

		@Override
		public UserTO createFromParcel(Parcel source) {
			UserTO user = new UserTO();
			user.uid = source.readLong();
			user.lastLoginTime = source.readLong();
			user.lastLoginTimeStr = source.readString();
			user.memberNum = source.readString();
			user.isBindMobile = source.readByte() == 1;
			user.createdDate = source.readLong();
			user.isFast = source.readByte() == 1;
			user.token = source.readString();
			user.uName = source.readString();
			user.nickName = source.readString();
			user.pwd = source.readString();
			user.mobile = source.readString();
			user.mail = source.readString();
			user.question = source.readString();
			user.answer = source.readString();
			user.version = source.readString();
			user.imei = source.readString();
			user.authCode = source.readString();
			user.gameKey = source.readString();
			user.thirdType = source.readInt();
			user.openId = source.readString();
			user.thirdToken = source.readString();
			user.flag = source.readInt();
			user.IDCard = source.readString();
			user.realName = source.readString();
			return user;
		}

		@Override
		public UserTO[] newArray(int size) {
			return new UserTO[size];
		}
	};

}
