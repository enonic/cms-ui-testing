package com.enonic.autotests.pages.v4.adminconsole.site;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.site.PageTemplate;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;

public class SiteTemplatesPage extends  AbstractAdminConsolePage
{
	private final String HEADER_XPATH = "//h1/a[text()='Page template']";

	@FindBy(xpath = "//button[text()='New']")
	private WebElement newButton;
	/**
	 * The constructor.
	 * 
	 * @param session
	 */
	public SiteTemplatesPage( TestSession session )
	{
		super(session);
		
	}
	/**
	 * @param templ
	 */
	public void doCreatePageTemplate(PageTemplate templ )
	{
		newButton.click();
		AddPageTemplateWizard templatewizard = new AddPageTemplateWizard(getSession());
		templatewizard.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		templatewizard.doTypeDataAndSave(templ);
	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(HEADER_XPATH)));		
	}

}
