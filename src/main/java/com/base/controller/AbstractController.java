package com.base.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.fasterxml.jackson.databind.ObjectMapper;

import core.util.Cookies;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public abstract class AbstractController {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	protected static ObjectMapper mapper = new ObjectMapper();
	protected final String POST_MAP_KEY = "postMap_key";

	public abstract void beforeCheck(HttpServletRequest request, HttpServletResponse response);

	public abstract void release(HttpServletRequest request, HttpServletResponse response);

	public abstract String defaultAction(HttpServletRequest request, HttpServletResponse response, ModelMap model);

	public AbstractController() {
	
	}
	
	@ModelAttribute
	public void excuseController(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		this.beforeCheck(httpRequest, httpResponse);
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

}
