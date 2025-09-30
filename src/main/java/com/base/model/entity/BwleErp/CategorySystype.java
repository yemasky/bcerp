package com.base.model.entity.BwleErp;

import com.base.model.entity.BwleErp.BaseAbstract.SystypeBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "category_systype", isAnnotationField = true)
public class CategorySystype extends SystypeBaseAbstract{
	@Column(name = "systype_id", primary_key = true, auto_increment = true)
	private Integer systype_id;

	public Integer getSystype_id() {
		return systype_id;
	}

	public void setSystype_id(Integer systype_id) {
		this.systype_id = systype_id;
	}
	
}
