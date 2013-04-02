package com.enonic.autotests.services;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.ContentType;

public interface IAdminConsoleService {

	void openAdminConsole(TestSession testSession, String user, String password);

	void createContentType(TestSession testSession, ContentType ctype);

	void editContentType(TestSession testSession, String name);

	void deleteContentType(TestSession testSession, String name);
}
