package com.base.model.entity.BwleErp.ProductMGT;

import com.base.model.entity.BwleErp.ProductMGT.BaseAbstract.ProductBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "product", isAnnotationField = true)
public class Product extends ProductBaseAbstract {
	@Column(name = "product_id", primary_key = true, auto_increment = true)
	private Integer product_id;

	public Integer getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Integer product_id) {
		this.product_id = product_id;
	}
	
	
}
