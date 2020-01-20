package com.automation.campaign.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.automation.campaign.service.ThemeService;

@Controller
@RequestMapping("/theme")
public class ThemeController {
	
	@Autowired
	ThemeService themeService;
	
	//THEME CONTROLLER-------------------------------------------------------------
	@RequestMapping("/themepage/{campaignId}")
	public ModelAndView themePage(@PathVariable("campaignId")long campaignId,
			HttpSession session) {
		ModelAndView mv = new ModelAndView();
		mv = themeService.setThemePage(campaignId, session);
		return mv;
	}
	
	@RequestMapping("/modifytheme")
	public ModelAndView modifyTheme(@RequestParam("campaignId")long campaignId,
			@RequestParam("themeId")long themeId,
			@RequestParam("themeName")String themeName,
			@RequestParam("description")String description,
			HttpSession session,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv = themeService.modifyTheme(campaignId, themeId, themeName, description, session, ra);
		return mv;
	}
	
	@RequestMapping("/processcreatetheme")
	public ModelAndView processCreateTheme(@RequestParam("campaignid")long campaignId,
			@RequestParam("themename")String themeName, @RequestParam("description")String description,
			HttpSession session, RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv = themeService.createTheme(campaignId, themeName, description, session, ra);
		return mv;
		
	}
	
	@RequestMapping("/archivetheme/{themeId}")
	public ModelAndView archiveCampaign(@PathVariable("themeId")Long themeId,
			RedirectAttributes ra,
			HttpSession session) {
		ModelAndView mv = new ModelAndView();
		mv = themeService.archiveTheme(themeId, ra, session);
		return mv;
	}
	
	@RequestMapping("/retrievearchivedtheme")
	public ModelAndView retrieveArchivedTheme(@RequestParam("themeId")List<Long>themeId,
			HttpSession session) {
		
		ModelAndView mv = new ModelAndView();
		mv = themeService.retrieveArchivedTheme(themeId, session);
		return mv;
		
	}
	
}
