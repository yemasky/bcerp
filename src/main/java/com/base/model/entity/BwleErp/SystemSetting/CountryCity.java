package com.base.model.entity.BwleErp.SystemSetting;

import com.base.model.entity.BwleErp.SystemSetting.BaseAbstract.CountryCityBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "country_city", isAnnotationField = true)
public class CountryCity extends CountryCityBaseAbstract {
	@Column(name = "city_id", primary_key = true, auto_increment = true)
	private Integer city_id;

	public Integer getCity_id() {
		return city_id;
	}

	public void setCity_id(Integer city_id) {
		this.city_id = city_id;
	}
	
}
