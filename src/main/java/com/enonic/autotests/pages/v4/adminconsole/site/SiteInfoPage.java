package com.enonic.autotests.pages.v4.adminconsole.site;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.utils.TestUtils;

/**
 * Page Object for 'Site Info' web-page.
 *
 */
public class SiteInfoPage extends AbstractAdminConsolePage
{

	private final String DELTE_SITE_BUTTON_XPATH = "//button[text()='Delete']"; 
	
	@FindBy(xpath = DELTE_SITE_BUTTON_XPATH)
	private WebElement deleteSiteButton;
	
	private final String CLEAR_CACHE_BUTTON_XPATH = "//input[@title = 'Clear Page Cache']"; 
	
	
	@FindBy(xpath = "//button[@title='Open in In Context Editing']")
	private WebElement openInICEButton;
	/**
	 * The Constructor.
	 * 
	 * @param session
	 */
	public SiteInfoPage( TestSession session )
	{
		super(session);
	}
	/**
	 * Clicks by 'Open in ICE' button.
	 */
	public void doOpenSiteInICE()
	{
		openInICEButton.click();
	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CLEAR_CACHE_BUTTON_XPATH)));
		
	}
	/**
	 * Clicks by 'Delete' button and delete site.
	 */
	public void doDeleteSite()
	{
		if(deleteSiteButton != null)
		{
			deleteSiteButton.click();
		}else{
			throw new TestFrameworkException("Delete button was not found!");
		}
		boolean isAlertPresent = TestUtils.getInstance().alertIsPresent(getSession(), 1l);
		if (isAlertPresent)
		{
			Alert alert = getSession().getDriver().switchTo().alert();
			// Get the Text displayed on Alert:
			String textOnAlert = alert.getText();
			getLogger().info("Deleting of the site, alert message:" + textOnAlert);
			// Click OK button, by calling accept() method of Alert Class:
			alert.accept();
		}
	}

}
