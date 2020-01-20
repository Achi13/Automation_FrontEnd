package com.automation.db.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "raw_data")
public class DataRaw {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "rawId")
	private int rawId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "testcase_number")
	@JsonBackReference
	private Record testcaseNumber;
	
	@Column(name = "webelementname")
	private String webELementName;
	
	@Column(name = "webelementnature")
	private String webElementNature;
	
	@Column(name = "natureofaction")
	private String natureOfAction;
	
	@Column(name = "triggerenter")
	private String triggerEnter;
	
	@Column(name = "inputoutputvalue")
	private String inputOutputValue;
	
	@Column(name = "screencapture")
	private String screenCapture;
	
	@Column(name = "label")
	private String label;
	
	@Column(name = "timestamp")
	private String timeStamp;
	
	@Column(name = "scpath")
	private String scPath;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "logfield")
	private String logField;

	
	public DataRaw() {}
	
	public DataRaw(Record testcaseNumber, String webELementName, String webElementNature, String natureOfAction,
			String triggerEnter, String inputOutputValue, String screenCapture, String label, String timeStamp,
			String scPath, String remarks, String logField) {
		super();
		this.testcaseNumber = testcaseNumber;
		this.webELementName = webELementName;
		this.webElementNature = webElementNature;
		this.natureOfAction = natureOfAction;
		this.triggerEnter = triggerEnter;
		this.inputOutputValue = inputOutputValue;
		this.screenCapture = screenCapture;
		this.label = label;
		this.timeStamp = timeStamp;
		this.scPath = scPath;
		this.remarks = remarks;
		this.logField = logField;
	}

	public Record getTestcaseNumber() {
		return testcaseNumber;
	}

	public void setTestcaseNumber(Record testcaseNumber) {
		this.testcaseNumber = testcaseNumber;
	}

	public String getWebELementName() {
		return webELementName;
	}

	public void setWebELementName(String webELementName) {
		this.webELementName = webELementName;
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getScPath() {
		return scPath;
	}

	public void setScPath(String scPath) {
		this.scPath = scPath;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getLogField() {
		return logField;
	}

	public void setLogField(String logField) {
		this.logField = logField;
	}
	
	
	
}
