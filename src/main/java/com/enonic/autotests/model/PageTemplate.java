package com.enonic.autotests.model;

public class PageTemplate
{
	private String name;
	
	private String description;
	
	private PageType type;
	
	private TemplateStylesheet stylesheet;
	
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

	public PageType getType()
	{
		return type;
	}

	public void setType(PageType type)
	{
		this.type = type;
	}

	public TemplateStylesheet getStylesheet()
	{
		return stylesheet;
	}

	public void setStylesheet(TemplateStylesheet stylesheet)
	{
		this.stylesheet = stylesheet;
	}

	
	
	public static class TemplateStylesheet
	{
		private String[] path;
		private String name;
	}
}
