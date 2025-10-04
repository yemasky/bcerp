package com.base.model.entity.BwleErp.SystemSetting.BaseAbstract;

import java.sql.Date;

public abstract class CurrencyRateBaseAbstract {
	private String currency_name;
	private String currency_sname;
	private String currency_symbol;
	private String buying_rate;
	private String cash_buying_rate;
	private String selling_rate;
	private String cash_selling_rate;
	private String boe_conversion_rate;
	private Date bank_release_date;
	private Date financial_release_date;
	private String add_datetime;
	private Integer employee_id;
	private Integer auditing_state;
	private Integer auditing_id;
	private Integer currency_valid;
	
	public String getCurrency_name() {
		return currency_name;
	}
	public void setCurrency_name(String currency_name) {
		this.currency_name = currency_name;
	}
	public String getCurrency_sname() {
		return currency_sname;
	}
	public void setCurrency_sname(String currency_sname) {
		this.currency_sname = currency_sname;
	}
	public String getCurrency_symbol() {
		return currency_symbol;
	}
	public void setCurrency_symbol(String currency_symbol) {
		this.currency_symbol = currency_symbol;
	}
	public String getBuying_rate() {
		return buying_rate;
	}
	public void setBuying_rate(String buying_rate) {
		this.buying_rate = buying_rate;
	}
	public String getCash_buying_rate() {
		return cash_buying_rate;
	}
	public void setCash_buying_rate(String cash_buying_rate) {
		this.cash_buying_rate = cash_buying_rate;
	}
	public String getSelling_rate() {
		return selling_rate;
	}
	public void setSelling_rate(String selling_rate) {
		this.selling_rate = selling_rate;
	}
	public String getCash_selling_rate() {
		return cash_selling_rate;
	}
	public void setCash_selling_rate(String cash_selling_rate) {
		this.cash_selling_rate = cash_selling_rate;
	}
	public String getBoe_conversion_rate() {
		return boe_conversion_rate;
	}
	public void setBoe_conversion_rate(String boe_conversion_rate) {
		this.boe_conversion_rate = boe_conversion_rate;
	}
	public Date getBank_release_date() {
		return bank_release_date;
	}
	public void setBank_release_date(Date bank_release_date) {
		this.bank_release_date = bank_release_date;
	}
	public Date getFinancial_release_date() {
		return financial_release_date;
	}
	public void setFinancial_release_date(Date financial_release_date) {
		this.financial_release_date = financial_release_date;
	}
	public String getAdd_datetime() {
		return add_datetime;
	}
	public void setAdd_datetime(String add_datetime) {
		this.add_datetime = add_datetime;
	}
	public Integer getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(Integer employee_id) {
		this.employee_id = employee_id;
	}
	public Integer getAuditing_state() {
		return auditing_state;
	}
	public void setAuditing_state(Integer auditing_state) {
		this.auditing_state = auditing_state;
	}
	public Integer getAuditing_id() {
		return auditing_id;
	}
	public void setAuditing_id(Integer auditing_id) {
		this.auditing_id = auditing_id;
	}
	public Integer getCurrency_valid() {
		return currency_valid;
	}
	public void setCurrency_valid(Integer currency_valid) {
		this.currency_valid = currency_valid;
	}
	
}
