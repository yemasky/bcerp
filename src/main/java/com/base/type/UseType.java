package com.base.type;

public class UseType {// HOUSE_ID/PERSONAL_ID/NORMAL/
	public static final String normal = "normal";
	public static final String AuthID = "AuthID";//身份认证
	public static final String Avatar = "Avatar";//用户头像
	public static final String Times = "Times";//时光
	public static final String Concern = "Concern";//关注
	public static final String Favor = "Favor";//收藏
	public static final String House = "House";//房产
	public static final String AuthHouse = "AuthHouse";//房产
	public static final String News = "News";//新鲜事
	public static final String Riji = "Riji";//新鲜事
	public static String getFileUseType(String type) {
		switch (type) {
		case "AuthID":
			return AuthID;
		case "Avatar":
			return Avatar;
		case "Times":
			return Times;
		case "Concern":
			return Concern;
		case "Favor":
			return Favor;
		case "House":
			return House;	
		case "AuthHouse":
			return AuthHouse;	
		case "News":
			return News;
		case "Riji":
			return Riji;
		default:
			return normal;
		}
	}
}