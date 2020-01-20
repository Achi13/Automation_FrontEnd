package com.automation.universe.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.automation.universe.service.ClientLoginAccountService;

@Controller
@RequestMapping("/clientloginaccount")
public class ClientLoginAccountController {
	
	@Autowired
	ClientLoginAccountService clientLoginAccountService;
	
	@RequestMapping("/setlogincredentialpage")
	public ModelAndView setLoginCredentialPage(HttpSession session,
			RedirectAttributes ra, Model model) {
		
		ModelAndView mv = new ModelAndView();
		
		mv = clientLoginAccountService.setLoginCredentialPage(session, ra, model);
		
		return mv;
		
	}
	
	@RequestMapping("/savelogincredential")
	public ModelAndView saveLoginCredential(@RequestParam("url")List<String>url,
			@RequestParam("counter")List<Integer>counter,
			@RequestParam("username")List<String>username,
			@RequestParam("password")List<String>password,
			HttpSession session,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv = clientLoginAccountService.saveLoginCredential(username, password, url, counter, session, ra);
		return mv;
	}
	
}
