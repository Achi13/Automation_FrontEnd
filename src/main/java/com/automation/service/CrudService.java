package com.automation.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.automation.db.entity.User;
import com.automation.db.repository.UserRepository;
import com.automation.domain.FileEntity;
import com.automation.domain.StoragePath;
import com.automation.domain.UserDomain;
import com.automation.session.UserSession;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Service
public class CrudService {
	
	@Autowired
	UserRepository userRepo;
	
	private final String SERVER_TIMEOUT = "5000";
	
	private final String LOCATION = StoragePath.TEMPLATE_HOLDER_LOCATION;
	
	public ModelAndView processDelete(int userId, RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		
		userRepo.delete(userRepo.findUserByUserId(userId));
		
		ra.addFlashAttribute("Info", "User deleted successfully.");
		mv.setViewName("redirect:/home");
		return mv;
	}
	
	
	@HystrixCommand(fallbackMethod="serverNotResponding", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", 
					value = SERVER_TIMEOUT)})
	public ModelAndView processUpdate(int userId, String userAccess,
			String uname,
			String pass,
			String status,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		
		User user = userRepo.findUserByUserId(userId);
		user.setUserAccess(userAccess);
		user.setUsername(uname);
		user.setPassword(pass);
		user.setStatus(status);
		
		userRepo.save(user);
		ra.addFlashAttribute("Info", "User updated successfully.");
		mv.setViewName("redirect:/home");
		return mv;
		
	}
	
	
	@HystrixCommand(fallbackMethod="serverNotResponding", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", 
					value = SERVER_TIMEOUT)})
	public ModelAndView processRegister(String userType,
			String uname,
			String pass,
			String status,
			RedirectAttributes ra) throws IOException {
		
		ModelAndView mv = new ModelAndView();
		
		if(userRepo.findUserByUsername(uname) == null) {
			User user = new User(uname, pass, userType, status);
			userRepo.save(user);
			
			ra.addFlashAttribute("Info", "User successfully registered.");
			mv.setViewName("redirect:/home");
			return mv;
		}else {
			ra.addFlashAttribute("Error", "Username aldready taken!");
			mv.setViewName("redirect:/home");
			return mv;
		}
		
		
		
	}
	
	@HystrixCommand(fallbackMethod="serverNotResponding", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", 
					value = SERVER_TIMEOUT)})
	public ModelAndView updatePage(int userId, HttpSession session, RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		User user = userRepo.findUserByUserId(userId);
		mv.addObject("userinfo", user);
		mv.addObject("user", session.getAttribute("uname"));
		mv.setViewName("registerpage");
		return mv;
	}
	
	@HystrixCommand(fallbackMethod="serverNotResponding", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", 
					value = SERVER_TIMEOUT)})
	public ModelAndView processLogin(String uname, String pass, HttpSession session, RedirectAttributes ra) throws IOException {
		Logger log = Logger.getLogger(getClass());
		ModelAndView mv = new ModelAndView();
		User user = userRepo.findUserByUsername(uname);
		
		/*
		JSONObject jsonData = new JSONObject();
		jsonData.put("username", uname);
		jsonData.put("password", pass);
		
		String url = "http://MOADONEA612SK69:2000/user/processlogin";
		
		Client client = new Client();
		WebResource webResource = client.resource(url);
		ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonData.toString());
		
		String response = new String(clientResponse.getEntity(String.class));
		
		log.info(response);
		*/
		/*
		if(user != null && user.getPassword().equals(pass)) {
			
			File location = new File(LOCATION);
			List<FileEntity> csvHolder = new ArrayList<>();
			FileEntity data;
			for (File files:location.listFiles()) {
				String fileName = files.toString();
				String extension = "";

				int i = fileName.lastIndexOf('.');
				int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

				if (i > p) {
				    extension = fileName.substring(i+1);
				    if(extension.equals("csv")) {
				    	data = new FileEntity(files.toString(), files.getName());
				    	csvHolder.add(data);
				    }
				}
			}
			UserDomain userDomain = new UserDomain(user,session.getId());
			UserSession.getInstance().setUser(userDomain);
			session.setAttribute("uname", userDomain.getUname());
			session.setAttribute("useraccess", userDomain.getUserAccess());
			session.setAttribute("assigned", "selenium1");
			mv.setViewName("universe");
			return mv;
		}else {
			ra.addFlashAttribute("Error", "Invalid Username or Password!");
			mv.setViewName("redirect:/login");
			return mv;
		}*/
		
		
		
		if(user != null && user.getPassword().equals(pass)) {
			
			if(user.getStatus().equals("active")) {
				if(user.getUserAccess().equals("officer")) {
					UserDomain userDomain = new UserDomain(user,session.getId());
					UserSession.getInstance().setUser(userDomain);
					session.setAttribute("uname", userDomain.getUname());
					session.setAttribute("useraccess", userDomain.getUserAccess());
					mv.addObject("user", session.getAttribute("uname"));
					mv.setViewName("redirect:/home");
					return mv;
				}else {
					File location = new File(LOCATION);
					
					List<FileEntity> csvHolder = new ArrayList<>();
					FileEntity data;
					for (File files:location.listFiles()) {
						String fileName = files.toString();
						String extension = "";

						int i = fileName.lastIndexOf('.');
						int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

						if (i > p) {
						    extension = fileName.substring(i+1);
						    if(extension.equals("csv")) {
						    	data = new FileEntity(files.toString(), files.getName());
						    	csvHolder.add(data);
						    }
						}
					}
					UserDomain userDomain = new UserDomain(user,session.getId());
					UserSession.getInstance().setUser(userDomain);
					session.setAttribute("uname", userDomain.getUname());
					session.setAttribute("useraccess", userDomain.getUserAccess());
					session.setAttribute("assigned", "selenium1");
					mv.setViewName("universe");
					return mv;
				}
			}else {
				ra.addFlashAttribute("Error", "Your account is inactive. Please contact the security officer for this matter.");
				mv.setViewName("redirect:/login");
				return mv;
			}
			
			
			
		}else {
			ra.addFlashAttribute("Error", "Invalid Username or Password!");
			mv.setViewName("redirect:/login");
			return mv;
		}
		
	}
	
	//HYSTRIX FALLBACK METHODS
	
	
	
	//HYSTRIX FALLBACK FOR processLogin
	public ModelAndView serverNotResponding(String uname, 
			String pass, 
			HttpSession session,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/login");
		return mv;
	}
	
	//HYSTRIX FALLBACK FOR processRegister
	public ModelAndView serverNotResponding(String userAccess,
			String uname,
			String pass,
			String status,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		ra.addFlashAttribute("Error", "Server not responding...");
		mv.setViewName("redirect:/home");
		return mv;
	}
	//HYSTRIX FALLBACK FOR updatePage
	public ModelAndView serverNotResponding(int userId, HttpSession session, RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		ra.addFlashAttribute("Error", "Server not responding...");
		mv.setViewName("redirect:/home");
		return mv;
	}
	
	//HYSTRIX FALLBACK FOR processUpdate
	public ModelAndView serverNotResponding(int userId, String userAccess,
			String uname,
			String pass,
			String status,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		ra.addFlashAttribute("Error", "Server not responding...");
		mv.setViewName("redirect:/home");
		return mv;
		
	}
	
	
	
}
