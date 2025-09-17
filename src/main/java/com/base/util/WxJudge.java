package com.base.util;

import org.json.JSONObject;

import com.BwleErp.config.Config;
import com.base.model.vo.AccessToken;
import com.base.model.vo.MsgSecCheck;
import com.google.gson.Gson;

import core.util.HttpRequest;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class WxJudge {
	private static WxJudge judge = null;
	private Cache cache;
	public static WxJudge instance() {
        if (judge == null) judge = new WxJudge();
        return judge;
    }
	//https://api.weixin.qq.com/wxa/msg_sec_check?access_token=ACCESS_TOKEN
	//scene	number	是	场景枚举值（1 资料；2 评论；3 论坛；4 社交日志）
	//"errcode": 0, "errmsg": "ok",
	public MsgSecCheck msgSecCheck(String access_token, String content, String openid, String scene, String title, String nickname) {
		String url = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token="+access_token;
		//
		title = title == null ? "" : title;
		nickname = nickname == null ? "" : nickname;
		//String param = "version=2&content="+content+"&scene="+scene+"&openid="+openid+"&title="+title+"&nickname="+nickname+"&signature=";
		String param = "{\"version\":2,\"content\":\""+content+"\",\"scene\":"+scene+",\"openid\":\""+openid+"\",\"title\":\""+title+"\",\"nickname\":\""+nickname+"\",\"signature\":\"\"}";
		String result = HttpRequest.instance().post(url, param);
		System.out.println(result);
		MsgSecCheck msgSecCheck = new Gson().fromJson(result, MsgSecCheck.class);
		return msgSecCheck;
	}
	
	public AccessToken getAccessToken(String APPID, String SECRET) {
		CacheManager cacheManager = CacheManager.create();// .getCacheManager(Config.cache24hour);//
		this.cache = cacheManager.getCache(Config.cache120min);
		if (this.cache != null) {
			Element getGreeting = this.cache.get(APPID);
			if (getGreeting != null) {
				Object token = getGreeting.getObjectValue();
				AccessToken accessToken = new Gson().fromJson(token.toString(), AccessToken.class);
				return accessToken;
			}
		}
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+APPID+"&secret="+SECRET;
		String result = HttpRequest.instance().get(url, null);
		AccessToken accessToken = new Gson().fromJson(result, AccessToken.class);
		if(accessToken.getAccess_token()!=null) {
			Element putGreeting = new Element(APPID, result);
			this.cache.put(putGreeting);
		}
		return accessToken;
	}
	
	//
	public String sendSubscribeMessage(String access_token, String openid, String templateid, String page, JSONObject data) {
		String api_url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + access_token;
		JSONObject paramData = new JSONObject();
		paramData.put("access_token", access_token);
		paramData.put("template_id", templateid);
		paramData.put("page", page);//点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转
		paramData.put("touser", openid);//接收者（用户）的 openid
		paramData.put("data", data);//模板内容，格式形如 { "key1": { "value": any }, "key2": { "value": any } }的object
		paramData.put("miniprogram_state", "formal");//跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
		paramData.put("lang", "zh_CN");//进入小程序查看”的语言类型，支持zh_CN(简体中文)、en_US(英文)、zh_HK(繁体中文)、zh_TW(繁体中文)，默认为zh_CN
		return HttpRequest.instance().post(api_url, paramData.toString());
	}
}
