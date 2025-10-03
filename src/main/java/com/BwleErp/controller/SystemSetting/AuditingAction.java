package com.BwleErp.controller.SystemSetting;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.controller.AbstractAction;
import com.base.model.entity.BwleErp.SystemSetting.Auditing;
import com.base.model.entity.BwleErp.SystemSetting.AuditingModule;
import com.base.model.entity.BwleErp.SystemSetting.BaseAbstract.AuditingExamine;
import com.base.model.entity.BwleErp.SystemSetting.company.CompanySector;
import com.base.model.entity.BwleErp.SystemSetting.employee.EmployeeSector;
import com.base.model.entity.BwleErp.SystemSetting.module.Modules;
import com.base.model.vo.BwleErp.SystemSetting.AuditingVo;
import com.base.service.GeneralService;
import com.base.type.CheckedStatus;
import com.base.util.EncryptUtiliy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import core.jdbc.mysql.NeedEncrypt;
import core.jdbc.mysql.WhereRelation;
import core.util.Utiliy;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuditingAction extends AbstractAction {
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
		case "getAuditing":
			this.doGetAuditing(request, response);
			break;
		case "saveAuditing":
			this.doSaveAuditing(request, response);
			break;
		case "deleteAuditing":
			this.doDeleteAuditing(request, response);
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

	public void doGetAuditing(HttpServletRequest request, HttpServletResponse response) throws Exception {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ("sector_valid", 1).setTable_clazz(CompanySector.class);
		List<CompanySector> companySectorList = this.generalService.getEntityList(whereRelation);
		//
		whereRelation = new WhereRelation();
		whereRelation.setTable_clazz(AuditingModule.class);
		List<AuditingModule> auditingModuleList = this.generalService.getEntityList(whereRelation);
		List<Integer> module_idsList = new ArrayList<>();
		for(AuditingModule AM : auditingModuleList) {
			module_idsList.add(AM.getModule_id());
		}
		whereRelation = new WhereRelation();
		whereRelation.setTable_clazz(Modules.class).IN("module_id", module_idsList).setField("module_id, module_name");
		auditingModuleList = this.generalService.getEntityList(whereRelation);
		//
		whereRelation = new WhereRelation();//取得所有员工
		whereRelation.EQ("is_system", 0).EQ("employee_valid", 1).setField("position_id,sector_id,employee_id,employee_name").setTable_clazz(EmployeeSector.class);
		List<HashMap<String, Object>> employeeList = this.generalService.getList(whereRelation, new NeedEncrypt());
		//
		whereRelation = new WhereRelation();//取得所有员工
		whereRelation.EQ("auditing_valid", 1).setTable_clazz(Auditing.class);
		List<Auditing> auditingList = this.generalService.getEntityList(whereRelation);
		List<AuditingVo> auditingVoList = new ArrayList<>();
		Type type = new TypeToken<HashMap<Integer, AuditingExamine>>(){}.getType();
		for(Auditing auditing : auditingList) {
			AuditingVo auditingVo = new AuditingVo();
			BeanUtils.copyProperties(auditing, auditingVo);
			auditingVo.setAuditing_id(EncryptUtiliy.instance().intIDEncrypt(auditing.getAuditing_id()));
			//Type type = new TypeToken<List<AuditingExamine>>(){}.getType();
			HashMap<Integer, AuditingExamine> examineList = new Gson().fromJson(auditing.getExamine(), type);
			//auditingVoList.add(auditing);
			//AuditingExamine[] auditingExamine = new Gson().fromJson(auditing.getExamine(), AuditingExamine[].class);
			//List<AuditingExamine> auditingExamineList = Arrays.asList(auditingExamine);
			auditingVo.setExamine(examineList);
			auditingVoList.add(auditingVo);
		}
		//
		this.success.setItem("companySectorList", companySectorList); 
		this.success.setItem("auditingModuleList", auditingModuleList);
		this.success.setItem("employeeList", employeeList);
		//this.success.setItem("auditingList", auditingList);
		this.success.setItem("auditingVoList", auditingVoList);
	}
	
	public void doSaveAuditing(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String edit_id = request.getParameter("edit_id");
		int auditing_id = 0;
		if(edit_id != null && !edit_id.equals("") && !edit_id.equals("undefined") && !edit_id.equals("0")) {
			auditing_id = EncryptUtiliy.instance().intIDDecrypt(edit_id);
		}
		AuditingVo auditingVo = this.modelMapper.map(request.getAttribute("auditing"), AuditingVo.class);
		Auditing auditing = new Auditing();
		BeanUtils.copyProperties(auditingVo, auditing);
		auditing.setExamine(new Gson().toJson(auditingVo.getExamine()));
		System.out.println("====>"+new Gson().toJson(auditingVo.getExamine()));
		if(auditing_id > 0) {//update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("auditing_id", auditing_id).setTable_clazz(Auditing.class);
			generalService.updateEntity(auditing, whereRelation);
		} else {
			auditing.setAdd_datetime(Utiliy.instance().getTodayDate());
			int id = generalService.save(auditing);
			edit_id = EncryptUtiliy.instance().intIDEncrypt(id);
		}
		this.success.setItem("auditing_id", edit_id);
	}
	
	public void doDeleteAuditing(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
