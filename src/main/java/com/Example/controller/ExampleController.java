package com.Example.controller;

import java.lang.reflect.Method;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;

import com.base.controller.AbstractController;
import com.base.controller.SpringContextInvoke;
import com.base.model.entity.example.Test;
import com.base.type.ErrorCode;
import com.base.type.Success;

import core.util.Encrypt;

//import com.Example.controller.action.*;
//RestController
@Controller
@RequestMapping("/example")
public class ExampleController extends AbstractController {
	private final String thisController = "Example";
	protected HttpServletRequest request;
	protected HttpServletResponse response;

	@Override
	public void beforeCheck(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		this.request = request;
		this.response = response;
	}

	@Override
	public void release(HttpServletRequest request, HttpServletResponse response) {

	}

	@Override
	@RequestMapping(value = "/**")
	public String defaultAction(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String sss = Encrypt.base64DESEncrypt("32337869076", "123400000000000000000");
		String xxx = Encrypt.base64DESDecrypt(sss, "123400000000000000000");
		System.out.println(sss);
		System.out.println(xxx);
		model.addAttribute("__RESOURCE", "/resource/");
		// TODO Auto-generated method stub
		return "404";
	}

	@RequestMapping(value = "/test_post")
	public String testPostAction(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		model.addAttribute("noLogin", 1);
		return "zhizu/test_post";
	}

	@RequestMapping(value = "/save_test_post")
	@ResponseBody
	public Success saveTestPostAction(Test test, HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		Success success = new Success();
		success.setItem("test", test);
		success.setSuccess(true);
		success.setErrorCode(ErrorCode.__T_SUCCESS);
		return success;
	}

	@RequestMapping(value = "/run/{module}/{method}")
	@ResponseBody
	public Success doGetAndPost(@PathVariable("module") String module, @PathVariable("method") String method,
			HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute(POST_MAP_KEY, false);
		request.setAttribute("module", module);
		request.setAttribute("method", method);
		request.setAttribute("action", request.getParameter("action"));
		return this.excuteController(request, response);
	}

	@RequestMapping(value = "/action.do") // 处理json post 数据action.do?module=module&method=method&action=action
	@ResponseBody
	public Success doPostJson(@RequestBody Map<String, Object> postJsonMap, HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute(POST_MAP_KEY, true);
		request.setAttribute("module", request.getParameter("module"));
		request.setAttribute("method", request.getParameter("method"));
		request.setAttribute("action", request.getParameter("action"));
		for (String key : postJsonMap.keySet()) {
			Object object = postJsonMap.get(key);
			request.setAttribute(key, object);
		}
		return this.excuteController(request, response);
	}

	public Success excuteController(HttpServletRequest request, HttpServletResponse response) {
		try {
			String actionInvoke = (String) request.getAttribute("action");
			if (actionInvoke == null || actionInvoke.isEmpty() || actionInvoke.equals(""))
				actionInvoke = "index";
			String action = (new StringBuilder()).append(Character.toUpperCase(actionInvoke.charAt(0)))
					.append(actionInvoke.substring(1)).toString() + "Action";
			String module = (String) request.getAttribute("module");
			if (module == null || module.isEmpty()) {
				module = "";
			} else {
				module = module.toLowerCase();
			}

			Class<?> controllerClass = Class
					.forName("com." + this.thisController + ".controller." + module + "." + action);
			Object controllerAction = SpringContextInvoke.getBean(controllerClass);

			Method execute = controllerClass.getMethod("execute", HttpServletRequest.class, HttpServletResponse.class);
			Success tempObj = (Success) execute.invoke(controllerAction, request, response);
			this.release(request, response);
			return tempObj;
		} catch (Exception e) {
			this.release(request, response);
			MDC.put("APP_NAME", "example_error");
			logger.error(e.getMessage(), e);
		}
		Success success = new Success();
		success.setSuccess(true);
		success.setErrorCode(ErrorCode.__F_SYS);
		return success;
	}

}
