package com.BwleErp.controller.SystemSetting;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.controller.AbstractAction;
import com.base.model.entity.BwleErp.SystemSetting.CountryCity;
import com.base.model.vo.BwleErp.SystemSetting.CountryCityVo;
import com.base.service.GeneralService;
import com.base.type.CheckedStatus;
import com.base.util.EncryptUtiliy;

import core.jdbc.mysql.NeedEncrypt;
import core.jdbc.mysql.WhereRelation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ProcurementAction extends AbstractAction {
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
		case "getCountryCity":
			this.doGetCountryCity(request, response);
			break;
		case "saveCountryCity":
			this.doSaveCountryCity(request, response);
			break;
		case "deleteCountryCity":

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

	public void doGetCountryCity(HttpServletRequest request, HttpServletResponse response) throws Exception {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.ORDER_ASC("city_father_id").ORDER_ASC("city_id").setTable_clazz(CountryCityVo.class);
		NeedEncrypt needEncrypt = new NeedEncrypt();
		//needEncrypt.setNeedEncrypt(true);
		//needEncrypt.setNeedEncrypt("city_id", NeedEncrypt._ENCRYPT);
		List<HashMap<String, Object>> countryCityVoList = this.generalService.getList(whereRelation, needEncrypt);
		//
		this.success.setItem("cityList", countryCityVoList);
	}
	
	public void doSaveCountryCity(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String edit_id = request.getParameter("edit_id");
		int city_id = 0;
		if(edit_id != null && !edit_id.equals("") && !edit_id.equals("undefined") && !edit_id.equals("0")) {
			city_id = EncryptUtiliy.instance().intIDDecrypt(edit_id);
		}
		CountryCity city = this.modelMapper.map(request.getAttribute("city"), CountryCity.class);	
		if(city_id > 0) {//update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("city_id", city_id).setTable_clazz(CountryCity.class);
			generalService.updateEntity(city, whereRelation);
		} else {
			city.setCity_id(null);
			int id = generalService.save(city);
			edit_id = EncryptUtiliy.instance().intIDEncrypt(id);
		}
		this.success.setItem("auditing_id", edit_id);
	}
	
	public void doDeleteAuditing(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String delete_id = (String) request.getAttribute("delete_id");
		int city_id = 0;
		if(delete_id != null && !delete_id.equals("") && !delete_id.equals("undefined") && !delete_id.equals("0")) {
			city_id = EncryptUtiliy.instance().intIDDecrypt(delete_id);
		}
		if(city_id > 0) {//update
			
		}
	}
	
}
