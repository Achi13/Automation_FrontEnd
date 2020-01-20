package com.automation.universe.domain;

public class Headers {

	private long headerId;
	
	private String webAddress;
	
	private String webElementName;
	
	private String webElementNature;
	
	private String natureOfAction;

	private String label;
	
	public Headers(String webAddress, String webElementName, String webElementNature, String natureOfAction, String label) {
		super();
		this.webAddress = webAddress;
		this.webElementName = webElementName;
		this.webElementNature = webElementNature;
		this.natureOfAction = natureOfAction;
		this.label = label;
	}

	public long getHeaderId() {
		return headerId;
	}

	public void setHeaderId(long headerId) {
		this.headerId = headerId;
	}

	public String getWebAddress() {
		return webAddress;
	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	
}
