package com.base.dao;

import java.sql.SQLException;

public class BaseDao {
	protected String jdbcDsn;

	public BaseDao(String jdbcDsn) throws SQLException {
		if(!jdbcDsn.equals("")) this.jdbcDsn = jdbcDsn;
	}

}
