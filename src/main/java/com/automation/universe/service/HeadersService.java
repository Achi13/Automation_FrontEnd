package com.automation.universe.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.automation.universe.domain.Headers;
import com.automation.universe.domain.Universe;
import com.automation.universe.domain.WebAddress;
import com.automation.user.domain.User;

@Service
public class HeadersService {

	public ModelAndView setLoginHeaderPage(HttpSession session) {
		
		ModelAndView mv = new ModelAndView();
		
		Universe universe = (Universe)session.getAttribute("universe");
		
		User user = (User)session.getAttribute("user");
		
		if(universe.getUniverseId() == 1) {
			mv.addObject("webAddressList", session.getAttribute("webAddressList"));
			mv.addObject("user", user.getUsername());
			mv.setViewName("webaddress");
			return mv;
		}else {
			boolean checkFlag = false;
			
			List<?>webAddressList = (List<?>)session.getAttribute("webAddressList");
			List<?>loginRestrictionFlagList = (List<?>)session.getAttribute("loginRestrictionFlagList");
			
			List<WebAddress>webAddress = new ArrayList<WebAddress>();
			
			for(int i=0; i<webAddressList.size(); i++) {
				
				if((Boolean)loginRestrictionFlagList.get(i)) {
					
					checkFlag = (Boolean)loginRestrictionFlagList.get(i);
					
					WebAddress url = (WebAddress)webAddressList.get(i);
					webAddress.add(url);
				}
				
			}
			if(checkFlag) {
				mv.addObject("user", user.getUsername());
				mv.addObject("webAddressList", webAddress);
				mv.setViewName("webaddress");
			}else {
				mv.setViewName("redirect:/client/clientsummary");
				
			}
			return mv;
		}
		
		
		
	}
	
	public ModelAndView saveHeaders(List<String>url, List<Integer>counter,
			List<String>label, List<String>webElementNature,
			List<String>natureOfAction, List<String>webElementName,
			HttpSession session, RedirectAttributes ra) {
		Logger log = Logger.getLogger(getClass());
		ModelAndView mv = new ModelAndView();
		
		Universe universe = (Universe)session.getAttribute("universe");
		
		if(universe.getUniverseId() == 1) {
			List<Headers>headerList = new ArrayList<Headers>();
			
			int count = 0;
			for(int i=0; i<url.size(); i++) {
				for(int j=count; j<(count + (Integer) counter.get(i)); j++) {
					log.info(webElementName.size());
					Headers header = new Headers(url.get(i), webElementName.get(j),
							webElementNature.get(j), natureOfAction.get(j),
							label.get(j));
					headerList.add(header);
					
				}
				count = count + (Integer)counter.get(i);
			}
			
			session.setAttribute("headerList", headerList);
			
			mv.setViewName("redirect:/tapservercredential/tapconnectionpage");
			return mv;
		}else {
			
			List<Headers>headerList = new ArrayList<Headers>();
			
			int count = 0;
			for(int i=0; i<url.size(); i++) {
				for(int j=count; j<(count + (Integer) counter.get(i)); j++) {
					log.info(webElementName.size());
					Headers header = new Headers(url.get(i), webElementName.get(j),
							webElementNature.get(j), natureOfAction.get(j),
							label.get(j));
					headerList.add(header);
					
				}
				count = count + (Integer)counter.get(i);
			}
			
			session.setAttribute("headerList", headerList);
			
			mv.setViewName("redirect:/client/clientsummary");
			return mv;
			
		}
		
		
	}
	
}
