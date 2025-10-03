package com.base.model.entity.BwleErp.SystemSetting.BaseAbstract;

public abstract class AuditingBaseAbstract {
	private String auditing_name;
	private Integer module_id;
	private Integer employee_id;
	private String add_datetime;
	private Integer auditing_valid;
	public String getAuditing_name() {
		return auditing_name;
	}
	public void setAuditing_name(String auditing_name) {
		this.auditing_name = auditing_name;
	}
	public Integer getModule_id() {
		return module_id;
	}
	public void setModule_id(Integer module_id) {
		this.module_id = module_id;
	}
	public Integer getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(Integer employee_id) {
		this.employee_id = employee_id;
	}
	public String getAdd_datetime() {
		return add_datetime;
	}
	public void setAdd_datetime(String add_datetime) {
		this.add_datetime = add_datetime;
	}
	public Integer getAuditing_valid() {
		return auditing_valid;
	}
	public void setAuditing_valid(Integer auditing_valid) {
		this.auditing_valid = auditing_valid;
	}
		
	
}
