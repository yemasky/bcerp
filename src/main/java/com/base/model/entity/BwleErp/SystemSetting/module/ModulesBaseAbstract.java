package com.base.model.entity.BwleErp.SystemSetting.module;

public abstract class ModulesBaseAbstract {
	private String module_channel;
	private String module_name;
	private String module;
	private String url;
	private String module_view;
	private Integer module_order;
	private String action;
	private Integer action_order;
	private String is_menu;
	private String is_recommend;
	private String ico;
	private String is_release;
	public String getModule_channel() {
		return module_channel;
	}
	public void setModule_channel(String module_channel) {
		this.module_channel = module_channel;
	}
	public String getModule_name() {
		return module_name;
	}
	public void setModule_name(String module_name) {
		this.module_name = module_name;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getModule_view() {
		return module_view;
	}
	public void setModule_view(String module_view) {
		this.module_view = module_view;
	}
	public Integer getModule_order() {
		return module_order;
	}
	public void setModule_order(Integer module_order) {
		this.module_order = module_order;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Integer getAction_order() {
		return action_order;
	}
	public void setAction_order(Integer action_order) {
		this.action_order = action_order;
	}
	public String getIs_menu() {
		return is_menu;
	}
	public void setIs_menu(String is_menu) {
		this.is_menu = is_menu;
	}
	public String getIs_recommend() {
		return is_recommend;
	}
	public void setIs_recommend(String is_recommend) {
		this.is_recommend = is_recommend;
	}
	public String getIco() {
		return ico;
	}
	public void setIco(String ico) {
		this.ico = ico;
	}
	public String getIs_release() {
		return is_release;
	}
	public void setIs_release(String is_release) {
		this.is_release = is_release;
	}
}
