package com.base.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import core.jdbc.mysql.NeedEncrypt;
import core.jdbc.mysql.WhereRelation;

public interface BaseService {

	int save(Object object) throws Exception;
	
	int update(WhereRelation whereRelation) throws SQLException;
	
	int updateEntity(Object object, WhereRelation whereRelation) throws Exception;
		
	Object getEntity(WhereRelation whereRelation) throws SQLException;
	
	Object getEntityByObject(Object object) throws SQLException;
		
	<T> List<T> getEntityList(Object object) throws SQLException;
	
	List<HashMap<String, Object>> getList(Object object, String fieId) throws SQLException;
	
	List<HashMap<String, Object>> getList(WhereRelation whereRelation, NeedEncrypt needEncrypt) throws SQLException;

	<T> List<T> getEntityList(WhereRelation whereRelation) throws SQLException;
}
