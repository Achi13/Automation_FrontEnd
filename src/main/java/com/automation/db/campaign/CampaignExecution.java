package com.automation.db.campaign;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;

import com.automation.db.campaign.repository.CampaignRepository;
import com.automation.db.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "campaign_execution")
public class CampaignExecution {
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "campaign_execution_number")
	private int campaignExecutionNumber;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "campaign_id")
	@JsonBackReference
	private Campaign campaignId;
	
	@Column(name = "timestamp")
	private String timestamp;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "campaign_name")
	private String campaignName;
	
	public CampaignExecution() {}

	public CampaignExecution(Campaign campaignId, String timestamp, String description, String status, String campaignName) {
		super();
		this.campaignId = campaignId;
		this.timestamp = timestamp;
		this.description = description;
		this.status = status;
		this.campaignName = campaignName;
	}
	/*
	public CampaignExecution(Campaign campaign) {
		this.campaignId = campaignRepo.findCampaignByCampaignId(campaign.getCampaignId());
		this.timestamp = campaign.getTimestamp();
		this.description = campaign.getDescription();
		this.status = campaign.getStatus();
		this.campaignName = campaign.getCampaignName();
	}*/
	
	public int getCampaignExecutionNumber() {
		return campaignExecutionNumber;
	}
	
	public void setCampaignExecutionNumber(int campaignExecutionNumber) {
		this.campaignExecutionNumber = campaignExecutionNumber;
	}
	
	public Campaign getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Campaign campaignId) {
		this.campaignId = campaignId;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
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
	
	public String getCampaignName() {
		return campaignName;
	}
	
	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}
	
}
