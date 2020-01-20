package com.automation.domain;

public class Entity {
	
	private String webElementName;
	private String webElementNature;
	private String natureOfAction;
	private String triggerEnter;
	private String inputOutputValue;
	private String screenCapture;
	private String notes;
	
	public Entity(String webElementName, String webElementNature, String natureOfAction, String triggerEnter,
			String inputOutputValue, String screenCapture, String notes) {
		super();
		this.webElementName = webElementName;
		this.webElementNature = webElementNature;
		this.natureOfAction = natureOfAction;
		this.triggerEnter = triggerEnter;
		this.inputOutputValue = inputOutputValue;
		this.screenCapture = screenCapture;
		this.notes = notes;
	}

	public String getWebElementName() {
		return webElementName;
	}

	public void setWebElementName(String webElementName) {
		this.webElementName = webElementName;
	}

	public String getWebElementNature() {
		return webElementNature;
	}

	public void setWebElementNature(String webElementNature) {
		this.webElementNature = webElementNature;
	}

	public String getNatureOfAction() {
		return natureOfAction;
	}

	public void setNatureOfAction(String natureOfAction) {
		this.natureOfAction = natureOfAction;
	}

	public String getTriggerEnter() {
		return triggerEnter;
	}

	public void setTriggerEnter(String triggerEnter) {
		this.triggerEnter = triggerEnter;
	}

	public String getInputOutputValue() {
		return inputOutputValue;
	}

	public void setInputOutputValue(String inputOutputValue) {
		this.inputOutputValue = inputOutputValue;
	}
	
	public String getScreenCapture() {
		return screenCapture;
	}
	
	public void setScreenCapture(String screenCapture) {
		this.screenCapture = screenCapture;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	
	

}
