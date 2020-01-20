package com.automation.campaign.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.automation.campaign.domain.Campaign;
import com.automation.campaign.domain.DependentTestcase;
import com.automation.campaign.domain.Story;
import com.automation.campaign.domain.Theme;
import com.automation.configuration.beans.UrlBeans;
import com.automation.universe.domain.Universe;
import com.automation.user.domain.User;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Service
public class CampaignService {
	
	//CAMPAIGN SERVICE-----------------------------------------------------------
	
	//MODEL AND VIEW FUNCTIONS=====================================================================================================
	
	//===================================================CREATE CAMPAIGN=============================================================
	public ModelAndView createCampaign(String campaignName, String description, HttpSession session,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		Universe universe = (Universe) session.getAttribute("universe");
		User user = (User) session.getAttribute("user");
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("campaignName", campaignName);
		jsonData.put("description", description);
		jsonData.put("universeId", universe.getUniverseId());
		jsonData.put("userId", user.getUserId());
		
		String url = String.format("%s/%s", UrlBeans.CAMPAIGN_SERVICE, "processcreatecampaign");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		ra.addFlashAttribute("Info", jsonResponse.get("Info"));
		mv.setViewName("redirect:/campaign/campaignpage");
		return mv;
	}
	
	public ModelAndView modifyCampaign(String campaignName, String description,
			long campaignId, HttpSession session, RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("campaignId", campaignId);
		jsonData.put("campaignName", campaignName);
		jsonData.put("description", description);
		
		String url = String.format("%s/%s", UrlBeans.CAMPAIGN_SERVICE, "processmodifycampaign");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		
		ra.addFlashAttribute("Info", jsonResponse.get("Info"));
		mv.setViewName("redirect:/campaign/campaignpage");
		return mv;
	}
	
	public ModelAndView setCampaignPage(HttpSession session, Model model) {
		
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		User user = (User) session.getAttribute("user");
		
		String url = String.format("%s/%s?userId=%d", UrlBeans.CAMPAIGN_SERVICE, "getcampaigndata", user.getUserId());
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		String response = new String(clientResponse.getEntity(String.class));
		
		
		
		List<Campaign>campaignList = new ArrayList<Campaign>();
		
		JSONObject jsonResponse = new JSONObject(response);
		JSONArray jsonArray = jsonResponse.getJSONArray("campaignList");
		
		
		
		JSONObject resultData = jsonResponse.getJSONObject("checker");
		
		log.info(jsonResponse.get("campaignArchiveList"));
		
		for(int i=0; i<jsonArray.length(); i++) {
			JSONObject campaignData = jsonArray.getJSONObject(i);
			JSONObject userId = campaignData.getJSONObject("userId");
			JSONObject universeId = campaignData.getJSONObject("universeId");
			
			
			log.info(campaignData);
			
			
			User userData = new User(userId.toString());
			Universe universeData = new Universe(universeId.toString());
			
			Campaign campaign = new Campaign(campaignData.toString(), userData, universeData);
			
			JSONArray checkArray = resultData.getJSONArray(Long.toString(campaign.getCampaignId()));
			if(checkArray.length() == 0) {
				campaign.setSuccess(true);
			}else {
				String testcaseList = "";
				for(int j=0; j<checkArray.length(); j++) {
					testcaseList = testcaseList + " " + checkArray.getString(j);
				}
				campaign.setSuccess(false);
				campaign.setTestcaseList(testcaseList);
			}
			
			campaignList.add(campaign);
			
		}
		
		JSONArray archiveCampaignArray = jsonResponse.getJSONArray("campaignArchiveList");
		List<Campaign>archiveCampaign = new ArrayList<Campaign>();
		
		for(int i=0; i<archiveCampaignArray.length(); i++) {
			
			JSONObject archiveData = archiveCampaignArray.getJSONObject(i);
			
			Campaign campaign = new Campaign();
			campaign.setCampaignId(archiveData.getLong("campaignId"));
			campaign.setCampaignName(archiveData.getString("campaignName"));
			campaign.setTimestamp(archiveData.getString("timestamp"));
			campaign.setDescription(archiveData.getString("description"));
			
			archiveCampaign.add(campaign);
		}
		
		
		mv.addObject("Info", model.asMap().get("Info"));
		mv.addObject("Error", model.asMap().get("Error"));
		mv.addObject("archiveCampaign", archiveCampaign);
		mv.addObject("user", user.getUsername());
		mv.addObject("campaignData", campaignList);
		mv.setViewName("campaign");
		return mv;
		
	}
	
