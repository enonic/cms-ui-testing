package com.enonic.autotests.services.v4;

import org.springframework.stereotype.Service;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.model.User;
import com.enonic.autotests.pages.Page;
import com.enonic.autotests.pages.v4.HomePage;
import com.enonic.autotests.pages.v4.adminconsole.ContentTypesFrame;
import com.enonic.autotests.pages.v4.adminconsole.LeftMenuFrame;
import com.enonic.autotests.services.IAdminConsoleService;


@Service("adminConsoleServiceV4")
public class AdminConsoleServiceImplV4 implements IAdminConsoleService {
	

	@Override
	public void createContentType(TestSession testSession, ContentType ctype) {
		navgateToAdminConsole(testSession);
		LeftMenuFrame menu = new LeftMenuFrame();
		ContentTypesFrame frame = menu.openContentTypesFrame(testSession);
		frame.createContentType(ctype);
		

	}

	@Override
	public void editContentType(TestSession testSession, String name) {
		navgateToAdminConsole(testSession);
		

	}

	@Override
	public void deleteContentType(TestSession testSession, String name) {
		navgateToAdminConsole(testSession);
		
	}

	
	public Page openAdminConsole(TestSession testSession, String userName, String password) {
		HomePage home = new HomePage(testSession);
		home.open();
		return home.openAdminConsole(userName, password);
	}

	private void navgateToAdminConsole(TestSession testSession) {
		User user = testSession.getCurrentUser();
		if (user != null) {
			openAdminConsole(testSession, user.getName(), user.getPassword());
		} else {
			openAdminConsole(testSession, "admin", "password");
		}
	}
}
