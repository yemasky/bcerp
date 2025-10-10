package com.base.model.vo.BwleErp.Marketing;

import java.io.Serializable;

public class TokenData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3320813896120573387L;
	private String companyId;
	private String appId;
	private String accessToken;
	private String ownerId;
	private String expireTime;
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}
}
