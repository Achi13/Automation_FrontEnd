package com.automation.db.campaign;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "theme")
public class Theme {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "theme_id")
	private int themeId;
	
	@ManyToOne
	@JoinColumn(name = "campaign_id")
	@JsonBackReference
	private Campaign campaignId;
	
	@Column(name = "timestamp")
	private String timestamp;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "theme_name")
	private String themeName;
	
	public Theme() {}

	public Theme(Campaign campaignId, String timestamp, String description, String status, String themeName) {
		super();
		this.campaignId = campaignId;
		this.timestamp = timestamp;
		this.description = description;
		this.status = status;
		this.themeName = themeName;
	}

	public int getThemeId() {
		return themeId;
	}

	public void setThemeId(int themeId) {
		this.themeId = themeId;
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
	
	public String getThemeName() {
		return themeName;
	}
	
	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}
	
	

}
