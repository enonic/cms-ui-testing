package com.enonic.autotests.pages.v4.adminconsole.content;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.utils.TestUtils;

/**
 * Path to Frame: Content
 * 
 * This Frame contains a table with list of Content Repositories.
 * 
 * <br>Page Object for 'Content Repositories ' frame. Version 4.7
 * 
 * 02.04.2013
 */
public class RepositoriesListFrame extends AbstractAdminConsolePage {

	public static final String CONTENT_REPOSITORIES_TABLE_NAME_TD_XPATH = "//td[@class='browsetablecell' or @class='browsetablecell row-last'][1]";

	public static final String CONTENT_FRAME_NAME_XPATH = "//a[text()='Content']";
	
	public static  String CONTENT_REPOSITORY_FRAME_NAME_XPATH = "//span[@id='titlename' and contains(.,'%s')]";

	/**
	 * The Constructor.
	 * 
	 * @param session {@link TestSession} instance.
	 */
	public RepositoriesListFrame(TestSession session) {
		super(session);
	}

	@FindBy(xpath = "//button[text()='New']")
	private WebElement buttonNew;


  

	/**
	 * Opens 'Create Content Repository'-wizard page, types test-data and clicks
	 * a 'Save' button.
	 * 
	 * @param cRepository
	 *            {@link ContentRepository} instance.
	 */
	public void createContentRepository(ContentRepository cRepository) {
		CreateContentRepositoryWizardPage wizardPage = openContentRepositoryWizard();
		wizardPage.verifyWizardOpened(getSession());
		wizardPage.doTypeDataAndSave(cRepository);
		TestUtils.getInstance().waitUntilVisible(getSession(), By.xpath(CONTENT_FRAME_NAME_XPATH));
		getLogger().info("Content Repository with name: " + cRepository.getName() + " was created!");

	}

	/**
	 * @return {@link CreateContentRepositoryWizardPage} instance
	 */
	private CreateContentRepositoryWizardPage openContentRepositoryWizard() {
		buttonNew.click();
		return new CreateContentRepositoryWizardPage(getSession());
	}

	public boolean verifyIsPresentedInTable(String repositoryName) {
		List<WebElement> elements = getSession().getDriver().findElements(By.xpath(CONTENT_REPOSITORIES_TABLE_NAME_TD_XPATH));

		for (WebElement el : elements) {
			if (repositoryName.equals(el.getText().trim())) {
				getLogger().info("new Content Repository was found in the Table! " + el.getText());
				TestUtils.getInstance().saveScreenshot(getSession());
				return true;
			}
		}
		getLogger().info("new Content Repository was not found in the Table! " + repositoryName);
		return false;
	}
}
