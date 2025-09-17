package com.base.service;

import java.sql.SQLException;

public interface GeneralService  extends BaseService {
	int increase(String field, String increase_key, int increase_key_id, int member_id, Class<?> clazz) throws SQLException;
	


}
