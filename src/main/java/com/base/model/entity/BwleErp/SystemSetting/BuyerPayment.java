package com.base.model.entity.BwleErp.SystemSetting;

import com.base.model.entity.BwleErp.SystemSetting.BaseAbstract.BuyerPaymentBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;
@Table(name = "marketing_payment", isAnnotationField = true)
public class BuyerPayment extends BuyerPaymentBaseAbstract {
	@Column(name = "payment_id", primary_key = true, auto_increment = true)
	private Integer payment_id;

	public Integer getPayment_id() {
		return payment_id;
	}

	public void setPayment_id(Integer payment_id) {
		this.payment_id = payment_id;
	}

	
}
