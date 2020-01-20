package com.automation.db.campaign;



import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "dependent_testcase_record")
public class DependentRecord {
	
	@Id
	@Column(name = "testcase_number")
	private String testcaseNumber;
	
	@ManyToOne
	@JoinColumn(name = "story_id")
	@JsonBackReference
	private Story storyId;
	
	@Column(name = "client_name")
	private String clientName;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "status")
	private String status;

	public DependentRecord() {}
	
	public DependentRecord(String testcaseNumber, Story storyId, String clientName, String description, String status) {
		super();
		this.testcaseNumber = testcaseNumber;
		this.storyId = storyId;
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

	public Story getStoryId() {
		return storyId;
	}

	public void setStoryId(Story storyId) {
		this.storyId = storyId;
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
