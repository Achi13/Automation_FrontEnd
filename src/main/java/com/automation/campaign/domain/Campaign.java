package com.automation.campaign.domain;

import org.json.JSONObject;

import com.automation.universe.domain.Universe;
import com.automation.user.domain.User;

public class Campaign {
	
	private long campaignId;
	
	private User userId;
	
	private Universe universeId;
	
	private String campaignName;
	
	private String description;
	
	private String status;

	private String timestamp;
	
	private String executionSchedule;
	
	private int executionVersionCurrent;
	
	private boolean isSuccess;
	
	private String testcaseList;
	
	public Campaign() {}
	
	public Campaign(String jsonData, User userId, Universe universeId) {
		
		JSONObject campaignData = new JSONObject(jsonData);
		this.userId = userId;
		this.universeId = universeId;
		this.campaignId = campaignData.getLong("campaignId");
		this.campaignName = campaignData.getString("campaignName");
		this.description = campaignData.getString("description");
		this.status = campaignData.getString("status");
		this.timestamp = campaignData.getString("timestamp");
		this.executionSchedule = campaignData.getString("executionSchedule");
		this.executionVersionCurrent = campaignData.getInt("executionVersionCurrent");
		
	}

	public Campaign(User userId, Universe universeId, String campaignName, String description, String status,
			String timestamp, String executionSchedule, int executionVersionCurrent) {
		super();
		this.userId = userId;
		this.universeId = universeId;
		this.campaignName = campaignName;
		this.description = description;
		this.status = status;
		this.timestamp = timestamp;
		this.executionSchedule = executionSchedule;
		this.executionVersionCurrent = executionVersionCurrent;
	}

	public long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(long campaignId) {
		this.campaignId = campaignId;
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

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
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

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getExecutionSchedule() {
		return executionSchedule;
	}

	public void setExecutionSchedule(String executionSchedule) {
		this.executionSchedule = executionSchedule;
	}

	public int getExecutionVersionCurrent() {
		return executionVersionCurrent;
	}

	public void setExecutionVersionCurrent(int executionVersionCurrent) {
		this.executionVersionCurrent = executionVersionCurrent;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getTestcaseList() {
		return testcaseList;
	}

	public void setTestcaseList(String testcaseList) {
		this.testcaseList = testcaseList;
	}
	
	
	
}
