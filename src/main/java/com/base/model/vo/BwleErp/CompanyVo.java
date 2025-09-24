package com.base.model.vo.BwleErp;

import com.base.model.entity.BwleErp.company.CompanyBaseAbstract;

import core.custom_interface.Table;
@Table(name = "company", isAnnotationField = true)
public class CompanyVo extends CompanyBaseAbstract{
	private String company_id;

	public String getCompany_id() {
		return company_id;
	}

	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}
	
}
