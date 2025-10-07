package com.base.model.vo.BwleErp.Procurement;

import java.util.HashMap;
import java.util.List;

import com.base.model.entity.BwleErp.Procurement.BaseAbstract.RsupplierBaseAbstract;
import com.base.model.entity.BwleErp.Procurement.BaseAbstract.RsupplierContact;
import com.base.model.entity.BwleErp.Procurement.BaseAbstract.RsupplierDetection;
import com.base.model.entity.BwleErp.Procurement.BaseAbstract.RsupplierPlant;
import com.base.model.entity.BwleErp.Procurement.BaseAbstract.RsupplierProduct;
import com.base.model.entity.BwleErp.Procurement.BaseAbstract.RsupplierUpload;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "procurement_supplier_research", isAnnotationField = true)
public class SupplierResearchVo extends RsupplierBaseAbstract{
	@Column(name = "rsupplier_id", primary_key = true, auto_increment = true)
	private String rsupplier_id;
	private HashMap<String ,RsupplierContact> rsupplier_contact;
	private HashMap<String ,RsupplierProduct> rsupplier_product;
	private List<Object> vehicle_type;
	private HashMap<String ,RsupplierPlant> rsupplier_plant;
	private HashMap<String ,RsupplierDetection> rsupplier_detection;
	private List<Object> tech_dev;
	private HashMap<String ,RsupplierUpload> rsupplier_upload;
	public String getRsupplier_id() {
		return rsupplier_id;
	}
	public void setRsupplier_id(String rsupplier_id) {
		this.rsupplier_id = rsupplier_id;
	}
	public HashMap<String, RsupplierContact> getRsupplier_contact() {
		return rsupplier_contact;
	}
	public void setRsupplier_contact(HashMap<String, RsupplierContact> rsupplier_contact) {
		this.rsupplier_contact = rsupplier_contact;
	}
	public HashMap<String, RsupplierProduct> getRsupplier_product() {
		return rsupplier_product;
	}
	public void setRsupplier_product(HashMap<String, RsupplierProduct> rsupplier_product) {
		this.rsupplier_product = rsupplier_product;
	}
	public List<Object> getVehicle_type() {
		return vehicle_type;
	}
	public void setVehicle_type(List<Object> vehicle_type) {
		this.vehicle_type = vehicle_type;
	}
	public HashMap<String, RsupplierPlant> getRsupplier_plant() {
		return rsupplier_plant;
	}
	public void setRsupplier_plant(HashMap<String, RsupplierPlant> rsupplier_plant) {
		this.rsupplier_plant = rsupplier_plant;
	}
	public HashMap<String, RsupplierDetection> getRsupplier_detection() {
		return rsupplier_detection;
	}
	public void setRsupplier_detection(HashMap<String, RsupplierDetection> rsupplier_detection) {
		this.rsupplier_detection = rsupplier_detection;
	}
	public List<Object> getTech_dev() {
		return tech_dev;
	}
	public void setTech_dev(List<Object> tech_dev) {
		this.tech_dev = tech_dev;
	}
	public HashMap<String, RsupplierUpload> getRsupplier_upload() {
		return rsupplier_upload;
	}
	public void setRsupplier_upload(HashMap<String, RsupplierUpload> rsupplier_upload) {
		this.rsupplier_upload = rsupplier_upload;
	}
		
}
