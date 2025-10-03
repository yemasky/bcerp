package com.BwleErp.controller.SystemSetting;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.controller.AbstractAction;
import com.base.model.entity.BwleErp.SystemSetting.Category;
import com.base.model.entity.BwleErp.SystemSetting.CategoryClassify;
import com.base.model.entity.BwleErp.SystemSetting.CategoryUnit;
import com.base.model.entity.BwleErp.SystemSetting.VehicleModel;
import com.base.model.vo.BwleErp.SystemSetting.CategoryUnitVo;
import com.base.model.vo.BwleErp.SystemSetting.VehicleModelVo;
import com.base.service.GeneralService;
import com.base.type.CheckedStatus;
import com.base.util.EncryptUtiliy;

import core.jdbc.mysql.NeedEncrypt;
import core.jdbc.mysql.WhereRelation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CategoryUtilAction extends AbstractAction {
	@Autowired
	private GeneralService generalService;
	@Override
	public CheckedStatus check(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return this.status;
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String method = (String) request.getAttribute("method");
		if (method == null)
			method = "";
		//
		switch (method) {
		case "getUnit":
			this.doGetUnit(request, response);
			break;
		case "saveUnit":
			this.doSaveUnit(request, response);
			break;
		case "deleteUnit":
			this.doDeleteUnit(request, response);
			break;
		case "getVehicleModel":
			this.doGetVehicleModel(request, response);
			break;
		case "saveVehicleModel":
			this.doSaveVehicleModel(request, response);
			break;
		case "deleteVehicleModel":
			this.doDeleteVehicleModel(request, response);
			break;
		default:
			this.doDefault(request, response);
			break;
		}
	}

	@Override
	public void release(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		generalService.closeConnection();
	}

	@Override
	public void rollback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub

	}

	public void doGetUnit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ("unit_valid", 1).setTable_clazz(CategoryUnitVo.class);
		NeedEncrypt needEncrypt = new NeedEncrypt();
		needEncrypt.setNeedEncrypt(true);
		needEncrypt.setNeedEncrypt("unit_id", NeedEncrypt._ENCRYPT);
		List<HashMap<String, Object>> unitList = this.generalService.getList(whereRelation, needEncrypt);
		//
		this.success.setItem("unitList", unitList);
	}
	
	public void doSaveUnit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String edit_id = request.getParameter("edit_id");
		int unit_id = 0;
		if(edit_id != null && !edit_id.equals("") && !edit_id.equals("undefined") && !edit_id.equals("0")) {
			unit_id = EncryptUtiliy.instance().intIDDecrypt(edit_id);
		}
		CategoryUnitVo categoryUnitVo = this.modelMapper.map(request.getAttribute("unit"), CategoryUnitVo.class);
		categoryUnitVo.setUnit_id(null);
		if(unit_id > 0) {//update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("unit_id", unit_id).setTable_clazz(CategoryUnit.class);
			generalService.updateEntity(categoryUnitVo, whereRelation);
		} else {
			int id = generalService.save(categoryUnitVo);
			edit_id = EncryptUtiliy.instance().intIDEncrypt(id);
		}
		this.success.setItem("unit_id", edit_id);
	}
	
	public void doDeleteUnit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String delete_id = (String) request.getAttribute("delete_id");
		int unit_id = 0;
		if(delete_id != null && !delete_id.equals("") && !delete_id.equals("undefined") && !delete_id.equals("0")) {
			unit_id = EncryptUtiliy.instance().intIDDecrypt(delete_id);
		}
		if(unit_id > 0) {//update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("unit_id", unit_id).setUpdate("unit_valid", 0).setTable_clazz(CategoryUnit.class);
			generalService.update(whereRelation);
		}
	}
	///////////////////////
	public void doGetVehicleModel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ("vehicle_valid", 1).setTable_clazz(VehicleModelVo.class);
		NeedEncrypt needEncrypt = new NeedEncrypt();
		needEncrypt.setNeedEncrypt(true);
		needEncrypt.setNeedEncrypt("vehicle_model_id", NeedEncrypt._ENCRYPT);
		List<HashMap<String, Object>> vehicleModelList = this.generalService.getList(whereRelation, needEncrypt);
		//
		whereRelation = new WhereRelation();
		whereRelation.EQ("classify_valid", 1).setTable_clazz(CategoryClassify.class);
		needEncrypt = new NeedEncrypt();
		List<HashMap<String, Object>> classifyList = this.generalService.getList(whereRelation, needEncrypt);
		//
		this.success.setItem("vehicleModelList", vehicleModelList);
		this.success.setItem("classifyList", classifyList);
	}
	
	public void doSaveVehicleModel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String edit_id = request.getParameter("edit_id");
		int vehicle_model_id = 0;
		if(edit_id != null && !edit_id.equals("") && !edit_id.equals("undefined") && !edit_id.equals("0")) {
			vehicle_model_id = EncryptUtiliy.instance().intIDDecrypt(edit_id);
		}
		VehicleModelVo vehicleModelVo = this.modelMapper.map(request.getAttribute("vehicleModel"), VehicleModelVo.class);
		vehicleModelVo.setVehicle_model_id(null);
		LocalDate today = LocalDate.now();
        // 转换为java.sql.Date
        Date sqlDate = Date.valueOf(today);
		vehicleModelVo.setVehicle_add_date(sqlDate);
		if(vehicle_model_id > 0) {//update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("vehicle_model_id", vehicle_model_id).setTable_clazz(Category.class);
			generalService.updateEntity(vehicleModelVo, whereRelation);
		} else {
			int id = generalService.save(vehicleModelVo);
			edit_id = EncryptUtiliy.instance().intIDEncrypt(id);
		}
		this.success.setItem("vehicle_model_id", edit_id);
	}
	
	public void doDeleteVehicleModel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String delete_id = (String) request.getAttribute("delete_id");
		int vehicle_model_id = 0;
		if(delete_id != null && !delete_id.equals("") && !delete_id.equals("undefined") && !delete_id.equals("0")) {
			vehicle_model_id = EncryptUtiliy.instance().intIDDecrypt(delete_id);
		}
		if(vehicle_model_id > 0) {//update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("vehicle_model_id", vehicle_model_id).setUpdate("systype_valid", 0).setTable_clazz(VehicleModel.class);
			generalService.update(whereRelation);
		}
	}
	////////////////
}
