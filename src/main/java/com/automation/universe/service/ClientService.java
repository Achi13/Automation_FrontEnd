package com.automation.universe.service;

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

import com.automation.configuration.beans.UrlBeans;
import com.automation.universe.domain.Universe;
import com.automation.universe.domain.WebAddress;
import com.automation.user.domain.User;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Service
public class ClientService {

	public ModelAndView setCreateTestcasePage(String storyId, HttpSession session) {
		
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		User user = (User) session.getAttribute("user");
		
		Universe universe = (Universe)session.getAttribute("universe");
		
		String url = String.format("%s/%s?universeId=%d&storyId=%s", UrlBeans.CLIENT_SERVICE, "getclientbyuniverseidandstoryid", universe.getUniverseId(), storyId);
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		JSONArray jsonArray = jsonResponse.getJSONArray("clientData");
		
		List<com.automation.universe.domain.Client>clientList = new ArrayList<com.automation.universe.domain.Client>();
		
		for(int i=0; i<jsonArray.length(); i++) {
			
			JSONObject clientData = jsonArray.getJSONObject(i);
			
			com.automation.universe.domain.Client clientEntity = new com.automation.universe.domain.Client(clientData.toString());
			
			clientList.add(clientEntity);
		}
		
		mv.addObject("storyId", storyId);
		mv.addObject("userId", user.getUserId());
		mv.addObject("clientData", clientList);
		mv.addObject("user", user.getUsername());
		mv.addObject("serverImport", jsonResponse.get("serverImport"));
		mv.setViewName("testcase");
		return mv;
	}
	
	public ModelAndView setCreateClientPage(HttpSession session, Model model) {
		
		ModelAndView mv = new ModelAndView();
		
		User user = (User)session.getAttribute("user");
		
		boolean wuiFlag = false;
		
		Universe universe = (Universe)session.getAttribute("universe");
		
		if(universe.getUniverseId() == 1) {
			
			wuiFlag = true;
			
		}
		
		mv.addObject("Info", model.asMap().get("Info"));
		mv.addObject("user", user.getUsername());
		mv.addObject("wuiFlag", wuiFlag);
		mv.setViewName("client");
		return mv;
		
	}
	
	public ModelAndView clientSummary(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		User user = (User)session.getAttribute("user");
		
		log.info("access: /Summary");
		
		List<?> webAddressList = (List<?>)session.getAttribute("webAddressList");
		List<?>loginCredentialList = (List<?>)session.getAttribute("loginCredentialList");
		
		List<?>tapLoginAccountList = (List<?>)session.getAttribute("tapLoginAccountList");
		
		com.automation.universe.domain.Client client = (com.automation.universe.domain.Client)session.getAttribute("clientData");
		 
		//List<Object>finalList = new ArrayList<Object>();
		
		
		/*
		for(int i=0; i<webAddressList.size(); i++) {
			
			WebAddress webAddress = (WebAddress)webAddressList.get(i);
			
			for(int j=0; j<loginCredentialList.size(); i++) {
				ClientLoginAccount clientLoginAccount = (ClientLoginAccount)loginCredentialList.get(j);
				if(webAddress.getUrl().equals(clientLoginAccount.getWebAddress())) {
					
					
					
				}
			}
			
		}*/
		
		mv.addObject("clientName", client.getClientName());
		mv.addObject("webAddressList", webAddressList);
		mv.addObject("user", user.getUsername());
		mv.addObject("loginCredentialList", loginCredentialList == null ? null : loginCredentialList);
		mv.addObject("tapLoginAccountList",tapLoginAccountList);
		mv.setViewName("summary");
		return mv;
	}
	
	public ModelAndView saveClient(String clientName,List<Boolean>loginRestrictionFlag, 
			List<String>url, List<String>name, List<String>description, 
			HttpSession session, RedirectAttributes ra) {
		
		ModelAndView mv = new ModelAndView();
		
		Universe universe = (Universe)session.getAttribute("universe");
		com.automation.universe.domain.Client client = new com.automation.universe.domain.Client(universe, clientName);
		
		if(universe.getUniverseId() == 1) {
			
			
			List<WebAddress>webAddressList = new ArrayList<WebAddress>();
			for(int i=0; i<url.size(); i++) {
				
				WebAddress webAddress = new WebAddress(client, url.get(i),
						name.get(i), description.get(i));
				webAddressList.add(webAddress);
				
			}
			
			session.setAttribute("clientData", client);
			session.setAttribute("webAddressList", webAddressList);
			
			ra.addFlashAttribute("webAddressList", webAddressList);
			mv.setViewName("redirect:/clientloginaccount/setlogincredentialpage");
			return mv;
			
		}else {
			boolean checkFlag = false;
			
			List<WebAddress>webAddressList = new ArrayList<WebAddress>();
			List<Boolean>loginRestrictionFlagList = new ArrayList<Boolean>();
			for(int i=0; i<url.size(); i++) {
				
				WebAddress webAddress = new WebAddress(client, url.get(i),
						name.get(i), description.get(i));
				webAddressList.add(webAddress);
				
				loginRestrictionFlagList.add(loginRestrictionFlag.get(i));
				if(!checkFlag) {
					checkFlag = loginRestrictionFlag.get(i) ? true : false;
				}
			}
			session.setAttribute("clientData", client);
			session.setAttribute("webAddressList", webAddressList);
			session.setAttribute("loginRestrictionFlagList", loginRestrictionFlagList);
			
			if(checkFlag) {
				
				mv.setViewName("redirect:/clientloginaccount/setlogincredentialpage");
				return mv;
				
			}else {
				
				mv.setViewName("redirect:/client/clientsummary");
				return mv;
				
			}
			
		}
		
	}
	
	
	public ModelAndView confirmSummary(HttpSession session, RedirectAttributes ra) {
		
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		log.info("access:/confirm summary");
		JSONObject jsonData = new JSONObject();
		
		com.automation.universe.domain.Client clientData = (com.automation.universe.domain.Client)session.getAttribute("clientData");
		
		List<?>webAddressList = (List<?>)session.getAttribute("webAddressList");
		
		List<?>tapLoginAccountList = (List<?>)session.getAttribute("tapLoginAccountList");
		
		List<?>headerList = (List<?>)session.getAttribute("headerList");
		
		List<?>loginCredentialList = (List<?>)session.getAttribute("loginCredentialList");
		
		jsonData.put("clientName", clientData.getClientName());
		jsonData.put("webAddressList", webAddressList);
		jsonData.put("tapLoginAccountList", tapLoginAccountList);
		jsonData.put("headerList", headerList);
		jsonData.put("loginCredentialList", loginCredentialList);
		
		
		Universe universe = (Universe)session.getAttribute("universe");
		
		jsonData.put("universeId", universe.getUniverseId());
		
		String url = String.format("%s/%s", UrlBeans.CLIENT_SERVICE, "processcreateclient");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		
		ra.addFlashAttribute("Info", jsonResponse.get("Info"));
		mv.setViewName("redirect:/home");
		
		return mv;
	}
	
}
