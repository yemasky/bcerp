package com.base.model.entity.BwleErp.company;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "company_sector", isAnnotationField = true)
public class CompanySector extends CompanySectorBaseAbstract {
	@Column(name="sector_id", primary_key = true, auto_increment = true)
	private Integer sector_id;

	public Integer getSector_id() {
		return sector_id;
	}

	public void setSector_id(Integer sector_id) {
		this.sector_id = sector_id;
	}
	
}
