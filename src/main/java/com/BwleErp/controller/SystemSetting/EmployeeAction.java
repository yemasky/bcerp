package com.BwleErp.controller.SystemSetting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.controller.AbstractAction;
import com.base.model.dto.BwleErp.RoleModuleDTO;
import com.base.model.entity.BwleErp.Role;
import com.base.model.entity.BwleErp.RoleModule;
import com.base.model.entity.BwleErp.company.Company;
import com.base.model.entity.BwleErp.company.CompanySector;
import com.base.service.GeneralService;
import com.base.type.CheckedStatus;
import com.google.gson.Gson;

import core.jdbc.mysql.WhereRelation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class EmployeeAction extends AbstractAction {
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
		if(method == null) method = "";
		//
		switch (method) {
		case "getSector":
			this.doGetSector(request, response);
			break;	
		case "saveSectorPosition":
			this.doSaveSectorPosition(request, response);
			break;	
		case "deleteSectorPosition":
			this.doDeleteSectorPosition(request, response);
			break;		
		case "savePermission":
			this.doSavePermission(request, response);
			break;	
		default:
			this.doDefault(request, response);
			break;
		}		
	}

	@Override
	public void release(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		this.generalService.closeConnection();
	}

	@Override
	public void rollback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		//this.generalService.rollback();
	}
	
	public void doGetSector(HttpServletRequest request, HttpServletResponse response) throws Exception { 
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ("sector_valid", 1).setTable_clazz(CompanySector.class);
		List<CompanySector> companySectorList = generalService.getEntityList(whereRelation);
		//
		whereRelation = new WhereRelation();
		whereRelation.setTable_clazz(Company.class);
		List<Company> companyList = generalService.getEntityList(whereRelation);
		//
		this.success.setItem("companySectorList", companySectorList); 
		this.success.setItem("companyList", companyList);
	}
	
	public void doSaveSectorPosition(HttpServletRequest request, HttpServletResponse response) throws Exception { 
		Integer edit_id = (Integer) request.getAttribute("edit_id");
		CompanySector sector = this.modelMapper.map(request.getAttribute("sector"), CompanySector.class);
		WhereRelation whereRelation = new WhereRelation();
		if(edit_id != null && edit_id > 0) {//update
			whereRelation.EQ("sector_id", edit_id).setTable_clazz(CompanySector.class);
			generalService.updateEntity(sector, whereRelation);
		} else {
			sector.setSector_id(null);
			edit_id = generalService.save(sector);
		}
		this.success.setItem("sector_id", edit_id);
		//
	}
	
	public void doDeleteSectorPosition(HttpServletRequest request, HttpServletResponse response) throws Exception { 
		Integer delete_id = (Integer) request.getAttribute("delete_id");
		WhereRelation whereRelation = new WhereRelation();
		if(delete_id != null && delete_id > 0) {//update
			whereRelation.EQ("sector_id", delete_id).setTable_clazz(CompanySector.class);
			whereRelation.setUpdate("sector_valid", 0);
			generalService.update(whereRelation);
		} 
		//
	}
	////doSavePermission
	public void doSavePermission(HttpServletRequest request, HttpServletResponse response) throws Exception { 
		Integer edit_id = (Integer) request.getAttribute("edit_id");
		RoleModuleDTO role_module = this.modelMapper.map(request.getAttribute("role_module"), RoleModuleDTO.class);;
		System.out.println(new Gson().toJson(role_module));
		WhereRelation whereRelation = new WhereRelation();
		if(edit_id != null && edit_id > 0) {//update
			//whereRelation.EQ("sector_id", edit_id).setTable_clazz(CompanySector.class);
			//generalService.updateEntity(sector, whereRelation);
		} else {
			Role role = new Role();
			role.setCompany_id(1);
			role.setRole_name(role_module.getRole_name());
			//this.generalService.setTransaction(true);
			edit_id = this.generalService.save(role);
			List<RoleModule> roleModuleList = new ArrayList<>();
			for (String key : role_module.getRole_module().keySet()) {
			    int module_id = Integer.parseInt(role_module.getRole_module().get(key));
			    System.out.println("====>"+module_id);
			    RoleModule roleModule = new RoleModule();
			    roleModule.setAccess(3);
			    roleModule.setModule_id(module_id);
			    roleModule.setRole_id(edit_id);
			    roleModuleList.add(roleModule);
			}
			this.generalService.batchSave(roleModuleList);
			//this.generalService.commit();
		}
		this.success.setItem("role_id", edit_id);
		//
	}
	
	
	
}
