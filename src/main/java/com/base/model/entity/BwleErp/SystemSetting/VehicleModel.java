package com.base.model.entity.BwleErp.SystemSetting;

import com.base.model.entity.BwleErp.SystemSetting.BaseAbstract.VehicleModelBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "category_vehicle_model", isAnnotationField = true)
public class VehicleModel extends VehicleModelBaseAbstract {
	@Column(name = "vehicle_model_id", primary_key = true, auto_increment = true)
	private Integer vehicle_model_id;

	public Integer getVehicle_model_id() {
		return vehicle_model_id;
	}

	public void setVehicle_model_id(Integer vehicle_model_id) {
		this.vehicle_model_id = vehicle_model_id;
	}

}
