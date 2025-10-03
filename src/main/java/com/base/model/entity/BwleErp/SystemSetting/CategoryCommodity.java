package com.base.model.entity.BwleErp.SystemSetting;

import com.base.model.entity.BwleErp.SystemSetting.BaseAbstract.CommodityBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;
@Table(name = "category_commodity", isAnnotationField = true)
public class CategoryCommodity extends CommodityBaseAbstract {
	@Column(name = "commodity_id", primary_key = true, auto_increment = true)
	private Integer commodity_id;
	private String commodity_spare;

	public Integer getCommodity_id() {
		return commodity_id;
	}

	public void setCommodity_id(Integer commodity_id) {
		this.commodity_id = commodity_id;
	}

	public String getCommodity_spare() {
		return commodity_spare;
	}

	public void setCommodity_spare(String commodity_spare) {
		this.commodity_spare = commodity_spare;
	}
	
}
