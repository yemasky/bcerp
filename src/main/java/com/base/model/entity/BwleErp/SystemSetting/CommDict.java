package com.base.model.entity.BwleErp.SystemSetting;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "comm_dict", isAnnotationField = true)
public class CommDict {
	@Column(name = "dict_id", primary_key = true, auto_increment = true)
	private Integer dict_id;
	private Integer dict_father_id;
	private String dict_val;
	private String dict_enval;
	private String dict_field;
	private String dict_linkage;
	private String dict_node;
	private Integer module_id;
	private Integer module_father_id;
	private Integer dict_valid;
	public Integer getDict_id() {
		return dict_id;
	}
	public void setDict_id(Integer dict_id) {
		this.dict_id = dict_id;
	}
	public Integer getDict_father_id() {
		return dict_father_id;
	}
	public void setDict_father_id(Integer dict_father_id) {
		this.dict_father_id = dict_father_id;
	}
	public String getDict_val() {
		return dict_val;
	}
	public void setDict_val(String dict_val) {
		this.dict_val = dict_val;
	}
	public String getDict_enval() {
		return dict_enval;
	}
	public void setDict_enval(String dict_enval) {
		this.dict_enval = dict_enval;
	}
	public String getDict_field() {
		return dict_field;
	}
	public void setDict_field(String dict_field) {
		this.dict_field = dict_field;
	}
	public String getDict_linkage() {
		return dict_linkage;
	}
	public void setDict_linkage(String dict_linkage) {
		this.dict_linkage = dict_linkage;
	}
	public String getDict_node() {
		return dict_node;
	}
	public void setDict_node(String dict_node) {
		this.dict_node = dict_node;
	}
	public Integer getModule_id() {
		return module_id;
	}
	public void setModule_id(Integer module_id) {
		this.module_id = module_id;
	}
	public Integer getModule_father_id() {
		return module_father_id;
	}
	public void setModule_father_id(Integer module_father_id) {
		this.module_father_id = module_father_id;
	}
	public Integer getDict_valid() {
		return dict_valid;
	}
	public void setDict_valid(Integer dict_valid) {
		this.dict_valid = dict_valid;
	}
	
}
