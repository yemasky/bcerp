package com.base.model.vo.BwleErp.SystemSetting;

import com.base.model.entity.BwleErp.SystemSetting.employee.EmployeeSectorBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "employee_sector", isAnnotationField = true)
public class EmployeeSectorVo extends EmployeeSectorBaseAbstract {
	@Column(name = "employee_id", update_ignore = true)
	private Integer e_id;
	
	public Integer getE_id() {
		return e_id;
	}
	public void setE_id(Integer e_id) {
		this.e_id = e_id;
	}
}
