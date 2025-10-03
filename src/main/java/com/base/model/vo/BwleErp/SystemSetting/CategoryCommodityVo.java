package com.base.model.vo.BwleErp.SystemSetting;

import java.util.HashMap;

import com.base.model.entity.BwleErp.SystemSetting.BaseAbstract.CommodityBaseAbstract;
import com.base.model.entity.BwleErp.SystemSetting.BaseAbstract.CommoditySpare;

import core.custom_interface.Column;
import core.custom_interface.Table;
@Table(name = "category_commodity", isAnnotationField = true)
public class CategoryCommodityVo extends CommodityBaseAbstract {
	@Column(name = "commodity_id", primary_key = true, auto_increment = true)
	private String commodity_id;
	private HashMap<String ,CommoditySpare> commodity_spare;

	public String getCommodity_id() {
		return commodity_id;
	}

	public void setCommodity_id(String commodity_id) {
		this.commodity_id = commodity_id;
	}

	public HashMap<String, CommoditySpare> getCommodity_spare() {
		return commodity_spare;
	}

	public void setCommodity_spare(HashMap<String, CommoditySpare> commodity_spare) {
		this.commodity_spare = commodity_spare;
	}
	
}
