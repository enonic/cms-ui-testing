package com.enonic.autotests;

import org.testng.annotations.Test;

import com.enonic.autotests.exceptions.AuthenticationException;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.services.v4.PageNavigatorV4;

public class AdminConsoleTest extends BaseTest {

	@Test(description = "open admin console with valid administartor's password and verify left and right screens")
	public void testOpenConsoleValidPassword() {
		PageNavigatorV4.navgateToAdminConsole(getTestSession());
		AbstractAdminConsolePage.verify(getTestSession());
	}

	@Test(description = "open admin console with invalid administartor's password", expectedExceptions = AuthenticationException.class)
	public void testOpenConsoleWrongPassword() {
		PageNavigatorV4.navgateToAdminConsole(getTestSession());
	}

}
