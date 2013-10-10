package com.enonic.autotests.pages.adminconsole.site;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.model.site.Site;
import com.enonic.autotests.pages.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.utils.TestUtils;

/**
 * Page Object for 'Add new Site Wizard'
 * 
 */
public class AddSiteWizardPage extends AbstractAdminConsolePage

{
	protected static final String SELECT_LANGUAGE_XPATH = "//select[@name='languagekey']";

	@FindBy(name = "name")
	private WebElement nameInput;

	@FindBy(name = "statistics")
	private WebElement statisticsInput;

	private final String SAVE_BUTTON_XPATH  = "//button[contains(.,'Save')]";
	@FindBy(xpath = SAVE_BUTTON_XPATH)
	protected WebElement saveButton;

	/**
	 * Constructor
	 * 
	 * @param session
	 */
	public AddSiteWizardPage( TestSession session )
	{
		super(session);
	}

	/**
	 * populates  data and saves new site.
	 * @param site
	 */
	public void doTypeDataAndSave(Site site)
	{
		getLogger().info("creation of new site, site name:" + site.getDispalyName());
		if (site.getDispalyName() != null)
		{
			nameInput.sendKeys(site.getDispalyName());
		}
		boolean isClickable = TestUtils.getInstance().waitUntilClickableNoException(getSession(), By.xpath(SELECT_LANGUAGE_XPATH),AppConstants.IMPLICITLY_WAIT);
		if (!isClickable)
		{
			TestUtils.getInstance().saveScreenshot(getSession());
			Assert.fail("The 'Language' select is not clickable!");
		}
		TestUtils.getInstance().selectByText(getSession(), By.xpath(SELECT_LANGUAGE_XPATH), site.getLanguage());
		if (site.getStatisticsUrl() != null)
		{
			statisticsInput.sendKeys(site.getStatisticsUrl());
		}
		saveButton.click();
		boolean isAlertPresent = TestUtils.getInstance().alertIsPresent(getSession(), 1l);
		if (isAlertPresent)
		{
			String errorMessage = TestUtils.getInstance().getAlertMessage(getSession());
			getLogger().error(errorMessage, getSession());
			throw new SaveOrUpdateException("Error during creation a Site: " + errorMessage);
		}

	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(SAVE_BUTTON_XPATH)));
	}

}
