package com.enonic.autotests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.enonic.autotests.exceptions.AuthenticationException;
import com.enonic.autotests.pages.v4.AdminConsoolePage;
import com.enonic.autotests.pages.v4.HomePage;

public class AdminConsoleTest extends BaseTest {

	@BeforeMethod
	public void openBrowser() {
		setDriver( TestUtils.createWebDriver( getDriverName()));

	}

	@AfterMethod
	public void closeBrowser() {
		getDriver().quit();
	}

	@Test
	public void testOpenConsoleLoginSucess() {
		HomePage home = new HomePage(getDriver(), getBaseUrl());
		home.open();
		AdminConsoolePage page = home.openAdminConsole("admin", "password");
		// TODO check correct info for console page
	}

	@Test(expectedExceptions = AuthenticationException.class)
	public void testWrongPassword() {
		HomePage home = new HomePage(getDriver(), getBaseUrl());
		home.setLogged(false);
		home.open();
		AdminConsoolePage page = home.openAdminConsole("admin", "password1");
	}

}
