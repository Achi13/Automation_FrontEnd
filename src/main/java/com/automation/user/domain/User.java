package com.automation.user.domain;

import org.json.JSONObject;

public class User {
	
	private long userId;
	
	private String universeAccessList;
	
	private String username;
	
	private String password;
	
	private String userType;
	
	private String status;

	public User() {
	}
	
	public User(String jsonData) {
		
		JSONObject userData = new JSONObject(jsonData);
		this.userId = userData.getLong("userId");
		this.universeAccessList = userData.getString("universeAccessList");
		this.username = userData.getString("username");
		this.password = userData.getString("password");
		this.userType = userData.getString("userType");
		this.status = userData.getString("status");
		
	}
	
	public User(String universeAccessList, String username, String password, String userType, String status) {
		super();
		this.universeAccessList = universeAccessList;
		this.username = username;
		this.password = password;
		this.userType = userType;
		this.status = status;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUniverseAccessList() {
		return universeAccessList;
	}

	public void setUniverseAccessList(String universeAccessList) {
		this.universeAccessList = universeAccessList;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
