package com.automation.universe.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.automation.universe.service.TapServerCredentialService;

@Controller
@RequestMapping("/tapservercredential")
public class TapServerCredentialController {
	
	@Autowired
	TapServerCredentialService tapServerService;
	
	@RequestMapping("/tapconnectionpage")
	public ModelAndView setTapConnectionPage(HttpSession session) {
		
		ModelAndView mv = new ModelAndView();
		
		mv = tapServerService.setTapConnectionPage(session);
		
		return mv;
	}
	
	@RequestMapping("/savetapconnection")
	public ModelAndView saveTapConnection(@RequestParam("url")List<String>url,
			@RequestParam("username")List<String>username,
			@RequestParam("password")List<String>password,
			@RequestParam("hostname")List<String>hostname,
			@RequestParam("ppkFile")List<String>ppkFile,
			HttpSession session) {
		ModelAndView mv = new ModelAndView();
		
		mv = tapServerService.saveTapConnection(url, username, password, hostname, ppkFile, session);
		
		return mv;
	}
	
	@RequestMapping("/uploadppk")
	public ModelAndView uploadPpk(@RequestParam("ppk")MultipartFile ppkFile) {
		ModelAndView mv = new ModelAndView();
		
		return mv;
	}
	
	
}
