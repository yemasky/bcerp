package com.BwleErp.controller.SystemSetting;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.controller.AbstractAction;
import com.base.model.entity.BwleErp.SystemSetting.Auditing;
import com.base.model.vo.BwleErp.SystemSetting.FinanceDepotVo;
import com.base.service.GeneralService;
import com.base.type.CheckedStatus;
import com.base.util.EncryptUtiliy;

import core.jdbc.mysql.NeedEncrypt;
import core.jdbc.mysql.WhereRelation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
		needEncrypt.setNeedEncrypt(true);
		needEncrypt.setNeedEncrypt("depot_id", NeedEncrypt._ENCRYPT);
		List<HashMap<String, Object>> depotList = this.generalService.getList(whereRelation, needEncrypt);
		//
		this.success.setItem("depotList", depotList);
	}
	
	public void doSaveDepot(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String edit_id = request.getParameter("edit_id");
		int depot_id = 0;
		if(edit_id != null && !edit_id.equals("") && !edit_id.equals("undefined") && !edit_id.equals("0")) {
			depot_id = EncryptUtiliy.instance().intIDDecrypt(edit_id);
		}
		FinanceDepotVo depotVo = this.modelMapper.map(request.getAttribute("depot"), FinanceDepotVo.class);
		if(depot_id > 0) {//update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("auditing_id", depot_id).setTable_clazz(Auditing.class);
			generalService.updateEntity(depotVo, whereRelation);
		} else {
			int id = generalService.save(depotVo);
			edit_id = EncryptUtiliy.instance().intIDEncrypt(id);
		}
		this.success.setItem("depot_id", edit_id);
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
