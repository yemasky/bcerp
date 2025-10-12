package com.base.model.entity.BwleErp.ProductMGT;

import core.custom_interface.Table;

@Table(name = "product_classify", isAnnotationField = true)
public class ProductClassify {
	private Integer product_id;
	private Integer classify_id;
	private String vehicle_model_ids;
	private String oems;
	public Integer getProduct_id() {
		return product_id;
	}
	public void setProduct_id(Integer product_id) {
		this.product_id = product_id;
	}
	public Integer getClassify_id() {
		return classify_id;
	}
	public void setClassify_id(Integer classify_id) {
		this.classify_id = classify_id;
	}
	public String getVehicle_model_ids() {
		return vehicle_model_ids;
	}
	public void setVehicle_model_ids(String vehicle_model_ids) {
		this.vehicle_model_ids = vehicle_model_ids;
	}
	public String getOems() {
		return oems;
	}
	public void setOems(String oems) {
		this.oems = oems;
	}
}
