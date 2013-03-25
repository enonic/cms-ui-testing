package com.enonic.autotests.pages.v4;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.TestUtils;
import com.enonic.autotests.logger.Logger;
import com.enonic.autotests.pages.Page;
import com.enonic.autotests.pages.v4.adminconsole.AdminConsolePage;

public class HomePage extends Page {

	private Logger logger = Logger.getInstance();

	public static String TITLE = "Enonic CMS - Boot Page";

	@FindBy(xpath = "//span[text()='Admin Console']")
	private WebElement admConsoleLink;

	/**
	 * @param session
	 */
	public HomePage(TestSession session) {
		super(session);

	}

	public void open() {
		// open page via the driver.get(BASE_URL)
		getSession().getDriver().get(getSession().getBaseUrl());
		TestUtils.getInstance().waitUntilTitleVisible(getSession(), getTitle().trim());

	}

	@Override
	public String getTitle() {

		return TITLE;
	}

	/**
	 * @param username
	 * @param password
	 * @return
	 */
	public AdminConsolePage openAdminConsole(String username, String password) {
		admConsoleLink.click();
		if (!getSession().isLoggedIn()) {
			logger.info("try to login with userName:" + username + " password: " + password);
			long start = System.currentTimeMillis();
			LoginPage loginPage = new LoginPage(getSession());
			loginPage.doLogin(username, password);

			logger.perfomance("user logged in " + username + "  password:" + password, start);
			getSession().setLoggedIn(true);
		}
		TestUtils.getInstance().waitUntilVisible(getSession(), By.className(AdminConsolePage.LEFT_FRAME_CLASSNAME));
		TestUtils.getInstance().waitUntilVisible(getSession(), By.name(AdminConsolePage.MAIN_FRAME_NAME));
		TestUtils.getInstance().saveScreenshot(getSession());
		return new AdminConsolePage(getSession());
	}

	/**
	 * @param siteName
	 * @return
	 */
	public boolean isSiteExistsOnHomePage(String siteName) {
		// "//a[text()='siteName']" <span class="name">gav</span>
		// List<WebElement> elements =
		// ((FindsByLinkText)getSession().getDriver()).findElementsByLinkText(siteName);
		return true;
	}
}
