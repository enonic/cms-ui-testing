package com.enonic.autotests.services;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.logger.Logger;
import com.enonic.autotests.model.User;
import com.enonic.autotests.pages.v4.HomePage;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.pages.v4.adminconsole.LeftMenuFrame;
import com.enonic.autotests.pages.v4.adminconsole.content.AbstractContentTableView;
import com.enonic.autotests.pages.v4.adminconsole.content.ContentRepositoryViewFrame;
import com.enonic.autotests.pages.v4.adminconsole.content.CreateContentRepositoryWizard;
import com.enonic.autotests.pages.v4.adminconsole.content.RepositoriesListFrame;
import com.enonic.autotests.utils.TestUtils;

public class PageNavigatorV4
{

	private static Logger logger = Logger.getLogger();

	public static CreateContentRepositoryWizard openRepositoryProperties(TestSession session, String repositoryName)
	{
		PageNavigatorV4.navgateToAdminConsole(session);
		LeftMenuFrame menu = new LeftMenuFrame(session);
		menu.openRepositoriesTableFrame();
		String repoxpath = String.format(RepositoriesListFrame.CONTENT_REPOSITORIES_TABLE_NAME_TD_XPATH, repositoryName);
		List<WebElement> elements = session.getDriver().findElements(By.xpath(repoxpath));

		for (WebElement el : elements)
		{
			if (repositoryName.equals(el.getText().trim()))
			{
				logger.info("Content Repository was found in the Table! " + el.getText());
				el.click();
				break;
			}
		}
		String xpathExpression = String.format(ContentRepositoryViewFrame.CONTENT_REPOSITORY_FRAME_NAME_XPATH, repositoryName);
		TestUtils.getInstance().waitUntilVisible(session, By.xpath(xpathExpression), AppConstants.PAGELOAD_TIMEOUT);
		logger.info("new Content Repository was not found in the Table! " + repositoryName);
		return new CreateContentRepositoryWizard(session);
	}

	/**
	 * Navigates to the admin-console and opens repository or category view.
	 * 
	 * @param session
	 * @param repositoryName
	 * @return
	 */
	public static AbstractContentTableView openContentsTableView(TestSession session, String... names)
	{
		PageNavigatorV4.navgateToAdminConsole(session);
		LeftMenuFrame leftMenu = new LeftMenuFrame(session);
		// expand the "Content" folder, click by RepositoryName, open repositoryview and click by 'New-Category' button
		if (names.length == 1)
		{
			String repositoryName = names[0];
			return leftMenu.openRepositoryViewFrame(repositoryName);
		} else
		{
			return leftMenu.openCategoryViewFrame(names);
		}

	}

	public static void navgateToAdminConsole(TestSession testSession)
	{
		User user = testSession.getCurrentUser();
		// if Admin-console page already loaded, return, otherwise navigate to the console
		if (testSession.getDriver().getTitle().contains(AbstractAdminConsolePage.TITLE))
		{
			return;
		}
		if (user != null)
		{
			openAdminConsole(testSession, user.getName(), user.getPassword());
		} else
		{
			openAdminConsole(testSession, "admin", "password");
			testSession.setWindowHandle(testSession.getDriver().getWindowHandle());
		}
	}

	private static void openAdminConsole(TestSession testSession, String userName, String password)
	{
		HomePage home = new HomePage(testSession);
		home.open();
		home.openAdminConsole(userName, password);
	}

	public static void clickMenuItemAndSwitchToRightFrame(TestSession session, String itemXpath)
	{
		String whandle = session.getDriver().getWindowHandle();
		session.getDriver().switchTo().window(whandle);
		session.getDriver().switchTo().frame(AbstractAdminConsolePage.LEFT_FRAME_NAME);

		TestUtils.getInstance().clickByLocator(By.xpath(itemXpath), session.getDriver());

		session.getDriver().switchTo().window(whandle);
		session.getDriver().switchTo().frame(AbstractAdminConsolePage.MAIN_FRAME_NAME);

	}

	public static void switchToFrame(TestSession session, String frameName)
	{
		String whandle = session.getDriver().getWindowHandle();
		session.getDriver().switchTo().window(whandle);
		session.getDriver().switchTo().frame(frameName);

	}

}
