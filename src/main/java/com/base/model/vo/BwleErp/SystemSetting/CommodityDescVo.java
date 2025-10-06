package com.base.model.vo.BwleErp.SystemSetting;

import com.base.model.entity.BwleErp.SystemSetting.BaseAbstract.CommodityDescBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "category_commodity_desc", isAnnotationField = true)
public class CommodityDescVo extends CommodityDescBaseAbstract{
	@Column(name = "desc_id", primary_key = true, auto_increment = true)
	private String desc_id;

	public String getDesc_id() {
		return desc_id;
	}

	public void setDesc_id(String desc_id) {
		this.desc_id = desc_id;
	}

}
