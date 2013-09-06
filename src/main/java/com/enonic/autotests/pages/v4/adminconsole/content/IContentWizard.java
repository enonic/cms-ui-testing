package com.enonic.autotests.pages.v4.adminconsole.content;

import java.util.Map;

import com.enonic.autotests.model.Content;

public interface IContentWizard<T>
{

	void typeDataAndSave(Content<T> content);

	String getContentKey();
	
	Map<ContentIndexes,String> getIndexedValues();

}
