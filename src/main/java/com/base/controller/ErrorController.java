package com.base.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController extends AbstractController {

	@Override
	public void beforeCheck(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
	}

	@Override
	public void release(HttpServletRequest request, HttpServletResponse response) {
	}

	@Override
	@RequestMapping(value = {"**","/"})
	public String defaultAction(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		model.addAttribute("__RESOURCE", "/resource/");
		// TODO Auto-generated method stub
		return "404";
	}

}
