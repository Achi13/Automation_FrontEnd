package com.automation.user.service;

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
import com.automation.user.domain.User;
import com.automation.user.session.UserSession;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


@Service
public class UserService {
	
	public ModelAndView setOfficerPage(Model model, HttpSession session) {
		
		
		Logger log = Logger.getLogger(getClass());
		ModelAndView mv = new ModelAndView();
		
		User username = (User)session.getAttribute("user");
		
		//============================================USER====================================
		
		String url = String.format("%s/%s", UrlBeans.USER_SERVICE, "getalluser");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		
		
		
		JSONArray userList = jsonResponse.getJSONArray("userList");
		
		List<User>users = new ArrayList<User>();
		for(int i=1; i<userList.length(); i++) {
			
			JSONObject userData = userList.getJSONObject(i);
			
			User user = new User();
			user.setUsername(userData.getString("username"));
			user.setUserType(userData.getString("userType"));
			user.setStatus(userData.getString("status"));
			user.setUniverseAccessList(userData.getString("universeAccessList"));
			user.setUserId(userData.getLong("userId"));
			
			users.add(user);
		}
		
		//===============================================CLIENT==============================================
		
		url = String.format("%s/%s", UrlBeans.UNIVERSE_SERVICE, "getalluniverse");
		
		webResource = client.resource(url);
		clientResponse = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		response = new String(clientResponse.getEntity(String.class));
		
		jsonResponse = new JSONObject(response);
		
		JSONArray universeJson = jsonResponse.getJSONArray("universeList");
		List<Universe>universeList = new ArrayList<Universe>();
		for(int i=0; i<universeJson.length(); i++) {
			JSONObject universeObject = universeJson.getJSONObject(i);
			
			Universe universeData = new Universe();
			universeData.setUniverseName(universeObject.getString("universeName"));
			
			universeList.add(universeData);
		}
		
		mv.addObject("Info", model.asMap().get("Info") != null ? model.asMap().get("Info"): null);
		mv.addObject("Error", model.asMap().get("Error") != null ? model.asMap().get("Error"): null);
		mv.addObject("userList", users);
		mv.addObject("universeList", universeList);
		mv.addObject("user", username.getUsername());
		mv.setViewName("securityofficer");
		return mv;
	}
	
	public ModelAndView setAccountSettingsPage(HttpSession session, Model model) {
		
		User user = (User)session.getAttribute("user");
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("user", user.getUsername());
		mv.addObject("Error", model.asMap().get("Error") != null ? model.asMap().get("Error"):null);
		mv.addObject("Info", model.asMap().get("Info") != null ? model.asMap().get("Info"):null);
		mv.setViewName("accountsettings");
		return mv;
	}
	
	
	//======================================PROCESS LOGIN====================================
	public ModelAndView processLogin(String uname, String pass, HttpSession session, RedirectAttributes ra){
		Logger log = Logger.getLogger(getClass());
		ModelAndView mv = new ModelAndView();
		
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("username", uname);
		jsonData.put("password", pass);
		
		String url = String.format("%s/%s", UrlBeans.USER_SERVICE, "processlogin");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		
		
		if(clientResponse.getStatus() == 200) {
			User user = new User(response);
			UserSession.getInstance().setUser(user);
			session.setAttribute("user", user);
			session.setMaxInactiveInterval(1200);
			mv.setViewName("redirect:/universe/universepage");
			return mv;
			
		}else {
			JSONObject jsonResponse = new JSONObject(response);
			ra.addFlashAttribute("Error", jsonResponse.get("Error"));
			mv.setViewName("redirect:/login");
			return mv;
		}
		
		
	}
	
	public ModelAndView processRegister(String username, String password,
			String useraccess, String usertype,
			RedirectAttributes ra) {
		Logger log = Logger.getLogger(getClass());
		ModelAndView mv = new ModelAndView();
		
		log.info(useraccess);
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("username", username);
		jsonData.put("password", password);
		jsonData.put("usertype", usertype);
		jsonData.put("useraccess", useraccess);
		
		String url = String.format("%s/%s", UrlBeans.USER_SERVICE, "processregister");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		
		if(clientResponse.getStatus() == 200) {
			ra.addFlashAttribute("Info", jsonResponse.get("Info"));
			mv.setViewName("redirect:/user/officerpage");
			return mv;
		}else {
			ra.addFlashAttribute("Error", jsonResponse.get("Error"));
			mv.setViewName("redirect:/user/officerpage");
			return mv;
		}
		
	}
	
	public ModelAndView processUpdate(long userId, String userType, String status,
			String password, String universeAccessList, RedirectAttributes ra) {
		Logger log = Logger.getLogger(getClass());
		ModelAndView mv = new ModelAndView();
		
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("userId", userId);
		jsonData.put("password", password);
		jsonData.put("status", status);
		jsonData.put("userType", userType);
		jsonData.put("universeAccessList", universeAccessList);
		
		String url = String.format("%s/%s", UrlBeans.USER_SERVICE, "processupdate");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject jsonResponse = new JSONObject(response);
		
		ra.addFlashAttribute("Info", jsonResponse.get("Info"));
		mv.setViewName("redirect:/user/officerpage");
		return mv;
	}
	
	public ModelAndView updateOwnAccount(String oldPassword, String newPassword, String confirmPassword,
			HttpSession session, RedirectAttributes ra) {
		
		Logger log = Logger.getLogger(getClass());
		ModelAndView mv = new ModelAndView();
		
		User user = (User)session.getAttribute("user");
		
		if(user.getPassword().equals(oldPassword)) {
			
			if(newPassword.equals(confirmPassword)) {
				
				JSONObject jsonData = new JSONObject();
				jsonData.put("userId", user.getUserId());
				jsonData.put("password", newPassword);
				jsonData.put("status", user.getStatus());
				jsonData.put("userType", user.getUserType());
				jsonData.put("universeAccessList", user.getUniverseAccessList());
				
				String url = String.format("%s/%s", UrlBeans.USER_SERVICE, "processupdate");
				
				Client client = new Client();
				WebResource webResource = client.resource(url);
				ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
				
				String response = new String(clientResponse.getEntity(String.class));
				
				log.info(response);
				
				JSONObject jsonResponse = new JSONObject(response);
				
				user.setPassword(newPassword);
				session.setAttribute("user", user);
				
				ra.addFlashAttribute("Info", jsonResponse.get("Info"));
				mv.setViewName("redirect:/user/accountsetingspage");
				return mv;
				
			}else {
				
				ra.addFlashAttribute("Error", "Data mismatch. Please try again.");
				mv.setViewName("redirect:/user/accountsetingspage");
				return mv;
			}
			
		}else {
			
			ra.addFlashAttribute("Error", "Data mismatch. Please try again.");
			mv.setViewName("redirect:/user/accountsetingspage");
			return mv;
		}
		
	}
	
}
