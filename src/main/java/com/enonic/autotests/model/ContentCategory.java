package com.enonic.autotests.model;

public class ContentCategory
{
	private String parentName;
	
	private String name;
	private String description;
	private String contentTypeName;
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getContentTypeName()
	{
		return contentTypeName;
	}
	public void setContentTypeName(String contentTypeName)
	{
		this.contentTypeName = contentTypeName;
	}
	
	public String getParentName()
	{
		return parentName;
	}
	public void setParentName(String parentName)
	{
		this.parentName = parentName;
	}
}
