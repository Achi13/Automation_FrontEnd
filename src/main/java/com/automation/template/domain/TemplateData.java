package com.automation.template.domain;

public class TemplateData {

	private long templateDataId;
	
	private long templateId;
	
	private String label;
	
	private String natureOfAction;
	
	private boolean isScreenCapture;
	
	private boolean isTriggerEnter;
	
	private String webElementName;
	
	private String webElementNature;
	
	private String screenshotPath;
	
	private String logfield;
	
	private String timestamp;
	
	private String remarks;
	
	private String inputOutputValue;
	
	public TemplateData() {}

	public TemplateData(long templateDataId, String label, String natureOfAction,
			boolean isScreenCapture, boolean isTriggerEnter, String webElementName, String webElementNature) {
		super();
		this.templateDataId = templateDataId;
		this.label = label;
		this.natureOfAction = natureOfAction;
		this.isScreenCapture = isScreenCapture;
		this.isTriggerEnter = isTriggerEnter;
		this.webElementName = webElementName;
		this.webElementNature = webElementNature;
	}

	public long getTemplateDataId() {
		return templateDataId;
	}

	public void setTemplateDataId(long templateDataId) {
		this.templateDataId = templateDataId;
	}

	public long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getNatureOfAction() {
		return natureOfAction;
	}

	public void setNatureOfAction(String natureOfAction) {
		this.natureOfAction = natureOfAction;
	}

	public boolean isScreenCapture() {
		return isScreenCapture;
	}

	public void setScreenCapture(boolean isScreenCapture) {
		this.isScreenCapture = isScreenCapture;
	}

	public boolean isTriggerEnter() {
		return isTriggerEnter;
	}

	public void setTriggerEnter(boolean isTriggerEnter) {
		this.isTriggerEnter = isTriggerEnter;
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

	public String getScreenshotPath() {
		return screenshotPath;
	}

	public void setScreenshotPath(String screenshotPath) {
		this.screenshotPath = screenshotPath;
	}

	public String getLogfield() {
		return logfield;
	}

	public void setLogfield(String logfield) {
		this.logfield = logfield;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getInputOutputValue() {
		return inputOutputValue;
	}

	public void setInputOutputValue(String inputOutputValue) {
		this.inputOutputValue = inputOutputValue;
	}
	
	
	
}
