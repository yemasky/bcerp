package com.base.model.entity.BwleErp.SystemSetting;

import com.base.model.entity.BwleErp.SystemSetting.BaseAbstract.CommodityDescBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "category_commodity_desc", isAnnotationField = true)
public class CommodityDesc extends CommodityDescBaseAbstract{
	@Column(name = "desc_id", primary_key = true, auto_increment = true)
	private Integer desc_id;

	public Integer getDesc_id() {
		return desc_id;
	}

	public void setDesc_id(Integer desc_id) {
		this.desc_id = desc_id;
	}

}
