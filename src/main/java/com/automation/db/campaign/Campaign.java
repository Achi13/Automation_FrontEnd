package com.automation.db.campaign;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.automation.db.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "campaign")
public class Campaign {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "campaign_id")
	private int campaignId;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonBackReference
	private User userId;
	
	@Column(name = "timestamp")
	private String timestamp;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "campaign_name")
	private String campaignName;
	
	public Campaign() {}

	public Campaign(User userId, String timestamp, String description, String status, String campaignName) {
		super();
		this.userId = userId;
		this.timestamp = timestamp;
		this.description = description;
		this.status = status;
		this.campaignName = campaignName;
	}

	public int getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
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
