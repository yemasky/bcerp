package com.base.controller;

import java.sql.SQLException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.base.dao.DBQueryDao;
import com.base.type.CheckedStatus;
import com.base.type.ErrorCode;
import com.base.type.Success;

import core.util.Cookies;

public abstract class AbstractAction {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	protected Success success;
	protected CheckedStatus status;
	protected final String CLIENT_KEY = "client_key";
	protected final String SESSION_KEY = "session_key";
	protected final String POST_MAP_KEY = "postMap_key";
	protected String client_key;
	protected HttpSession httpSession;
	protected ModelMapper modelMapper = new ModelMapper();

	public abstract CheckedStatus check(HttpServletRequest request, HttpServletResponse response) throws Exception;

	public abstract void service(HttpServletRequest request, HttpServletResponse response) throws Exception;

	// 资源回收
	public abstract void release(HttpServletRequest request, HttpServletResponse response) throws Exception;

	// 事務回滾
	public abstract void rollback(HttpServletRequest request, HttpServletResponse response) throws Exception;

	public void doDefault(HttpServletRequest request, HttpServletResponse response) throws Exception {
	}

	private void freeAllConnection() throws SQLException {
		DBQueryDao dBQueryDao = new DBQueryDao("");
		dBQueryDao.releaseAllConnection();
	}

	private void rollbackAllConnection() throws SQLException {
		DBQueryDao dBQueryDao = new DBQueryDao("");
		dBQueryDao.rollbackAll();
	}

	public String getCookie(HttpServletRequest httpRequest, String name) {
		Cookie cookie =  Cookies.getCookie(httpRequest, name);
		if(cookie != null) {
			return cookie.getValue();
		}
		return "";
	}
	
	public void setCookie(HttpServletResponse httpResponse, String name, String value) {
		Cookies.setCookie(httpResponse, name, value);
	}
	
	public void removeCookie(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String name) {
		Cookies.removeCookie(httpRequest, httpResponse, name);
	}

	public Success execute(HttpServletRequest request, HttpServletResponse response) {
		try {
			//为每个用户产生唯一session
			this.httpSession = request.getSession();
			this.success = new Success();
			this.status = new CheckedStatus();
			CheckedStatus checkedStatus = this.check(request, response);
			if(checkedStatus.isStatus()) {
				this.service(request, response);
				this.release(request, response);
				this.freeAllConnection();
			} else {
				success.setSuccess(checkedStatus.isStatus());
				success.setErrorCode(checkedStatus.getError());
				//
				return success;
			}
		} catch (Exception e) {
			success.setSuccess(false);
			success.setErrorCode(ErrorCode.__F_SYS);
			try {
				this.rollback(request, response);
				this.rollbackAllConnection();
				this.freeAllConnection();
			} catch (Exception ex) {
				
				MDC.put("APP_NAME", "default_error");
				logger.error(success.getMessage(), ex);
			}
			
			MDC.put("APP_NAME", "default_error");
			logger.error(success.getMessage(), e);
		}
		return success;
	}
}

