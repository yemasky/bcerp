package com.base.model.entity.BwleErp.employee;

import java.sql.Date;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "employee_sector", isAnnotationField = true)
public class EmployeeSector extends EmployeeSectorBaseAbstract {
	@Column(name = "employee_id", update_ignore = true)
	private Integer e_id;
	private String is_default;                            
	private String country;                           
	private String weixin;                                 
	private Date birthday;                    
	private String id_card;                     
	private String address;                      
	private Integer sex;                       
	private String present_address;                
	private String positive_id_card;         
	private String back_id_card;         
	private Integer full_time;           
	private Date entry_date;                  
	private Date probation_date;      
	private String photo_labor;       
	private String employee_number;           
	private String emergency_contact;   
	private String emergency_relation;  
	private String emergency_mobile;    
	private Integer dimission;              
	private Date dimission_date;
	private Integer employee_valid; 
	
	public Integer getE_id() {
		return e_id;
	}
	public void setE_id(Integer e_id) {
		this.e_id = e_id;
	}
	public String getIs_default() {
		return is_default;
	}
	public void setIs_default(String is_default) {
		this.is_default = is_default;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getWeixin() {
		return weixin;
	}
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getId_card() {
		return id_card;
	}
	public void setId_card(String id_card) {
		this.id_card = id_card;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getPresent_address() {
		return present_address;
	}
	public void setPresent_address(String present_address) {
		this.present_address = present_address;
	}
	public String getPositive_id_card() {
		return positive_id_card;
	}
	public void setPositive_id_card(String positive_id_card) {
		this.positive_id_card = positive_id_card;
	}
	public String getBack_id_card() {
		return back_id_card;
	}
	public void setBack_id_card(String back_id_card) {
		this.back_id_card = back_id_card;
	}
	public Integer getFull_time() {
		return full_time;
	}
	public void setFull_time(Integer full_time) {
		this.full_time = full_time;
	}
	public Date getEntry_date() {
		return entry_date;
	}
	public void setEntry_date(Date entry_date) {
		this.entry_date = entry_date;
	}
	public Date getProbation_date() {
		return probation_date;
	}
	public void setProbation_date(Date probation_date) {
		this.probation_date = probation_date;
	}
	public String getPhoto_labor() {
		return photo_labor;
	}
	public void setPhoto_labor(String photo_labor) {
		this.photo_labor = photo_labor;
	}
	public String getEmployee_number() {
		return employee_number;
	}
	public void setEmployee_number(String employee_number) {
		this.employee_number = employee_number;
	}
	public String getEmergency_contact() {
		return emergency_contact;
	}
	public void setEmergency_contact(String emergency_contact) {
		this.emergency_contact = emergency_contact;
	}
	public String getEmergency_relation() {
		return emergency_relation;
	}
	public void setEmergency_relation(String emergency_relation) {
		this.emergency_relation = emergency_relation;
	}
	public String getEmergency_mobile() {
		return emergency_mobile;
	}
	public void setEmergency_mobile(String emergency_mobile) {
		this.emergency_mobile = emergency_mobile;
	}
	public Integer getDimission() {
		return dimission;
	}
	public void setDimission(Integer dimission) {
		this.dimission = dimission;
	}
	public Date getDimission_date() {
		return dimission_date;
	}
	public void setDimission_date(Date dimission_date) {
		this.dimission_date = dimission_date;
	}
	public Integer getEmployee_valid() {
		return employee_valid;
	}
	public void setEmployee_valid(Integer employee_valid) {
		this.employee_valid = employee_valid;
	}
}
