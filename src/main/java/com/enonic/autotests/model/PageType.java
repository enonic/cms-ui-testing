package com.enonic.autotests.model;

public enum PageType
{
	DEFAULT("Default"), DOCUMENT("Document"), FORM("Form"), Content("Content");
	
	private String value;

	/**
	 * @return
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * The constructor.
	 * 
	 * @param value
	 */
	private PageType( String value )
	{
		this.value = value;
	}
}
