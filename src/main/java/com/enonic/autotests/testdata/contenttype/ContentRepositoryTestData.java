package com.enonic.autotests.testdata.contenttype;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "contentRepositories" }, name = "testdata")
@XmlRootElement
public class ContentRepositoryTestData {

	private List<ContentRepositoryXml> contentRepositories = new ArrayList<ContentRepositoryXml>();

	@XmlElementWrapper(name = "contentRepositories")
	@XmlElement(name = "contentRepositories")
	public List<ContentRepositoryXml> getContentRepositories() {
		return contentRepositories;
	}

	public void setContentRepositories(List<ContentRepositoryXml> contentTypes) {
		this.contentRepositories = contentTypes;
	}

}