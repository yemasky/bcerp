package com.base.model.entity.BwleErp;

import com.base.model.entity.BwleErp.BaseAbstract.AuditingBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "auditing", isAnnotationField = true)
public class Auditing extends AuditingBaseAbstract{
	@Column(name = "auditing_id", primary_key = true, auto_increment = true)
	private Integer auditing_id;
	private String examine;

	public Integer getAuditing_id() {
		return auditing_id;
	}

	public void setAuditing_id(Integer auditing_id) {
		this.auditing_id = auditing_id;
	}

	public String getExamine() {
		return examine;
	}

	public void setExamine(String examine) {
		this.examine = examine;
	}
	
	
}
