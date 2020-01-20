package com.automation.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "testcase_record")
@Audited
public class Record {
	@Id
	@Column(name = "testcase_number")
	private String testcase;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonBackReference
	private User user;
	
	@Column(name = "client_name")
	private String clientName;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "execution_schedule")
	private String executionSchedule;
	
	public Record() {}
	
	public Record(User user, String testcase, String clientName, String status, String description) {
		super();
		this.user = user;
		this.testcase = testcase;
		this.clientName = clientName;
		this.status = status;
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTestcase() {
		return testcase;
	}

	public void setTestcase(String testcase) {
		this.testcase = testcase;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getExecutionSchedule() {
		return executionSchedule;
	}
	
	public void setExecutionSchedule(String executionSchedule) {
		this.executionSchedule = executionSchedule;
	}
	
	
	
}
