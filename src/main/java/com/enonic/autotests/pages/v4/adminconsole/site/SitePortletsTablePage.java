package com.enonic.autotests.pages.v4.adminconsole.site;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.site.Portlet;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;

public class SitePortletsTablePage extends  AbstractAdminConsolePage
{

	private final String PAGE_HEADER_XPATH = "//a[text()='Portlets']";
	
	@FindBy(xpath = "//button[text()='New']")
	private WebElement newButton;
	/**
	 * The Constructor
	 * 
	 * @param session
	 */
	public SitePortletsTablePage( TestSession session )
	{
		super(session);
	}
	public void doCreatePortlet(Portlet portlet )
	{
		newButton.click();
		AddPortletWizardPage portletWizard = new AddPortletWizardPage(getSession());
		portletWizard.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		portletWizard.doTypeDataAndSave(portlet);
	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(PAGE_HEADER_XPATH)));
		
	}

}
