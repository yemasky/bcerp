package com.base.model.vo.BwleErp.SystemSetting;

import com.base.model.entity.BwleErp.SystemSetting.BaseAbstract.ClassifyBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "category_classify", isAnnotationField = true)
public class CategoryClassifyVo extends ClassifyBaseAbstract{
	@Column(name = "classify_id", primary_key = true, auto_increment = true)
	private String classify_id;

	public String getClassify_id() {
		return classify_id;
	}

	public void setClassify_id(String classify_id) {
		this.classify_id = classify_id;
	}

	
}
