package com.base.model.entity.example;

import core.custom_interface.Table;

@Table(name = "test", isAnnotationField = false)
public class Test {
	private Integer id;
	private String title;
	private String description;
	private String add_datetime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAdd_datetime() {
		return add_datetime;
	}
	public void setAdd_datetime(String add_datetime) {
		this.add_datetime = add_datetime;
	}
	
	
}
