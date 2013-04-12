package com.enonic.autotests.model;

public class ImageContent extends BaseAbstractContent {
	
	private String name;
	private String description;
	private String fileName;

	private String photographerName;

	private String photographerEmail;

	private String copyright;
	private String keyWords;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPhotographerName() {
		return photographerName;
	}

	public void setPhotographerName(String photographerName) {
		this.photographerName = photographerName;
	}

	public String getPhotographerEmail() {
		return photographerEmail;
	}

	public void setPhotographerEmail(String photographerEmail) {
		this.photographerEmail = photographerEmail;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

}
