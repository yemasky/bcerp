package com.base.model.vo.BwleErp;

import com.base.model.entity.BwleErp.BaseAbstract.VehicleModelBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "category_vehicle_model", isAnnotationField = true)
public class VehicleModelVo extends VehicleModelBaseAbstract {
	@Column(name = "vehicle_model_id", primary_key = true, auto_increment = true)
	private String vehicle_model_id;

	public String getVehicle_model_id() {
		return vehicle_model_id;
	}

	public void setVehicle_model_id(String vehicle_model_id) {
		this.vehicle_model_id = vehicle_model_id;
	}

}
