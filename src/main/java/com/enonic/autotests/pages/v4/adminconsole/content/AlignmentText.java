package com.enonic.autotests.pages.v4.adminconsole.content;

public enum AlignmentText
{
	LEFT("left"), RIGHT("right"), CENTER("center"), FULL("justify");
	private String value;

	public String getValue()
	{
		return value;
	}

	private AlignmentText( String value )
	{
		this.value = value;
	}
}
