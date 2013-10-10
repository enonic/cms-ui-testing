package com.enonic.autotests.pages.adminconsole.content;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.ContentPublishingException;
import com.enonic.autotests.utils.TestUtils;

/**
 * Page object for second page of 'Publish Content Wizard'
 *
 */
public class ContentPublishingWizardPage2 extends AbstractContentTableView
{
	private final String HREF_TITLE_XPATH = "//a[text()='Step 2 of 3: Publishing']";
	
	private String SECTION_CHECKBOX_XPATH = "//td[contains(.,'%s')]/../td[1]/input[@type='checkbox']";
	private String SECTION_MANULLY_ORDER_CHECKBOX_XPATH = "//td[contains(.,'%s')]/../td[2]/input[contains(@name,'menuitem_manually_order')]";
	
	
	
	@FindBy(name = "next")
	private WebElement nextButton;
	/**
	 * The constructor
	 * 
	 * @param session
	 */
	public ContentPublishingWizardPage2( TestSession session )
	{
		super(session);
		
	}

	/**
	 * Selects a section name and click by the 'next' button.
	 * @param sectionName
	 */
	public void doSelectSectionAndNext(String sectionName,boolean isManuallyOrdered)
	{

		String checkboxXpath = String.format(SECTION_CHECKBOX_XPATH, sectionName);
		boolean result = TestUtils.getInstance().waitAndFind(By.xpath(checkboxXpath), getDriver());
		if(!result)
		{
			throw new ContentPublishingException("Publishing failed, section with name: "+sectionName +" was not found in the second wizars's page!");
		}
		findElement(By.xpath(checkboxXpath)).click();
		System.out.println(findElement(By.xpath(checkboxXpath)).isSelected());
		if(isManuallyOrdered)
		{
			String checkboxManuallyOrderedXpath = String.format(SECTION_MANULLY_ORDER_CHECKBOX_XPATH, sectionName);
			result = TestUtils.getInstance().waitAndFind(By.xpath(checkboxManuallyOrderedXpath), getDriver());
			if(!result)
			{
				throw new ContentPublishingException("Manually Order checkbox was not found on the wizard page!");
			}
			findElement(By.xpath(checkboxManuallyOrderedXpath)).click();
		}
		nextButton.click();
	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(HREF_TITLE_XPATH)));
		
	}
	
	
}
