package com.automation.universe.domain;

public class SupportedSoftware {
	
	private long softwareId;
	
	private Client clientId;
	
	private String clientName;

	public SupportedSoftware(Client clientId, String clientName) {
		super();
		this.clientId = clientId;
		this.clientName = clientName;
	}

	public long getSoftwareId() {
		return softwareId;
	}

	public void setSoftwareId(long softwareId) {
		this.softwareId = softwareId;
	}

	public Client getClientId() {
		return clientId;
	}

	public void setClientId(Client clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
}
