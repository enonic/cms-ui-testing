package com.enonic.autotests.testdata.content;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlType(propOrder = { "contentList" }, name = "testdata")
@XmlRootElement
public class ContentTestData
{
	private List<AbstractContentXml> contentList = new ArrayList<>();

	@XmlElements({ @XmlElement(name = "file", type = FileContentXml.class), @XmlElement(name = "image", type = ImageContentXml.class)
                 })
	public List<AbstractContentXml> getContentList()
	{
		return contentList;
	}

	public void setContentList(List<AbstractContentXml> contentList)
	{
		this.contentList = contentList;
	}

}
