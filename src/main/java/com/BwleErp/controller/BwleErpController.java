package com.BwleErp.controller;

import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.BwleErp.config.Config;
import com.BwleErp.service.impl.employee.EmployeeServiceImpl;
import com.base.controller.AbstractController;
import com.base.controller.SpringContextInvoke;
import com.base.model.dto.BwleErp.EmployeePermissionDto;
import com.base.type.ErrorCode;
import com.base.type.Success;
import com.base.util.EncryptUtiliy;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/erp")
public class BwleErpController extends AbstractController {
	private final String thisController = "BwleErp";
	private int noLogin = 1;
	private String employeeCookieName = "token";
	//private 
	@Autowired
	private EmployeeServiceImpl employeeService;

	@Override
	public void beforeCheck(HttpServletRequest request, HttpServletResponse response) {
		
		String eidDecryptHeader = request.getHeader(this.employeeCookieName);
		String eidDecryptCookie = this.getCookie(request, this.employeeCookieName);
		String eidDecrypt = eidDecryptHeader == null || eidDecryptHeader.equals("") ? eidDecryptCookie : eidDecryptHeader;	
		int employee_id = 0;//获取登录的用户ID
		try {
			employee_id = EncryptUtiliy.instance().myIDDecrypt(eidDecrypt);
			if (employee_id > 0) {
				this.noLogin = 0;
			} else {
				this.noLogin = 1;
			}
			request.setAttribute("employee_id", employee_id);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	@Override
	public void release(HttpServletRequest request, HttpServletResponse response) {
		
	}

	@Override
	@RequestMapping(value = "/")//value = "/**"
	public String defaultAction(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		model.addAttribute("__WEB", Config.webUrl);
		model.addAttribute("__IMGWEB", Config.uploadImagesUrl);
		model.addAttribute("__FILEWEB", Config.uploadFileUrl);
		model.addAttribute("__RESOURCE", Config.resourceUrl);
		model.addAttribute("__VERSION", "");
		model.addAttribute("__TITLE", "博威利尔.ERP ᵛ¹·⁰·⁰ ");
		model.addAttribute("__ImagesUploadUrl", EncryptUtiliy.instance().intIDEncrypt(114));
		model.addAttribute("__ImagesManagerUrl", EncryptUtiliy.instance().intIDEncrypt(115));
		model.addAttribute("__AuditingViewUrl", EncryptUtiliy.instance().intIDEncrypt(117));
		//model.addAttribute("thisDateTime", Utiliy.instance().getTodayDate());
		model.addAttribute("noLogin", this.noLogin);
		return "BwleErp/default";
	}

	/*@RequestMapping({ "index", "index.html", "default.html", "/", "/**", "*", "" })
	public String indexAction(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		model.addAttribute("__TITLE", "welcome");
		model.addAttribute("__VERSION", "1.20");
		model.addAttribute("__RESOURCE", "/static/");
		Date dateNow = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		model.addAttribute("thisDateTime", dateFormat.format(dateNow));
		model.addAttribute("home_channel", "");
		model.addAttribute("noLogin", this.noLogin);

		return "404";
	}*/
	///module/method?action=actionX
	@RequestMapping(value = "/{module}/{method}")
	@ResponseBody
	public Object doGetAndPost(@PathVariable("module") String module, @PathVariable("method") String method,
			HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("module", module);
		request.setAttribute("method", method);
		if(request.getHeader("method") != null) request.setAttribute("method", request.getHeader("method"));
		request.setAttribute("action", request.getParameter("action"));
		return this.excuteController(request, response);
	}

	@RequestMapping(value = "/app.do") // 处理JSON post
	@ResponseBody // 数据app.do?module=module&method=method&action=action {@RequestBody Map<String, Object> postJsonMap}
	public Object doPostJson(@RequestBody Map<String, Object> postJsonMap, HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute("module", request.getParameter("module"));
		request.setAttribute("method", request.getParameter("method"));
		if(request.getHeader("method") != null) request.setAttribute("method", request.getHeader("method"));
		request.setAttribute("action", request.getParameter("action"));
		for (String key : postJsonMap.keySet()) { //key=>value的value有可能是一组数据模型
			Object object = postJsonMap.get(key);
			request.setAttribute(key, object);
		}
		return this.excuteController(request, response);
	}

	public Object excuteController(HttpServletRequest request, HttpServletResponse response) {
		try {
			String module = "Index";
			String actionInvoke = "Index";
			Object method = request.getAttribute("method");
			if(method == null) method = "";
			//除去checkLogin\logout\forgetPassword\refresh 都需要檢查權限
			//檢查權限
			if(method.equals("logout") || method.equals("checkLogin") || method.equals("refresh")) {
			} else {
				String channel = request.getParameter("channel");
				int module_id = EncryptUtiliy.instance().intIDDecrypt(channel);
				EmployeePermissionDto employeePermission = employeeService.permissionCheck(module_id, (int)request.getAttribute("employee_id"));
				if(!employeePermission.isPermission()) {
					Success success = new Success();
					success.setErrorCode(ErrorCode.__F_PERMISSION);
					success.setMessage("缺少"+employeePermission.getModule_name()+"权限");
					return success;
				}
				if(employeePermission.getModule_id() > 0) {
					actionInvoke = employeePermission.getAction();
					module = employeePermission.getModule();
					request.setAttribute("module_id", module_id);
				}
			}
			
			//action
			String action = (new StringBuilder()).append(Character.toUpperCase(actionInvoke.charAt(0)))
					.append(actionInvoke.substring(1)).toString() + "Action";
			//module
			//module = module.toLowerCase();

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
			Throwable cause = e.getCause(); // 获取实际抛出的异常
			if (cause != null) {
				System.out.println("Actual Exception: " + cause);
			}
			e.printStackTrace();
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

