package com.automation.universe.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.automation.universe.service.UniverseService;

@Controller
@RequestMapping("/universe")
public class UniverseController {
	
	@Autowired
	UniverseService universeService;
	
	@RequestMapping("/universepage")
	public ModelAndView universePage(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		mv = universeService.setUniversePage(session);
		return mv;
		
	}
	
	@RequestMapping("/setuniverse")
	public ModelAndView setUniverse(@RequestParam("universeid")long universeId,
			HttpSession session) {
		ModelAndView mv = new ModelAndView();
		mv = universeService.setUniverseSession(universeId, session);
		return mv;
	}
	
}
