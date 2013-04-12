package com.enonic.autotests.testdata.contenttype;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "contents" }, name = "testdata")
@XmlRootElement
public class ContentTestData {

	private List<ContentXml> content = new ArrayList<ContentXml>();

	@XmlElementWrapper(name = "content")
	@XmlElement(name = "content")
	public List<ContentXml> getContentTypes() {
		return content;
	}

	public void setContentTypes(List<ContentXml> content) {
		this.content = content;
	}

}