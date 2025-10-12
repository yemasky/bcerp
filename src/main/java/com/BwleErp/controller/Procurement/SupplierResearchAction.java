package com.BwleErp.controller.Procurement;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.controller.AbstractAction;
import com.base.model.entity.BwleErp.Procurement.SupplierResearch;
import com.base.model.entity.BwleErp.Procurement.BaseAbstract.RsupplierContact;
import com.base.model.entity.BwleErp.Procurement.BaseAbstract.RsupplierDetection;
import com.base.model.entity.BwleErp.Procurement.BaseAbstract.RsupplierPlant;
import com.base.model.entity.BwleErp.Procurement.BaseAbstract.RsupplierProduct;
import com.base.model.entity.BwleErp.Procurement.BaseAbstract.RsupplierUpload;
import com.base.model.entity.BwleErp.SystemSetting.MarketingCollection;
import com.base.model.vo.BwleErp.Procurement.SupplierResearchVo;
import com.base.service.GeneralService;
import com.base.type.CheckedStatus;
import com.base.util.EncryptUtiliy;
import com.base.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import core.jdbc.mysql.WhereRelation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SupplierResearchAction extends AbstractAction {
	@Autowired
	private GeneralService generalService;

	@Override
	public CheckedStatus check(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return this.status;
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = (String) request.getAttribute("method");
		if (method == null)
			method = "";
		//
		switch (method) {
		case "getResearch"://
			this.doGetResearch(request, response);
			break;
		case "saveResearch"://
			this.doSaveResearch(request, response);
			break;
		case "deleteResearch"://
			this.doDeleteSalesPayment(request, response);
			break;
		default:
			this.doDefault(request, response);
			break;
		}
	}

	@Override
	public void release(HttpServletRequest request, HttpServletResponse response) throws Exception {
		

	}

	@Override
	public void rollback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		

	}

	public void doGetResearch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.setTable_clazz(SupplierResearch.class);
		
		List<SupplierResearch> researchList = generalService.getEntityList(whereRelation);
		List<SupplierResearchVo> rsupplierList = new ArrayList<>();
		Type typeRC = new TypeToken<HashMap<String, RsupplierContact>>() {}.getType();
		Type typeRP = new TypeToken<HashMap<String, RsupplierProduct>>() {}.getType();
		Type typeOB = new TypeToken<List<Object>>() {}.getType();
		Type typeRPT = new TypeToken<HashMap<String, RsupplierPlant>>() {}.getType();
		Type typeRD = new TypeToken<HashMap<String, RsupplierDetection>>() {}.getType();
		Type typeRU = new TypeToken<HashMap<String, RsupplierUpload>>() {}.getType();
		Gson gson = new Gson();
		for(int i = 0; i < researchList.size(); i++) {
			SupplierResearch research = researchList.get(i);
			SupplierResearchVo rsupplier = new SupplierResearchVo();
			BeanUtils.copyProperties(research, rsupplier);
			rsupplier.setRsupplier_id(research.getRsupplier_id()+"");
			HashMap<String, RsupplierContact> contactList = gson.fromJson(research.getRsupplier_contact(), typeRC);
			rsupplier.setRsupplier_contact(contactList);
			HashMap<String, RsupplierProduct> productList = gson.fromJson(research.getRsupplier_product(), typeRP);
			rsupplier.setRsupplier_product(productList);
			List<Object> vehicle_type =  gson.fromJson(research.getVehicle_type(), typeOB);
			rsupplier.setVehicle_type(vehicle_type);
			HashMap<String ,RsupplierPlant> plantList = gson.fromJson(research.getRsupplier_plant(), typeRPT);
			rsupplier.setRsupplier_plant(plantList);
			HashMap<String ,RsupplierDetection> detectionList = gson.fromJson(research.getRsupplier_plant(), typeRD);
			rsupplier.setRsupplier_detection(detectionList);
			List<Object> tech_dev =  gson.fromJson(research.getTech_dev(), typeOB);
			rsupplier.setTech_dev(tech_dev);
			HashMap<String ,RsupplierUpload> uploadList = gson.fromJson(research.getRsupplier_upload(), typeRU);
			rsupplier.setRsupplier_upload(uploadList);
			rsupplierList.add(rsupplier);
		}
		
		this.success.setItem("rsupplierList", rsupplierList);
	}

	public void doSaveResearch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String _edit_id = request.getParameter("edit_id");
		int rsupplier_id = 0;
		if (_edit_id != null && !_edit_id.equals("") && !_edit_id.equals("undefined") && !_edit_id.equals("null")) {
			rsupplier_id = Integer.parseInt(_edit_id);
		}
		SupplierResearchVo rsupplierVo = this.modelMapper.map(request.getAttribute("rsupplier"), SupplierResearchVo.class);
		Gson gson = new Gson();
		//System.out.println("===>"+gson.toJson(rsupplierVo));
		
		SupplierResearch rsupplier = new SupplierResearch();
		BeanUtils.copyProperties(rsupplierVo, rsupplier);
		
		rsupplier.setRsupplier_contact(gson.toJson(rsupplierVo.getRsupplier_contact()));
		rsupplier.setRsupplier_product(gson.toJson(rsupplierVo.getRsupplier_product()));
		rsupplier.setVehicle_type(gson.toJson(rsupplierVo.getVehicle_type()));
		rsupplier.setRsupplier_plant(gson.toJson(rsupplierVo.getRsupplier_plant()));
		rsupplier.setRsupplier_detection(gson.toJson(rsupplierVo.getRsupplier_detection()));
		rsupplier.setTech_dev(gson.toJson(rsupplierVo.getTech_dev()));
		rsupplier.setRsupplier_upload(gson.toJson(rsupplierVo.getRsupplier_upload()));
		
		WhereRelation whereRelation = new WhereRelation();
		if (rsupplier_id > 0) {// update
			whereRelation.EQ("rsupplier_id", rsupplier_id).setTable_clazz(MarketingCollection.class);
			generalService.updateEntity(rsupplier, whereRelation);
		} else {
			rsupplier.setEmployee_id((Integer) request.getAttribute("employee_id"));
			rsupplier.setRsupplier_add_datetime(Utility.instance().getTodayDate());
			rsupplier_id = generalService.save(rsupplier);
		}
		this.success.setItem("rsupplier_id", rsupplier_id);
		//
	}

	public void doDeleteSalesPayment(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String delete_id = (String) request.getAttribute("delete_id");
		int collection_id = 0;
		if (delete_id != null && !delete_id.equals("") && !delete_id.equals("undefined") && !delete_id.equals("0")) {
			collection_id = EncryptUtiliy.instance().intIDDecrypt(delete_id);
		}
		if (collection_id > 0) {// update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("collection_id", collection_id).setUpdate("collection_valid", 0)
					.setTable_clazz(MarketingCollection.class);
			generalService.update(whereRelation);
		}
	}

}
