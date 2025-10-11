package com.base.model.entity.BwleErp.SystemSetting;

import core.custom_interface.Table;

@Table(name = "category_commodity_attribute", isAnnotationField = false)//未使用 FieldAnnotation 
public class CategoryCommodityAttribute {
	private Integer attribute_id;
	private Integer commodity_id;
	private String attribute_type;
	private Integer attribute_check;
	private String attribute_key;
	private String attribute_enkey;
	private String attribute_val;
	private String attribute_enval;
	private String attribute_mark;
	public Integer getAttribute_id() {
		return attribute_id;
	}
	public void setAttribute_id(Integer attribute_id) {
		this.attribute_id = attribute_id;
	}
	public Integer getCommodity_id() {
		return commodity_id;
	}
	public void setCommodity_id(Integer commodity_id) {
		this.commodity_id = commodity_id;
	}
	public String getAttribute_type() {
		return attribute_type;
	}
	public void setAttribute_type(String attribute_type) {
		this.attribute_type = attribute_type;
	}
	public Integer getAttribute_check() {
		return attribute_check;
	}
	public void setAttribute_check(Integer attribute_check) {
		this.attribute_check = attribute_check;
	}
	public String getAttribute_key() {
		return attribute_key;
	}
	public void setAttribute_key(String attribute_key) {
		this.attribute_key = attribute_key;
	}
	public String getAttribute_enkey() {
		return attribute_enkey;
	}
	public void setAttribute_enkey(String attribute_enkey) {
		this.attribute_enkey = attribute_enkey;
	}
	public String getAttribute_val() {
		return attribute_val;
	}
	public void setAttribute_val(String attribute_val) {
		this.attribute_val = attribute_val;
	}
	public String getAttribute_enval() {
		return attribute_enval;
	}
	public void setAttribute_enval(String attribute_enval) {
		this.attribute_enval = attribute_enval;
	}
	public String getAttribute_mark() {
		return attribute_mark;
	}
	public void setAttribute_mark(String attribute_mark) {
		this.attribute_mark = attribute_mark;
	}
}
