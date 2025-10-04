package com.base.model.entity.BwleErp.SystemSetting;

import com.base.model.entity.BwleErp.SystemSetting.BaseAbstract.AuditingExaminesBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "auditing_examines", isAnnotationField = true)
public class AuditingExamines extends AuditingExaminesBaseAbstract{
	@Column(name = "examine_id", primary_key = true, auto_increment = true)
	private Integer examine_id;

	public Integer getExamine_id() {
		return examine_id;
	}

	public void setExamine_id(Integer examine_id) {
		this.examine_id = examine_id;
	}	
	
}
