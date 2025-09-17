package com.Example.service.impl;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.Example.config.Config;
import com.base.dao.example.ExampleDao;
import com.base.model.entity.example.Test;
import com.base.service.example.ExampleService;

import core.jdbc.mysql.NeedEncrypt;
import core.jdbc.mysql.WhereRelation;

@Service
public class ExampleServiceImpl implements ExampleService {
	private ExampleDao examplelDao;

	public ExampleServiceImpl() throws SQLException {
		examplelDao = new ExampleDao(Config.jdbcDsnTest);
	}

	@Override
	public Test geTest(int id) throws SQLException {
		// TODO Auto-generated method stub
		return examplelDao.geTest(id);
	}

	@Override
	public List<Test> getTest(int[] id) throws SQLException {
		// TODO Auto-generated method stub
		return examplelDao.geTest(id);
	}

	@Override
	public List<Test> getTest(Test test) throws SQLException {
		// TODO Auto-generated method stub
		return examplelDao.geTest(test);
	}
	
	@Override
	public BigInteger saveTest(Test test) throws Exception {
		// TODO Auto-generated method stub
		return examplelDao.saveTest(test);
	}

	@Override
	public int updateTest(Test test, int id) throws Exception {
		// TODO Auto-generated method stub
		return examplelDao.updateTest(test, id);
	}

	@Override
	public void setTransaction(boolean isTransaction) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commit() throws SQLException {
		// TODO Auto-generated method stub
		
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
	public <T> List<T> getEntityList(WhereRelation whereRelation) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateEntity(Object object, WhereRelation whereRelation) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<HashMap<String, Object>> getList(WhereRelation whereRelation, NeedEncrypt needEncrypt) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getEntity(WhereRelation whereRelation) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
