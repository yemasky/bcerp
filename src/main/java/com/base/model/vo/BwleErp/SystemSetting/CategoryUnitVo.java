package com.base.model.vo.BwleErp.SystemSetting;

import com.base.model.entity.BwleErp.SystemSetting.BaseAbstract.UnitBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "category_unit", isAnnotationField = true)
public class CategoryUnitVo extends UnitBaseAbstract{
	@Column(name = "unit_id", primary_key = true, auto_increment = true)
	private String unit_id;

	public String getUnit_id() {
		return unit_id;
	}

	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}
	
}
