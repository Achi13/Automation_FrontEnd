package com.automation.universe.domain;

import org.json.JSONObject;

public class Universe {
	
	private long universeId;
	
	private String universeName;

	public Universe() {}
	
	public Universe(String jsonData) {
		JSONObject universeData = new JSONObject(jsonData);
		this.universeId = universeData.getLong("universeId");
		this.universeName = universeData.getString("universeName");
	}
	
	public Universe(long universeId, String universeName) {
		super();
		this.universeId = universeId;
		this.universeName = universeName;
	}

	public long getUniverseId() {
		return universeId;
	}

	public void setUniverseId(long universeId) {
		this.universeId = universeId;
	}

	public String getUniverseName() {
		return universeName;
	}

	public void setUniverseName(String universeName) {
		this.universeName = universeName;
	}
	
	
	
	

}
