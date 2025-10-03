package com.base.model.entity.BwleErp.SystemSetting.BaseAbstract;

public abstract class SystypeBaseAbstract {
	private String systype_name;
	private String systype_enname;
	private String systype_belong;
	private String systype_belong_en;
	private String systype_code;
	private Integer systype_valid;
	public String getSystype_name() {
		return systype_name;
	}
	public void setSystype_name(String systype_name) {
		this.systype_name = systype_name;
	}
	public String getSystype_enname() {
		return systype_enname;
	}
	public void setSystype_enname(String systype_enname) {
		this.systype_enname = systype_enname;
	}
	public String getSystype_belong() {
		return systype_belong;
	}
	public void setSystype_belong(String systype_belong) {
		this.systype_belong = systype_belong;
	}
	public String getSystype_belong_en() {
		return systype_belong_en;
	}
	public void setSystype_belong_en(String systype_belong_en) {
		this.systype_belong_en = systype_belong_en;
	}
	public String getSystype_code() {
		return systype_code;
	}
	public void setSystype_code(String systype_code) {
		this.systype_code = systype_code;
	}
	public Integer getSystype_valid() {
		return systype_valid;
	}
	public void setSystype_valid(Integer systype_valid) {
		this.systype_valid = systype_valid;
	}
	
}
