package com.enonic.autotests;

import org.testng.annotations.Test;

import com.enonic.autotests.exceptions.AuthenticationException;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;

public class AdminConsoleTest extends BaseTest {

	@Test(description = "open admin console with valid administartor's password and verify left and right screens")
	public void testOpenConsoleValidPassword() {
		adminConsoleServiceV4.openAdminConsole(getTestSession(), "admin", "password");
		AbstractAdminConsolePage.verify(getTestSession());
	}

	@Test(description = "open admin console with invalid administartor's password", expectedExceptions = AuthenticationException.class)
	public void testOpenConsoleWrongPassword() {
		adminConsoleServiceV4.openAdminConsole(getTestSession(), "admin", "password1");
	}

}
