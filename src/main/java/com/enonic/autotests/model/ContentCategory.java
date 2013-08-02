package com.enonic.autotests.model;

/**
 * Model class for 'Category' cms-object
 * 
 */
public class ContentCategory
{
	private String[] parentNames;	
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

	public String[] getParentNames()
	{
		return parentNames;
	}

	public void setParentNames(String[] parentNames)
	{
		this.parentNames = parentNames;
	}
	
}
