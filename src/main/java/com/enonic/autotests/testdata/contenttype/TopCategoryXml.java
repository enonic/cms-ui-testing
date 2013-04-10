package com.enonic.autotests.testdata.contenttype;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "contentRepository")
@XmlType(propOrder = { "name", "description", "contentType" }, name = "topCategory")
public class TopCategoryXml {

	private String name;
	private String description;
	private ContentTypeXml contentType;

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

	public ContentTypeXml getContentType() {
		return contentType;
	}

	public void setContentType(ContentTypeXml contentType) {
		this.contentType = contentType;
	}
}
