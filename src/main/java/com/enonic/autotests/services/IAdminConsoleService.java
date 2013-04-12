package com.enonic.autotests.services;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.BaseAbstractContent;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.pages.Page;

public interface IAdminConsoleService {
	<T extends Page> T createContentType(TestSession testSession, ContentType ctype);

	void editContentType(TestSession testSession, String name);

	<T extends Page> T deleteContentType(TestSession testSession, String name);
	
	<T extends Page> T createContentRepository(TestSession testSession, ContentRepository ctype);
	
	void addContentToRepository(TestSession testSession,ContentRepository cRepository,BaseAbstractContent content); 
}
