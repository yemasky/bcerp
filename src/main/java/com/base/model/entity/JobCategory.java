package com.base.model.entity;

import core.custom_interface.Table;

@Table(name = "job_category", isAnnotationField = false)
public class JobCategory {
	private String job_id;
	private String job_name;
	public String getJob_id() {
		return job_id;
	}
	public void setJob_id(String job_id) {
		this.job_id = job_id;
	}
	public String getJob_name() {
		return job_name;
	}
	public void setJob_name(String job_name) {
		this.job_name = job_name;
	}
	
}
