package com.base.model.entity.BwleErp.SystemSetting.BaseAbstract;

public abstract class BuyerPaymentBaseAbstract extends AuditingBase {
	private Integer payment_num;
	private Integer payment_deposit;
	private Integer payment_before;
	private Integer payment_days;
	private Integer payment_after;
	private Integer payment_receipt;
	private String payment_desc;
	private String payment_sdesc;
	private Integer employee_id;
	private Integer payment_valid;
	public Integer getPayment_num() {
		return payment_num;
	}
	public void setPayment_num(Integer payment_num) {
		this.payment_num = payment_num;
	}
	public Integer getPayment_deposit() {
		return payment_deposit;
	}
	public void setPayment_deposit(Integer payment_deposit) {
		this.payment_deposit = payment_deposit;
	}
	public Integer getPayment_before() {
		return payment_before;
	}
	public void setPayment_before(Integer payment_before) {
		this.payment_before = payment_before;
	}
	public Integer getPayment_days() {
		return payment_days;
	}
	public void setPayment_days(Integer payment_days) {
		this.payment_days = payment_days;
	}
	public Integer getPayment_after() {
		return payment_after;
	}
	public void setPayment_after(Integer payment_after) {
		this.payment_after = payment_after;
	}
	public Integer getPayment_receipt() {
		return payment_receipt;
	}
	public void setPayment_receipt(Integer payment_receipt) {
		this.payment_receipt = payment_receipt;
	}
	public String getPayment_desc() {
		return payment_desc;
	}
	public void setPayment_desc(String payment_desc) {
		this.payment_desc = payment_desc;
	}
	public String getPayment_sdesc() {
		return payment_sdesc;
	}
	public void setPayment_sdesc(String payment_sdesc) {
		this.payment_sdesc = payment_sdesc;
	}
	public Integer getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(Integer employee_id) {
		this.employee_id = employee_id;
	}
	public Integer getPayment_valid() {
		return payment_valid;
	}
	public void setPayment_valid(Integer payment_valid) {
		this.payment_valid = payment_valid;
	}
	
}
