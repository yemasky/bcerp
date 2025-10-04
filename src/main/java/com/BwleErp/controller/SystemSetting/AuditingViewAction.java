package com.BwleErp.controller.SystemSetting;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.controller.AbstractAction;
import com.base.model.entity.BwleErp.SystemSetting.Auditing;
import com.base.model.entity.BwleErp.SystemSetting.AuditingExamine;
import com.base.model.entity.BwleErp.SystemSetting.AuditingExamines;
import com.base.model.entity.BwleErp.SystemSetting.CurrencyRate;
import com.base.model.entity.BwleErp.SystemSetting.company.CompanySector;
import com.base.model.entity.BwleErp.SystemSetting.module.Modules;
import com.base.model.vo.BwleErp.SystemSetting.AuditingVo;
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
public class AuditingViewAction extends AbstractAction {
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
		case "commitAuditing":
			this.doCommitAuditing(request, response);
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
		String channel = request.getParameter("c");
		int module_id = EncryptUtiliy.instance().intIDDecrypt(channel);
		WhereRelation whereRelation = new WhereRelation();//取得所有员工
		whereRelation.EQ("auditing_valid", 1).EQ("module_id", module_id).setTable_clazz(Auditing.class);
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
		whereRelation = new WhereRelation();
		whereRelation.EQ("sector_valid", 1).setTable_clazz(CompanySector.class);
		List<CompanySector> companySectorList = this.generalService.getEntityList(whereRelation);
		
		this.success.setItem("companySectorList", companySectorList);
		this.success.setItem("auditingList", auditingVoList);
	}
	//遞交審核
	public void doCommitAuditing(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		String channel = request.getParameter("c");
		String auditingStatus = request.getParameter("status");
		Object _auditing_id = request.getAttribute("auditing_id");
		int update_id  = 0;
		if(!Utility.instance().isNumber(id)) {
			update_id = EncryptUtiliy.instance().intIDDecrypt(id);
		} else {
			update_id = Integer.parseInt(id);
		}
		int auditing_id  = 0;
		if(!Utility.instance().isNumber(_auditing_id+"")) {
			auditing_id = EncryptUtiliy.instance().intIDDecrypt(_auditing_id+"");
		} else {
			auditing_id = Integer.parseInt(_auditing_id+"");
		}
		int module_id = EncryptUtiliy.instance().intIDDecrypt(channel);
		WhereRelation whereRelation = new WhereRelation();//取得所有员工
		whereRelation.EQ("module_id", module_id).setTable_clazz(Modules.class);
		Modules module = (Modules) this.generalService.getEntity(whereRelation);
		String view = module.getModule_view();
		Method execute = AuditingExaminesAction.class.getMethod(view, Integer.class, String.class);
		//
		whereRelation = new WhereRelation();//取得所有员工
		whereRelation.EQ("auditing_valid", 1).EQ("auditing_id", auditing_id).setTable_clazz(Auditing.class);
		Auditing auditing = (Auditing) this.generalService.getEntity(whereRelation);
		Type type = new TypeToken<HashMap<Integer, AuditingExamine>>(){}.getType();
		HashMap<Integer, AuditingExamine> examineMap = new Gson().fromJson(auditing.getExamine(), type);
		System.out.println(new Gson().toJson(examineMap));
		//
		this.generalService.setTransaction(true);
		execute.invoke(new AuditingExaminesAction(this.generalService), update_id, auditingStatus);//更新狀態
		
		//插入工作流待審核數據
		
		List<AuditingExamines> auditingExaminesList = new ArrayList<>();
		for(Integer key : examineMap.keySet()) {
			AuditingExamines auditingExamines = new AuditingExamines();
			AuditingExamine examine = examineMap.get(key);
			auditingExamines.setAdd_datetime(Utility.instance().getTodayDate());
			auditingExamines.setAgent(examine.getAgent());
			auditingExamines.setAudit_employee_id(examine.getEmployee_id());
			auditingExamines.setAudit_step(examine.getStep());
			auditingExamines.setAuditing_id(auditing.getAuditing_id());
			auditingExamines.setAuditing_state(0);//剛開始插入初始狀態是 0 未遞交審核
			auditingExamines.setEmployee_id((Integer) request.getAttribute("employee_id"));
			auditingExamines.setPosition_id(examine.getPosition_id());
			auditingExamines.setSector_id(examine.getSector_id());
			auditingExaminesList.add(auditingExamines);
		}
		this.generalService.batchSave(auditingExaminesList);
		//
		this.generalService.commit();
		//this.success.setItem("auditing_id", edit_id);
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
