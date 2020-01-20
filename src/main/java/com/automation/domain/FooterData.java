package com.automation.domain;

public class FooterData {
	
	private String clientName;
	private String transactionType;
	private String website;
	private String serverImport;
	private String sender;
	private String assignedAccount;
	private String ignoreSeverity;
	private String testcaseNumber;
	
	public FooterData() {}
	
	public FooterData(String clientName, String transactionType, String website, String serverImport, String sender,
			String assignedAccount, String ignoreSeverity, String testcaseNumber) {
		super();
		this.clientName = clientName;
		this.transactionType = transactionType;
		this.website = website;
		this.serverImport = serverImport;
		this.sender = sender;
		this.assignedAccount = assignedAccount;
		this.ignoreSeverity = ignoreSeverity;
		this.testcaseNumber = testcaseNumber;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getServerImport() {
		return serverImport;
	}
	public void setServerImport(String serverImport) {
		this.serverImport = serverImport;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getAssignedAccount() {
		return assignedAccount;
	}
	public void setAssignedAccount(String assignedAccount) {
		this.assignedAccount = assignedAccount;
	}
	public String getIgnoreSeverity() {
		return ignoreSeverity;
	}
	public void setIgnoreSeverity(String ignoreSeverity) {
		this.ignoreSeverity = ignoreSeverity;
	}
	public String getTestcaseNumber() {
		return testcaseNumber;
	}
	public void setTestcaseNumber(String testcaseNumber) {
		this.testcaseNumber = testcaseNumber;
	}
	
	
		
}
