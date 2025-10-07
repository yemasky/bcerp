package com.BwleErp.controller.SystemSetting;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.controller.AbstractAction;
import com.base.model.dto.BwleErp.AuditingExamine;
import com.base.model.dto.BwleErp.AuditingExaminesDTO;
import com.base.model.entity.BwleErp.SystemSetting.Auditing;
import com.base.model.entity.BwleErp.SystemSetting.AuditingExamines;
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
/*
 * 工作流操作 作者 CooC email yemasky@msn.com
 */
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
		case "verifyAuditing":
			this.doVerifyAuditing(request, response);
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
	}

	@Override
	public void rollback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub

	}

	public void doGetAuditing(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");//提出工作流的module的表的id
		int link_id  = 0;//提出工作流的module的表的id
		if(!Utility.instance().isNumber(id) && id.length() > 8) {
			link_id = EncryptUtiliy.instance().intIDDecrypt(id);
		} else {
			link_id = Integer.parseInt(id);
		}
		String channel = request.getParameter("c");
		int module_id = EncryptUtiliy.instance().intIDDecrypt(channel);
		WhereRelation whereRelation = new WhereRelation();//
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
		//获取审核状态
		whereRelation = new WhereRelation();
		whereRelation.EQ("link_id", link_id).EQ("module_id", module_id).setTable_clazz(AuditingExamines.class);
		List<AuditingExamines> examinesList = this.generalService.getEntityList(whereRelation);
		//
		this.success.setItem("companySectorList", companySectorList);
		this.success.setItem("auditingList", auditingVoList);
		this.success.setItem("examinesList", examinesList);
	}
	//遞交審核
	public void doCommitAuditing(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");//提出工作流的module的表的id
		String channel = request.getParameter("c");
		String auditingStatus = request.getParameter("status");
		Object _auditing_id = request.getAttribute("auditing_id");
		int update_id  = 0;//提出工作流的module的表的id
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
		Method executeExamines = AuditingExaminesAction.class.getMethod(view, AuditingExaminesDTO.class);
		//
		whereRelation = new WhereRelation();//
		whereRelation.EQ("auditing_valid", 1).EQ("auditing_id", auditing_id).setTable_clazz(Auditing.class);
		Auditing auditing = (Auditing) this.generalService.getEntity(whereRelation);
		Type type = new TypeToken<HashMap<Integer, AuditingExamine>>(){}.getType();
		HashMap<Integer, AuditingExamine> examineMap = new Gson().fromJson(auditing.getExamine(), type);
		System.out.println(new Gson().toJson(examineMap));
		//
		AuditingExaminesDTO update = new AuditingExaminesDTO();
		update.setUpdate_id(update_id);
		update.setAuditing_id(auditing_id);
		update.setStatus(auditingStatus);
		update.setAuditing_date(Utility.instance().getTodayDate());
		update.setAuditing_employee_id((Integer) request.getAttribute("employee_id"));
		//开始事务
		this.generalService.setTransaction(true);
		executeExamines.invoke(new AuditingExaminesAction(this.generalService), update);//更新狀態
		
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
			auditingExamines.setEmployee_id((Integer) request.getAttribute("employee_id"));
			auditingExamines.setPosition_id(examine.getPosition_id());
			auditingExamines.setSector_id(examine.getSector_id());
			auditingExamines.setModule_id(module_id);
			auditingExamines.setLink_id(update_id);
			auditingExamines.setAdd_datetime(Utility.instance().getTodayDate());
			auditingExamines.setAudit_state(0);//审核状态 0未流转未审核 1已流转未审核 2審核通過 -1退回
			if(examine.getStep() == 0) auditingExamines.setAudit_state(1);
			auditingExamines.setExamine_valid(1);
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
			this.generalService.update(whereRelation);
		}
	}
	
	public void doVerifyAuditing(HttpServletRequest request, HttpServletResponse response) throws Exception {
		AuditingExamines examines = this.modelMapper.map(request.getAttribute("examines"), AuditingExamines.class);
		if(examines != null && examines.getExamine_id() > 0) {//update
			WhereRelation whereRelation = new WhereRelation();//可以加上判断代理人来进行审核
			whereRelation.EQ("examine_id", examines.getExamine_id()).EQ("audit_employee_id", request.getAttribute("employee_id"))
				.EQ("audit_step", examines.getAudit_step()).setUpdate("audit_state", examines.getAudit_state());
			if(examines.getAudit_content() != null) whereRelation.setUpdate("audit_content", examines.getAudit_content());
			whereRelation.setUpdate("audit_datetime", Utility.instance().getTodayDate()).setTable_clazz(AuditingExamines.class);
			this.generalService.setTransaction(true);
			this.generalService.update(whereRelation);
			//判断更新下一步 
			whereRelation = new WhereRelation();//可以加上判断代理人来进行审核
			whereRelation.EQ("auditing_id", examines.getAuditing_id()).EQ("module_id", examines.getModule_id())
				.EQ("link_id", examines.getLink_id()).EQ("employee_id", examines.getEmployee_id())
				.EQ("audit_step", examines.getAudit_step()+1).setTable_clazz(AuditingExamines.class);
			AuditingExamines isNextExamines = (AuditingExamines) this.generalService.getEntity(whereRelation);
			if(isNextExamines != null && examines.getAudit_state() != -1) {//如果退回则不更新下一步
				whereRelation.setUpdate("audit_state", 1);
				this.generalService.update(whereRelation);//更新流转
			} 
			//如果是退回 把还没审核的之后的数据删除
			if(examines.getAudit_state() == -1) {
				whereRelation = new WhereRelation();//可以加上判断代理人来进行审核
				whereRelation.EQ("auditing_id", examines.getAuditing_id()).EQ("module_id", examines.getModule_id())
					.EQ("link_id", examines.getLink_id()).EQ("audit_state", 0).setTable_clazz(AuditingExamines.class);
				this.generalService.delete(whereRelation);
			}
			if(isNextExamines == null || examines.getAudit_state() == -1) {//最后一步 或者退回 更新提出审核的模块 
				String channel = request.getParameter("c");
				int module_id = EncryptUtiliy.instance().intIDDecrypt(channel);
				whereRelation = new WhereRelation();//
				whereRelation.EQ("module_id", module_id).setTable_clazz(Modules.class);
				Modules module = (Modules) this.generalService.getEntity(whereRelation);
				String view = module.getModule_view();
				Method executeExamines = AuditingExaminesAction.class.getMethod(view, AuditingExaminesDTO.class);
				AuditingExaminesDTO update = new AuditingExaminesDTO();
				update.setUpdate_id(examines.getLink_id());
				update.setStatus(examines.getAudit_state()+"");
				update.setAuditing_date(Utility.instance().getTodayDate());
				update.setAuditing_employee_id((Integer) request.getAttribute("employee_id"));
				this.generalService.setTransaction(true);
				executeExamines.invoke(new AuditingExaminesAction(this.generalService), update);//更新狀態
			}
			this.generalService.commit();
		}
		this.success.setItem("audit_datetime", Utility.instance().getTodayDate());
	}

}
