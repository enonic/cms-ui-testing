package com.enonic.autotests.pages.v4.adminconsole.content.search;

public enum SearchWhere
{
	ATTACHMENTS("File attachments"), ALL_FIELDS("All fields"), TITLE_FIELD_ONLY("Title field only");
	private String value;
	public String getValue()
	{
		return value;
	}
	private SearchWhere(String value)
	{
		this.value = value;
	}
	

}
