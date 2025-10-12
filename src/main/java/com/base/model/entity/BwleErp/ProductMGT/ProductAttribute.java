package com.base.model.entity.BwleErp.ProductMGT;

import core.custom_interface.Table;

@Table(name = "product_attribute", isAnnotationField = true)
public class ProductAttribute {
	private Integer product_id;
	private Integer attribute_id;
	private String attribute_key;
	private String attribute_val;
	private String attribute_enval;
	private String attribute_mark;
	private String attribute_type;
	public Integer getProduct_id() {
		return product_id;
	}
	public void setProduct_id(Integer product_id) {
		this.product_id = product_id;
	}
	public Integer getAttribute_id() {
		return attribute_id;
	}
	public void setAttribute_id(Integer attribute_id) {
		this.attribute_id = attribute_id;
	}
	public String getAttribute_key() {
		return attribute_key;
	}
	public void setAttribute_key(String attribute_key) {
		this.attribute_key = attribute_key;
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
	public String getAttribute_type() {
		return attribute_type;
	}
	public void setAttribute_type(String attribute_type) {
		this.attribute_type = attribute_type;
	}
}
