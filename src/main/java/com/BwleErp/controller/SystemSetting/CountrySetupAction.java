package com.BwleErp.controller.SystemSetting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.controller.AbstractAction;
import com.base.model.entity.BwleErp.SystemSetting.Auditing;
import com.base.model.entity.BwleErp.SystemSetting.Country;
import com.base.model.entity.BwleErp.SystemSetting.Seaport;
import com.base.service.GeneralService;
import com.base.type.CheckedStatus;
import com.base.util.EncryptUtiliy;
import com.google.gson.Gson;

import core.jdbc.mysql.WhereRelation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CountrySetupAction extends AbstractAction {
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
		case "getCountry":
			this.doGetCountry(request, response);
			break;
		case "saveCountry":
			this.doSaveCountry(request, response);
			break;
		case "getSeaport"://海港
			this.doGetSeaport(request, response);
			break;
		case "saveSeaport"://海港
			this.doSaveSeaport(request, response);
			break;	
		case "getCurrencyRate"://
			this.doGetCurrencyRate(request, response);
			break;
		case "saveCurrencyRate"://
			this.doSaveCurrencyRate(request, response);
			break;	
		case "deleteCurrencyRate"://
			this.doDeleteCurrencyRate(request, response);
			break;	
		default:
			this.doDefault(request, response);
			break;
		}
	}

	@Override
	public void release(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rollback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void doGetCountry(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.setTable_clazz(Country.class);
		List<Country> countryList = generalService.getEntityList(whereRelation);
		this.success.setItem("countryList", countryList);
	}
	
	public void doSaveCountry(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String _edit_id = request.getParameter("edit_id");
		int edit = 0;
		if(_edit_id != null && !_edit_id.equals("") && !_edit_id.equals("undefined") && !_edit_id.equals("null")) {
			edit = Integer.parseInt(_edit_id);
		}
		// TODO Auto-generated method stub
		Country country = this.modelMapper.map(request.getAttribute("country"), Country.class);
		System.out.println(new Gson().toJson(country));
		WhereRelation whereRelation = new WhereRelation();
		if(edit > 0) {//update
			whereRelation.EQ("country_id", country.getCountry_id()).setTable_clazz(Country.class);
			generalService.updateEntity(country, whereRelation);
		} else {
			generalService.save(country);
		}
		//
		whereRelation = new WhereRelation();
		whereRelation.setTable_clazz(Country.class);
		List<Country> countryList = generalService.getEntityList(whereRelation);
		this.success.setItem("countryList", countryList);
	}
	
	public void doGetSeaport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		//
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.ORDER_ASC("country_enname").setTable_clazz(Country.class);
		List<Country> countryList = generalService.getEntityList(whereRelation);
		//
		whereRelation = new WhereRelation();
		whereRelation.setTable_clazz(Seaport.class);
		List<Seaport> seaportList = generalService.getEntityList(whereRelation);
		this.success.setItem("seaportList", seaportList);
		this.success.setItem("countryList", countryList);
	}
	
	public void doSaveSeaport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String _edit_id = request.getParameter("edit_id");
		int edit = 0;
		if(_edit_id != null && !_edit_id.equals("") && !_edit_id.equals("undefined") && !_edit_id.equals("null")) {
			edit = Integer.parseInt(_edit_id);
		}
		// TODO Auto-generated method stub
		Seaport seaport = this.modelMapper.map(request.getAttribute("seaport"), Seaport.class);
		WhereRelation whereRelation = new WhereRelation();
		if(edit > 0) {//update
			whereRelation.EQ("country_id", seaport.getSeaport_id()).setTable_clazz(Country.class);
			generalService.updateEntity(seaport, whereRelation);
		} else {
			generalService.save(seaport);
		}
		//
	}
	
	public void doGetCurrencyRate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		//
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.ORDER_ASC("country_enname").setTable_clazz(Country.class);
		List<Country> countryList = generalService.getEntityList(whereRelation);
		//
		whereRelation = new WhereRelation();
		whereRelation.setTable_clazz(Seaport.class);
		List<Seaport> seaportList = generalService.getEntityList(whereRelation);
		this.success.setItem("seaportList", seaportList);
		this.success.setItem("countryList", countryList);
	}
	
	public void doSaveCurrencyRate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String _edit_id = request.getParameter("edit_id");
		int edit = 0;
		if(_edit_id != null && !_edit_id.equals("") && !_edit_id.equals("undefined") && !_edit_id.equals("null")) {
			edit = Integer.parseInt(_edit_id);
		}
		// TODO Auto-generated method stub
		Seaport seaport = this.modelMapper.map(request.getAttribute("seaport"), Seaport.class);
		WhereRelation whereRelation = new WhereRelation();
		if(edit > 0) {//update
			whereRelation.EQ("country_id", seaport.getSeaport_id()).setTable_clazz(Country.class);
			generalService.updateEntity(seaport, whereRelation);
		} else {
			generalService.save(seaport);
		}
		//
	}
	
	public void doDeleteCurrencyRate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String delete_id = (String) request.getAttribute("delete_id");
		int auditing_id = 0;
		if(delete_id != null && !delete_id.equals("") && !delete_id.equals("undefined") && !delete_id.equals("0")) {
			auditing_id = EncryptUtiliy.instance().intIDDecrypt(delete_id);
		}
		if(auditing_id > 0) {//update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("auditing_id", auditing_id).setUpdate("auditing_valid", 0).setTable_clazz(Auditing.class);
			generalService.update(whereRelation);
		}
	}

}
