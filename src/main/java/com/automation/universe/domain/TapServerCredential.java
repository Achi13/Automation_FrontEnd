package com.automation.universe.domain;

public class TapServerCredential {
	
	private long credentialId;
	
	private WebAddress webAddressId;

	private String hostname;
	
	private String username;
	
	private String password;
	
	private String ppkPath;
	
	public TapServerCredential() {}

	public TapServerCredential(WebAddress webAddressId, String hostname, String username, String password,
			String ppkPath) {
		super();
		this.webAddressId = webAddressId;
		this.hostname = hostname;
		this.username = username;
		this.password = password;
		this.ppkPath = ppkPath;
	}

	public long getCredentialId() {
		return credentialId;
	}

	public void setCredentialId(long credentialId) {
		this.credentialId = credentialId;
	}

	public WebAddress getWebAddressId() {
		return webAddressId;
	}

	public void setWebAddressId(WebAddress webAddressId) {
		this.webAddressId = webAddressId;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
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

	public String getPpkPath() {
		return ppkPath;
	}

	public void setPpkPath(String ppkPath) {
		this.ppkPath = ppkPath;
	}
	
}
