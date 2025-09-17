package com.base.model.entity.BwleErp.employee;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "employee", isAnnotationField = true)
public class Employee extends EmployeeBaseAbstract {
	@Column(name = "employee_id", update_ignore = true)
	private Integer e_id;
	private String password;
	@Column(name = "password_salt", update_ignore = true)
	private String password_salt;
	private String wx_openid;
	private String wx_unionid;
	private String dimission;
	private String valid;
	private String is_system;
	public Integer getE_id() {
		return e_id;
	}
	public void setE_id(Integer e_id) {
		this.e_id = e_id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword_salt() {
		return password_salt;
	}
	public void setPassword_salt(String password_salt) {
		this.password_salt = password_salt;
	}
	public String getWx_openid() {
		return wx_openid;
	}
	public void setWx_openid(String wx_openid) {
		this.wx_openid = wx_openid;
	}
	public String getWx_unionid() {
		return wx_unionid;
	}
	public void setWx_unionid(String wx_unionid) {
		this.wx_unionid = wx_unionid;
	}
	public String getDimission() {
		return dimission;
	}
	public void setDimission(String dimission) {
		this.dimission = dimission;
	}
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}
	public String getIs_system() {
		return is_system;
	}
	public void setIs_system(String is_system) {
		this.is_system = is_system;
	}
	
}
