package com.base.model.entity.BwleErp.SystemSetting;

import core.custom_interface.Table;

@Table(name = "country", isAnnotationField = true)
public class Country {
	private Integer country_id;
	private String country_name;
	private String country_sname;
	private String country_enname;
	private String area_code;
	private String area;
	private String wheel_direction;
	private Integer country_valid;
	public Integer getCountry_id() {
		return country_id;
	}
	public void setCountry_id(Integer country_id) {
		this.country_id = country_id;
	}
	public String getCountry_name() {
		return country_name;
	}
	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}
	public String getCountry_sname() {
		return country_sname;
	}
	public void setCountry_sname(String country_sname) {
		this.country_sname = country_sname;
	}
	public String getCountry_enname() {
		return country_enname;
	}
	public void setCountry_enname(String country_enname) {
		this.country_enname = country_enname;
	}
	public String getArea_code() {
		return area_code;
	}
	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getWheel_direction() {
		return wheel_direction;
	}
	public void setWheel_direction(String wheel_direction) {
		this.wheel_direction = wheel_direction;
	}
	public Integer getCountry_valid() {
		return country_valid;
	}
	public void setCountry_valid(Integer country_valid) {
		this.country_valid = country_valid;
	}
	
}
