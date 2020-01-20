package com.automation.domain;

import com.automation.db.entity.User;

public class UserDomain {
	
	private long id;
	private String userType;
	private String uname;
	private String sessionId;
	public UserDomain() {}
	public UserDomain(int id, String userAccess ,String uname, String sessionId) {
		super();
		this.id = id;
		this.userType = userAccess;
		this.uname = uname;
		this.sessionId = sessionId;
	}
	
	public UserDomain(User user, String sessionId) {
		super();
		this.id = user.getUserId();
		this.userType = user.getUserAccess();
		this.uname = user.getUsername();
		this.sessionId = sessionId;
	}
	public long getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserAccess() {
		return userType;
	}
	public void setUserAccess(String userAccess) {
		this.userType = userAccess;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}
