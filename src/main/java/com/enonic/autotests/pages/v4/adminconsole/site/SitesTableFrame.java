package com.enonic.autotests.pages.v4.adminconsole.site;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.model.Site;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;


/**
 * Page object for 'Sites Frame'
 *
 */
public class SitesTableFrame extends AbstractAdminConsolePage
{
	private final String NEW_BUTTON_XPATH = "//a[@class='button_link']/button[@class='button_text']";
	private final String TD_HEADER_XPATH = "//td[contains(@class,'browsetablecolumnheader')]";
	protected String TD_SITE_NAME_XPATH = "//table[@class='browsetable']//td[contains(.,'%s')]";
	
	@FindBy(xpath = NEW_BUTTON_XPATH)
	private WebElement buttonNew;
	
	

	/**
	 * The constructor.
	 * 
	 * @param session
	 */
	public SitesTableFrame( TestSession session )
	{
		super(session);
	}

	/**
	 * Verifies that  Site present in the table.
	 * 
	 * @param siteName
	 * @return true if site is present in the table, otherwise false.
	 */
	public boolean verifyIsPresent(String siteName)
	{
		String contentTypeXpath = String.format(TD_SITE_NAME_XPATH, siteName);
		getSession().getDriver().manage().timeouts().implicitlyWait(AppConstants.IMPLICITLY_WAIT, TimeUnit.SECONDS);
		List<WebElement> elements = getSession().getDriver().findElements(By.xpath(contentTypeXpath));
		if (elements.size() == 0)
		{
			getLogger().info("siteName with name  " + siteName + "was not found!");
			return false;
		}

		getLogger().info(String.format("site with name  %s was found in the Table! ",siteName) );
		return true;
	}
	
	/**
	 * Clicks by site in the table, opens 'Edit site'-wizard and update data.
	 * @param siteName
	 * @param newSite
	 */
	public void doEditSite(String siteName, Site newSite)
	{
		String siteNameXpath = String.format(TD_SITE_NAME_XPATH, siteName);
		List<WebElement> elems = getSession().getDriver().findElements(By.xpath(siteNameXpath));
		if(elems.size() == 0)
		{
			throw new TestFrameworkException("site was not found, name:"+ siteName);
		}
		elems.get(0).click();
		EditSitePage editWizard = new EditSitePage(getSession());
		editWizard.doEditSite(siteName, newSite);
		
	}
	
	/**
	 * Creates new site.
	 * 
	 * @param site
	 */
	public void doAddSite(Site site)
	{
		buttonNew.click(); 
		AddSiteWizardPage wizard = new AddSiteWizardPage(getSession());
		wizard.waituntilPageLoaded(1l);
		wizard.doTypeDataAndSave(site);
	}
	
	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(TD_HEADER_XPATH)));
		
	}

}
