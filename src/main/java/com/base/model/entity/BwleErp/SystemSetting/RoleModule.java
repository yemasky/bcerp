package com.base.model.entity.BwleErp.SystemSetting;

import core.custom_interface.Table;

@Table(name = "role_module", isAnnotationField = false)
public class RoleModule {
	private Integer role_id;
	private Integer module_id;
	private Integer access ;
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
	public Integer getAccess() {
		return access;
	}
	public void setAccess(Integer access) {
		this.access = access;
	}
}
