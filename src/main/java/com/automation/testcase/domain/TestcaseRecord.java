package com.automation.testcase.domain;

import com.automation.user.domain.User;

public class TestcaseRecord {
	
	private long recordId;
	
	private String clientName;
	
	private String testcaseNumber;
	
	private User userId;
	
	private long universeId;
	
	private String description;
	
	private String status;
	
	private String executionSchedule;
	
	private int executionVersion;

	public long getRecordId() {
		return recordId;
	}

	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getTestcaseNumber() {
		return testcaseNumber;
	}

	public void setTestcaseNumber(String testcaseNumber) {
		this.testcaseNumber = testcaseNumber;
	}

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public long getUniverseId() {
		return universeId;
	}

	public void setUniverseId(long universeId) {
		this.universeId = universeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExecutionSchedule() {
		return executionSchedule;
	}

	public void setExecutionSchedule(String executionSchedule) {
		this.executionSchedule = executionSchedule;
	}

	public int getExecutionVersion() {
		return executionVersion;
	}

	public void setExecutionVersion(int executionVersion) {
		this.executionVersion = executionVersion;
	}
	
	
	
}
