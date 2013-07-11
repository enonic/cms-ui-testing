package com.enonic.autotests.testdata.content;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.enonic.autotests.testdata.contenttype.ContentTypeXml;

/**
 * Top Category panel from 'new Content repository' wizard 
 *
 */
@XmlRootElement(name = "contentRepository")
@XmlType(propOrder = { "name", "description", "contentType" }, name = "topCategory")
public class TopCategoryXml {

	private String name;
	private String description;
    private ContentTypeXml contentType;
	//private String contentTypeName;

	

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
	
//	public String getContentTypeName()
//	{
//		return contentTypeName;
//	}
//
//	public void setContentTypeName(String contentTypeName)
//	{
//		this.contentTypeName = contentTypeName;
//	}

	public ContentTypeXml getContentType() {
		return contentType;
	}

	public void setContentType(ContentTypeXml contentType) {
		this.contentType = contentType;
	}
}
