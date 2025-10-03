package com.base.model.entity.BwleErp.SystemSetting;

import com.base.model.entity.BwleErp.SystemSetting.BaseAbstract.CategoryBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "category", isAnnotationField = true)
public class Category extends CategoryBaseAbstract{
	@Column(name = "category_id", primary_key = true, auto_increment = true)
	private Integer category_id;
	
	public Integer getCategory_id() {
		return category_id;
	}
	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}
	
}
