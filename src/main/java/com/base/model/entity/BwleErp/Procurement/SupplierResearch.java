package com.base.model.entity.BwleErp.Procurement;

import com.base.model.entity.BwleErp.Procurement.BaseAbstract.RsupplierBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "procurement_supplier_research", isAnnotationField = true)
public class SupplierResearch extends RsupplierBaseAbstract{
	@Column(name = "rsupplier_id", primary_key = true, auto_increment = true)
	private Integer rsupplier_id;
	private String rsupplier_contact;
	private String rsupplier_product;
	private String vehicle_type;
	private String rsupplier_plant;
	private String rsupplier_detection;
	private String tech_dev;
	private String rsupplier_upload;
	
	public Integer getRsupplier_id() {
		return rsupplier_id;
	}
	public void setRsupplier_id(Integer rsupplier_id) {
		this.rsupplier_id = rsupplier_id;
	}
	public String getRsupplier_contact() {
		return rsupplier_contact;
	}
	public void setRsupplier_contact(String rsupplier_contact) {
		this.rsupplier_contact = rsupplier_contact;
	}
	public String getRsupplier_product() {
		return rsupplier_product;
	}
	public void setRsupplier_product(String rsupplier_product) {
		this.rsupplier_product = rsupplier_product;
	}
	public String getVehicle_type() {
		return vehicle_type;
	}
	public void setVehicle_type(String vehicle_type) {
		this.vehicle_type = vehicle_type;
	}
	public String getRsupplier_plant() {
		return rsupplier_plant;
	}
	public void setRsupplier_plant(String rsupplier_plant) {
		this.rsupplier_plant = rsupplier_plant;
	}
	public String getRsupplier_detection() {
		return rsupplier_detection;
	}
	public void setRsupplier_detection(String rsupplier_detection) {
		this.rsupplier_detection = rsupplier_detection;
	}
	public String getTech_dev() {
		return tech_dev;
	}
	public void setTech_dev(String tech_dev) {
		this.tech_dev = tech_dev;
	}
	public String getRsupplier_upload() {
		return rsupplier_upload;
	}
	public void setRsupplier_upload(String rsupplier_upload) {
		this.rsupplier_upload = rsupplier_upload;
	}
	
}
