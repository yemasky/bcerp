package com.BwleErp.controller.home;

import com.base.controller.AbstractAction;
import com.base.type.CheckedStatus;
import com.base.type.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class IndexAction extends AbstractAction {
	
	@Override
	public CheckedStatus check(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("action check"); 
		return this.status;
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = (String) request.getAttribute("method");
		//
		switch (method) {
		case "login":
			this.doLogin(request, response);
			break;
		case "test":
			this.doTest(request, response);
			break;
		default:
			this.doDefault(request, response);
			break;
		}
	}

	@Override
	public void release(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
	}

	@Override
	public void rollback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
	}
	
	public void doLogin(HttpServletRequest request, HttpServletResponse response) {
		
		this.success.setSuccessCode(ErrorCode.__T_LOGIN);
	}

	public void doTest(HttpServletRequest request, HttpServletResponse response) throws Exception {
				
		this.success.setSuccessCode(ErrorCode.__T_SUCCESS);
	}

	
}
