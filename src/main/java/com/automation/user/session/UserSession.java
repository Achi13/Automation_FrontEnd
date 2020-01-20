package com.automation.user.session;

import java.util.ArrayList;
import java.util.List;

import com.automation.user.domain.User;

public class UserSession {
	
	private static UserSession instance;
	private List<User>userList = new ArrayList<User>();
	
	public static UserSession getInstance() {
		
		if(instance == null) {
			instance = new UserSession();
		}
		return instance;
	}
	
	public void setUser(User user) {
		userList.add(user);
	}
	
	public List<User> getUserList() {
		return userList;
	}
}
