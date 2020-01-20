package com.automation.template.service;

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

import com.automation.configuration.beans.UrlBeans;
import com.automation.template.domain.TemplateData;
import com.automation.universe.domain.Universe;
import com.automation.user.domain.User;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Service
public class TemplatesService {
	
	public ModelAndView processCreateTemplate(List<String>label, List<String>natureOfAction,
			List<String>webElementName, List<String>webElementNature, List<String>isTriggerEnter,
			List<String>isScreenCapture, String clientName, String templateName,
			String isPublic, HttpSession session,
			RedirectAttributes ra) {
		Logger log = Logger.getLogger(getClass());
		ModelAndView mv = new ModelAndView();
		
		Universe universe = (Universe)session.getAttribute("universe");
		User user = (User)session.getAttribute("user");
		
		JSONObject jsonData = new JSONObject();
		List<TemplateData>templateDataList = new ArrayList<TemplateData>();
		for(int i=0; i<webElementName.size(); i++) {
			
			TemplateData templateData = new TemplateData(0, label.get(i), natureOfAction.get(i),
					isScreenCapture.get(i).equals("on") ? true:false, isTriggerEnter.get(i).equals("on") ? true:false,
					webElementName.get(i), webElementNature.get(i));
			templateDataList.add(templateData);
			
		}
		jsonData.put("templateDataList", templateDataList);
		jsonData.put("clientName", clientName);
		jsonData.put("templateName", templateName);
		jsonData.put("isPublic", isPublic.equals("on") ? true: false);
		jsonData.put("universeId", universe.getUniverseId());
		jsonData.put("userId", user.getUserId());
		
		String url = String.format("%s/%s", UrlBeans.TEMPLATES_SERVICE, "processcreatetemplate");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		
		ra.addFlashAttribute("Info", jsonResponse.get("Info"));
		mv.setViewName("redirect:/index");
		return mv;
	}
	
	public ModelAndView setCreateTemplatePage(HttpSession session) {
		
		Logger log = Logger.getLogger(getClass());
		ModelAndView mv = new ModelAndView();
		
		Universe universe = (Universe)session.getAttribute("universe");
		
		User user = (User)session.getAttribute("user");
		
		String url = String.format("%s/%s?universeId=%d", UrlBeans.CLIENT_SERVICE, "getclientbyuniverseid", universe.getUniverseId());
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		
		JSONArray jsonArray = jsonResponse.getJSONArray("clientData");
		List<com.automation.universe.domain.Client>clientList = new ArrayList<com.automation.universe.domain.Client>();
		for(int i=0; i<jsonArray.length(); i++) {
			JSONObject clientDataObject = jsonArray.getJSONObject(i);
			com.automation.universe.domain.Client clientData = new com.automation.universe.domain.Client();
			clientData.setClientId(clientDataObject.getLong("clientId"));
			clientData.setClientName(clientDataObject.getString("clientName"));
			clientList.add(clientData);
		}
		
		mv.addObject("universeId", universe.getUniverseId());
		mv.addObject("clientList", clientList);
		mv.addObject("user", user.getUsername());
		mv.setViewName("newtemplate");
		return mv;
	}
	
	public ModelAndView setModifyTemplatePage(HttpSession session) {
		Logger log = Logger.getLogger(getClass());
		ModelAndView mv = new ModelAndView();
		
		User user = (User)session.getAttribute("user");
		
		Universe universe = (Universe)session.getAttribute("universe");
		
		String url = String.format("%s/%s?universeId=%d", UrlBeans.CLIENT_SERVICE, "getclientbyuniverseid", universe.getUniverseId());
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		
		JSONArray jsonArray = jsonResponse.getJSONArray("clientData");
		List<com.automation.universe.domain.Client>clientList = new ArrayList<com.automation.universe.domain.Client>();
		for(int i=0; i<jsonArray.length(); i++) {
			JSONObject clientDataObject = jsonArray.getJSONObject(i);
			com.automation.universe.domain.Client clientData = new com.automation.universe.domain.Client();
			clientData.setClientId(clientDataObject.getLong("clientId"));
			clientData.setClientName(clientDataObject.getString("clientName"));
			clientList.add(clientData);
		}
		
		mv.addObject("universeId", universe.getUniverseId());
		mv.addObject("userId", user.getUserId());
		mv.addObject("clientData", clientList);
		mv.addObject("user", user.getUsername());
		mv.setViewName("extemplate");
		return mv;
	}
	
	public ModelAndView processModifyTemplate(List<String>label, List<String>natureOfAction,
			List<String>webElementName, List<String>webElementNature, List<Boolean>isTriggerEnter,
			List<Boolean>isScreenCapture, List<Long>templateDataId, String templateId,
			Boolean isPublic, HttpSession session,
			RedirectAttributes ra) {
		Logger log = Logger.getLogger(getClass());
		ModelAndView mv = new ModelAndView();
		
		JSONObject jsonData = new JSONObject();
		List<TemplateData>templateDataList = new ArrayList<TemplateData>();
		try {
			for(int i=0; i<webElementName.size(); i++) {
				
				log.info(templateDataId.get(i));
				
				TemplateData templateData = new TemplateData(templateDataId.get(i), label.get(i), natureOfAction.get(i),
						isScreenCapture.get(i), isTriggerEnter.get(i), webElementName.get(i), 
						webElementNature.get(i));
				templateDataList.add(templateData);
				
			}
		}catch(Exception e) {}
		
		jsonData.put("templateDataList", templateDataList);
		jsonData.put("templateId", templateId);
		jsonData.put("isPublic", isPublic);
		
		String url = String.format("%s/%s", UrlBeans.TEMPLATES_SERVICE, "processmodifytemplate");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		if(clientResponse.getStatus() == 200) {
			JSONObject jsonResponse = new JSONObject(response);
			
			ra.addFlashAttribute("Info", jsonResponse.get("Info"));
			mv.setViewName("redirect:/index");
			return mv;
		}else {
			JSONObject jsonResponse = new JSONObject(response);
			
			ra.addFlashAttribute("Error", jsonResponse.get("Error"));
			mv.setViewName("redirect:/index");
			return mv;
		}
		
		
	}
	
}
