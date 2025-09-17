package com.Example.controller.home;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
//import java.util.Date;
import java.util.HashMap;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.controller.AbstractAction;
import com.base.model.entity.example.Test;
import com.base.service.example.ExampleService;
import com.base.type.CheckedStatus;
import com.base.type.ErrorCode;

import core.util.Utiliy;

@Component("example.home.index")
public class IndexAction extends AbstractAction {
	@Autowired
	private ExampleService exampleService;
	private ModelMapper modelMapper;
	
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
		switch (method) {
		case "login":
			this.doLogin(request, response);
			break;
		case "test":
			this.doTest(request, response);
			break;
		case "test_update":
			this.doTestUpdate(request, response);
			break;
		case "test_post":
			this.doTestPost(request, response);
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
	
	public void doLogin(HttpServletRequest request, HttpServletResponse response) {
		
		this.success.setSuccessCode(ErrorCode.__T_LOGIN);
	}

	public void doTest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SimpleDateFormat toDay = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String ssString = toDay.format(System.currentTimeMillis());
		HashMap<String, Object> insertData = new HashMap<>();
		insertData.put("title", "你好");
		insertData.put("description", "描述");
		insertData.put("add_datetime", ssString);
		//exampleService = new ExampleServiceImpl();
		
		exampleService.setTransaction(true);
		//Date currentTime= new Date();
		Test test = new Test();
		test.setTitle("你好");
		test.setDescription("描述2");

		test.setAdd_datetime(ssString);
		BigInteger id = exampleService.saveTest(test);
		exampleService.commit();
		exampleService.setTransaction(false);

		int[] bb = new int[] {1,2,3};
		List<Test> aa = exampleService.getTest(bb);
		this.success.setItem("aa", aa);
		test.setId(id.intValue());
		List<Test> cc = exampleService.getTest(test);
		this.success.setItem("cc", cc);
		this.success.setItem("dd", test);
		//exampleService.freeConnection();
		this.success.setSuccessCode(ErrorCode.__T_SUCCESS);
	}

	public void doTestPost(HttpServletRequest request, HttpServletResponse response) {
		Test post = modelMapper.map(Utiliy.instance().getRequestMap(request), Test.class);
		this.success.setItem("post", post);
		this.success.setSuccessCode(ErrorCode.__T_SUCCESS);
	}
	
	public void doTestUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SimpleDateFormat toDay = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String ssString = toDay.format(System.currentTimeMillis());
		
		Test test = new Test();
		test.setTitle("你好嘛嘛嘛嘛嘛嘛");
		test.setDescription("描述2描述描述描述3");
		test.setAdd_datetime(ssString);
		int effectRow = exampleService.updateTest(test, 4);
		this.success.setItem("effectRow", effectRow);
		this.success.setSuccessCode(ErrorCode.__T_SUCCESS);
	}

}
