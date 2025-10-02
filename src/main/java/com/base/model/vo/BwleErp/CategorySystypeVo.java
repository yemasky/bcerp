package com.base.model.vo.BwleErp;

import com.base.model.entity.BwleErp.BaseAbstract.SystypeBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "category_systype", isAnnotationField = true)
public class CategorySystypeVo extends SystypeBaseAbstract{
	@Column(name = "systype_id", primary_key = true, auto_increment = true)
	private String systype_id;

	public String getSystype_id() {
		return systype_id;
	}

	public void setSystype_id(String systype_id) {
		this.systype_id = systype_id;
	}
	
}
