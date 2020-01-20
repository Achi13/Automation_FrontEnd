package com.automation.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.automation.domain.UserDomain;
import com.automation.domain.ViewElements;

public class UserSession{
	
	private static UserSession instance;
	private UserDomain user;
	private static List<UserDomain>userDomainList = new ArrayList<>();
	private static HashMap<String, HashMap<String,ViewElements>>viewMap = new HashMap<String, HashMap<String,ViewElements>>();
	
	private static HashMap<String, String>statuses;
	
	public static UserSession getInstance() {
		
		if(instance == null) {
			instance = new UserSession();
		}
		
		return instance;
		
	}
	
	public UserDomain getUser() {
		return this.user;
	}
	
	public void setUser(UserDomain user) {
		userDomainList.add(user);
	}
	
	public List<UserDomain> getUserDomain(){
		
		return userDomainList;
		
	}
	
	public void setViews(HttpSession session, ViewElements elements) {
		
		HashMap<String, ViewElements>elementHolder = new HashMap<String, ViewElements>();
		ViewElements views = elements;
		elementHolder.put(elements.getTestCaseNumber(), views);
		viewMap.put((String)session.getAttribute("uname"), elementHolder);
		
	}
	
	public HashMap<String, HashMap<String,ViewElements>>getViews(){
		return viewMap;
	}
	
	public void setValStatuses(String key, String value) {
		statuses.put(key, value);
	}
	
	public HashMap<String, String>getStatuses(){
		return statuses;
	}
	
}
