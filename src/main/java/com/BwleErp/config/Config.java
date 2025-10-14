package com.BwleErp.config;

import core.util.ReadProperties;

public class Config {
	public static String erpDSN = "erpDSN";
	public static String imagesDomain = "";
	public static String defaultHouseImages = ReadProperties.instance().read("defaultHouseImages");
	public static String uploadPath = ReadProperties.instance().read("xiaoquUploadPath");
	public static String webUrl = ReadProperties.instance().read("webUrl");
	public static String resourceUrl = ReadProperties.instance().read("resourceUrl");
	public static String uploadFileUrl = ReadProperties.instance().read("uploadFileUrl");
	public static String uploadImagesUrl = ReadProperties.instance().read("xiaoquUploadImagesUrl");
	public static String uniqueKey = "c111ba587854480f2a4de8d44204505f";
	public static String uniqueMd5Key = "1236789acds";
	public static String cache24hour = "cache24hour";
	public static String cache30min = "cache30min";
	public static String cache120min = "cache120min";
	public static String cacheforever = "cacheforever";
	public static String APPID = "wxeba3463faae34128";
	public static String SECRET = "50d6f56c26c20c8621a2b437a6e3c626";
	public static String WxPushToken = "ct1aPQLaxEJQxzVuFkoBSVAU7s41A0l";
	public static String WxLiuYanTemplateId = "h6G-R7pLHTuYTQtQpyWVZJ3OEmrCQKKVGwjVEAyjLxk";
	public static String firefoxbin = ReadProperties.instance().read("firefoxbin");
	public static String geckoDriver = ReadProperties.instance().read("geckoDriver");
	public static String chromebin = ReadProperties.instance().read("chromebin");
	public static String chromedriver = ReadProperties.instance().read("chromedriver");
	public static String CAPath =  ReadProperties.instance().read("CAPath");
	
}
