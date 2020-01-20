package com.automation.universe.domain;

import com.automation.configuration.enums.AccountType;

public class ClientLoginAccount {
	
	private long loginAccountId;
	
	private String webAddress;
	
	private String username;
	
	private String password;
	
	private String hostname;
	
	private String ppkFilepath;
	
	private AccountType accountType;
	
	private String description;
	
	
	public ClientLoginAccount() {}


	public ClientLoginAccount(String webAddress, String username, String password, String hostname,
			String ppkFilepath, AccountType accountType) {
		super();
		this.webAddress = webAddress;
		this.username = username;
		this.password = password;
		this.hostname = hostname;
		this.ppkFilepath = ppkFilepath;
		this.accountType = accountType;
	}


	public long getLoginAccountId() {
		return loginAccountId;
	}


	public void setLoginAccountId(long loginAccountId) {
		this.loginAccountId = loginAccountId;
	}


	public String getWebAddress() {
		return webAddress;
	}


	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
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


	public String getHostname() {
		return hostname;
	}


	public void setHostname(String hostname) {
		this.hostname = hostname;
	}


	public String getPpkFilepath() {
		return ppkFilepath;
	}


	public void setPpkFilepath(String ppkFilepath) {
		this.ppkFilepath = ppkFilepath;
	}


	public AccountType getAccountType() {
		return accountType;
	}


	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
