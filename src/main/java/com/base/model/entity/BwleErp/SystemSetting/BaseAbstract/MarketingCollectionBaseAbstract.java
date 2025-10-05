package com.base.model.entity.BwleErp.SystemSetting.BaseAbstract;

public abstract class MarketingCollectionBaseAbstract extends AuditingBase {
	private Integer collection_num;
	private String collection_deposit;
	private String collection_before;
	private Integer collection_days;
	private String collection_after;
	private String collection_cn;
	private String collection_en;
	private Integer employee_id;
	private Integer collection_valid;
	public Integer getCollection_num() {
		return collection_num;
	}
	public void setCollection_num(Integer collection_num) {
		this.collection_num = collection_num;
	}
	public String getCollection_deposit() {
		return collection_deposit;
	}
	public void setCollection_deposit(String collection_deposit) {
		this.collection_deposit = collection_deposit;
	}
	public String getCollection_before() {
		return collection_before;
	}
	public void setCollection_before(String collection_before) {
		this.collection_before = collection_before;
	}
	public Integer getCollection_days() {
		return collection_days;
	}
	public void setCollection_days(Integer collection_days) {
		this.collection_days = collection_days;
	}
	public String getCollection_after() {
		return collection_after;
	}
	public void setCollection_after(String collection_after) {
		this.collection_after = collection_after;
	}
	public String getCollection_cn() {
		return collection_cn;
	}
	public void setCollection_cn(String collection_cn) {
		this.collection_cn = collection_cn;
	}
	public String getCollection_en() {
		return collection_en;
	}
	public void setCollection_en(String collection_en) {
		this.collection_en = collection_en;
	}
	public Integer getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(Integer employee_id) {
		this.employee_id = employee_id;
	}
	public Integer getCollection_valid() {
		return collection_valid;
	}
	public void setCollection_valid(Integer collection_valid) {
		this.collection_valid = collection_valid;
	}
}
