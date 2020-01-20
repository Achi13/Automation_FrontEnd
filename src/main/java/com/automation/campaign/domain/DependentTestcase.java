package com.automation.campaign.domain;

import org.json.JSONObject;

import com.automation.universe.domain.Universe;
import com.automation.user.domain.User;

public class DependentTestcase {

	private String testcaseNumber;
	
	private Story storyId;
	
	private User userId;
	
	private Universe universeId;
	
	private String clientName;
	
	private String description;
	
	private String status;
	
	private String sender;
	
	private int executionVersionStart;
	
	private int executionVersionCurrent;
	
	public DependentTestcase() {}
	
	public DependentTestcase(String jsonData, Universe universe, User user) {
		
		JSONObject dTestcase = new JSONObject(jsonData);
		JSONObject clientName = dTestcase.getJSONObject("clientId");
		
		this.testcaseNumber = dTestcase.getString("testcaseNumber");
		this.universeId = universe;
		this.userId = user;
		this.clientName = clientName.getString("clientName");
		this.description = dTestcase.getString("description");
		this.status = dTestcase.getString("status");
		this.executionVersionStart = dTestcase.getInt("executionVersionStart");
		this.executionVersionCurrent = dTestcase.getInt("executionVersionCurrent");
		
	}

	public DependentTestcase(String testcaseNumber, Story storyId, User userId, Universe universeId, String clientName,
			String description, String status, int executionVersionStart, int executionVersionCurrent) {
		super();
		this.testcaseNumber = testcaseNumber;
		this.storyId = storyId;
		this.userId = userId;
		this.universeId = universeId;
		this.clientName = clientName;
		this.description = description;
		this.status = status;
		this.executionVersionStart = executionVersionStart;
		this.executionVersionCurrent = executionVersionCurrent;
	}

	public String getTestcaseNumber() {
		return testcaseNumber;
	}

	public void setTestcaseNumber(String testcaseNumber) {
		this.testcaseNumber = testcaseNumber;
	}

	public Story getStoryId() {
		return storyId;
	}

	public void setStoryId(Story storyId) {
		this.storyId = storyId;
	}

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public Universe getUniverseId() {
		return universeId;
	}

	public void setUniverseId(Universe universeId) {
		this.universeId = universeId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getExecutionVersionStart() {
		return executionVersionStart;
	}

	public void setExecutionVersionStart(int executionVersionStart) {
		this.executionVersionStart = executionVersionStart;
	}

	public int getExecutionVersionCurrent() {
		return executionVersionCurrent;
	}

	public void setExecutionVersionCurrent(int executionVersionCurrent) {
		this.executionVersionCurrent = executionVersionCurrent;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
	
	
		
}
