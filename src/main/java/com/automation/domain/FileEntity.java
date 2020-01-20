package com.automation.domain;

import java.util.HashMap;
import java.util.List;

public class FileEntity {

	private String fileLoc;
	private String fileName;
	private List<Entity> elements;
	private HashMap<String,String>websites;
	private HashMap<String, String>assigned;
	
	public FileEntity(List<Entity> elements) {
		super();
		this.elements = elements;
	}
	
	public FileEntity() {
		//WEBSITE HASHMAP
		websites = new HashMap<String, String>();
		websites.put("uob", "http://3.1.175.154:15050/wui/activity/index");
		websites.put("tsb", "https://13.251.30.191:15051/wui/login");
		
	}
	
	
	public FileEntity(String fileLoc, String fileName) {
		super();
		this.fileLoc = fileLoc;
		this.fileName = fileName;
	}
	
	public String getFileLoc() {
		return fileLoc;
	}

	public void setFileLoc(String fileLoc) {
		this.fileLoc = fileLoc;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public List<Entity>getElements(){
		return elements;
	}
	
	public void setElements(List<Entity> elements) {
		this.elements = elements;
	}
	
	public HashMap<String, String>getWebsites(){
		return websites;
	}
	
	
	
}
