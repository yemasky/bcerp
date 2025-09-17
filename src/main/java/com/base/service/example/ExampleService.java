package com.base.service.example;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

import com.base.model.entity.example.Test;
import com.base.service.BaseService;

public interface ExampleService extends BaseService {
	public Test geTest(int id) throws SQLException;
	
	public List<Test> getTest(int[] id) throws SQLException;
	
	public List<Test> getTest(Test test) throws SQLException;
	
	public BigInteger saveTest(Test test) throws Exception; 
	
	public int updateTest(Test test, int id) throws Exception;
	
	public void setTransaction(boolean isTransaction) throws SQLException;

	public void commit() throws SQLException;
}
