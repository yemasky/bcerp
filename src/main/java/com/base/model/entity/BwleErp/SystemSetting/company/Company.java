package com.base.model.entity.BwleErp.SystemSetting.company;

import core.custom_interface.Table;

@Table(name = "company", isAnnotationField = true)
public class Company extends CompanyBaseAbstract {
	private Integer company_id;
	
	public Integer getCompany_id() {
		return company_id;
	}
	public void setCompany_id(Integer company_id) {
		this.company_id = company_id;
	}
	
	
}
