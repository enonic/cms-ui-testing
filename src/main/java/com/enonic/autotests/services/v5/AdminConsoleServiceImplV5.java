package com.enonic.autotests.services.v5;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.BaseAbstractContent;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.pages.Page;
import com.enonic.autotests.services.IAdminConsoleService;

public class AdminConsoleServiceImplV5 implements IAdminConsoleService {

//	@Override
//	public void openAdminConsole(TestSession testSession, String user, String password) {
//		// TODO Auto-generated method stub
//		// return null;
//
//	}

	@Override
	public Page createContentType(TestSession testSession, ContentType ctype) {
		return null;

	}

	@Override
	public void editContentType(TestSession testSession, String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public Page deleteContentType(TestSession testSession, String name) {
		return null;

	}

	@Override
	public Page createContentRepository(TestSession testSession, ContentRepository ctype) {
		return null;
		
	}

	@Override
	public void addContentToRepository(TestSession testSession, ContentRepository ctype,BaseAbstractContent content) {
		// TODO Auto-generated method stub
		
	}

}
