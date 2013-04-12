package com.enonic.autotests.testdata.contenttype;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "contents")
@XmlType(propOrder = { "description", "inputsList","contentHandler","selectList"}, name = "content")
public class ContentXml {

	private String description;
	private String contentHandler;
	
	@XmlElementWrapper(name = "inputs")
	@XmlElement(name = "inputs")
	private List<ContentInputXml> inputsList;
	
	@XmlElementWrapper(name = "selects")
	@XmlElement(name = "select")
	private List<ContentSelectXml> selectList;
	

	public List<ContentSelectXml> getSelectList() {
		return selectList;
	}

	public void setSelectList(List<ContentSelectXml> selectList) {
		this.selectList = selectList;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContentHandler() {
		return contentHandler;
	}

	public void setContentHandler(String contentHandler) {
		this.contentHandler = contentHandler;
	}

	public List<ContentInputXml> getInputsList() {
		return inputsList;
	}

	public void setInputsList(List<ContentInputXml> inputsList) {
		this.inputsList = inputsList;
	}
	
}
