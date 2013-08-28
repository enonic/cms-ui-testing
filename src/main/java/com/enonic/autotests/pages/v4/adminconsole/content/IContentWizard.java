package com.enonic.autotests.pages.v4.adminconsole.content;

import com.enonic.autotests.model.Content;

public interface IContentWizard<T>
{

	void typeDataAndSave(Content<T> content);

	String getContentKey();

}
