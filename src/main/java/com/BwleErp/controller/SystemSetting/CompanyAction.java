package com.BwleErp.controller.SystemSetting;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.controller.AbstractAction;
import com.base.model.entity.BwleErp.SystemSetting.company.Bank;
import com.base.model.entity.BwleErp.SystemSetting.company.Company;
import com.base.model.vo.BwleErp.SystemSetting.CompanyVo;
import com.base.service.GeneralService;
import com.base.type.CheckedStatus;
import com.base.util.EncryptUtiliy;

import core.jdbc.mysql.NeedEncrypt;
import core.jdbc.mysql.WhereRelation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/*
 * 公司 作者 CooC email yemasky@msn.com
 */
@Component
public class CompanyAction extends AbstractAction {
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
		case "getCompany":
			this.doGetCompany(request, response);
			break;
		case "saveCompany":
			this.doSaveCompany(request, response);
			break;
		case "saveBank":
			this.doSaveBank(request, response);
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

	public void doGetCompany(HttpServletRequest request, HttpServletResponse response) throws Exception {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.setTable_clazz(CompanyVo.class);
		NeedEncrypt needEncrypt = new NeedEncrypt();
		needEncrypt.setNeedEncrypt(true);
		needEncrypt.setNeedEncrypt("company_id", NeedEncrypt._ENCRYPT);
		List<HashMap<String, Object>> companyList = this.generalService.getList(whereRelation, needEncrypt);
		//
		whereRelation = new WhereRelation();
		whereRelation.setTable_clazz(Bank.class);
		List<Bank> bankList = generalService.getEntityList(whereRelation);
		//
		this.success.setItem("companyList", companyList);
		this.success.setItem("bankList", bankList);
	}
	
	public void doSaveCompany(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String company_edit_id = request.getParameter("company_edit_id");
		int company_id = 0;
		if(company_edit_id != null && !company_edit_id.equals("") && !company_edit_id.equals("undefined")) {
			System.out.println("====>"+company_edit_id);
			company_id = EncryptUtiliy.instance().intIDDecrypt(company_edit_id);
		}
		CompanyVo companyVo = this.modelMapper.map(request.getAttribute("company"), CompanyVo.class);
		companyVo.setCompany_id(null);
		if(company_id > 0) {//update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("company_id", company_id).setTable_clazz(Company.class);
			generalService.updateEntity(companyVo, whereRelation);
		} else {
			generalService.save(companyVo);
		}
	}
	
	public void doSaveBank(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String _edit_id = request.getParameter("edit_id");
		int edit = 0;
		if(_edit_id != null && !_edit_id.equals("") && !_edit_id.equals("undefined") && !_edit_id.equals("null")) {
			edit = Integer.parseInt(_edit_id);
		}
		// TODO Auto-generated method stub
		Bank bank = this.modelMapper.map(request.getAttribute("bank"), Bank.class);
		WhereRelation whereRelation = new WhereRelation();
		if(edit > 0) {//update
			whereRelation.EQ("bank_id", bank.getBank_id()).setTable_clazz(Bank.class);
			generalService.updateEntity(bank, whereRelation);
		} else {
			generalService.save(bank);
		}
		//
	}

}
