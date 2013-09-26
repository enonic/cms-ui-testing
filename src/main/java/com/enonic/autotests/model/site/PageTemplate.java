package com.enonic.autotests.model.site;


/**
 * Model for Page Template.
 *
 */
public class PageTemplate
{
	private String name;
	
	private String description;
	
	private PageType type;
	
	private STKResource stylesheet;
	
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

	public STKResource getStylesheet()
	{
		return stylesheet;
	}

	public void setStylesheet(STKResource stylesheet)
	{
		this.stylesheet = stylesheet;
	}

	
	
//	public static class TemplateStylesheet
//	{
//		private String[] path;
//		private String name;
//		public String[] getPath()
//		{
//			return path;
//		}
//		public void setPath(String... path)
//		{
//			this.path = path;
//		}
//		public String getName()
//		{
//			return name;
//		}
//		public void setName(String name)
//		{
//			this.name = name;
//		}
//		
//	}
}
