package com.base.model.entity.BwleErp.SystemSetting;

import core.custom_interface.Table;

@Table(name = "comm_dict_module", isAnnotationField = false)
public class CommDictModule {
	private Integer module_id;
	private Integer module_father_id;
	private String dict_field;
	private String dict_field_name;

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

	public String getDict_field() {
		return dict_field;
	}

	public void setDict_field(String dict_field) {
		this.dict_field = dict_field;
	}

	public String getDict_field_name() {
		return dict_field_name;
	}

	public void setDict_field_name(String dict_field_name) {
		this.dict_field_name = dict_field_name;
	}
	
}
