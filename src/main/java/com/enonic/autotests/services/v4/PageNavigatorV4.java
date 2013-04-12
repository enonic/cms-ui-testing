package com.enonic.autotests.services.v4;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.logger.Logger;
import com.enonic.autotests.model.User;
import com.enonic.autotests.pages.v4.HomePage;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.pages.v4.adminconsole.LeftMenuFrame;
import com.enonic.autotests.pages.v4.adminconsole.content.RepositoriesListFrame;
import com.enonic.autotests.pages.v4.adminconsole.content.ContentRepositoryFrame;
import com.enonic.autotests.pages.v4.adminconsole.content.CreateContentRepositoryWizardPage;
import com.enonic.autotests.utils.TestUtils;

public class PageNavigatorV4 {

	private  static Logger logger = Logger.getLogger();
	
	 public static CreateContentRepositoryWizardPage openRepositoryProperties(TestSession session,String repositoryName){
	    	PageNavigatorV4.navgateToAdminConsole(session);
	    	LeftMenuFrame menu = new LeftMenuFrame(session);
			menu.openContentFrame(session);
			List<WebElement> elements = session.getDriver().findElements(By.xpath(RepositoriesListFrame.CONTENT_REPOSITORIES_TABLE_NAME_TD_XPATH));

			for (WebElement el : elements) {
				if (repositoryName.equals(el.getText().trim())) {
					logger.info("Content Repository was found in the Table! " + el.getText());
					el.click();
				    break;
				}
			}
			 String xpathExpression = String.format(ContentRepositoryFrame.CONTENT_REPOSITORY_FRAME_NAME_XPATH, repositoryName);
			 TestUtils.getInstance().waitUntilVisible(session, By.xpath(xpathExpression ));
			 logger.info("new Content Repository was not found in the Table! " + repositoryName);
			 return new CreateContentRepositoryWizardPage(session);
	    }

	public static ContentRepositoryFrame openContentRepositoryDashboard(TestSession session, String repositoryName) {
		PageNavigatorV4.navgateToAdminConsole(session);
		String xpath = String.format(ContentRepositoryFrame.REPOSITORY_LINK_XPATH, repositoryName);
		ContentRepositoryFrame frame = new ContentRepositoryFrame(session);
		frame.open(xpath);
		logger.info("The Dashboard was opened for repository: " + repositoryName);
		return new ContentRepositoryFrame(session);
	}
	
	public static void navgateToAdminConsole(TestSession testSession) {
		User user = testSession.getCurrentUser();
		// if Admin-console page already loaded, return, otherwise navigate to
		// the console
		if (testSession.getDriver().getTitle().contains(AbstractAdminConsolePage.TITLE)) {
			return;
		}
		if (user != null) {
			openAdminConsole(testSession, user.getName(), user.getPassword());
		} else {
			openAdminConsole(testSession, "admin", "password");
		}
	}
	
	private static void openAdminConsole(TestSession testSession, String userName, String password) {
		HomePage home = new HomePage(testSession);
		home.open();
		home.openAdminConsole(userName, password);
	}
	
}
