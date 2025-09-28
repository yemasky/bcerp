package com.base.service;

import java.sql.SQLException;
import java.util.List;

public interface GeneralService  extends BaseService {
	int increase(String field, String increase_key, int increase_key_id, int member_id, Class<?> clazz) throws SQLException;
	
	<T> int batchSave(List<T> objectList) throws Exception;
	
	void closeConnection() throws SQLException;
	
	void setTransaction(boolean isTransaction) throws SQLException;
	
	void commit() throws SQLException;

	void rollback() throws SQLException;
}
