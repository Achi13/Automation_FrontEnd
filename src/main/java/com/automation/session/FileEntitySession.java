package com.automation.session;

import com.automation.domain.FileEntity;

public class FileEntitySession {
	
	private static FileEntitySession instance;
	private FileEntity fileEntity;
	
	public static FileEntitySession getInstance() {
		
		if (instance == null) {
			instance = new FileEntitySession();
		}
		return instance;
		
	}
	
	public FileEntity getFileEntity() {
		return this.fileEntity;
	}
	
	public void setFileEntity(FileEntity fileEntity) {
		this.fileEntity = fileEntity;
	}
	
}
