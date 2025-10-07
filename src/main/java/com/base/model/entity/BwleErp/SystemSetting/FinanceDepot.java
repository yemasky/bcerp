package com.base.model.entity.BwleErp.SystemSetting;

import com.base.model.entity.BwleErp.SystemSetting.BaseAbstract.DepotBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "finance_depot", isAnnotationField = true)
public class FinanceDepot extends DepotBaseAbstract{
	@Column(name = "depot_id", primary_key = true, auto_increment = true)
	private Integer depot_id;
	private Integer depot_father_id;
	public Integer getDepot_id() {
		return depot_id;
	}

	public void setDepot_id(Integer depot_id) {
		this.depot_id = depot_id;
	}

	public Integer getDepot_father_id() {
		return depot_father_id;
	}

	public void setDepot_father_id(Integer depot_father_id) {
		this.depot_father_id = depot_father_id;
	}

}
