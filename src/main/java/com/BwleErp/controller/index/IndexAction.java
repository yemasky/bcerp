package com.BwleErp.controller.index;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.controller.AbstractAction;
import com.base.service.category.CategoryService;
import com.base.type.CheckedStatus;
import com.base.type.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("BwleErp.index.index")
public class IndexAction extends AbstractAction {
	CategoryService categoryService;

	@Autowired
	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@Override
	public CheckedStatus check(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		System.out.println("action check");
		return this.status;
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String method = (String) request.getAttribute("method");
		if(method == null) method = "";
		//
		System.out.println(method+"============》");
		switch (method) {
		case "test":
			this.doTest(request, response);
			break;	
		case "checkLogin":
			System.out.println("checkLogin============》");
			this.doCheckLogin(request, response);
			break;	
		default:
			this.doDefault(request, response);
			break;
		}
	}

	@Override
	public void release(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void rollback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
	}
	
	public void doDefault(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取所有的category
		
		
		this.success.setSuccessCode(ErrorCode.__T_SUCCESS);
	}

	public void doTest(HttpServletRequest request, HttpServletResponse response) throws Exception {		
		this.success.setSuccessCode(ErrorCode.__T_SUCCESS);
	}
	
	public void doCheckLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		
		this.success.setSuccessCode(ErrorCode.__F_NO_MATCH_MEMBER);

	}
}
