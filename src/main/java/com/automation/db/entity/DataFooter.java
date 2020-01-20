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
@Table(name = "footer_data")
public class DataFooter {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "footer_id")
	private int footerId;
	
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "testcase_number")
	@JsonBackReference
	private Record testcaseNumber;
	
	@Column(name = "clientname")
	private String clientName;
	
	@Column(name = "transactiontype")
	private String transactionType;
	
	@Column(name = "website")
	private String website;
	
	@Column(name = "serverimport")
	private String serverImport;
	
	@Column(name = "ignoreseverity")
	private String ignoreSeverity;
	
	@Column(name = "sender")
	private String sender;
	
	@Column(name = "assignedaccount")
	private String assignedAccount;
	
	@Column(name = "testcasestatus")
	private String testcaseStatus;
	
	public DataFooter() {}

	public DataFooter(Record testcaseNumber, String clientName, String transactionType, String website,
			String serverImport, String ignoreSeverity, String sender, String assignedAccount, String testcaseStatus) {
		super();
		this.testcaseNumber = testcaseNumber;
		this.clientName = clientName;
		this.transactionType = transactionType;
		this.website = website;
		this.serverImport = serverImport;
		this.ignoreSeverity = ignoreSeverity;
		this.sender = sender;
		this.assignedAccount = assignedAccount;
		this.testcaseStatus = testcaseStatus;
	}

	public Record getTestcaseNumber() {
		return testcaseNumber;
	}

	public void setTestcaseNumber(Record testcaseNumber) {
		this.testcaseNumber = testcaseNumber;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getServerImport() {
		return serverImport;
	}

	public void setServerImport(String serverImport) {
		this.serverImport = serverImport;
	}

	public String getIgnoreSeverity() {
		return ignoreSeverity;
	}

	public void setIgnoreSeverity(String ignoreSeverity) {
		this.ignoreSeverity = ignoreSeverity;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public String getAssignedAccount() {
		return assignedAccount;
	}
	
	public void setAssignedAccount(String assignedAccount) {
		this.assignedAccount = assignedAccount;
	}

	public String getTestcaseStatus() {
		return testcaseStatus;
	}

	public void setTestcaseStatus(String testcaseStatus) {
		this.testcaseStatus = testcaseStatus;
	}
	
	
	
}
