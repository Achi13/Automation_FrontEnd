package com.automation.user.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.automation.user.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@RequestMapping("/officerpage")
	public ModelAndView officerPage(Model model, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		mv = userService.setOfficerPage(model, session);
		return mv;
	}
	
	@RequestMapping("/accountsetingspage")
	public ModelAndView accountSettingsPage(HttpSession session,
			Model model) {
		ModelAndView mv = new ModelAndView();
		mv = userService.setAccountSettingsPage(session, model);
		return mv;
		
	}
	
	//===================================LOGIN CONTROLLER=================================
	@RequestMapping("/processlogin")
	public ModelAndView processLogin(@RequestParam("uname")String username,
			@RequestParam("pass")String password, HttpSession session,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv = userService.processLogin(username, password, session, ra);
		return mv;
	}
	
	@RequestMapping("/processregister")
	public ModelAndView processRegister(@RequestParam("username")String username,
			@RequestParam("password")String password,
			@RequestParam("usertype")String usertype,
			@RequestParam("useraccess")String useraccess,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv = userService.processRegister(username, password, useraccess, usertype, ra);
		return mv;
	}
	
	@RequestMapping("/processupdate")
	public ModelAndView processUpdate(@RequestParam("userId")long userId,
			@RequestParam("userType")String userType,
			@RequestParam("status")String status,
			@RequestParam("password")String password,
			@RequestParam("universeAccessList")String universeAccessList,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv = userService.processUpdate(userId, userType, status, password, universeAccessList, ra);
		return mv;
	}
	
	@RequestMapping("/processupdateuser")
	public ModelAndView processUpdateUser(@RequestParam("oldPassword")String oldPassword,
			@RequestParam("newPassword")String newPassword,
			@RequestParam("confirmPassword")String confirmPassword,
			HttpSession session,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv = userService.updateOwnAccount(oldPassword, newPassword, confirmPassword, session, ra);
		return mv;
	}
	
	
}
