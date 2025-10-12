package com.base.model.entity.BwleErp.ProductMGT.BaseAbstract;

import com.base.model.entity.BwleErp.SystemSetting.BaseAbstract.AuditingBase;

public abstract class ProductBaseAbstract extends AuditingBase {
	private String product_spread_name;
	private String product_lm_old;
	private String product_lm_exact;
	private Integer category_id;
	private Integer systype_id;
	private Integer commodity_id;
	private String product_desc;
	private String product_endesc;
	private String product_node;
	private String product_dev_type;
	private String product_dev_from;
	private String product_perfect;
	private Integer product_spread;
	private Integer product_offert_id;
	private Integer product_ispause;
	private String product_pause_reason;
	private Integer employee_id;
	private String product_add_date;
	public String getProduct_spread_name() {
		return product_spread_name;
	}
	public void setProduct_spread_name(String product_spread_name) {
		this.product_spread_name = product_spread_name;
	}
	public String getProduct_lm_old() {
		return product_lm_old;
	}
	public void setProduct_lm_old(String product_lm_old) {
		this.product_lm_old = product_lm_old;
	}
	public String getProduct_lm_exact() {
		return product_lm_exact;
	}
	public void setProduct_lm_exact(String product_lm_exact) {
		this.product_lm_exact = product_lm_exact;
	}
	public Integer getCategory_id() {
		return category_id;
	}
	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}
	public Integer getSystype_id() {
		return systype_id;
	}
	public void setSystype_id(Integer systype_id) {
		this.systype_id = systype_id;
	}
	public Integer getCommodity_id() {
		return commodity_id;
	}
	public void setCommodity_id(Integer commodity_id) {
		this.commodity_id = commodity_id;
	}
	public String getProduct_desc() {
		return product_desc;
	}
	public void setProduct_desc(String product_desc) {
		this.product_desc = product_desc;
	}
	public String getProduct_endesc() {
		return product_endesc;
	}
	public void setProduct_endesc(String product_endesc) {
		this.product_endesc = product_endesc;
	}
	public String getProduct_node() {
		return product_node;
	}
	public void setProduct_node(String product_node) {
		this.product_node = product_node;
	}
	public String getProduct_dev_type() {
		return product_dev_type;
	}
	public void setProduct_dev_type(String product_dev_type) {
		this.product_dev_type = product_dev_type;
	}
	public String getProduct_dev_from() {
		return product_dev_from;
	}
	public void setProduct_dev_from(String product_dev_from) {
		this.product_dev_from = product_dev_from;
	}
	public String getProduct_perfect() {
		return product_perfect;
	}
	public void setProduct_perfect(String product_perfect) {
		this.product_perfect = product_perfect;
	}
	public Integer getProduct_spread() {
		return product_spread;
	}
	public void setProduct_spread(Integer product_spread) {
		this.product_spread = product_spread;
	}
	public Integer getProduct_offert_id() {
		return product_offert_id;
	}
	public void setProduct_offert_id(Integer product_offert_id) {
		this.product_offert_id = product_offert_id;
	}
	public Integer getProduct_ispause() {
		return product_ispause;
	}
	public void setProduct_ispause(Integer product_ispause) {
		this.product_ispause = product_ispause;
	}
	public String getProduct_pause_reason() {
		return product_pause_reason;
	}
	public void setProduct_pause_reason(String product_pause_reason) {
		this.product_pause_reason = product_pause_reason;
	}
	public Integer getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(Integer employee_id) {
		this.employee_id = employee_id;
	}
	public String getProduct_add_date() {
		return product_add_date;
	}
	public void setProduct_add_date(String product_add_date) {
		this.product_add_date = product_add_date;
	}
}
