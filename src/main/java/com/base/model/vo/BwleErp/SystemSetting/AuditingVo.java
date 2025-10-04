package com.base.model.vo.BwleErp.SystemSetting;

import java.util.HashMap;

import com.base.model.dto.BwleErp.AuditingExamine;
import com.base.model.entity.BwleErp.SystemSetting.BaseAbstract.AuditingBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "auditing", isAnnotationField = true)
public class AuditingVo extends AuditingBaseAbstract{
	@Column(name = "auditing_id", primary_key = true, auto_increment = true)
	private String auditing_id;
	private HashMap<Integer ,AuditingExamine> examine;

	public String getAuditing_id() {
		return auditing_id;
	}

	public void setAuditing_id(String auditing_id) {
		this.auditing_id = auditing_id;
	}

	public HashMap<Integer, AuditingExamine> getExamine() {
		return examine;
	}

	public void setExamine(HashMap<Integer, AuditingExamine> examine) {
		this.examine = examine;
	}
		
}
