package com.enonic.autotests.services;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.pages.Page;

public interface IAdminConsoleService {

	Page openAdminConsole(TestSession testSession, String user, String password);

	void createContentType(TestSession testSession, ContentType ctype);

	void editContentType(TestSession testSession, String name);

	void deleteContentType(TestSession testSession, String name);
}
