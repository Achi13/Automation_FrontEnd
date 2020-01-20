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
@Table(name = "story_execution")
public class StoryExecution {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "story_execution_number")
	private int storyExecutionNumber;
	
	@ManyToOne
	@JoinColumn(name = "story_id")
	@JsonBackReference
	private Story storyId;
	
	@ManyToOne
	@JoinColumn(name = "theme_execution_number")
	@JsonBackReference
	private ThemeExecution themeId;
	
	@Column(name = "timestamp")
	private String timestamp;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "story_name")
	private String storyName;
	
	@Column(name = "server_import")
	private String serverImport;
	
	@Column(name = "ignore_severity")
	private String ignoreSeverity;
	
	public StoryExecution() {}
	

	public StoryExecution(Story storyId, ThemeExecution themeId, String timestamp, String description, String status, String storyName) {
		super();
		this.storyId = storyId;
		this.themeId = themeId;
		this.timestamp = timestamp;
		this.description = description;
		this.status = status;
		this.storyName = storyName;
	}
	
	

	public int getStoryExecutionNumber() {
		return storyExecutionNumber;
	}


	public void setStoryExecutionNumber(int storyExecutionNumber) {
		this.storyExecutionNumber = storyExecutionNumber;
	}


	public Story getStoryId() {
		return storyId;
	}

	public void setStoryId(Story storyId) {
		this.storyId = storyId;
	}

	public ThemeExecution getThemeId() {
		return themeId;
	}

	public void setThemeId(ThemeExecution themeId) {
		this.themeId = themeId;
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
	
	public String getStoryName() {
		return storyName;
	}
	
	public void setStoryName(String storyName) {
		this.storyName = storyName;
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
	
	
	
}
