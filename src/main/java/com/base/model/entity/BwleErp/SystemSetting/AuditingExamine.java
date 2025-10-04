package com.base.model.entity.BwleErp.SystemSetting;

public class AuditingExamine {
	private Integer employee_id;
	private Integer sector_id;
	private Integer position_id;
	private Integer step;//第幾步
	private String proviso;//限制条件
	private Integer agent;//代理
	public Integer getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(Integer employee_id) {
		this.employee_id = employee_id;
	}
	public Integer getSector_id() {
		return sector_id;
	}
	public void setSector_id(Integer sector_id) {
		this.sector_id = sector_id;
	}
	public Integer getPosition_id() {
		return position_id;
	}
	public void setPosition_id(Integer position_id) {
		this.position_id = position_id;
	}
	public Integer getStep() {
		return step;
	}
	public void setStep(Integer step) {
		this.step = step;
	}
	public String getProviso() {
		return proviso;
	}
	public void setProviso(String proviso) {
		this.proviso = proviso;
	}
	public Integer getAgent() {
		return agent;
	}
	public void setAgent(Integer agent) {
		this.agent = agent;
	}
	
}
