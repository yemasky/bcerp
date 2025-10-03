package com.base.model.vo.BwleErp.SystemSetting;

import com.base.model.entity.BwleErp.SystemSetting.company.CompanySectorBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "company_sector", isAnnotationField = true)
public class CompanySectorVo extends CompanySectorBaseAbstract {
	private Integer sector_id;
	@Column(name = "c_s_id", ignore = true)
	private String c_s_id;
	public Integer getSector_id() {
		return sector_id;
	}
	public void setSector_id(Integer sector_id) {
		this.sector_id = sector_id;
	}
	public String getC_s_id() {
		return c_s_id;
	}
	public void setC_s_id(String c_s_id) {
		this.c_s_id = c_s_id;
	}
	
}
