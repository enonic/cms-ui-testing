package com.enonic.autotests.pages.v4.adminconsole.content;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.utils.TestUtils;

/**
 * Path to Frame: Content
 * 
 * This Frame contains a table with list of Content Repositories.
 * 
 * <br>
 * Page Object for 'Content Repositories ' frame. Version 4.7
 * 
 * 02.04.2013
 */
public class RepositoriesListFrame extends AbstractAdminConsolePage
{

	public static final String CONTENT_REPOSITORIES_TABLE_NAME_TD_XPATH = "//td[contains(@class,'browsetablecell'  ) and text()='%s']";
	public static final String CONTENT_FRAME_NAME_XPATH = "//a[text()='Content']";

	public static String CONTENT_REPOSITORY_FRAME_NAME_XPATH = "//span[@id='titlename' and contains(.,'%s')]";

	@FindBy(how = How.NAME, using = "searchtext")
	private WebElement searchtext;
	@FindBy(how = How.NAME, using = "search")
	private WebElement searchButton;

	@FindBy(xpath = "//button[text()='New']")
	private WebElement buttonNew;

	private String SPAN_CONTENTS_NAME_XPATH ="//tr[contains(@class,'tablerowpainter_')]//td[contains(@class,'browsetablecell')]//div[contains(@style,'font-weight: bold')]";
	

	/**
	 * The Constructor.
	 * 
	 * @param session {@link TestSession} instance.
	 */
	public RepositoriesListFrame( TestSession session )
	{
		super(session);
	}

	/**
	 * Types a content name to the 'searchtext' and click by the "Search" button.
	 * 
	 * @param contentName
	 * @param repositoryName
	 */
	public List<String> doSearchContent(String contentName)
	{
		searchtext.sendKeys(contentName);
		searchButton.click();
		List<String> contentNames = new ArrayList<>();
		List<WebElement> names = getSession().getDriver().findElements(By.xpath(SPAN_CONTENTS_NAME_XPATH));
		for(WebElement name:names)
		{
			contentNames.add(name.getText());
		}
		
		return contentNames;

	}

	/**
	 * Opens 'Create Content Repository'-wizard page, types test-data and clicks a 'Save' button.
	 * 
	 * @param cRepository {@link ContentRepository} instance.
	 */
	public void createContentRepository(ContentRepository cRepository)
	{
		CreateContentRepositoryWizard wizardPage = openContentRepositoryWizard();
		wizardPage.doTypeDataAndSave(cRepository);
		TestUtils.getInstance().waitUntilVisible(getSession(), By.xpath(CONTENT_FRAME_NAME_XPATH), AppConstants.PAGELOAD_TIMEOUT);
		getLogger().info("Content Repository with name: " + cRepository.getName() + " was created!");

	}

	/**
	 * Opens wizard for creation new 'Content Repository'
	 * 
	 * @return {@link CreateContentRepositoryWizard} instance
	 */
	private CreateContentRepositoryWizard openContentRepositoryWizard()
	{
		buttonNew.click();
		CreateContentRepositoryWizard wizardPage = new CreateContentRepositoryWizard(getSession());
		wizardPage.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return wizardPage;
	}

	/**
	 * Verifies is present repository in the table.
	 * 
	 * @param repositoryName the name of the repository.
	 * @return true if repository is present, otherwise false.
	 */
	public boolean verifyIsRepositoryPresentedInTable(String repositoryName)
	{
		String repoXpath = String.format(CONTENT_REPOSITORIES_TABLE_NAME_TD_XPATH, repositoryName);
		List<WebElement> elements = getSession().getDriver().findElements(By.xpath(repoXpath));
		if (elements.size() == 0)
		{
			getLogger().info("new Content Repository was not found in the Table! " + repositoryName);
			return false;
		}

		return true;
	}

	/**
	 * @param timeout
	 */
	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CONTENT_FRAME_NAME_XPATH)));

	}
}
