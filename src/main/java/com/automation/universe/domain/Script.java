package com.automation.universe.domain;

public class Script {
	
	private long scriptId;
	
	private ClientLoginAccount loginAccountId;
	
	private Universe universeId;
	
	private String name;
	
	private String description;
	
	private String scriptFilepath;
	
	public Script() {}

	public Script(long scriptId, ClientLoginAccount loginAccountId, Universe universeId, String name,
			String description, String scriptFilepath) {
		super();
		this.scriptId = scriptId;
		this.loginAccountId = loginAccountId;
		this.universeId = universeId;
		this.name = name;
		this.description = description;
		this.scriptFilepath = scriptFilepath;
	}

	public long getScriptId() {
		return scriptId;
	}

	public void setScriptId(long scriptId) {
		this.scriptId = scriptId;
	}

	public ClientLoginAccount getLoginAccountId() {
		return loginAccountId;
	}

	public void setLoginAccountId(ClientLoginAccount loginAccountId) {
		this.loginAccountId = loginAccountId;
	}

	public Universe getUniverseId() {
		return universeId;
	}

	public void setUniverseId(Universe universeId) {
		this.universeId = universeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScriptFilepath() {
		return scriptFilepath;
	}

	public void setScriptFilepath(String scriptFilepath) {
		this.scriptFilepath = scriptFilepath;
	}
	
	
}
