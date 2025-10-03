package com.base.model.entity.BwleErp.SystemSetting.company;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "company_bank", isAnnotationField = true)
public class Bank {
	@Column(name="bank_id", primary_key = true, auto_increment = true)
	private Integer bank_id;
	private String bank_name;
	private String bank_sname;
	private String bank_account_name;
	private String bank_account;
	private String bank_currency;
	private String bank_swift_bic;
	private String bank_middle_intermediary;
	private String bank_middle_swift_bic;
	private String bank_address;
	private Integer bank_valid;
	public Integer getBank_id() {
		return bank_id;
	}
	public void setBank_id(Integer bank_id) {
		this.bank_id = bank_id;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	public String getBank_sname() {
		return bank_sname;
	}
	public void setBank_sname(String bank_sname) {
		this.bank_sname = bank_sname;
	}
	public String getBank_account_name() {
		return bank_account_name;
	}
	public void setBank_account_name(String bank_account_name) {
		this.bank_account_name = bank_account_name;
	}
	public String getBank_account() {
		return bank_account;
	}
	public void setBank_account(String bank_account) {
		this.bank_account = bank_account;
	}
	public String getBank_currency() {
		return bank_currency;
	}
	public void setBank_currency(String bank_currency) {
		this.bank_currency = bank_currency;
	}
	public String getBank_swift_bic() {
		return bank_swift_bic;
	}
	public void setBank_swift_bic(String bank_swift_bic) {
		this.bank_swift_bic = bank_swift_bic;
	}
	public String getBank_middle_intermediary() {
		return bank_middle_intermediary;
	}
	public void setBank_middle_intermediary(String bank_middle_intermediary) {
		this.bank_middle_intermediary = bank_middle_intermediary;
	}
	public String getBank_middle_swift_bic() {
		return bank_middle_swift_bic;
	}
	public void setBank_middle_swift_bic(String bank_middle_swift_bic) {
		this.bank_middle_swift_bic = bank_middle_swift_bic;
	}
	public String getBank_address() {
		return bank_address;
	}
	public void setBank_address(String bank_address) {
		this.bank_address = bank_address;
	}
	public Integer getBank_valid() {
		return bank_valid;
	}
	public void setBank_valid(Integer bank_valid) {
		this.bank_valid = bank_valid;
	}
}
