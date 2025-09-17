package com.base.dao.example;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.base.dao.BaseDao;
import com.base.dao.DBQueryDao;
import com.base.model.entity.example.Test;

import core.jdbc.mysql.WhereRelation;

public class ExampleDao extends BaseDao {
	DBQueryDao dBQueryDao;
	public ExampleDao(String jdbcDsn) throws SQLException {
		super(jdbcDsn);
		// TODO Auto-generated constructor stub
		dBQueryDao = new DBQueryDao(jdbcDsn);
	}

	public Test geTest(int id) throws SQLException {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ("id", id);
		Test test = dBQueryDao.getEntity(Test.class, whereRelation);
		return test;
	}
	
	public List<Test> geTest(int[] ids) throws SQLException {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.IN("id", ids);
		List<Test> test = dBQueryDao.setDsn(this.jdbcDsn).getEntityList(Test.class, whereRelation);
		return test;
	}
	

	public List<Test> geTest(Test test) throws SQLException {
		// TODO Auto-generated method stub
		return dBQueryDao.setDsn(this.jdbcDsn).getEntityList(Test.class, test);
	}
	
	public BigInteger saveTest(Test test) throws Exception {
		return (BigInteger) dBQueryDao.setDsn(this.jdbcDsn).intInsertObject(test);
	}
	
	public int updateTest(Test test, int id) throws Exception {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ("id", id);
		return (int) dBQueryDao.setDsn(this.jdbcDsn).updateObject(test, whereRelation);
	}
	//
	public void example() throws SQLException {
		HashMap<String, Object> whereSQL = new HashMap<>();
		whereSQL.put("book_order_number_ourter", "");
		//Book book = DBQuery.instance(dsn).table(Book.class).getEntity(WhereRelation.instance().where(whereSQL));
		
		HashMap<String, Object> whereEQ = new HashMap<>();
		whereEQ.put("book_id", "");
		//whereEQ.put("book_change", "0");
		HashMap<String, Object> whereNE = new HashMap<>();
		whereNE.put("book_order_number_ourter", "");
	}
	
}


