package com.automation.universe.service;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.automation.configuration.enums.AccountType;
import com.automation.universe.domain.ClientLoginAccount;
import com.automation.user.domain.User;

@Service
public class TapServerCredentialService {
	
	public ModelAndView setTapConnectionPage(HttpSession session) {
		
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		User user = (User)session.getAttribute("user");
		
		List<?>webAddressList = (List<?>)session.getAttribute("webAddressList");
		
		log.info(webAddressList.size());
		
		mv.addObject("webAddressList", webAddressList);
		mv.addObject("user", user.getUsername());
		mv.setViewName("tap");
		return mv;
	}
	
	public ModelAndView saveTapConnection(List<String>url, List<String>username,
			List<String>password, List<String>hostname, List<String>ppkFile,
			HttpSession session) {
		Logger log = Logger.getLogger(getClass());
		log.info("Access:/tapConnection");
		ModelAndView mv = new ModelAndView();
		
		List<ClientLoginAccount>tapLoginAccountList = new ArrayList<ClientLoginAccount>();	
		
		
		
		for(int i=0; i<url.size(); i++) {
			
			
			
			ClientLoginAccount tapLoginAccount = new ClientLoginAccount(url.get(i), username.get(i),
					password.get(i), hostname.get(i), ppkFile.get(i), AccountType.tap);
			tapLoginAccountList.add(tapLoginAccount);
		}
		
		session.setAttribute("tapLoginAccountList", tapLoginAccountList);
		mv.setViewName("redirect:/client/clientsummary");
		return mv;
	}
	/*
	public ModelAndView uploadPpk(MultipartFile ppkFile, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		Logger log = Logger.getLogger(getClass());
		
		InputStream uploadedInputStream = new BufferedInputStream(ppkFile.getInputStream());
		
		FormDataMultiPart multiPart = new FormDataMultiPart().field("file", uploadedInputStream, MediaType.MULTIPART_FORM_DATA);
		
	
		
		
		String url = String.format("%s/%s?universeId=%d", UrlBeans.CLIENT_SERVICE, "getclientbyuniverseid");
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
	}*/
	
}
