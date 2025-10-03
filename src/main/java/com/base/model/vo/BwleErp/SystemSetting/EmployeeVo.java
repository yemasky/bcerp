package com.base.model.vo.BwleErp.SystemSetting;

import com.base.model.entity.BwleErp.SystemSetting.employee.EmployeeBaseAbstract;

import core.custom_interface.Table;
@Table(name = "employee", isAnnotationField = true)
public class EmployeeVo extends EmployeeBaseAbstract{
	private String e_id;

	public String getE_id() {
		return e_id;
	}

	public void setE_id(String e_id) {
		this.e_id = e_id;
	}
	
}
