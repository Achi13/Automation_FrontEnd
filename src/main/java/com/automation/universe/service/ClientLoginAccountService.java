package com.automation.universe.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.automation.configuration.enums.AccountType;
import com.automation.universe.domain.ClientLoginAccount;
import com.automation.universe.domain.Universe;
import com.automation.universe.domain.WebAddress;
import com.automation.user.domain.User;

@Service
public class ClientLoginAccountService {
	
	public ModelAndView setLoginCredentialPage(HttpSession session,
			RedirectAttributes ra, Model model) {
		
		ModelAndView mv = new ModelAndView();
		
		Universe universe = (Universe)session.getAttribute("universe");
		User user = (User)session.getAttribute("user");
		
		if(universe.getUniverseId() == 1) {
			mv.addObject("webAddressList", (List<?>)session.getAttribute("webAddressList"));
			mv.addObject("user", user.getUsername());
			mv.setViewName("logincredential");
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
				mv.addObject("webAddressList", webAddress);
				mv.addObject("user", user.getUsername());
				mv.setViewName("logincredential");
			}else {
				mv.setViewName("redirect:/client/clientsummary");
				
			}
			return mv;
		}
		
	}
	
	public ModelAndView saveLoginCredential(List<String>username,
			List<String>password, List<String>url, List<Integer>counter,
			HttpSession session, RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		
		List<ClientLoginAccount>loginCredentialList = new ArrayList<ClientLoginAccount>();		
		int count = 0;
		
		Universe universe = (Universe)session.getAttribute("universe");
		
		if(universe.getUniverseId() == 1) {
			for(int i=0; i<url.size(); i++) {
				
				for(int j=count; j<(count + (Integer)counter.get(i)); j++) {
					
					ClientLoginAccount loginCredential = new ClientLoginAccount(url.get(i), username.get(j), 
							password.get(j), null, null, AccountType.generic);
					loginCredentialList.add(loginCredential);
					
				}
				count = count + counter.get(i);
			}
			
			session.setAttribute("loginCredentialList", loginCredentialList);
			
			mv.setViewName("redirect:/headers/setloginheaderpage");
			return mv;
		}else {
			
			for(int i=0; i<url.size(); i++) {
				
				for(int j=count; j<(count + (Integer)counter.get(i)); j++) {
					
					ClientLoginAccount loginCredential = new ClientLoginAccount(url.get(i), username.get(j), 
							password.get(j), null, null, AccountType.generic);
					loginCredentialList.add(loginCredential);
					
				}
				count = count + counter.get(i);
			}
			
			session.setAttribute("loginCredentialList", loginCredentialList);
			
			mv.setViewName("redirect:/headers/setloginheaderpage");
			return mv;
			
		}
		
		
	}
	
}
