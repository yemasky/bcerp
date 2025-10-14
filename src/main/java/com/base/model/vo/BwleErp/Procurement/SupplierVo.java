package com.base.model.vo.BwleErp.Procurement;

import java.util.HashMap;

import com.base.model.entity.BwleErp.Procurement.BaseAbstract.SupplierBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "procurement_supplier", isAnnotationField = true)
public class SupplierVo extends SupplierBaseAbstract{
	@Column(name = "supplier_id", primary_key = true, auto_increment = true)
	private String supplier_id;
	private HashMap<String ,SupplierLawsuit> supplier_lawsuit;
	private HashMap<String ,SupplierContact> supplier_contact;
	private HashMap<String ,SupplierPayment> supplier_payment;
	private HashMap<String ,SupplierBank> supplier_bank;
	public String getSupplier_id() {
		return supplier_id;
	}
	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}
	public HashMap<String, SupplierLawsuit> getSupplier_lawsuit() {
		return supplier_lawsuit;
	}
	public void setSupplier_lawsuit(HashMap<String, SupplierLawsuit> supplier_lawsuit) {
		this.supplier_lawsuit = supplier_lawsuit;
	}
	public HashMap<String, SupplierContact> getSupplier_contact() {
		return supplier_contact;
	}
	public void setSupplier_contact(HashMap<String, SupplierContact> supplier_contact) {
		this.supplier_contact = supplier_contact;
	}
	public HashMap<String, SupplierPayment> getSupplier_payment() {
		return supplier_payment;
	}
	public void setSupplier_payment(HashMap<String, SupplierPayment> supplier_payment) {
		this.supplier_payment = supplier_payment;
	}
	public HashMap<String, SupplierBank> getSupplier_bank() {
		return supplier_bank;
	}
	public void setSupplier_bank(HashMap<String, SupplierBank> supplier_bank) {
		this.supplier_bank = supplier_bank;
	}	
		
}
