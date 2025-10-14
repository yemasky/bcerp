package com.base.model.entity.BwleErp.Procurement;

import com.base.model.entity.BwleErp.Procurement.BaseAbstract.SupplierBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "procurement_supplier", isAnnotationField = true)
public class Supplier extends SupplierBaseAbstract{
	@Column(name = "supplier_id", primary_key = true, auto_increment = true)
	private Integer supplier_id;
	private String supplier_lawsuit;
	private String supplier_contact;
	private String supplier_payment;
	private String supplier_bank;
	public Integer getSupplier_id() {
		return supplier_id;
	}
	public void setSupplier_id(Integer supplier_id) {
		this.supplier_id = supplier_id;
	}
	public String getSupplier_lawsuit() {
		return supplier_lawsuit;
	}
	public void setSupplier_lawsuit(String supplier_lawsuit) {
		this.supplier_lawsuit = supplier_lawsuit;
	}
	public String getSupplier_contact() {
		return supplier_contact;
	}
	public void setSupplier_contact(String supplier_contact) {
		this.supplier_contact = supplier_contact;
	}
	public String getSupplier_payment() {
		return supplier_payment;
	}
	public void setSupplier_payment(String supplier_payment) {
		this.supplier_payment = supplier_payment;
	}
	public String getSupplier_bank() {
		return supplier_bank;
	}
	public void setSupplier_bank(String supplier_bank) {
		this.supplier_bank = supplier_bank;
	}
			
}
