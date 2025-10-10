package com.base.service;

import java.sql.SQLException;
import java.util.List;

import com.base.model.vo.PageVo;
import com.base.model.vo.PageVoEntity;

import core.jdbc.mysql.NeedEncrypt;
import core.jdbc.mysql.WhereRelation;

public interface GeneralService  extends BaseService {
	int increase(String field, String increase_key, int increase_key_id, int member_id, Class<?> clazz) throws SQLException;
	
	<T> PageVoEntity<T> getPageEntityList(WhereRelation whereRelation, PageVoEntity<T> pageVo) throws Exception;
	
	PageVo getPageList(WhereRelation whereRelation, PageVo pageVo, NeedEncrypt needEncrypt) throws Exception;
	
	<T> int batchSave(List<T> objectList) throws Exception;
	
	<T> int batchSave(List<T> objectList, String insertType) throws Exception;
	
	void delete(WhereRelation whereRelation) throws Exception;
	
	void closeConnection() throws SQLException;
	
	void setTransaction(boolean isTransaction) throws SQLException;
	
	void commit() throws SQLException;

	void rollback() throws SQLException;
}
