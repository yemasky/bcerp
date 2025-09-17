package com.base.type;

import java.sql.Timestamp;
import java.util.HashMap;

public class Success {
	private boolean success = true;
	private String code = "100001";
	private String message = "请求数据成功";
	private HashMap<String, Object> item = new HashMap<>();
	private Timestamp date = null;
	
	public Success() {
		long thisDatetime = System.currentTimeMillis();
		this.date = new Timestamp(thisDatetime);
	}
	
	public Success(ErrorCode error) {
		this.code = error.getError_code();
		this.message = error.getDescribe();
		long thisDatetime = System.currentTimeMillis();
		this.date = new Timestamp(thisDatetime);
	}
	
	public void setSuccessCode(ErrorCode error) {
		this.code = error.getError_code();
		this.message = error.getDescribe();
		long thisDatetime = System.currentTimeMillis();
		this.date = new Timestamp(thisDatetime);
		this.success = true;
	}
	public void setErrorCode(ErrorCode error) {
		this.code = error.getError_code();
		this.message = error.getDescribe();
		long thisDatetime = System.currentTimeMillis();
		this.date = new Timestamp(thisDatetime);
		this.success = false;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getItem() {
		return item;
	}

	public Object getItem(String key) {
		if(this.item.containsKey(key)) return this.item.get(key);
		return null;
	}
	
	public void setItem(String key, Object item) {
		this.item.put(key, item);
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}
	
}
