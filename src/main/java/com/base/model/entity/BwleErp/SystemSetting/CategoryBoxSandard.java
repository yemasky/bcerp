package com.base.model.entity.BwleErp.SystemSetting;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "category_box_standard", isAnnotationField = true)
public class CategoryBoxSandard {
	@Column(name = "box_id", primary_key = true, auto_increment = true)
	private Integer box_id;
	private String box_no;
	private String box_name;
	private String box_enname;
	private String box_qty_ctn;
	private String box_unit;
	private String box_gw_ctn_kg;
	private String box_nw_ctn_kg;
	private String box_l;
	private String box_w;
	private String box_h;
	private String box_weight_pcs;
	private String box_volume_pcs;
	private String box_pcontract;
	private String box_desc;
	private String box_node;
	private Integer box_valid;
	public Integer getBox_id() {
		return box_id;
	}
	public void setBox_id(Integer box_id) {
		this.box_id = box_id;
	}
	public String getBox_no() {
		return box_no;
	}
	public void setBox_no(String box_no) {
		this.box_no = box_no;
	}
	public String getBox_name() {
		return box_name;
	}
	public void setBox_name(String box_name) {
		this.box_name = box_name;
	}
	public String getBox_enname() {
		return box_enname;
	}
	public void setBox_enname(String box_enname) {
		this.box_enname = box_enname;
	}
	public String getBox_qty_ctn() {
		return box_qty_ctn;
	}
	public void setBox_qty_ctn(String box_qty_ctn) {
		this.box_qty_ctn = box_qty_ctn;
	}
	public String getBox_unit() {
		return box_unit;
	}
	public void setBox_unit(String box_unit) {
		this.box_unit = box_unit;
	}
	public String getBox_gw_ctn_kg() {
		return box_gw_ctn_kg;
	}
	public void setBox_gw_ctn_kg(String box_gw_ctn_kg) {
		this.box_gw_ctn_kg = box_gw_ctn_kg;
	}
	public String getBox_nw_ctn_kg() {
		return box_nw_ctn_kg;
	}
	public void setBox_nw_ctn_kg(String box_nw_ctn_kg) {
		this.box_nw_ctn_kg = box_nw_ctn_kg;
	}
	public String getBox_l() {
		return box_l;
	}
	public void setBox_l(String box_l) {
		this.box_l = box_l;
	}
	public String getBox_w() {
		return box_w;
	}
	public void setBox_w(String box_w) {
		this.box_w = box_w;
	}
	public String getBox_h() {
		return box_h;
	}
	public void setBox_h(String box_h) {
		this.box_h = box_h;
	}
	public String getBox_weight_pcs() {
		return box_weight_pcs;
	}
	public void setBox_weight_pcs(String box_weight_pcs) {
		this.box_weight_pcs = box_weight_pcs;
	}
	public String getBox_volume_pcs() {
		return box_volume_pcs;
	}
	public void setBox_volume_pcs(String box_volume_pcs) {
		this.box_volume_pcs = box_volume_pcs;
	}
	public String getBox_pcontract() {
		return box_pcontract;
	}
	public void setBox_pcontract(String box_pcontract) {
		this.box_pcontract = box_pcontract;
	}
	public String getBox_desc() {
		return box_desc;
	}
	public void setBox_desc(String box_desc) {
		this.box_desc = box_desc;
	}
	public String getBox_node() {
		return box_node;
	}
	public void setBox_node(String box_node) {
		this.box_node = box_node;
	}
	public Integer getBox_valid() {
		return box_valid;
	}
	public void setBox_valid(Integer box_valid) {
		this.box_valid = box_valid;
	}
}
