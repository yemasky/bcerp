package com.base.model.entity.BwleErp.SystemSetting.employee;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "employee", isAnnotationField = true)
public class Employee extends EmployeeBaseAbstract {
	@Column(name = "employee_id", update_ignore = true)
	private Integer e_id;
	private Long mobile;
	private String email;
	private String password;
	@Column(name = "password_salt", update_ignore = true)
	private String password_salt;
	private String wx_openid;
	private String wx_unionid;
	private Integer employee_valid;
	private Integer is_system;
	public Integer getE_id() {
		return e_id;
	}
	public void setE_id(Integer e_id) {
		this.e_id = e_id;
	}
	public Long getMobile() {
		return mobile;
	}
	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public Integer getEmployee_valid() {
		return employee_valid;
	}
	public void setEmployee_valid(Integer employee_valid) {
		this.employee_valid = employee_valid;
	}
	public Integer getIs_system() {
		return is_system;
	}
	public void setIs_system(Integer is_system) {
		this.is_system = is_system;
	}
	
}
