package com.enonic.autotests.model;

/**
 * Representation for 'Content Type'. Located in the  'Admin/Content types' frame
 * 
 */
public class ContentType {

	public static String DEFAULT_CONTENTHANDLER_NAME = "Custom content";
	/** Name of the content type */
	private String name;
	
	/** Content handler's name, 'Custom handler', by default */
	private String contentHandler;

	/** description of the Content type */
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
