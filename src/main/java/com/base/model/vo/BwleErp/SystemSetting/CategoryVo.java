package com.base.model.vo.BwleErp.SystemSetting;

import com.base.model.entity.BwleErp.SystemSetting.BaseAbstract.CategoryBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "category", isAnnotationField = true)
public class CategoryVo extends CategoryBaseAbstract{
	@Column(name = "category_id", primary_key = true, auto_increment = true)
	private String category_id;
	
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	
}
