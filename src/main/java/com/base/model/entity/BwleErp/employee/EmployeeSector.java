package com.base.model.entity.BwleErp.employee;

import java.sql.Date;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "employee_sector", isAnnotationField = false)
public class EmployeeSector {
	private Integer company_id;                        
	private String company_ids;                   
	private Integer sector_father_id;    
	private String sector_father_ids;          
	private Integer sector_id;                  
	private String sector_ids;                   
	private Integer role_id;                   
	private String role_ids;           
	private String is_default;                
	@Column(name = "employee_id", update_ignore = true)
	private Integer e_id;            
	private String employee_name;       
	private String country;                           
	private String email;                                 
	private Long mobile;                                   
	private String weixin;                                     
	private Date birthday;                    
	private String photo;                  
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
	public Integer getCompany_id() {
		return company_id;
	}
	public void setCompany_id(Integer company_id) {
		this.company_id = company_id;
	}
	public String getCompany_ids() {
		return company_ids;
	}
	public void setCompany_ids(String company_ids) {
		this.company_ids = company_ids;
	}
	public Integer getSector_father_id() {
		return sector_father_id;
	}
	public void setSector_father_id(Integer sector_father_id) {
		this.sector_father_id = sector_father_id;
	}
	public String getSector_father_ids() {
		return sector_father_ids;
	}
	public void setSector_father_ids(String sector_father_ids) {
		this.sector_father_ids = sector_father_ids;
	}
	public Integer getSector_id() {
		return sector_id;
	}
	public void setSector_id(Integer sector_id) {
		this.sector_id = sector_id;
	}
	public String getSector_ids() {
		return sector_ids;
	}
	public void setSector_ids(String sector_ids) {
		this.sector_ids = sector_ids;
	}
	public Integer getRole_id() {
		return role_id;
	}
	public void setRole_id(Integer role_id) {
		this.role_id = role_id;
	}
	public String getRole_ids() {
		return role_ids;
	}
	public void setRole_ids(String role_ids) {
		this.role_ids = role_ids;
	}
	public String getIs_default() {
		return is_default;
	}
	public void setIs_default(String is_default) {
		this.is_default = is_default;
	}
	public Integer getE_id() {
		return e_id;
	}
	public void setE_id(Integer e_id) {
		this.e_id = e_id;
	}
	public String getEmployee_name() {
		return employee_name;
	}
	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getMobile() {
		return mobile;
	}
	public void setMobile(Long mobile) {
		this.mobile = mobile;
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
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
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
}
