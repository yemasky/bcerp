package com.base.model.entity.BwleErp.SystemSetting.module;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "module", isAnnotationField = true)
public class Modules extends ModulesBaseAbstract {
	@Column(name = "module_id", update_ignore = true)
	private Integer module_id;
	@Column(name = "module_father_id")
	private Integer module_father_id;
	@Column(name = "submenu_father_id")
	private Integer submenu_father_id;
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
	public Integer getSubmenu_father_id() {
		return submenu_father_id;
	}
	public void setSubmenu_father_id(Integer submenu_father_id) {
		this.submenu_father_id = submenu_father_id;
	}
}
