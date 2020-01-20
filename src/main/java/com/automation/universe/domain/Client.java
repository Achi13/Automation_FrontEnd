package com.automation.universe.domain;

import org.json.JSONObject;

public class Client {
	
	private long clientId;
	
	private Universe universeId;
	
	private String clientName;
	
	public Client() {}
	
	public Client(String jsonData) {
		
		JSONObject clientData = new JSONObject(jsonData);
		this.clientId = clientData.getLong("clientId");
		this.clientName = clientData.getString("clientName");
		
	}

	public Client(Universe universeId, String clientName) {
		super();
		this.universeId = universeId;
		this.clientName = clientName;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
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
	
	

}
