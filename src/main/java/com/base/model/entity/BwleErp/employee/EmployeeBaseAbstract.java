package com.base.model.entity.BwleErp.employee;

public abstract class EmployeeBaseAbstract {
	private Integer company_id;
	private String add_datetime;

	public Integer getCompany_id() {
		return company_id;
	}
	public void setCompany_id(Integer company_id) {
		this.company_id = company_id;
	}
	public String getAdd_datetime() {
		return add_datetime;
	}
	public void setAdd_datetime(String add_datetime) {
		this.add_datetime = add_datetime;
	}	
}
