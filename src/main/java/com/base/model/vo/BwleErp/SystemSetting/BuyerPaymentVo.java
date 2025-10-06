package com.base.model.vo.BwleErp.SystemSetting;

import com.base.model.entity.BwleErp.SystemSetting.BaseAbstract.BuyerPaymentBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;
@Table(name = "marketing_payment", isAnnotationField = true)
public class BuyerPaymentVo extends BuyerPaymentBaseAbstract {
	@Column(name = "payment_id", primary_key = true, auto_increment = true)
	private String payment_id;

	public String getPayment_id() {
		return payment_id;
	}

	public void setPayment_id(String payment_id) {
		this.payment_id = payment_id;
	}

}
