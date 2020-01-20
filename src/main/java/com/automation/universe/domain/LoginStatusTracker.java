package com.automation.universe.domain;

public class LoginStatusTracker {
	
	private long loginStatusTrackerId;
	
	private ClientLoginAccount loginAccountId;
	
	private String status;

	public LoginStatusTracker(ClientLoginAccount loginAccountId, String status) {
		super();
		this.loginAccountId = loginAccountId;
		this.status = status;
	}

	public long getLoginStatusTrackerId() {
		return loginStatusTrackerId;
	}

	public void setLoginStatusTrackerId(long loginStatusTrackerId) {
		this.loginStatusTrackerId = loginStatusTrackerId;
	}

	public ClientLoginAccount getLoginAccountId() {
		return loginAccountId;
	}

	public void setLoginAccountId(ClientLoginAccount loginAccountId) {
		this.loginAccountId = loginAccountId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
