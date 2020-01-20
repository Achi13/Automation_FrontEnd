package com.automation.campaign.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.automation.campaign.service.StoryService;

@Controller
@RequestMapping("/story")
public class StoryContoller {
	
	@Autowired
	StoryService storyService;
	
	@RequestMapping("/storypage/{themeId}")
	public ModelAndView storyPage(@PathVariable("themeId")long themeId,
			HttpSession session) {
		ModelAndView mv = new ModelAndView();
		mv = storyService.setStoryPage(themeId, session);
		return mv;
	}
	
	@RequestMapping("/modifystory")
	public ModelAndView modifyStory(@RequestParam("themeId")long themeId,
			@RequestParam("storyId")long storyId,
			@RequestParam("storyName")String storyName,
			@RequestParam("description")String description,
			@RequestParam("isServerImport")Boolean isServerImport,
			@RequestParam("isIgnoreSeverity")Boolean isIgnoreSeverity,
			HttpSession session,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv = storyService.modifyStory(themeId, storyId, storyName, description, isServerImport, isIgnoreSeverity, session, ra);
		return mv;
	}
	
	@RequestMapping("/archivestory/{storyId}")
	public ModelAndView archiveStory(@PathVariable("storyId")Long storyId,
			RedirectAttributes ra,
			HttpSession session) {
		ModelAndView mv = new ModelAndView();
		mv = storyService.archiveStory(storyId, ra, session);
		return mv;
	}
	
	@RequestMapping("/processcreatestory")
	public ModelAndView processCreateStory(@RequestParam("themeid")long themeId,
			@RequestParam("storyname")String storyName, @RequestParam("description")String description,
			@RequestParam("serverimport")boolean serverImport, @RequestParam("severity")boolean ignoreSeverity,
			HttpSession session, RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		log.info(serverImport + ": " + ignoreSeverity);
		try {
			mv = storyService.createStory(themeId, storyName, description, serverImport, ignoreSeverity, session, ra);
			return mv;
		}finally {
			log.info(serverImport + ": " + ignoreSeverity);
		}
		
	}
	
	@RequestMapping("/retrievearchivedstory")
	public ModelAndView retrieveArchivedStory(@RequestParam("storyId")List<Long>storyId,
			HttpSession session) {
		ModelAndView mv = new ModelAndView();
		mv = storyService.retrieveArchivedStory(storyId, session);
		return mv;
	}
}
