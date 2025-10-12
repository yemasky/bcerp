package com.BwleErp.service.impl.employee;

import java.sql.SQLException;

import org.springframework.stereotype.Service;

import com.BwleErp.service.impl.GeneralServiceImpl;
import com.base.model.dto.BwleErp.EmployeePermissionDto;
import com.base.model.entity.BwleErp.SystemSetting.RoleModule;
import com.base.model.entity.BwleErp.SystemSetting.employee.EmployeeSector;
import com.base.model.entity.BwleErp.SystemSetting.module.Modules;
import com.base.service.GeneralService;
import com.base.service.BwleErp.EmployeeService;
import com.base.util.Utility;

import core.jdbc.mysql.WhereRelation;

@Service
public class EmployeeServiceImpl implements EmployeeService{

	@Override
	public EmployeePermissionDto permissionCheck(int module_id, int employee_id) throws SQLException {
		
		EmployeePermissionDto employeePermission = new EmployeePermissionDto();
		GeneralService generalService = new GeneralServiceImpl();
		// 获取用户权限
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ("employee_id", employee_id).setTable_clazz(EmployeeSector.class);
		EmployeeSector employeeSector = (EmployeeSector) generalService.getEntity(whereRelation);
		if(employeeSector != null && employeeSector.getRole_id() > 0) {
			int role_id = employeeSector.getRole_id();
			String role_ids = employeeSector.getRole_ids();
			if (role_ids == null) {
				role_ids = "";
			}
			role_ids = role_id + "," + role_ids;
			int[] iRole_idsList = Utility.instance().StringToIntArray(role_ids.split(","));
			whereRelation = new WhereRelation();
			whereRelation.IN("role_id", iRole_idsList).EQ("module_id", module_id).setTable_clazz(RoleModule.class);
			RoleModule roleModule = (RoleModule) generalService.getEntity(whereRelation);
			if(roleModule != null) {//有權限
				employeePermission.setPermission(true);
				//
				whereRelation = new WhereRelation();
				whereRelation.EQ("module_id", module_id).setTable_clazz(Modules.class);
				Modules module = (Modules) generalService.getEntity(whereRelation);
				employeePermission.setModule_id(module_id);
				employeePermission.setModule_channel(module.getModule_channel());
				employeePermission.setModule(module.getModule());
				employeePermission.setModule_name(module.getModule_name());
				employeePermission.setModule_view(module.getModule_view());
				employeePermission.setAction(module.getAction());
			} else {
				whereRelation = new WhereRelation();
				whereRelation.EQ("module_id", module_id).setTable_clazz(Modules.class);
				Modules module = (Modules) generalService.getEntity(whereRelation);
				employeePermission.setModule_name(module.getModule_name());
			}
		}
		return employeePermission;
	}

}
