package com.enonic.autotests;

import org.testng.annotations.Test;

import com.enonic.autotests.exceptions.AuthenticationException;
import com.enonic.autotests.services.PageNavigator;

public class AdminConsoleTest extends BaseTest {

	@Test(description = "open admin console with valid administartor's password and verify left and right screens")
	public void testOpenConsoleValidPassword() {
		PageNavigator.navgateToAdminConsole( getTestSession() );
		//boolean result = AbstractAdminConsolePage.verify(getTestSession());
		//Assert.assertTrue(result,"error during verifyin the AdminConsole page");
	}

	@Test(description = "open admin console with invalid administartor's password", expectedExceptions = AuthenticationException.class)
	public void testOpenConsoleWrongPassword() {
		PageNavigator.navgateToAdminConsole( getTestSession() );
	}

}
