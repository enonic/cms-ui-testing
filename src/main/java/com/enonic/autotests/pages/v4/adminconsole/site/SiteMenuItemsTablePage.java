package com.enonic.autotests.pages.v4.adminconsole.site;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.utils.TestUtils;

/**
 *  Path: Sites / site-name / Menu items
 *  <br>Page object for site's menu items. This page contains a table with menu items: title columns, type columns...
 *
 */
public class SiteMenuItemsTablePage extends  AbstractAdminConsolePage
{
	private final String SELECT_TYPE_OF_MENU_ITEMS="//select[@name='gui_type_combo']";
	private final String PAGE_HEADER_XPATH = "//a[text()='Menu items']";
	private final String MENU_ITEM_TITLE_XPATH = "//tr[contains(@class,'tablerowpainter')]//td[2]/span[contains(.,'%s')]";
	@FindBy(name = "cmdNew")
	private WebElement newButton;
	
	//button settings
	
	// button open in ICE

	/**
	 * The Constructor
	 * 
	 * @param session
	 */
	public SiteMenuItemsTablePage( TestSession session )
	{
		super(session);
	}
	/**
	 * Verify: menu item is present in Menu folder.
	 * @param menuItemTitle
	 * @return true if item present, otherwise false.
	 */
	public boolean verifyIsPresent(String menuItemTitle)
	{
		String menuItemXpath = String.format(MENU_ITEM_TITLE_XPATH, menuItemTitle);
		List<WebElement> elems = getSession().getDriver().findElements(By.xpath(menuItemXpath));
		if (elems.size() == 0)
		{
			getLogger().info("Menu item  " + menuItemTitle + "was not found!");
			return false;
		}

		getLogger().info("Menu Item was found in the Table! " + menuItemTitle);
		return true;
	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(PAGE_HEADER_XPATH)));
		
	}
	/**
	 * Click by 'new' button and opens the AddSectionWizardPage. 
	 * @return {@link AddSectionMenuItemWizardPage} instance. 
	 */
	public AddSectionMenuItemWizardPage startAddNewSection()
	{
		//1. click by 'New' button
		newButton.click();
		//2. verify: 'select type' drop down list appeared:
		boolean isPresent = TestUtils.getInstance().waitUntilClickableNoException(getSession(), By.xpath(SELECT_TYPE_OF_MENU_ITEMS), 1l);
		if(!isPresent)
		{
			throw new TestFrameworkException("Select with Names of content types was not found!");
		}
		//3. select 'Section' option from drop down list.
		TestUtils.getInstance().selectByText(getSession(), By.xpath(SELECT_TYPE_OF_MENU_ITEMS), "Section");
		//4. wait until wizard opened.
		AddSectionMenuItemWizardPage sectionWizard = new AddSectionMenuItemWizardPage(getSession());
		sectionWizard.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return sectionWizard;
	}
	public AddPageMenuItemWizardPage startAddNewPage(String templateName)
	{
		//1. click by 'New' button
		newButton.click();
		//2. verify: 'select type' drop down list appeared:
		boolean isPresent = TestUtils.getInstance().waitUntilClickableNoException(getSession(), By.xpath(SELECT_TYPE_OF_MENU_ITEMS), 1l);
		if(!isPresent)
		{
			throw new TestFrameworkException("Select with Names of content types was not found!");
		}
		//3. select 'Section' option from drop down list.
		String typeText = String.format("Page (%s)",templateName);
		TestUtils.getInstance().selectByText(getSession(), By.xpath(SELECT_TYPE_OF_MENU_ITEMS), typeText);//
		//4. wait until wizard opened.
		AddPageMenuItemWizardPage wizard = new AddPageMenuItemWizardPage(getSession());
		wizard.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return wizard;
	}

}
