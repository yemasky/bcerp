package com.base.model.entity.BwleErp;

import com.base.model.entity.BwleErp.BaseAbstract.ClassifyBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "category_unit", isAnnotationField = true)
public class CategoryUnit extends ClassifyBaseAbstract{
	@Column(name = "classify", primary_key = true, auto_increment = true)
	private Integer unit_id;

	public Integer getUnit_id() {
		return unit_id;
	}

	public void setUnit_id(Integer unit_id) {
		this.unit_id = unit_id;
	}
	
}
