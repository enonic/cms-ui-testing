package com.enonic.autotests.model.site;

public class Portlet
{
	private String name;
	private STKResource stylesheet;
	private STKResource borderStylesheet;
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public STKResource getStylesheet()
	{
		return stylesheet;
	}
	public void setStylesheet(STKResource stylesheet)
	{
		this.stylesheet = stylesheet;
	}
	public STKResource getBorderStylesheet()
	{
		return borderStylesheet;
	}
	public void setBorderStylesheet(STKResource borderStylesheet)
	{
		this.borderStylesheet = borderStylesheet;
	}
}
