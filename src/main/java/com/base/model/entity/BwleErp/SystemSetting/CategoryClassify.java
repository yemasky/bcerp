package com.base.model.entity.BwleErp.SystemSetting;

import com.base.model.entity.BwleErp.SystemSetting.BaseAbstract.ClassifyBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "category_classify", isAnnotationField = true)
public class CategoryClassify extends ClassifyBaseAbstract{
	@Column(name = "classify", primary_key = true, auto_increment = true)
	private Integer classify_id;

	public Integer getClassify_id() {
		return classify_id;
	}

	public void setClassify_id(Integer classify_id) {
		this.classify_id = classify_id;
	}

	
}
