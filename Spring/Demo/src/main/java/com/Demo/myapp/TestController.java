package com.Demo.myapp;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpServerErrorException;

import com.Demo.myapp.bean.TestBean;
import com.Demo.myapp.service.TestService;



@Controller

public class TestController {

	@Inject
	TestService service;


	@RequestMapping(value="/test",method = RequestMethod.GET)
	public String test(Model model) throws Exception {
		List<TestBean> list;		
		list = service.test();	
		model.addAttribute("list",list);	
		return "test";

	}
	
	

}