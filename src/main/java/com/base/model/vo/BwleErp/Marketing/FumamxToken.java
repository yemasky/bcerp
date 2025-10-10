package com.base.model.vo.BwleErp.Marketing;

import java.io.Serializable;

public class FumamxToken implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private TokenData data;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public TokenData getData() {
		return data;
	}
	public void setData(TokenData data) {
		this.data = data;
	}
}
