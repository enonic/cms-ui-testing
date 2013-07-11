package com.enonic.autotests.testdata.content;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "pathToFile","description" }, name = "file")
public class FileContentXml extends AbstractContentXml
{

	private String pathToFile;
	private String description;
	
	public String getPathToFile()
	{
		return pathToFile;
	}
	public void setPathToFile(String pathToFile)
	{
		this.pathToFile = pathToFile;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
}
