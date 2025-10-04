package com.base.model.dto.BwleErp;

public class AuditingExaminesDTO {
	private Integer auditing_id;
	private Integer update_id;
	private String status;
	private String auditing_date;
	private Integer auditing_employee_id;
	public Integer getAuditing_id() {
		return auditing_id;
	}
	public void setAuditing_id(Integer auditing_id) {
		this.auditing_id = auditing_id;
	}
	public Integer getUpdate_id() {
		return update_id;
	}
	public void setUpdate_id(Integer update_id) {
		this.update_id = update_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAuditing_date() {
		return auditing_date;
	}
	public void setAuditing_date(String auditing_date) {
		this.auditing_date = auditing_date;
	}
	public Integer getAuditing_employee_id() {
		return auditing_employee_id;
	}
	public void setAuditing_employee_id(Integer auditing_employee_id) {
		this.auditing_employee_id = auditing_employee_id;
	}
	
}
