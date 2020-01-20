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
import com.automation.configuration.enums.AccountType;
import com.automation.universe.domain.ClientLoginAccount;
import com.automation.universe.domain.Universe;
import com.automation.user.domain.User;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Service
public class ScriptService {

	public ModelAndView setScriptPage(HttpSession session, Model model) {
		
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		User user = (User)session.getAttribute("user");
		
		String url = String.format("%s/%s", UrlBeans.CLIENT_LOGIN_ACCOUNT_SERVICE, "getclientloginaccountscript");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		JSONArray jsonArray = jsonResponse.getJSONArray("clientLoginAccountList");
		
		
		List<ClientLoginAccount>clientLoginAccountList = new ArrayList<ClientLoginAccount>();
		for(int i=0; i<jsonArray.length(); i++) {
			
			JSONObject clientLogin = jsonArray.getJSONObject(i);
			ClientLoginAccount clientLoginAccount = new ClientLoginAccount();
			clientLoginAccount.setLoginAccountId(clientLogin.getLong("loginAccountId"));
			clientLoginAccount.setHostname(clientLogin.getString("hostname"));
			clientLoginAccount.setAccountType(AccountType.script);
			clientLoginAccount.setDescription(clientLogin.getString("description"));
			clientLoginAccountList.add(clientLoginAccount);
			
		}
		
		mv.addObject("clientLoginAccountList", clientLoginAccountList);
		mv.addObject("user", user.getUsername());
		mv.addObject("Info", model.asMap().get("Info") != null ? model.asMap().get("Info"): null);
		mv.addObject("Error", model.asMap().get("Error") != null ? model.asMap().get("Error"): null);
		mv.setViewName("registration");
		return mv;
	}
	
	public ModelAndView setAddScriptPage(HttpSession session,
			long loginAccountId) {
		ModelAndView mv = new ModelAndView();
		
		
		User user = (User)session.getAttribute("user");
		
		mv.addObject("user", user.getUsername());
		mv.addObject("loginAccountId", loginAccountId);
		mv.addObject("checker", false);
		mv.setViewName("script");
		
		
		return mv;
	}
	
	public ModelAndView processCreateCredential(String hostname, String username,
			String password, String ppkFile, String description,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("hostname", hostname);
		jsonData.put("username", username);
		jsonData.put("password", password);
		jsonData.put("ppkFile", ppkFile);
		jsonData.put("description", description);
		
		String url = String.format("%s/%s", UrlBeans.CLIENT_LOGIN_ACCOUNT_SERVICE, "processcreatecredential");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		
		ra.addFlashAttribute("Info", jsonResponse.get("Info"));
		mv.setViewName("redirect:/script/scriptpage");
		return mv;
	}
	
	public ModelAndView processAddScript(long loginAccountId,
			String name, String shFile, String description,
			int noOfVar,
			RedirectAttributes ra,
			HttpSession session) {
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		User user = (User)session.getAttribute("user");
		
		Universe universe = (Universe)session.getAttribute("universe");
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("universeId", universe.getUniverseId());
		jsonData.put("loginAccountId", loginAccountId);
		jsonData.put("name", name);
		jsonData.put("shFile", shFile);
		jsonData.put("description", description);
		
		String url = String.format("%s/%s", UrlBeans.SCRIPT_SERVICE, "processaddscript");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		
		List<Integer>numberList = new ArrayList<Integer>();
		for(int i=0; i<noOfVar; i++) {
			
			numberList.add(i);
			
		}
		
		if(noOfVar > 0) {
			mv.addObject("user", user.getUsername());
			mv.addObject("checker", true);
			mv.addObject("noOfVar", numberList);
			mv.addObject("scriptId", jsonResponse.get("scriptId"));
			mv.setViewName("script");
			return mv;
		}else {
			ra.addFlashAttribute("Info", jsonResponse.get("Info"));
			mv.setViewName("redirect:/script/scriptpage");
			return mv;
		}
		
		
		
	}
	
	public ModelAndView processAddScriptVariable(List<String>name,
			List<String>description, long scriptId,
			HttpSession session,
			RedirectAttributes ra) {
		
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("scriptId", scriptId);
		jsonData.put("name", name);
		jsonData.put("description", description);
		
		String url = String.format("%s/%s", UrlBeans.SCRIPT_SERVICE, "processaddscriptvariable");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		
		ra.addFlashAttribute("Info", jsonResponse.get("Info"));
		mv.setViewName("redirect:/script/scriptpage");
		return mv;
		
	}

}
