package com.base.model.entity.BwleErp;

import core.custom_interface.Table;

@Table(name = "role", isAnnotationField = true)
public class Role {
	private Integer role_id;
	private Integer company_id;
	private Integer sector_id;
	private String role_name;
	private Integer is_system;
	private Integer role_valid;
	public Integer getRole_id() {
		return role_id;
	}
	public void setRole_id(Integer role_id) {
		this.role_id = role_id;
	}
	public Integer getCompany_id() {
		return company_id;
	}
	public void setCompany_id(Integer company_id) {
		this.company_id = company_id;
	}
	public Integer getSector_id() {
		return sector_id;
	}
	public void setSector_id(Integer sector_id) {
		this.sector_id = sector_id;
	}
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	public Integer getIs_system() {
		return is_system;
	}
	public void setIs_system(Integer is_system) {
		this.is_system = is_system;
	}
	public Integer getRole_valid() {
		return role_valid;
	}
	public void setRole_valid(Integer role_valid) {
		this.role_valid = role_valid;
	}
		
}
