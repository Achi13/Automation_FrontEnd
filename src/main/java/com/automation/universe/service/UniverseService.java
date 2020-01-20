package com.automation.universe.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.automation.configuration.beans.UrlBeans;
import com.automation.universe.domain.Universe;
import com.automation.user.domain.User;
import com.automation.user.session.UserSession;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Service
public class UniverseService {
	
	//======================================================SET UNIVERSE PAGE===========================================================
	public ModelAndView setUniversePage(HttpSession session) {
		
		Logger log = Logger.getLogger(getClass());
		ModelAndView mv = new ModelAndView();
		
		if(session.getAttributeNames().hasMoreElements() || UserSession.getInstance().getUserList().size() > 0) {
			//SET USER FROM SESSION
			User user = (User) session.getAttribute("user");
			
			//GET UNIVERSE ACCESS LIST OF USER
			String[]universeAccessList = user.getUniverseAccessList().split(",");
			
			
			
			
			
			String url = String.format("%s/%s", UrlBeans.UNIVERSE_SERVICE, "getalluniverse");
			
			Client client = new Client();
			WebResource webResource = client.resource(url);
			ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
			
			String response = new String(clientResponse.getEntity(String.class));
			
			log.info(response);
			
			JSONObject jsonResponse = new JSONObject(response);
			JSONArray jsonArray = jsonResponse.getJSONArray("universeList");
			
			
			
			List<Universe>universeList = new ArrayList<Universe>();
			for(int i=0; i<jsonArray.length(); i++) {
				JSONObject universeData = jsonArray.getJSONObject(i);
				Universe universe = new Universe(universeData.getLong("universeId"), universeData.getString("universeName"));
				for(int j=0; j<universeAccessList.length; j++) {
					if(universe.getUniverseName().equals(universeAccessList[j])) {
						universeList.add(universe);
					}
				}
				
				
			}
			mv.addObject("user", user.getUsername());
			mv.addObject("universeList", universeList);
			mv.setViewName("universe");
			return mv;
		}else {
			mv.setViewName("redirect:/login");
			return mv;
		}
		
		
	}
	
	//=========================================================SET UNIVERSE ID IN SESSION==================================================
	public ModelAndView setUniverseSession(long universeId, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		
		Logger log = Logger.getLogger(getClass());
		
		String url = String.format("%s/%s?universeId=%d", UrlBeans.UNIVERSE_SERVICE, "getuniverse", universeId);
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		
		JSONObject universeData = new JSONObject(response);
		
		Universe universe = new Universe(universeData.getLong("universeId"), universeData.getString("universeName"));
		
		session.setAttribute("universe", universe);
		mv.setViewName("redirect:/home");
		return mv;
	}
	
}
