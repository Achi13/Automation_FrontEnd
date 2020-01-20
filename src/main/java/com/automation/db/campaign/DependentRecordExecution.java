package com.automation.db.campaign;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "dependent_testcase_record_execution")
public class DependentRecordExecution {
	
	@Id
	@Column(name = "dependent_testcase_number")
	private String testcaseNumber;
	
	@ManyToOne
	@JoinColumn(name = "story_execution_number")
	@JsonBackReference
	private StoryExecution storyExecutionNumber;
	
	@Column(name = "client_name")
	private String clientName;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "status")
	private String status;

	public DependentRecordExecution() {}
	
	public DependentRecordExecution(String testcaseNumber, StoryExecution storyExecutionNumber, String clientName, String description, String status) {
		super();
		this.testcaseNumber = testcaseNumber;
		this.storyExecutionNumber = storyExecutionNumber;
		this.clientName = clientName;
		this.description = description;
		this.status = status;
	}

	public String getTestcaseNumber() {
		return testcaseNumber;
	}

	public void setTestcaseNumber(String testcaseNumber) {
		this.testcaseNumber = testcaseNumber;
	}

	public StoryExecution getStoryId() {
		return storyExecutionNumber;
	}

	public void setStoryId(StoryExecution storyExecutionNumber) {
		this.storyExecutionNumber = storyExecutionNumber;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
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

}
