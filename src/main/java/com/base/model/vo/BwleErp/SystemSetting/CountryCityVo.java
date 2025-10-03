package com.base.model.vo.BwleErp.SystemSetting;

import com.base.model.entity.BwleErp.SystemSetting.BaseAbstract.CountryCityBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;
@Table(name = "country_city", isAnnotationField = true)
public class CountryCityVo extends CountryCityBaseAbstract {
	@Column(name = "city_id", primary_key = true, auto_increment = true)
	private String city_id;

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}
	
}
