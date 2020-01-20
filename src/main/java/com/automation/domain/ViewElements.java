package com.automation.domain;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class ViewElements {

	private String testCaseNumber;
	private String clientName;
	private String transactionType;
	private String description;
	
	public ViewElements(String testCaseNumber, String clientName, String transactionType, String description) {
		super();
		this.testCaseNumber = testCaseNumber;
		this.clientName = clientName;
		this.transactionType = transactionType;
		this.description = description;
	}
	
	
	public ViewElements(JSONArray json) {
		super();
		JSONObject jsonData = new JSONObject(json.get(0).toString());
		Logger log = Logger.getLogger(getClass());
		log.info(jsonData);
		
		this.testCaseNumber = jsonData.getString("testCaseNumber");
		this.clientName = jsonData.getString("clientName");
		this.transactionType = jsonData.getString("transactionType");
		this.description = jsonData.getString("description");
	}
	
	
	public String getTestCaseNumber() {
		return testCaseNumber;
	}
	public void setTestCaseNumber(String testCaseNumber) {
		this.testCaseNumber = testCaseNumber;
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
	public String getDescription() {
		return description;
	}
	public void SetDescription(String description) {
		this.description = description;
	}
	
	
		
}