	public ModelAndView setOneReportPage(HttpSession session) {
		
		
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		Universe universe = (Universe)session.getAttribute("universe");
		User user = (User)session.getAttribute("user");
		
		String url = String.format("%s/%s?userId=%d&universeId=%d", UrlBeans.CAMPAIGN_SERVICE, "getallcampaigndatabyuser", user.getUserId(),
				universe.getUniverseId());
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		
		JSONArray campaignArray = jsonResponse.getJSONArray("campaignList");
		JSONArray themeArray = jsonResponse.getJSONArray("themeList");
		JSONArray storyArray = jsonResponse.getJSONArray("storyList");
		JSONArray dependentTestcaseArray = jsonResponse.getJSONArray("dependentTestcaseList");
		
		
		List<Campaign>campaignList = new ArrayList<Campaign>();
		for(int i=0; i<campaignArray.length(); i++) {
			
			JSONObject campaignData = campaignArray.getJSONObject(i);
			
			Campaign campaign = new Campaign();
			campaign.setCampaignId(campaignData.getLong("campaignId"));
			campaign.setCampaignName(campaignData.getString("campaignName"));
			campaign.setStatus(campaignData.getString("status"));
			
			campaignList.add(campaign);
		}
		
		List<Theme>themeList = new ArrayList<Theme>();
		for(int i=0; i<themeArray.length(); i++) {
			
			JSONObject themeData = themeArray.getJSONObject(i);
			JSONObject campaignData = themeData.getJSONObject("campaignId");
			
			Campaign campaign = new Campaign();
			campaign.setCampaignId(campaignData.getLong("campaignId"));
			campaign.setCampaignName(campaignData.getString("campaignName"));
			campaign.setStatus(campaignData.getString("status"));
			
			Theme theme = new Theme(themeData, campaign);
			
			themeList.add(theme);
		}
		
		List<Story>storyList = new ArrayList<Story>();
		for(int i=0; i<storyArray.length(); i++) {
			
			JSONObject storyData = storyArray.getJSONObject(i);
			JSONObject themeData = storyData.getJSONObject("themeId");
			Theme theme = new Theme();
			theme.setThemeId(themeData.getLong("themeId"));
			
			Story story = new Story(storyData.toString(), theme);
			storyList.add(story);
		}
		
		List<DependentTestcase>dependentTestcaseList = new ArrayList<DependentTestcase>();
		for(int i=0; i<dependentTestcaseArray.length(); i++) {
			
			JSONObject dependentTestcaseData = dependentTestcaseArray.getJSONObject(i);
			JSONObject storyData = dependentTestcaseData.getJSONObject("storyId");
			
			Story story = new Story();
			story.setStoryId(storyData.getLong("storyId"));
			
			DependentTestcase dTestcase = new DependentTestcase();
			dTestcase.setTestcaseNumber(dependentTestcaseData.getString("testcaseNumber"));
			dTestcase.setStatus(dependentTestcaseData.getString("status"));
			dTestcase.setStoryId(story);
			
			dependentTestcaseList.add(dTestcase);
		}
		
		mv.addObject("campaignList", campaignList);
		mv.addObject("themeList", themeList);
		mv.addObject("storyList", storyList);
		mv.addObject("dependentTestcaseList", dependentTestcaseList);
		mv.addObject("user", user.getUsername());
		mv.setViewName("dashboard");
		return mv;
	}
	
	public ModelAndView executeCampaign(long campaignId, HttpSession session, 
			RedirectAttributes ra) {
		
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		String url = String.format("%s/%s?campaignId=%d", UrlBeans.CAMPAIGN_SERVICE, "executecampaign", campaignId);
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		String response = new String(clientResponse.getEntity(String.class));
		
		JSONObject jsonResponse = new JSONObject(response);
		
		log.info(response);
		
		if(clientResponse.getStatus() == 200) {
			ra.addFlashAttribute("Info", jsonResponse.get("Info"));
			mv.setViewName("redirect:/campaign/campaignpage");
			return mv;
		}else {
			ra.addFlashAttribute("Error", jsonResponse.get("Error"));
			mv.setViewName("redirect:/campaign/campaignpage");
			return mv;
		}
		
	}
	
	public ModelAndView archiveCampaign(long campaignId, RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		String url = String.format("%s/%s", UrlBeans.CAMPAIGN_SERVICE, "archivecampaign");
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("campaignId", campaignId);
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		
		if(clientResponse.getStatus() == 200) {
			ra.addFlashAttribute("Info", jsonResponse.get("Info"));
			mv.setViewName("redirect:/campaign/campaignpage");
			return mv;
		}else {
			ra.addFlashAttribute("Error", jsonResponse.get("Error"));
			mv.setViewName("redirect:/campaign/campaignpage");
			return mv;
		}
		
	}
	
	public ModelAndView retrieveArchivedCampaign(List<Long>campaignId) {
		
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		JSONArray jsonArray = new JSONArray();
		
		for(Long id : campaignId) {
			jsonArray.put(id);
		}
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("campaignIdList", jsonArray);
		
		
		String url = String.format("%s/%s", UrlBeans.ARCHIVE_SERVICE, "reverseArchiveCampaign");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		
		mv.addObject("Info", jsonResponse.get("Info"));
		mv.setViewName("redirect:/campaign/campaignpage");
		return mv;
	}
	
}
