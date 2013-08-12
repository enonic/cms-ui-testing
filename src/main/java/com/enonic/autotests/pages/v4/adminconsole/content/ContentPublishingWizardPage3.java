package com.enonic.autotests.pages.v4.adminconsole.content;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;

/**
 * Page object for final page of 'Publish Content Wizard'
 *
 */
public class ContentPublishingWizardPage3 extends AbstractContentTableView
{


	private final String HREF_TITLE_XPATH = "//a[text()='Step 3 of 3: Confirm publishing']";
	
	@FindBy(name = "finish")
	private WebElement finishButton;
	
	/**
	 * The constructor.
	 * 
	 * @param session
	 */
	public ContentPublishingWizardPage3( TestSession session )
	{
		super(session);
		
	}
	
	public void doFinish()
	{
		finishButton.click();
	}
	
	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(HREF_TITLE_XPATH)));
		
	}
}
