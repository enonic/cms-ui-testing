package com.enonic.autotests;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.enonic.autotests.exceptions.AuthenticationException;
import com.enonic.autotests.pages.v4.AdminConsoolePage;
import com.enonic.autotests.pages.v4.HomePage;

public class AdminConsoleTest {
	private String baseUrl;
	private WebDriver driver;
	private static Properties prop = new Properties();

	@BeforeClass
	public static void initProperties() throws IOException {
		InputStream in = AdminConsoleTest.class.getClassLoader().getResourceAsStream("test.properties");

		prop.load(in);
	}

	@Before
	public void openBrowser() {
		baseUrl = prop.getProperty("base.url");
		driver = new FirefoxDriver();

	}

	@Test
	public void testOpenConsoleLoginSucess() {
		HomePage home = new HomePage(driver, baseUrl);
		home.open();
		AdminConsoolePage page = home.openAdminConsole("admin", "password");
		// TODO check correct info for console page
	}

	@Test(expected = AuthenticationException.class)
	public void testWrongPassword() {
		HomePage home = new HomePage(driver, baseUrl);
		home.open();
		AdminConsoolePage page = home.openAdminConsole("admin", "password1");
	}

	@After
	public void saveScreenshotAndCloseBrowser() throws IOException {
		TestUtils.saveScreenshot("screenshot.png", driver);
		driver.quit();
	}

}
