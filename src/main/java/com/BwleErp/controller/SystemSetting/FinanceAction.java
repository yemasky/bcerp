package com.BwleErp.controller.SystemSetting;

import java.util.HashMap;
import java.util.List;

import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.controller.AbstractAction;
import com.base.model.vo.BwleErp.SystemSetting.FinanceDepotVo;
import com.base.service.GeneralService;
import com.base.type.CheckedStatus;
import com.base.util.EncryptUtiliy;

import core.jdbc.mysql.NeedEncrypt;
import core.jdbc.mysql.WhereRelation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/*
 * 财务模块 作者 CooC email yemasky@msn.com
 */
@Component
public class FinanceAction extends AbstractAction {
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
		case "getDepot":
			this.doGetDepot(request, response);
			break;
		case "saveDepot":
			this.doSaveDepot(request, response);
			break;
		case "deleteDepot":
			this.doDeleteDepot(request, response);
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

	public void doGetDepot(HttpServletRequest request, HttpServletResponse response) throws Exception {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ("depot_valid", 1).setTable_clazz(FinanceDepotVo.class);
		NeedEncrypt needEncrypt = new NeedEncrypt();
		List<HashMap<String, Object>> depotList = this.generalService.getList(whereRelation, needEncrypt);
		//
		this.success.setItem("depotList", depotList);
	}
	
	public void doSaveDepot(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String edit_id = request.getParameter("edit_id");
		int depot_id = 0;
		if(edit_id != null && !edit_id.equals("") && !edit_id.equals("undefined") && !edit_id.equals("0")) {
			depot_id = Integer.parseInt(edit_id);
		}
		this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		FinanceDepotVo depotVo = this.modelMapper.map(request.getAttribute("depot"), FinanceDepotVo.class);
		Integer type = depotVo.getDepot_type();
		if(type == 1) {
			depotVo.setDepot_code(depotVo.getDepot_temp_code());
		}
		if(depot_id > 0) {//update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("auditing_id", depot_id).setTable_clazz(FinanceDepotVo.class);
			generalService.updateEntity(depotVo, whereRelation);
		} else {
			if(depotVo.getDepot_father_id() == null) {
				depotVo.setDepot_father_id(0);
			}
			depot_id = generalService.save(depotVo);
		}
		this.success.setItem("depot_id", depot_id);
	}
	
	public void doDeleteDepot(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String delete_id = (String) request.getAttribute("delete_id");
		int depot_id = 0;
		if(delete_id != null && !delete_id.equals("") && !delete_id.equals("undefined") && !delete_id.equals("0")) {
			depot_id = EncryptUtiliy.instance().intIDDecrypt(delete_id);
		}
		if(depot_id > 0) {//update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("depot_id", depot_id).setUpdate("depot_valid", 0).setTable_clazz(FinanceDepotVo.class);
			generalService.update(whereRelation);
		}
	}
	
}
