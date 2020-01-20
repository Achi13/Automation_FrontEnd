package com.automation.universe.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.automation.universe.service.ClientService;

@Controller
@RequestMapping("/client")
public class ClientController {
	
	@Autowired
	ClientService clientService;
	
	@RequestMapping("/createtestcasepage")
	public ModelAndView createTestcasePage(@QueryParam("storyId")String storyId, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		mv = clientService.setCreateTestcasePage(storyId, session);
		return mv;
	}
	
	@RequestMapping("/createclientpage")
	public ModelAndView createClientPage(HttpSession session, Model model) {
		
		ModelAndView mv = new ModelAndView();
		
		mv = clientService.setCreateClientPage(session, model);
		
		return mv;
		
	}
	
	@RequestMapping("/clientsummary")
	public ModelAndView clientSummary(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		
		mv = clientService.clientSummary(session);
		
		return mv;
	}
	
	@RequestMapping("/saveclient")
	public ModelAndView saveClient(@RequestParam("clientName")String clientName,
			@RequestParam(value="loginRestrictionFlag", required=false)List<Boolean>loginRestrictionFlag,
			@RequestParam("url")List<String>url,
			@RequestParam("name")List<String>name,
			@RequestParam("description")List<String>description,
			HttpSession session,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		
		mv = clientService.saveClient(clientName, loginRestrictionFlag, url, name, description, session, ra);
		
		return mv;
	}
	
	@RequestMapping("/confirmsummary")
	public ModelAndView confirmSummary(HttpSession session, RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		
		mv = clientService.confirmSummary(session, ra);
		
		return mv;
	}

}
