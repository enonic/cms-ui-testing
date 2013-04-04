package com.enonic.autotests.testdata.contenttype;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "contentTypes")
@XmlType(propOrder = { "name", "contentHandler","description","pathToCSS","cfgFile","caseInfo" }, name = "contentType")
public class ContentTypeXml {
	private String name;
	
	private String caseInfo;
		
	/** Content handler's name, 'Custom handler', by default */
	private String contentHandler;

	/** description of the Content type */
	private String description;

	private String pathToCSS;
	
	private String cfgFile;
	

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

	public String getCfgFile() {
		return cfgFile;
	}

	public void setCfgFile(String cfgFile) {
		this.cfgFile = cfgFile;
	}
	public String getCaseInfo() {
		return caseInfo;
	}

	public void setCaseInfo(String caseInfo) {
		this.caseInfo = caseInfo;
	}

}
