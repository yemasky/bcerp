package com.base.model.entity.BwleErp.Marketing;

import core.custom_interface.Table;

@Table(name = "fumamx_api", isAnnotationField = true)
public class FumamxApi {
	private Long key_id;
	private String val;
	private Integer fumamx_type;
	public Long getKey_id() {
		return key_id;
	}
	public void setKey_id(Long key_id) {
		this.key_id = key_id;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public Integer getFumamx_type() {
		return fumamx_type;
	}
	public void setFumamx_type(Integer fumamx_type) {
		this.fumamx_type = fumamx_type;
	}
	
}
