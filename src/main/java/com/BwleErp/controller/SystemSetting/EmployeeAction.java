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
import com.base.model.entity.BwleErp.employee.Employee;
import com.base.model.entity.BwleErp.employee.EmployeeSector;
import com.base.model.vo.PageVo;
import com.base.model.vo.BwleErp.EmployeeSectorVo;
import com.base.model.vo.BwleErp.EmployeesVo;
import com.base.service.GeneralService;
import com.base.type.CheckedStatus;
import com.base.util.EncryptUtiliy;

import core.jdbc.mysql.NeedEncrypt;
import core.jdbc.mysql.WhereRelation;
import core.util.Encrypt;
import core.util.Utiliy;
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
		case "getPermission":
			this.doGetPermission(request, response);
			break;		
		case "saveEmployee":
			this.doSaveEmployee(request, response);
			break;	
		case "getEmployee":
			this.doGetEmployeeSector(request, response);
			break;		
		case "deleteEmployee":
			this.doDeleteEmployee(request, response);
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
		this.generalService.rollback();
	}
	
	public void doGetSector(HttpServletRequest request, HttpServletResponse response) throws Exception { 
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ("sector_valid", 1).setTable_clazz(CompanySector.class);
		List<CompanySector> companySectorList = this.generalService.getEntityList(whereRelation);
		//
		whereRelation = new WhereRelation();
		whereRelation.setTable_clazz(Company.class);
		List<Company> companyList = this.generalService.getEntityList(whereRelation);
		//
		whereRelation = new WhereRelation();
		whereRelation.EQ("role_valid", 1).setTable_clazz(Role.class);
		List<Role> roleList = this.generalService.getEntityList(whereRelation);
		//
		whereRelation = new WhereRelation();//取得所有员工
		whereRelation.EQ("is_system", 0).setTable_clazz(EmployeeSectorVo.class);
		PageVo pageVo = new PageVo();
		NeedEncrypt needEncrypt = new NeedEncrypt();
		needEncrypt.setNeedEncrypt(true);
		needEncrypt.setNeedEncrypt("e_id", NeedEncrypt._ENCRYPT);
		pageVo = this.generalService.getPageList(whereRelation, pageVo, needEncrypt);
		
		this.success.setItem("companySectorList", companySectorList); 
		this.success.setItem("companyList", companyList);
		this.success.setItem("roleList", roleList);
		this.success.setItem("employeePage", pageVo);
	}
	
	public void doGetEmployeeSector(HttpServletRequest request, HttpServletResponse response) throws Exception { 
		Object _page = request.getAttribute("page");//post
		int page = 1;
		if (_page != null && !_page.equals("")) {
			page = Integer.parseInt(_page.toString());
		}
		PageVo pageVo = new PageVo();
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ("is_system", 0).setTable_clazz(EmployeeSectorVo.class);
		NeedEncrypt needEncrypt = new NeedEncrypt();
		needEncrypt.setNeedEncrypt(true);
		needEncrypt.setNeedEncrypt("e_id", NeedEncrypt._ENCRYPT);
		pageVo.setCurrentPage(page);
		pageVo = this.generalService.getPageList(whereRelation, pageVo, needEncrypt);
		this.success.setItem("employeePage", pageVo);
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
		String _edit_id = request.getParameter("edit_id");
		int edit_id = 0;
		if(_edit_id != null && !_edit_id.equals("")) edit_id = Integer.parseInt(_edit_id);
		RoleModuleDTO role_module = this.modelMapper.map(request.getAttribute("role_module"), RoleModuleDTO.class);;
		WhereRelation whereRelation = new WhereRelation();
		this.generalService.setTransaction(true);
		if(edit_id > 0) {//update
			Role role = new Role();//更新名字
			role.setRole_name(role_module.getRole_name());
			whereRelation.EQ("role_id", edit_id).setTable_clazz(Role.class);
			generalService.updateEntity(role, whereRelation);
			//
			whereRelation = new WhereRelation();
			whereRelation.EQ("role_id", edit_id).setTable_clazz(RoleModule.class);
			generalService.delete(whereRelation);
			
		} else {
			Role role = new Role();
			role.setCompany_id(1);
			role.setRole_name(role_module.getRole_name());
			edit_id = this.generalService.save(role);
		}
		//更新权限
		List<RoleModule> roleModuleList = new ArrayList<>();
		for (String key : role_module.getRole_module().keySet()) {
		    int module_id = Integer.parseInt(role_module.getRole_module().get(key));
		    RoleModule roleModule = new RoleModule();
		    roleModule.setAccess(3);
		    roleModule.setModule_id(module_id);
		    roleModule.setRole_id(edit_id);
		    roleModuleList.add(roleModule);
		}
		this.generalService.batchSave(roleModuleList);
		this.generalService.commit();
		//
		this.success.setItem("role_id", edit_id);
		//
	}
	
	public void doGetPermission(HttpServletRequest request, HttpServletResponse response) throws Exception { 
		int role_id = (int) request.getAttribute("role_id");
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ("role_id", role_id).setTable_clazz(RoleModule.class);
		List<RoleModule> roleModuleList = this.generalService.getEntityList(whereRelation);
		//
		this.success.setItem("roleModuleList", roleModuleList); 
	}
	//doSaveEmployee
	public void doSaveEmployee(HttpServletRequest request, HttpServletResponse response) throws Exception { 
		String _edit_id = request.getParameter("edit_id");//
		EmployeesVo employees = this.modelMapper.map(request.getAttribute("employees"), EmployeesVo.class);
		int edit_id = 0;
		if(_edit_id != null && !_edit_id.equals("") && !_edit_id.equals("0")) {
			edit_id = EncryptUtiliy.instance().intIDDecrypt(_edit_id);
		}
		if(edit_id > 0) {//update  
			this.generalService.setTransaction(true);
			Employee employee = new Employee();
			EmployeeSector employeeSector = new EmployeeSector();
			if(employees.getPassword() != null && !employees.getPassword().equals("")) {
				String _salt = Encrypt.getRandomUUID();
				employee.setPassword(Encrypt.md5Encrypt(employees.getPassword() + _salt));
				employee.setPassword_salt(_salt);
			}
			
			employee.setEmail(employees.getEmail());
			employee.setMobile(employees.getMobile());
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("employee_id", edit_id).setTable_clazz(Employee.class);
			generalService.updateEntity(employee, whereRelation);
			//
			whereRelation = new WhereRelation();
			employeeSector.setMobile(employees.getMobile());
			employeeSector.setEmail(employees.getEmail());
			employeeSector.setAvatar(employees.getAvatar());
			employeeSector.setPosition_id(employees.getPosition_id());
			employeeSector.setSector_id(employees.getSector_id());
			employeeSector.setRole_id(employees.getRole_id());
			employeeSector.setEmployee_name(employees.getEmployee_name());
			employeeSector.setEmployee_enname(employees.getEmployee_enname());
			whereRelation.EQ("employee_id", edit_id).setTable_clazz(EmployeeSector.class);
			generalService.updateEntity(employeeSector, whereRelation);
			this.generalService.commit();
			//
		} else {
			this.generalService.setTransaction(true);
			Employee employee = new Employee();
			EmployeeSector employeeSector = new EmployeeSector();
			String _salt = Encrypt.getRandomUUID();
			employee.setPassword(Encrypt.md5Encrypt(employees.getPassword() + _salt));
			employee.setPassword_salt(_salt);
			employee.setEmail(employees.getEmail());
			employee.setMobile(employees.getMobile());
			employee.setAdd_datetime(Utiliy.instance().getTodayDate());
			employee.setCompany_id(employees.getCompany_id());
			edit_id = this.generalService.save(employee);
			//
			employeeSector.setMobile(employees.getMobile());
			employeeSector.setEmail(employees.getEmail());
			employeeSector.setAvatar(employees.getAvatar());
			employeeSector.setPosition_id(employees.getPosition_id());
			employeeSector.setSector_id(employees.getSector_id());
			employeeSector.setRole_id(employees.getRole_id());
			employeeSector.setEmployee_name(employees.getEmployee_name());
			employeeSector.setEmployee_enname(employees.getEmployee_enname());
			employeeSector.setE_id(edit_id);
			employeeSector.setCompany_id(employees.getCompany_id());
			this.generalService.save(employeeSector);
			this.generalService.commit();
		}
		this.success.setItem("e_id", edit_id);
		//
	}
	
	public void doDeleteEmployee(HttpServletRequest request, HttpServletResponse response) throws Exception { 
		Object _edit_id = request.getAttribute("delete_id");//
		int edit_id = 0;
		if(_edit_id != null && !_edit_id.equals("")) {
			edit_id = EncryptUtiliy.instance().intIDDecrypt(_edit_id+"");
		}
		if(edit_id > 0) {//update  
			this.generalService.setTransaction(true);
			Employee employee = new Employee();
			EmployeeSector employeeSector = new EmployeeSector();
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("employee_id", edit_id).setTable_clazz(Employee.class);
			employee.setEmployee_valid(0);
			generalService.updateEntity(employee, whereRelation);
			//
			employeeSector.setEmployee_valid(0);
			generalService.updateEntity(employeeSector, whereRelation);
			this.generalService.commit();
		}
	}
}
