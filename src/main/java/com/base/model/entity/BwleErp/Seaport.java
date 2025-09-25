package com.base.model.entity.BwleErp;

import core.custom_interface.Table;

@Table(name = "seaport", isAnnotationField = true)
public class Seaport {
	private Integer seaport_id;
	private String seaport_name;
	private String seaport_enname;
	private String area_code;
	private String country_id;
	private Integer seaport_valid;
	public Integer getSeaport_id() {
		return seaport_id;
	}
	public void setSeaport_id(Integer seaport_id) {
		this.seaport_id = seaport_id;
	}
	public String getSeaport_name() {
		return seaport_name;
	}
	public void setSeaport_name(String seaport_name) {
		this.seaport_name = seaport_name;
	}
	public String getSeaport_enname() {
		return seaport_enname;
	}
	public void setSeaport_enname(String seaport_enname) {
		this.seaport_enname = seaport_enname;
	}
	public String getArea_code() {
		return area_code;
	}
	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}
	public String getCountry_id() {
		return country_id;
	}
	public void setCountry_id(String country_id) {
		this.country_id = country_id;
	}
	public Integer getSeaport_valid() {
		return seaport_valid;
	}
	public void setSeaport_valid(Integer seaport_valid) {
		this.seaport_valid = seaport_valid;
	}
		
}
