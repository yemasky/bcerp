package com.base.model.dto.BwleErp;

import java.util.HashMap;

public class RoleModuleDTO {
	private String role_name;
	private String company_id;
	private HashMap<String, String> role_module;
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	public String getCompany_id() {
		return company_id;
	}
	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}
	public HashMap<String, String> getRole_module() {
		return role_module;
	}
	public void setRole_module(HashMap<String, String> role_module) {
		this.role_module = role_module;
	}
	
}
