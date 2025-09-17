package com.base.dao;

import java.sql.SQLException;

import core.jdbc.mysql.DBQuery;

public final class DBQueryDao extends DBQuery {
	protected String jdbcDsn;

	public DBQueryDao(String jdbcDsn) throws SQLException {
		super(jdbcDsn);
		if(!jdbcDsn.equals("")) this.jdbcDsn = jdbcDsn;
	}

	// 释放连接资源
	public void releaseAllConnection() throws SQLException {
		this.freeAllConnection();
	}
	//回滚所有数据
	public void rollbackAll() throws SQLException {
		this.rollbackAllConnection();
	}
}
