package com.automation.db.campaign;

import java.util.Set;

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
@Table(name = "story")
public class Story {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "story_id")
	private int storyId;
	
	@ManyToOne
	@JoinColumn(name = "theme_id")
	@JsonBackReference
	private Theme themeId;
	
	@Column(name = "timestamp")
	private String timestamp;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "story_name")
	private String storyName;
	
	@Column(name = "ignore_severity")
	private String ignoreSeverity;
	
	@Column(name = "server_import")
	private String serverImport;
	
	public Story() {}
	

	public Story(Theme themeId, String timestamp, String description, String status, String storyName) {
		super();
		this.themeId = themeId;
		this.timestamp = timestamp;
		this.description = description;
		this.status = status;
		this.storyName = storyName;
	}

	public int getStoryId() {
		return storyId;
	}

	public void setStoryId(int storyId) {
		this.storyId = storyId;
	}

	public Theme getThemeId() {
		return themeId;
	}

	public void setThemeId(Theme themeId) {
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


	public String getIgnoreSeverity() {
		return ignoreSeverity;
	}


	public void setIgnoreSeverity(String ignoreSeverity) {
		this.ignoreSeverity = ignoreSeverity;
	}


	public String getServerImport() {
		return serverImport;
	}


	public void setServerImport(String serverImport) {
		this.serverImport = serverImport;
	}
	
	
	
}
