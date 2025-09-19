package com.base.model.entity.BwleErp;

import core.custom_interface.Table;

@Table(name = "employee_sector", isAnnotationField = false)
public class RoleModule {
	private Integer role_id;
	private Integer module_id;
	public Integer getRole_id() {
		return role_id;
	}
	public void setRole_id(Integer role_id) {
		this.role_id = role_id;
	}
	public Integer getModule_id() {
		return module_id;
	}
	public void setModule_id(Integer module_id) {
		this.module_id = module_id;
	}
}
