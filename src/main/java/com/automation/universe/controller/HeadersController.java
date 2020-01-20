package com.automation.universe.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.automation.universe.service.HeadersService;

@Controller
@RequestMapping("/headers")
public class HeadersController {
	
	@Autowired
	HeadersService headersService;
	
	@RequestMapping("/setloginheaderpage")
	public ModelAndView setLoginHeaderPage(HttpSession session) {
		
		ModelAndView mv = new ModelAndView();
		mv = headersService.setLoginHeaderPage(session);
		
		return mv;
	}
	
	@RequestMapping("/saveheaders")
	public ModelAndView saveHeaders(@RequestParam("url")List<String>url,
			@RequestParam("counter")List<Integer>counter,
			@RequestParam("webElementNature")List<String>webElementNature,
			@RequestParam("webElementName")List<String>webElementName,
			@RequestParam("natureOfAction")List<String>natureOfAction,
			@RequestParam("label")List<String>label,
			HttpSession session,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView(); 
		
		mv = headersService.saveHeaders(url, counter, label, webElementNature, natureOfAction, webElementName, session, ra);
		
		return mv;
	}
	
	
}
