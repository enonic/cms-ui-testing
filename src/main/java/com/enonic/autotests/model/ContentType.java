package com.enonic.autotests.model;

public class ContentType {

	private String name;
	private String contentHandler;
	private String description;
	private String pathToCSS;
	private String configuration;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContentHandler() {
		return contentHandler;
	}
	public void setContentHandler(String contentHandler) {
		this.contentHandler = contentHandler;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPathToCSS() {
		return pathToCSS;
	}
	public void setPathToCSS(String pathToCSS) {
		this.pathToCSS = pathToCSS;
	}
	public String getConfiguration() {
		return configuration;
	}
	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}
	
}
