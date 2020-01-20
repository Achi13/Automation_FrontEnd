package com.automation.testcase.domain;

public class TestcaseActualData {

	private long actualDataId;

	private String testcaseNumber;
	
	private String inputOutputValue;
	
	private String label;
	
	private String natureOfAction;
	
	private String remarks;
	
	private String screenshotPath;
	
	private boolean isScreenCapture;
	
	private String timestamp;
	
	private boolean isTriggerEnter;
	
	private String webElementName;
	
	private String webElementNature;
	
	private String logfield;
	private int executionVersion;
	
	public TestcaseActualData() {}

	public long getActualDataId() {
		return actualDataId;
	}

	public void setActualDataId(long actualDataId) {
		this.actualDataId = actualDataId;
	}

	public String getTestcaseNumber() {
		return testcaseNumber;
	}

	public void setTestcaseNumber(String testcaseNumber) {
		this.testcaseNumber = testcaseNumber;
	}

	public String getInputOutputValue() {
		return inputOutputValue;
	}

	public void setInputOutputValue(String inputOutputValue) {
		this.inputOutputValue = inputOutputValue;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getScreenshotPath() {
		return screenshotPath;
	}

	public void setScreenshotPath(String screenshotPath) {
		this.screenshotPath = screenshotPath;
	}

	public boolean isScreenCapture() {
		return isScreenCapture;
	}

	public void setScreenCapture(boolean isScreenCapture) {
		this.isScreenCapture = isScreenCapture;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
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

	public String getLogfield() {
		return logfield;
	}

	public void setLogfield(String logfield) {
		this.logfield = logfield;
	}

	public int getExecutionVersion() {
		return executionVersion;
	}

	public void setExecutionVersion(int executionVersion) {
		this.executionVersion = executionVersion;
	}
	
	

	
}
