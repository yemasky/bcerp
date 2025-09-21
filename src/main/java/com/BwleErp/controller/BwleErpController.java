package com.BwleErp.controller;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.MDC;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.AbstractController;
import com.base.controller.SpringContextInvoke;
import com.base.type.ErrorCode;
import com.base.type.Success;
import com.base.util.EncryptUtiliy;

import core.util.Utiliy;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/erp")
public class BwleErpController extends AbstractController {
	private final String thisController = "BwleErp";
	private int noLogin = 1;
	private String employeeCookieName = "token";

	@Override
	public void beforeCheck(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String eidDecryptHeader = request.getHeader(this.employeeCookieName);
		String eidDecryptCookie = this.getCookie(request, this.employeeCookieName);
		String eidDecrypt = eidDecryptHeader == null || eidDecryptHeader.equals("") ? eidDecryptCookie : eidDecryptHeader;	    
		try {
			int employee_id = EncryptUtiliy.instance().myIDDecrypt(eidDecrypt);
			if (employee_id > 0) {
				this.noLogin = 0;
			} else {
				this.noLogin = 1;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void release(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	@RequestMapping(value = "/**")
	public String defaultAction(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		model.addAttribute("__WEB", "/erp/");
		model.addAttribute("__RESOURCE", "/static/");
		model.addAttribute("__VERSION", "");
		model.addAttribute("__TITLE", "博威利尔.ERP v1.0.0");
		model.addAttribute("thisDateTime", Utiliy.instance().getTodayDate());
		model.addAttribute("noLogin", this.noLogin);
		return "BwleErp/default";
	}

	@RequestMapping({ "*", "/", "", "index", "index.html", "default.html" })
	public String indexAction(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		model.addAttribute("Title", "welcome");
		model.addAttribute("__VERSION", "1.20");
		model.addAttribute("__RESOURCE", "/resource/");
		Date dateNow = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		model.addAttribute("thisDateTime", dateFormat.format(dateNow));
		model.addAttribute("home_channel", "");
		model.addAttribute("noLogin", this.noLogin);

		return "404";
	}

	@RequestMapping(value = "/{module}/{method}")
	@ResponseBody
	public Object doGetAndPost(@PathVariable("module") String module, @PathVariable("method") String method,
			HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute(POST_MAP_KEY, false);
		request.setAttribute("module", module);
		request.setAttribute("method", method);
		request.setAttribute("action", request.getParameter("action"));
		return this.excuteController(request, response);
	}

	@RequestMapping(value = "/app.do") // 处理JSON post
	@ResponseBody // 数据app.do?module=module&method=method&action=action {@RequestBody Map<String, Object> postJsonMap}
	public Object doPostJson(@RequestBody Map<String, Object> postJsonMap, HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute(POST_MAP_KEY, true);
		request.setAttribute("module", request.getParameter("module"));
		request.setAttribute("method", request.getParameter("method"));
		request.setAttribute("action", request.getParameter("action"));
		for (String key : postJsonMap.keySet()) { //key=>value的value有可能是一组数据模型
			Object object = postJsonMap.get(key);
			request.setAttribute(key, object);
		}
		return this.excuteController(request, response);
	}

	public Object excuteController(HttpServletRequest request, HttpServletResponse response) {
		try {
			String actionInvoke = (String) request.getAttribute("action");
			if (actionInvoke == null || actionInvoke.isEmpty())
				actionInvoke = "index";
			String action = Utiliy.instance().ucfirst(actionInvoke) + "Action";
			String module = (String) request.getAttribute("module");
			if (module == null || module.isEmpty()) {
				module = "index";
			} else {
				module = module.toLowerCase();
			}

			Class<?> controllerClass = Class
					.forName("com." + this.thisController + ".controller." + module + "." + action);
			Object controllerAction = SpringContextInvoke.getBean(controllerClass);

			Method execute = controllerClass.getMethod("execute", HttpServletRequest.class, HttpServletResponse.class);
			Success tempObj = (Success) execute.invoke(controllerAction, request, response);
			this.release(request, response);
			//tempObj.setModule(module);
			//if(tempObj.getCode().equals("-1")) return tempObj.getData("API_DATA");
			return tempObj;
		} catch (Exception e) {
			this.release(request, response);
			MDC.put("APP_NAME", "bwle_error");
			logger.error(e.getMessage(), e);
		}
		Success success = new Success();
		success.setSuccess(true);
		success.setErrorCode(ErrorCode.__F_SYS);
		return success;
	}

}
