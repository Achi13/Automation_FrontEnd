package com.automation.campaign.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.automation.campaign.domain.Campaign;
import com.automation.campaign.domain.Theme;
import com.automation.configuration.beans.UrlBeans;
import com.automation.universe.domain.Universe;
import com.automation.user.domain.User;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Service
public class ThemeService {
	
	//THEME SERVICE----------------------------------------------------------------------------------------------------
	public ModelAndView createTheme(long campaignId, String themeName, String description,
			HttpSession session, RedirectAttributes ra) {
		
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		Universe universe = (Universe) session.getAttribute("universe");
		User user = (User) session.getAttribute("user");
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("campaignId", campaignId);
		jsonData.put("themeName", themeName);
		jsonData.put("description", description);
		jsonData.put("universeId", universe.getUniverseId());
		jsonData.put("userId", user.getUserId());
		
		String url = String.format("%s/%s", UrlBeans.THEME_SERVICE, "processcreatetheme");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		
		ra.addFlashAttribute("Info", jsonResponse.get("Info"));
		mv.setViewName("redirect:/theme/themepage/" + campaignId);
		
		return mv;
		
	}
	
	public ModelAndView modifyTheme(long campaignId, long themeId,
			String themeName, String description,
			HttpSession session, RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("themeId", themeId);
		jsonData.put("themeName", themeName);
		jsonData.put("description", description);
		
		String url = String.format("%s/%s", UrlBeans.THEME_SERVICE, "processmodifytheme");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		
		ra.addFlashAttribute("Info", jsonResponse.get("Info"));
		mv.setViewName("redirect:/theme/themepage/" + campaignId);
		return mv;
	}
	
	public ModelAndView archiveTheme(long themeId, RedirectAttributes ra,
			HttpSession session) {
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		String url = String.format("%s/%s", UrlBeans.THEME_SERVICE, "archivetheme");
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("themeId", themeId);
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		
		if(clientResponse.getStatus() == 200) {
			ra.addFlashAttribute("Info", jsonResponse.get("Info"));
			mv.setViewName("redirect:/theme/themepage/" + session.getAttribute("campaignId"));
			return mv;
		}else {
			ra.addFlashAttribute("Error", jsonResponse.get("Error"));
			mv.setViewName("redirect:/theme/themepage/" + session.getAttribute("campaignId"));
			return mv;
		}
		
	}
	
	public ModelAndView setThemePage(long campaignId, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		User userr = (User)session.getAttribute("user");
		
		session.setAttribute("campaignId", campaignId);
		
		String url = String.format("%s/%s?campaignId=%d", UrlBeans.THEME_SERVICE, "getthemedata", campaignId);
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		JSONArray jsonArray = jsonResponse.getJSONArray("themeData");
		
		List<Theme>themeList = new ArrayList<Theme>();
		
		for(int i=0; i<jsonArray.length(); i++) {
			
			JSONObject themeData = jsonArray.getJSONObject(i);
			JSONObject campaignData = themeData.getJSONObject("campaignId");
			JSONObject universeId = themeData.getJSONObject("universeId");
			JSONObject userId = themeData.getJSONObject("userId");
			
			User user = new User(userId.toString());
			Universe universe = new Universe(universeId.toString());
			Campaign campaign = new Campaign(campaignData.toString(), user, universe);
			
			Theme theme = new Theme(themeData.toString(), campaign, user, universe);
			
			themeList.add(theme);
			
		}
		
		JSONArray archiveThemeArray = jsonResponse.getJSONArray("themeArchive");
		List<Theme>archiveTheme = new ArrayList<Theme>();
		
		for(int i=0; i<archiveThemeArray.length(); i++) {
			
			JSONObject archiveData = archiveThemeArray.getJSONObject(i);
			
			
			
			Theme theme = new Theme();
			theme.setThemeName(archiveData.getString("themeName"));
			theme.setThemeId(archiveData.getLong("themeId"));
			theme.setTimestamp(archiveData.getString("timestamp"));
			theme.setDescription(archiveData.getString("description"));
			
			
			archiveTheme.add(theme);
		}
		
		mv.addObject("campaignId", campaignId);
		mv.addObject("themeData", themeList);
		mv.addObject("archiveData", archiveTheme);
		mv.addObject("user", userr.getUsername());
		mv.setViewName("theme");
		return mv;
		
	}
	
	public ModelAndView retrieveArchivedTheme(List<Long>themeId,
			HttpSession session) {
		
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		JSONArray jsonArray = new JSONArray();
		
		for(Long id : themeId) {
			jsonArray.put(id);
		}
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("themeIdList", jsonArray);
		
		
		String url = String.format("%s/%s", UrlBeans.ARCHIVE_SERVICE, "reverseArchiveTheme");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		//JSONObject jsonResponse = new JSONObject(response);
		
		mv.setViewName("redirect:/theme/themepage/" + session.getAttribute("campaignId"));
		return mv;
		
	}
	
}
