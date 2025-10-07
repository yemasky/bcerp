package com.base.model.vo.BwleErp.SystemSetting;

import com.base.model.entity.BwleErp.SystemSetting.BaseAbstract.DepotBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "finance_depot", isAnnotationField = true)
public class FinanceDepotVo extends DepotBaseAbstract{
	@Column(name = "depot_id", primary_key = true, auto_increment = true)
	private String depot_id;
	private String depot_father_id;
	public String getDepot_id() {
		return depot_id;
	}

	public void setDepot_id(String depot_id) {
		this.depot_id = depot_id;
	}

	public String getDepot_father_id() {
		return depot_father_id;
	}

	public void setDepot_father_id(String depot_father_id) {
		this.depot_father_id = depot_father_id;
	}
}
