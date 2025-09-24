package com.base.service.BwleErp;

import java.sql.SQLException;

import com.base.model.dto.BwleErp.EmployeePermissionDto;

public interface EmployeeService {
	EmployeePermissionDto permissionCheck(int module_id, int employee_id)  throws SQLException;
}
