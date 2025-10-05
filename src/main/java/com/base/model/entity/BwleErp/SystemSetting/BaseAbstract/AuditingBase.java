package com.base.model.entity.BwleErp.SystemSetting.BaseAbstract;

public abstract class AuditingBase {
	private Integer auditing_employee_id;
	private Integer auditing_state;
	private Integer auditing_id;
	private String auditing_date;
	public Integer getAuditing_employee_id() {
		return auditing_employee_id;
	}
	public void setAuditing_employee_id(Integer auditing_employee_id) {
		this.auditing_employee_id = auditing_employee_id;
	}
	public Integer getAuditing_state() {
		return auditing_state;
	}
	public void setAuditing_state(Integer auditing_state) {
		this.auditing_state = auditing_state;
	}
	public Integer getAuditing_id() {
		return auditing_id;
	}
	public void setAuditing_id(Integer auditing_id) {
		this.auditing_id = auditing_id;
	}
	public String getAuditing_date() {
		return auditing_date;
	}
	public void setAuditing_date(String auditing_date) {
		this.auditing_date = auditing_date;
	}
}
