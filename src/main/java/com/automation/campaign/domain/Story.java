package com.automation.campaign.domain;

import org.json.JSONObject;

import com.automation.universe.domain.Universe;
import com.automation.user.domain.User;

public class Story {
	
	private long storyId;
	
	private Theme themeId;
	
	private User userId;
	
	private Universe universeId;
	
	private String storyName;
	
	private String description;
	
	private String status;
	
	private String timestamp;
	
	private boolean isIgnoreSeverity;
	
	private boolean isServerImport;
	
	private int executionVersionStart;
	
	private int executionVersionCurrent;
	
	public Story() {}
	
	public Story(String jsonData, Theme themeId) {
		JSONObject storyData = new JSONObject(jsonData);
		this.storyId = storyData.getLong("storyId");
		this.themeId = themeId;
		this.storyName = storyData.getString("storyName");
		this.description = storyData.getString("description");
		this.status = storyData.getString("status");
		this.timestamp = storyData.getString("timestamp");
		this.isIgnoreSeverity = storyData.getBoolean("isIgnoreSeverity");
		this.isServerImport = storyData.getBoolean("isServerImport");
		this.executionVersionStart = storyData.getInt("executionVersionStart");
		this.executionVersionCurrent = storyData.getInt("executionVersionCurrent");
	}
	
	public Story(String jsonData, Universe universeId, User userId) {
		
		JSONObject storyData = new JSONObject(jsonData);
		this.storyId = storyData.getLong("storyId");
		this.userId = userId;
		this.universeId = universeId;
		this.storyName = storyData.getString("storyName");
		this.description = storyData.getString("description");
		this.status = storyData.getString("status");
		this.timestamp = storyData.getString("timestamp");
		this.isIgnoreSeverity = storyData.getBoolean("isIgnoreSeverity");
		this.isServerImport = storyData.getBoolean("isServerImport");
		this.executionVersionStart = storyData.getInt("executionVersionStart");
		this.executionVersionCurrent = storyData.getInt("executionVersionCurrent");
	}

	public Story(Theme themeId, User userId, Universe universeId, String storyName, String description, String status,
			String timestamp, boolean isIgnoreSeverity, boolean isServerImport, int executionVersionStart,
			int executionVersionCurrent) {
		super();
		this.themeId = themeId;
		this.userId = userId;
		this.universeId = universeId;
		this.storyName = storyName;
		this.description = description;
		this.status = status;
		this.timestamp = timestamp;
		this.isIgnoreSeverity = isIgnoreSeverity;
		this.isServerImport = isServerImport;
		this.executionVersionStart = executionVersionStart;
		this.executionVersionCurrent = executionVersionCurrent;
	}

	public long getStoryId() {
		return storyId;
	}

	public void setStoryId(long storyId) {
		this.storyId = storyId;
	}

	public Theme getThemeId() {
		return themeId;
	}

	public void setThemeId(Theme themeId) {
		this.themeId = themeId;
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

	public String getStoryName() {
		return storyName;
	}

	public void setStoryName(String storyName) {
		this.storyName = storyName;
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

	public boolean getIsIgnoreSeverity() {
		return isIgnoreSeverity;
	}

	public void setIsIgnoreSeverity(boolean isIgnoreSeverity) {
		this.isIgnoreSeverity = isIgnoreSeverity;
	}

	public boolean getIsServerImport() {
		return isServerImport;
	}

	public void setIsServerImport(boolean isServerImport) {
		this.isServerImport = isServerImport;
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
