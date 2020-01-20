package com.automation.universe.domain;

public class ScriptVariable {

	private long variableId;
	
	private Script scriptId;
	
	private String name;
	
	private String description;
	
	public ScriptVariable() {}

	public long getVariableId() {
		return variableId;
	}

	public void setVariableId(long variableId) {
		this.variableId = variableId;
	}

	public Script getScriptId() {
		return scriptId;
	}

	public void setScriptId(Script scriptId) {
		this.scriptId = scriptId;
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
	
	
	
}
