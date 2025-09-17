package com.BwleErp.service.impl.employee;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.BwleErp.config.Config;
import com.base.dao.GeneralDao;
import com.base.model.entity.BwleErp.employee.Employee;
import com.base.service.BwleErp.EmployeeService;

import core.jdbc.mysql.NeedEncrypt;
import core.jdbc.mysql.WhereRelation;

public class EmployeeServiceImpl implements EmployeeService {
	private GeneralDao genernalDao;

	public EmployeeServiceImpl() throws SQLException {
		this.genernalDao = new GeneralDao(Config.erpDSN);
		this.genernalDao.setTableClass(Employee.class);
	}
	
	@Override
	public int save(Object object) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(WhereRelation whereRelation) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateEntity(Object object, WhereRelation whereRelation) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getEntity(WhereRelation whereRelation) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getEntityByObject(Object object) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> getEntityList(Object object) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HashMap<String, Object>> getList(Object object, String fieId) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HashMap<String, Object>> getList(WhereRelation whereRelation, NeedEncrypt needEncrypt)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> getEntityList(WhereRelation whereRelation) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
