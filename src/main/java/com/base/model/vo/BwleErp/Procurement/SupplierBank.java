package com.base.model.vo.BwleErp.Procurement;

public class SupplierBank {
	private String type;
	private String payee;
	private String name;
	private String account;
	private String address;
	private String swift_no;
	private String node;
	private Integer bank_default;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getSwift_no() {
		return swift_no;
	}
	public void setSwift_no(String swift_no) {
		this.swift_no = swift_no;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public Integer getBank_default() {
		return bank_default;
	}
	public void setBank_default(Integer bank_default) {
		this.bank_default = bank_default;
	}
}
