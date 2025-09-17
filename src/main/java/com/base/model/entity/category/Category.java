package com.base.model.entity.category;

import java.io.Serializable;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "category", isAnnotationField = true)
public class Category implements Serializable {
	@Column(name="serialVersionUID", ignore = true)
	private static final long serialVersionUID = 1077099710025337308L;
	@Column(name="category_id", primary_key = true, auto_increment = true)
	private Integer category_id;
	private Integer category_father_id;
	private String category_name;
	private String category_type;
	private Boolean category_valid;
	public Integer getCategory_id() {
		return category_id;
	}
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	public Integer getCategory_father_id() {
		return category_father_id;
	}
	public void setCategory_father_id(int category_father_id) {
		this.category_father_id = category_father_id;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public String getCategory_type() {
		return category_type;
	}
	public void setCategory_type(String category_type) {
		this.category_type = category_type;
	}
	public boolean getCategory_valid() {
		return category_valid;
	}
	public void setCategory_valid(boolean category_valid) {
		this.category_valid = category_valid;
	}
	
}
