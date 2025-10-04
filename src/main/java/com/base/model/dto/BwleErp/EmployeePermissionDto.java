package com.base.model.dto.BwleErp;

public class EmployeePermissionDto {
	private boolean permission = false;
	private int module_id;
	private String module_channel;
	private String module;
	private String module_name;
	private String module_view;
	private String action;
	
	public boolean isPermission() {
		return permission;
	}

	public void setPermission(boolean permission) {
		this.permission = permission;
	}

	public int getModule_id() {
		return module_id;
	}

	public void setModule_id(int module_id) {
		this.module_id = module_id;
	}

	public String getModule_channel() {
		return module_channel;
	}

	public void setModule_channel(String module_channel) {
		this.module_channel = module_channel;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getModule_name() {
		return module_name;
	}

	public void setModule_name(String module_name) {
		this.module_name = module_name;
	}

	public String getModule_view() {
		return module_view;
	}

	public void setModule_view(String module_view) {
		this.module_view = module_view;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	
}
