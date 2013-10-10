package com.enonic.autotests.pages.adminconsole.content;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.ContentPublishingException;
import com.enonic.autotests.utils.TestUtils;

/**
 * Page object for first page of 'Publish Content Wizard'
 *
 */
public class ContentPublishingWizardPage1 extends AbstractContentTableView
{
	private final String SITES_FIELDSET = "//fieldset[@id='sites']";
	private final String HREF_TITLE_XPATH = "//a[text()='Step 1 of 3 : Choose availability and sites']";
	private String SITE_CHECKBOX_XPATH = "//td//label[text()='%s']/input[@type='checkbox']";
	
	@FindBy(name = "next")
	private WebElement nextButton;

	/**
	 * The constructor.
	 * 
	 * @param session
	 */
	public ContentPublishingWizardPage1( TestSession session )
	{
		super(session);
	}
	
	/**
	 * Select a site and press button "Next".
	 * @param sitename
	 * @return second wizard page.
	 */
	public ContentPublishingWizardPage2 doSelectSiteAndNext(String sitename )
	{
		String checkBoxXpath= String.format(SITE_CHECKBOX_XPATH, sitename);
		boolean isPresent = TestUtils.getInstance().waitAndFind(By.xpath(checkBoxXpath), getSession().getDriver());
		if(!isPresent)
		{
			throw new ContentPublishingException("Publishing failed, site with name: "+sitename +" was not found in the first wizars's page!");
		}
		findElement(By.xpath(checkBoxXpath)).click();
		nextButton.click();
		ContentPublishingWizardPage2 secondPage = new ContentPublishingWizardPage2(getSession());
		secondPage.waituntilPageLoaded(2l);
		return secondPage;
	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		findElements(By.xpath(HREF_TITLE_XPATH));
		//new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(HREF_TITLE_XPATH)));
		
	}

}
