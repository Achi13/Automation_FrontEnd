package com.automation.campaign.domain;

import org.json.JSONObject;

import com.automation.universe.domain.Universe;
import com.automation.user.domain.User;

public class Theme {
	
	private long themeId;
	
	private Campaign campaignId;
	
	private User userId;
	
	private Universe universeId;
	
	private String themeName;
	
	private String description;
	
	private String status;
	
	private String timestamp;
	
	private int executionVersionStart;
	
	private int executionVersionCurrent;
	
	public Theme() {}
	
public Theme(JSONObject themeData, Campaign campaignId) {
		
		this.themeId = themeData.getLong("themeId");
		this.campaignId = campaignId;
		this.themeName = themeData.getString("themeName");
		this.description = themeData.getString("description");
		this.status = themeData.getString("status");
		this.timestamp = themeData.getString("timestamp");
		this.executionVersionStart = themeData.getInt("executionVersionStart");
		this.executionVersionCurrent = themeData.getInt("executionVersionCurrent");
		
	}
	
	public Theme(String jsonData, Campaign campaignId, User userId, Universe universeId) {
		
		JSONObject themeData = new JSONObject(jsonData);
		this.themeId = themeData.getLong("themeId");
		this.campaignId = campaignId;
		this.userId = userId;
		this.universeId = universeId;
		this.themeName = themeData.getString("themeName");
		this.description = themeData.getString("description");
		this.status = themeData.getString("status");
		this.timestamp = themeData.getString("timestamp");
		this.executionVersionStart = themeData.getInt("executionVersionStart");
		this.executionVersionCurrent = themeData.getInt("executionVersionCurrent");
		
	}

	public Theme(Campaign campaignId, User userId, Universe universeId, String themeName, String description,
			String status, String timestamp, int executionVersionStart, int executionVersionCurrent) {
		super();
		this.campaignId = campaignId;
		this.userId = userId;
		this.universeId = universeId;
		this.themeName = themeName;
		this.description = description;
		this.status = status;
		this.timestamp = timestamp;
		this.executionVersionStart = executionVersionStart;
		this.executionVersionCurrent = executionVersionCurrent;
	}

	public long getThemeId() {
		return themeId;
	}

	public void setThemeId(long themeId) {
		this.themeId = themeId;
	}

	public Campaign getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Campaign campaignId) {
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

	public String getThemeName() {
		return themeName;
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
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
	
}
