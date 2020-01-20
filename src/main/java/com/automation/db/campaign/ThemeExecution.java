package com.automation.db.campaign;

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
@Table(name = "theme_execution")
public class ThemeExecution {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "theme_execution_number")
	private int themeExecutionNumber;
	
	
	@ManyToOne
	@JoinColumn(name = "theme_id")
	@JsonBackReference
	private Theme themeId;
	
	@ManyToOne
	@JoinColumn(name = "campaign_execution_number")
	@JsonBackReference
	private CampaignExecution campaignId;
	
	
	
	@Column(name = "timestamp")
	private String timestamp;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "theme_name")
	private String themeName;
	
	public ThemeExecution() {}

	public ThemeExecution(Theme themeId, CampaignExecution campaignId, String timestamp, String description, String status, String themeName) {
		super();
		this.themeId = themeId;
		this.campaignId = campaignId;
		this.timestamp = timestamp;
		this.description = description;
		this.status = status;
		this.themeName = themeName;
	}
	
	public int getThemeExecutionNumber() {
		return themeExecutionNumber;
	}
	
	public void setThemeExecutionNumber(int themeExecutionNumber) {
		this.themeExecutionNumber = themeExecutionNumber;
	}

	public Theme getThemeId() {
		return themeId;
	}

	public void setThemeId(Theme themeId) {
		this.themeId = themeId;
	}

	public CampaignExecution getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(CampaignExecution campaignId) {
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
