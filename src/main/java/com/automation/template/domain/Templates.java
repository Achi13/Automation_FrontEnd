package com.automation.template.domain;

public class Templates {

	private long templateId;
	
	private long clientId;
	
	private long userId;
	
	private String templateName;
	
	boolean isPublic;
	
	int templateVersion = 1;
	
	public Templates() {}
	
	public Templates(long clientId, long userId, String templateName, boolean isPublic) {
		super();
		this.clientId = clientId;
		this.userId = userId;
		this.templateName = templateName;
		this.isPublic = isPublic;
	}

	public long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public int getTemplateVersion() {
		return templateVersion;
	}

	public void setTemplateVersion(int templateVersion) {
		this.templateVersion = templateVersion;
	}
	
}
