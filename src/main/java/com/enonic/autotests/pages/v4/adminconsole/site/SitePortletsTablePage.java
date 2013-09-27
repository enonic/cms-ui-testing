package com.enonic.autotests.pages.v4.adminconsole.site;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.site.Portlet;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;

/**
 * Page Object for web-page, that contains table of portlets
 * 
 * Sites/'site name'/Portlets
 *
 */
public class SitePortletsTablePage extends  AbstractAdminConsolePage
{

	public static String PORTLET_TITLE_XPATH = "//td[contains(@class,'browsetablecell') and text()='%s']";
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
	/**
	 * Creates new Portlet.
	 * 
	 * @param portlet
	 */
	public void doCreatePortlet(Portlet portlet )
	{
		newButton.click();
		AddPortletWizardPage portletWizard = new AddPortletWizardPage(getSession());
		portletWizard.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		portletWizard.doTypeDataAndSave(portlet);
	}
	/**
	 * Verifies: is present portlet in the table.
	 * 
	 * @param portletName
	 * @return
	 */
	public boolean verifyIsPresent(String portletName)
	{
		String menuItemXpath = String.format(PORTLET_TITLE_XPATH, portletName);
		List<WebElement> elems = getSession().getDriver().findElements(By.xpath(menuItemXpath));
		if (elems.size() == 0)
		{
			getLogger().info("portlet  " + portletName + "was not found!");
			return false;
		}

		getLogger().info("portlet was found in the Table! " + portletName);
		return true;
	}


	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(PAGE_HEADER_XPATH)));
		
	}

}
