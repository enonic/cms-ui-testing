package com.enonic.autotests.pages.adminconsole.content;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.utils.TestUtils;

/**
 * Select a repository and show Frame. Title: Content/'Repository Name' <br>
 * "New", "Search","Advanced search", "Edit Content Repository" buttons should
 * be present on this Frame. <br>
 * This Frame contain a table with 'Content'. <br>
 * Page Object for 'Content Repository' frame. Version 4.7.
 * 
 * 09.04.2013
 */
public class ContentRepositoryViewFrame extends AbstractContentTableView
{

	public static final String EDIT_REPOSITORY_BUTTON_XPATH = "//button[text()='Edit content repository']";
	public static String CONTENT_REPOSITORY_FRAME_NAME_XPATH = "//span[@id='titlename' and contains(.,'%s')]";

	@FindBy(xpath = "//button[text()='Remove content repository']")
	private WebElement buttonDelete;

	

	/**
	 * The constructor.
	 * 
	 * @param session {@link TestSession} instance.
	 */
	public ContentRepositoryViewFrame( TestSession session )
	{
		super(session);

	}



	
	/**
	 * @param repositoryName
	 * @return
	 */
	public RepositoriesListFrame doDeleteEmptyRepository()
	{
		buttonDelete.click();
		boolean isAlertPresent = TestUtils.getInstance().alertIsPresent(getSession(), 1l);
		if (isAlertPresent)
		{
			Alert alert = getDriver().switchTo().alert();
			// Get the Text displayed on Alert:
			String textOnAlert = alert.getText();
			getLogger().info("Deleting of the repository, alert message:" + textOnAlert);
			// Click OK button, by calling accept() method of Alert Class:
			alert.accept();
		}
		return new RepositoriesListFrame(getSession());
	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By
				.xpath(EDIT_REPOSITORY_BUTTON_XPATH)));

	}





	

}
