package com.base.model.entity.BwleErp.SystemSetting.company;

public abstract class CompanySectorBaseAbstract {
	private Integer company_id;
	private Integer sector_father_id;
	private String sector_name;
	private Long sector_order;
	private String sector_type;
	private Integer sector_valid;
	public Integer getCompany_id() {
		return company_id;
	}
	public void setCompany_id(Integer company_id) {
		this.company_id = company_id;
	}
	public Integer getSector_father_id() {
		return sector_father_id;
	}
	public void setSector_father_id(Integer sector_father_id) {
		this.sector_father_id = sector_father_id;
	}
	public String getSector_name() {
		return sector_name;
	}
	public void setSector_name(String sector_name) {
		this.sector_name = sector_name;
	}
	public Long getSector_order() {
		return sector_order;
	}
	public void setSector_order(Long sector_order) {
		this.sector_order = sector_order;
	}
	public String getSector_type() {
		return sector_type;
	}
	public void setSector_type(String sector_type) {
		this.sector_type = sector_type;
	}
	public Integer getSector_valid() {
		return sector_valid;
	}
	public void setSector_valid(Integer sector_valid) {
		this.sector_valid = sector_valid;
	}
	
}
