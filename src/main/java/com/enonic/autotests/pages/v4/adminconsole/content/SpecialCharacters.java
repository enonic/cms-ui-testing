package com.enonic.autotests.pages.v4.adminconsole.content;

public enum SpecialCharacters
{
	//TODO add all special characters
	AMP("38"), QUOT("34"), CENT("162"), EURO("8364");
	
	private String value;

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	private SpecialCharacters( String value )
	{
		this.value = value;
	}

}
