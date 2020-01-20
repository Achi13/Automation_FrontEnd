package com.automation.campaign.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.automation.campaign.service.CampaignService;

@Controller
@RequestMapping("/campaign")
public class CampaignController {
	
	@Autowired
	CampaignService campaignService;
	//PAGE CONTROLLER---------------------------------------------------------------------------
	@RequestMapping("/campaignpage")
	public ModelAndView campaignPage(HttpSession session, Model model) {
		ModelAndView mv = new ModelAndView();
		mv = campaignService.setCampaignPage(session, model);
		return mv;
	}
	
	@RequestMapping("/modifycampaign")
	public ModelAndView modifyCampaign(@RequestParam("campaignId")long campaignId,
			@RequestParam("campaignName")String campaignName,
			@RequestParam("description")String description,
			HttpSession session,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv = campaignService.modifyCampaign(campaignName, description, campaignId, session, ra);
		return mv;
	}
	
	@RequestMapping("/dashboardpage")
	public ModelAndView dashboardPage(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		mv = campaignService.setOneReportPage(session);
		return mv;
	}
	
	//CAMPAIGN CONTROLLER--------------------------------------------------------------
	@RequestMapping("/processcreatecampaign")
	public ModelAndView processCreateCampaign(@RequestParam("campaignname")String campaignName,
			@RequestParam("description")String description, HttpSession session,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv = campaignService.createCampaign(campaignName, description, session, ra);
		return mv;
	}
	
	@RequestMapping("/executecampaign/{campaignId}")
	public ModelAndView executeCampaign(@PathVariable("campaignId")long campaignId, HttpSession session,
			RedirectAttributes ra) {
		Logger log = Logger.getLogger(getClass());
		log.info("access: /execute campaign");
		ModelAndView mv = new ModelAndView();
		mv = campaignService.executeCampaign(campaignId, session, ra);
		return mv;
	}
	
	@RequestMapping("/archivecampaign/{campaignId}")
	public ModelAndView archiveCampaign(@PathVariable("campaignId")Long campaignId,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv = campaignService.archiveCampaign(campaignId, ra);
		return mv;
	}
	
	@RequestMapping("/retrievearchivedcampaign")
	public ModelAndView retrieveArchivedCampaign(@RequestParam("campaignId")List<Long>campaignId) {
		
		ModelAndView mv = new ModelAndView();
		mv = campaignService.retrieveArchivedCampaign(campaignId);
		return mv;
		
	}
	
}
