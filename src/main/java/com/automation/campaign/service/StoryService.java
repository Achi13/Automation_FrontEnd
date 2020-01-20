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

import com.automation.campaign.domain.Story;
import com.automation.configuration.beans.UrlBeans;
import com.automation.universe.domain.Universe;
import com.automation.user.domain.User;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Service
public class StoryService {
	
	public ModelAndView createStory(long themeId, String storyName, String description,
			boolean serverImport, boolean ignoreSeverity, HttpSession session, RedirectAttributes ra) {
		
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		log.info(serverImport + ": " + ignoreSeverity);
		
		Universe universe = (Universe) session.getAttribute("universe");
		User user = (User) session.getAttribute("user");
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("themeId", themeId);
		jsonData.put("storyName", storyName);
		jsonData.put("description", description);
		jsonData.put("isServerImport", serverImport);
		jsonData.put("isIgnoreSeverity", ignoreSeverity);
		jsonData.put("universeId", universe.getUniverseId());
		jsonData.put("userId", user.getUserId());
		
		String url = String.format("%s/%s", UrlBeans.STORY_SERVICE, "processcreatestory");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		
		ra.addFlashAttribute("Info", jsonResponse.get("Info"));
		mv.setViewName("redirect:/story/storypage/" + themeId);
		return mv;
	}
	
	public ModelAndView modifyStory(long themeId, long storyId,
			String storyName, String description,
			Boolean isServerImport, Boolean isIgnoreSeverity,
			HttpSession session, RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		log.info(isServerImport + ": " + isIgnoreSeverity);
		
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("storyId", storyId);
		jsonData.put("storyName", storyName);
		jsonData.put("description", description);
		jsonData.put("isServerImport", isServerImport);
		jsonData.put("isIgnoreSeverity", isIgnoreSeverity);
		
		String url = String.format("%s/%s", UrlBeans.STORY_SERVICE, "processmodifystory");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		
		ra.addFlashAttribute("Info", jsonResponse.get("Info"));
		mv.setViewName("redirect:/story/storypage/" + themeId);
		return mv;
	}
	
	public ModelAndView setStoryPage(long themeId, HttpSession session) {
		
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		User userr = (User)session.getAttribute("user");
		
		session.setAttribute("themeId", themeId);
		
		String url = String.format("%s/%s?themeId=%d", UrlBeans.STORY_SERVICE, "getstorydata", themeId);
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		JSONArray jsonArray = jsonResponse.getJSONArray("storyData");
		
		List<Story>storyList = new ArrayList<Story>();
		
		for(int i=0; i<jsonArray.length(); i++) {
			
			JSONObject storyData = jsonArray.getJSONObject(i);
			JSONObject universeId = storyData.getJSONObject("universeId");
			JSONObject userId = storyData.getJSONObject("userId");
			
			Universe universe = new Universe(universeId.toString());
			User user = new User(userId.toString());
			
			Story story = new Story(storyData.toString(), universe, user);
			
			storyList.add(story);			
		}
		
		JSONArray archiveStoryArray = jsonResponse.getJSONArray("storyArchive");
		List<Story>archiveStory = new ArrayList<Story>();
		
		for(int i=0; i<archiveStoryArray.length(); i++) {
			
			JSONObject archiveData = archiveStoryArray.getJSONObject(i);
			
			Story story = new Story();
			story.setStoryId(archiveData.getLong("storyId"));
			story.setStoryName(archiveData.getString("storyName"));
			story.setTimestamp(archiveData.getString("timestamp"));
			story.setDescription(archiveData.getString("description"));
			
			archiveStory.add(story);
		}
		
		
		
		mv.addObject("themeId", themeId);
		mv.addObject("storyData", storyList);
		mv.addObject("archiveData", archiveStory);
		mv.addObject("user", userr.getUsername());
		mv.setViewName("story");
		return mv;
		
	}
	
	public ModelAndView archiveStory(long storyId, RedirectAttributes ra,
			HttpSession session) {
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		String url = String.format("%s/%s", UrlBeans.STORY_SERVICE, "archivestory");
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("storyId", storyId);
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		
		if(clientResponse.getStatus() == 200) {
			ra.addFlashAttribute("Info", jsonResponse.get("Info"));
			mv.setViewName("redirect:/story/storypage/" + session.getAttribute("themeId"));
			return mv;
		}else {
			ra.addFlashAttribute("Error", jsonResponse.get("Error"));
			mv.setViewName("redirect:/story/storypage/" + session.getAttribute("themeId"));
			return mv;
		}
		
	}
	
	public ModelAndView retrieveArchivedStory(List<Long>storyId,
			HttpSession session) {
		
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		JSONArray jsonArray = new JSONArray();
		
		for(Long id : storyId) {
			jsonArray.put(id);
		}
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("storyIdList", jsonArray);
		
		
		String url = String.format("%s/%s", UrlBeans.ARCHIVE_SERVICE, "reverseArchiveStory");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		
		mv.setViewName("redirect:/story/storypage/" + session.getAttribute("themeId"));
		return mv;
		
	}
	
	public String convertBoolean(boolean onOff) {
		
		String tempA = "off";
		
		if(onOff) {
			tempA = "on";
		}
		
		return tempA;
	}
	
}
