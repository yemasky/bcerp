package com.base.model.vo;

import java.util.List;

public class MsgSecCheck {
	private Integer errcode;
	private String errmsg;
	private List<WxMsgDetail> detail;
	private String trace_id;
	private Object result;
	public Integer getErrcode() {
		return errcode;
	}
	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public List<WxMsgDetail> getDetail() {
		return detail;
	}
	public void setDetail(List<WxMsgDetail> detail) {
		this.detail = detail;
	}
	public String getTrace_id() {
		return trace_id;
	}
	public void setTrace_id(String trace_id) {
		this.trace_id = trace_id;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
}