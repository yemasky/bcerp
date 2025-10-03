package com.base.model.entity.BwleErp.SystemSetting.BaseAbstract;

public abstract class CountryCityBaseAbstract {
	private Integer country_id;
	private String city_name;
	private String city_py;
	private Integer city_father_id;
	public Integer getCountry_id() {
		return country_id;
	}
	public void setCountry_id(Integer country_id) {
		this.country_id = country_id;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public String getCity_py() {
		return city_py;
	}
	public void setCity_py(String city_py) {
		this.city_py = city_py;
	}
	public Integer getCity_father_id() {
		return city_father_id;
	}
	public void setCity_father_id(Integer city_father_id) {
		this.city_father_id = city_father_id;
	}
	
}
