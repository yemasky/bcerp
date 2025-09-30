package com.base.model.entity.BwleErp.BaseAbstract;

public abstract class CategoryBaseAbstract {
	private String category_type;
	private String category_name;
	private String category_serial;
	private String category_number;
	private Integer category_valid;
	public String getCategory_type() {
		return category_type;
	}
	public void setCategory_type(String category_type) {
		this.category_type = category_type;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public String getCategory_serial() {
		return category_serial;
	}
	public void setCategory_serial(String category_serial) {
		this.category_serial = category_serial;
	}
	public String getCategory_number() {
		return category_number;
	}
	public void setCategory_number(String category_number) {
		this.category_number = category_number;
	}
	public Integer getCategory_valid() {
		return category_valid;
	}
	public void setCategory_valid(Integer category_valid) {
		this.category_valid = category_valid;
	}
}
