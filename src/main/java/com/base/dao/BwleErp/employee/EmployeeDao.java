package com.base.dao.BwleErp.employee;

import java.sql.SQLException;

import com.base.dao.BaseDao;
import com.base.dao.DBQueryDao;

public class EmployeeDao extends BaseDao  {
	DBQueryDao dBQueryDao;
	public EmployeeDao(String jdbcDsn) throws SQLException {
		super(jdbcDsn);
		
		this.dBQueryDao = new DBQueryDao(jdbcDsn);
	}

	
}
