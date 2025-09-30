package com.base.model.entity.BwleErp.BaseAbstract;

import java.sql.Date;

public abstract class VehicleModelBaseAbstract {
	private Integer classify_id;
	private String vehicle_series;
	private String vehicle_model;
	private String vehicle_motor;
	private String vehicle_engine;
	private Date vehicle_begin_date;
	private Date vehicle_end_date;
	private Date vehicle_add_date;
	private Integer vehicle_valid;
	public Integer getClassify_id() {
		return classify_id;
	}
	public void setClassify_id(Integer classify_id) {
		this.classify_id = classify_id;
	}
	public String getVehicle_series() {
		return vehicle_series;
	}
	public void setVehicle_series(String vehicle_series) {
		this.vehicle_series = vehicle_series;
	}
	public String getVehicle_model() {
		return vehicle_model;
	}
	public void setVehicle_model(String vehicle_model) {
		this.vehicle_model = vehicle_model;
	}
	public String getVehicle_motor() {
		return vehicle_motor;
	}
	public void setVehicle_motor(String vehicle_motor) {
		this.vehicle_motor = vehicle_motor;
	}
	public String getVehicle_engine() {
		return vehicle_engine;
	}
	public void setVehicle_engine(String vehicle_engine) {
		this.vehicle_engine = vehicle_engine;
	}
	public Date getVehicle_begin_date() {
		return vehicle_begin_date;
	}
	public void setVehicle_begin_date(Date vehicle_begin_date) {
		this.vehicle_begin_date = vehicle_begin_date;
	}
	public Date getVehicle_end_date() {
		return vehicle_end_date;
	}
	public void setVehicle_end_date(Date vehicle_end_date) {
		this.vehicle_end_date = vehicle_end_date;
	}
	public Date getVehicle_add_date() {
		return vehicle_add_date;
	}
	public void setVehicle_add_date(Date vehicle_add_date) {
		this.vehicle_add_date = vehicle_add_date;
	}
	public Integer getVehicle_valid() {
		return vehicle_valid;
	}
	public void setVehicle_valid(Integer vehicle_valid) {
		this.vehicle_valid = vehicle_valid;
	}
	
}
