package com.enonic.autotests;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.enonic.autotests.exceptions.AuthenticationException;
import com.enonic.autotests.services.IAdminConsoleService;

@ContextConfiguration(locations = { "/test-applicationContext.xml" })
public class AdminConsoleTest extends BaseTest {
	
	@Autowired
	IAdminConsoleService adminConsoleServiceV4;

	@BeforeMethod
	public void openBrowser() throws IOException {
		TestUtils.getInstance().createDriverAndOpenBrowser(getTestSession());
		

	}

	@AfterMethod
	public void closeBrowser() {
		getTestSession().closeBrowser();
	
	}

	@Test
	public void testOpenConsoleLoginSucess() {
		adminConsoleServiceV4.openAdminConsole(getTestSession(), "admin", "password");
	}

	@Test(expectedExceptions = AuthenticationException.class)
	public void testWrongPassword() {
		adminConsoleServiceV4.openAdminConsole(getTestSession(), "admin", "password1");
	}

}
