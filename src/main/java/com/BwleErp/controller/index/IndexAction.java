package com.BwleErp.controller.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.controller.AbstractAction;
import com.base.model.entity.BwleErp.RoleModule;
import com.base.model.entity.BwleErp.employee.Employee;
import com.base.model.entity.BwleErp.employee.EmployeeSector;
import com.base.model.entity.BwleErp.module.Modules;
import com.base.model.vo.BwleErp.employ.ModulesVo;
import com.base.service.GeneralService;
import com.base.type.CheckedStatus;
import com.base.type.ErrorCode;
import com.base.util.Utility;
import com.google.gson.Gson;

import core.jdbc.mysql.NeedEncrypt;
import core.jdbc.mysql.WhereRelation;
import core.util.Encrypt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("BwleErp.index.index")
public class IndexAction extends AbstractAction {
	@Autowired
	private GeneralService generalService;

	@Override
	public CheckedStatus check(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		System.out.println("action check");
		return this.status;
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String method = (String) request.getAttribute("method");
		if(method == null) method = "";
		//
		switch (method) {
		case "test":
			this.doTest(request, response);
			break;	
		case "checkLogin":
			this.doCheckLogin(request, response);
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
	
	public void doDefault(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取所有的category
		
		
		this.success.setSuccessCode(ErrorCode.__T_SUCCESS);
	}

	public void doTest(HttpServletRequest request, HttpServletResponse response) throws Exception {		
		this.success.setSuccessCode(ErrorCode.__T_SUCCESS);
	}
	
	public void doCheckLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String email = (String) request.getAttribute("email");
		String password = (String) request.getAttribute("password");
		WhereRelation whereRelation = new WhereRelation();
		if(email != null && !email.equals("") && password != null && !password.equals("")) {
			if(email.contains("@")) {
				whereRelation.EQ("email", email).EQ("valid", 1).setTable_clazz(Employee.class);
			} else {
				whereRelation.EQ("mobile", email).EQ("valid", 1).setTable_clazz(Employee.class);
			}
			Employee employee = (Employee) this.generalService.getEntity(whereRelation);
			if(employee != null) {
				String _password = employee.getPassword();
				String _salt = employee.getPassword_salt();
				if(Encrypt.md5Encrypt(password + _salt).equals(_password)) {
					//获取用户权限
					whereRelation = new WhereRelation();
					whereRelation.EQ("employee_id", employee.getE_id()).setTable_clazz(EmployeeSector.class);
					EmployeeSector employeeSector = (EmployeeSector) this.generalService.getEntity(whereRelation);
					int role_id = employeeSector.getRole_id();
					String role_ids = employeeSector.getRole_ids();
					if(role_ids == null) {
						role_ids = "";
					} 
					role_ids = role_id + ","+role_ids;
					int[] iRole_idsList = Utility.instance().StringToIntArray(role_ids.split(","));
					whereRelation = new WhereRelation();
					whereRelation.IN("role_id", iRole_idsList).setField("module_id").setTable_clazz(RoleModule.class);
					List<HashMap<String, Object>> moduleList = this.generalService.getList(whereRelation, new NeedEncrypt());
					int[] iModule_idsList = Utility.instance().hashMapListToIntArray(moduleList, "module_id");
					//获取菜单
					whereRelation = new WhereRelation();
					whereRelation.IN("module_id", iModule_idsList).ORDER_DESC("module_channel").ORDER_DESC("module_father_id")
						.ORDER_DESC("module_order").ORDER_DESC("action_order").setTable_clazz(Modules.class);
					NeedEncrypt needEncrypt = new NeedEncrypt();
					/*needEncrypt.setNeedEncrypt(true);
					needEncrypt.setNeedEncrypt("m_id", NeedEncrypt._ENCRYPT);
					needEncrypt.setNeedEncrypt("mf_id", NeedEncrypt._ENCRYPT);
					needEncrypt.setNeedEncrypt("sf_id", NeedEncrypt._ENCRYPT);*/
					//List<HashMap<String, Object>> employeeModuleList = this.generalService.getList(whereRelation, needEncrypt);
					//List<ModulesVo> employeeModuleVoList = new ArrayList<>();
					//BeanUtils.copyProperties(employeeModuleList, employeeModuleVoList);
					List<Modules> employeeModuleVoList = this.generalService.getEntityList(whereRelation);
					//System.out.println(new Gson().toJson(employeeModuleVoList));
					this.success.setItem("employeeMenu", employeeModuleVoList);
					this.success.setItem("module_channel", "Setting");
					this.success.setItem("employee", employee);
					return; 
				}
			}
		}
		//System.out.println(Encrypt.md5Encrypt(password + "567899585"));
		this.success.setErrorCode(ErrorCode.__F_NO_MATCH_MEMBER);

	}
}
