package com.base.model.vo.BwleErp.SystemSetting;

import com.base.model.entity.BwleErp.SystemSetting.BaseAbstract.CurrencyRateBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;
@Table(name = "currency_rate", isAnnotationField = true)
public class CurrencyRateVo extends CurrencyRateBaseAbstract{
	@Column(name = "currency_id", primary_key = true, auto_increment = true)
	private Integer currency_id;
	
	private String employee_name;
	
	public Integer getCurrency_id() {
		return currency_id;
	}

	public void setCurrency_id(Integer currency_id) {
		this.currency_id = currency_id;
	}

	public String getEmployee_name() {
		return employee_name;
	}

	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}
	
}
