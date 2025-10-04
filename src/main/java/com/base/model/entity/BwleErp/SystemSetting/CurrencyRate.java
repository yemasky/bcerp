package com.base.model.entity.BwleErp.SystemSetting;

import com.base.model.entity.BwleErp.SystemSetting.BaseAbstract.CurrencyRateBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;
@Table(name = "currency_rate", isAnnotationField = true)
public class CurrencyRate extends CurrencyRateBaseAbstract{
	@Column(name = "currency_id", primary_key = true, auto_increment = true)
	private Integer currency_id;

	public Integer getCurrency_id() {
		return currency_id;
	}

	public void setCurrency_id(Integer currency_id) {
		this.currency_id = currency_id;
	}
	
}
