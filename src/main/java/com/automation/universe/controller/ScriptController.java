package com.automation.universe.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.automation.universe.service.ScriptService;

@Controller
@RequestMapping("/script")
public class ScriptController {
	
	@Autowired
	ScriptService scriptService;
	
	@RequestMapping("/scriptpage")
	public ModelAndView scriptPage(HttpSession session, Model model) {
		ModelAndView mv = new ModelAndView();
		mv = scriptService.setScriptPage(session, model);
		return mv;
	}
	
	@RequestMapping("/addscriptpage/{loginAccountId}")
	public ModelAndView addScriptPage(@PathVariable("loginAccountId")long loginAccountId,
			HttpSession session) {
		ModelAndView mv = new ModelAndView();
		mv = scriptService.setAddScriptPage(session, loginAccountId);
		return mv;
	}
	
	@RequestMapping("/processcreatecredential")
	public ModelAndView processCreateCredential(@RequestParam("hostname")String hostname,
			@RequestParam("username")String username,
			@RequestParam("password")String password,
			@RequestParam("ppkFile")String ppkFile,
			@RequestParam("description")String description,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv = scriptService.processCreateCredential(hostname, username, password, ppkFile, description, ra);
		return mv;
	}
	
	@RequestMapping("/processaddscriptvariable")
	public ModelAndView processAddScriptVariable(@RequestParam("name")List<String>name,
			@RequestParam("description")List<String>description,
			@RequestParam("scriptId")long scriptId,
			HttpSession session,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv = scriptService.processAddScriptVariable(name, description, scriptId, session, ra);
		return mv;
	}
	
	@RequestMapping("/processaddscript")
	public ModelAndView processAddScript(@RequestParam("loginAccountId")long loginAccountId,
			@RequestParam("name")String name,
			@RequestParam("shFile")String shFile,
			@RequestParam("description")String description,
			@RequestParam("noOfVar")int noOfVar,
			RedirectAttributes ra,
			HttpSession session) {
		ModelAndView mv = new ModelAndView();
		mv = scriptService.processAddScript(loginAccountId, name, shFile, description, noOfVar, ra, session);
		return mv;
	}
}
