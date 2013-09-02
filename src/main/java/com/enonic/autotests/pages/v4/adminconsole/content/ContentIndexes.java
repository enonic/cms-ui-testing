package com.enonic.autotests.pages.v4.adminconsole.content;

/**
assignee/key	

assigner/key

attachment	
categorykey
contenttype	
contenttypekey	
created	
language	
modified	
modifier
owner

priority	
timestamp
title
*/
public enum ContentIndexes
{
	ASSIGNEE("assignee/key"),ATTACHMENT("attachment"),CREATED("created"),CATEGORYKEY("categorykey");
	private String value;
	private ContentIndexes(String value)
{
	this.value = value;
}

	public String getValue(){
		return value;
	}
	
}
