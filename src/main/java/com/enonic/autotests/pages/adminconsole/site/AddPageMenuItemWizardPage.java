package com.enonic.autotests.pages.adminconsole.site;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.model.site.PageMenuItem;
import com.enonic.autotests.model.site.PageMenuItem.PageMenuItemPortlet;
import com.enonic.autotests.model.site.PageMenuItem.Region;
import com.enonic.autotests.pages.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.services.PageNavigator;

/**
 * Page Object for AddPageMenuItemWizard.
 * 
 */
public class AddPageMenuItemWizardPage extends AbstractMenuItemWizardPage<PageMenuItem>
{

	@FindBy(xpath = "//span[@class='tab']/a[text()='Page configuration']")
	private WebElement pageConfTab;

	/**
	 * The constructor
	 * 
	 * @param session
	 */
	public AddPageMenuItemWizardPage( TestSession session )
	{
		super(session);

	}

	@Override
	public void doTypeDataAndSave(PageMenuItem pageItem)
	{
		String dispalayName = pageItem.getDisplayName();
		if (dispalayName == null)
		{
			throw new IllegalArgumentException("display name should not be null !");
		}
		displaynameInput.sendKeys(dispalayName);
		String menuname = pageItem.getMenuName();
		if (menuname != null)
		{
			menunameInput.sendKeys(menuname);
		}
		boolean isShow = pageItem.isShowInMenu();
		if ((!showInMenuCheckBox.isSelected() && isShow) || (!isShow && showInMenuCheckBox.isSelected()))
		{
			showInMenuCheckBox.click();
		}
		saveButton.click();

	}

	@Override
	public void doEdit(PageMenuItem pageItem, PageMenuItem pageItemNew)
	{
		if (!pageItem.getDisplayName().equals(pageItemNew.getDisplayName()))
		{
			displaynameInput.sendKeys(pageItemNew.getDisplayName());
		}

		if (!pageItem.getMenuName().equals(pageItemNew.getMenuName()))
		{
			menunameInput.sendKeys(pageItemNew.getMenuName());
		}
		if (!pageItem.getPortlets().equals(pageItemNew.getPortlets()))
		{
			pageConfTab.click();
			List<PageMenuItemPortlet> portlets = pageItemNew.getPortlets();
			for (PageMenuItemPortlet p : portlets)
			{
				if (p.getRegion().equals(Region.CENTER))
				{
					//click by 'Choose' button 
					findElement(By.xpath("//button[@name='btncenter_portlet']")).click();
					//select a portletName in the POPUP WINDOW:
					Set<String> allWindows = getDriver().getWindowHandles();
					if (!allWindows.isEmpty())
					{
						String whandle = getDriver().getWindowHandle();
						for (String windowId : allWindows)
						{
							try
							{
								//switch to POPUP-WINDOW
								if (getDriver().switchTo().window(windowId).getTitle().contains("http://"))
								{
									String portletXpath = String.format(SitePortletsTablePage.PORTLET_TITLE_XPATH,p.getPortletName()); 
									findElement(By.xpath(portletXpath)).click();								
									// POPUP WINDOW CLOSED, need to switch to the main  window
									getDriver().switchTo().window(whandle);
									PageNavigator.switchToFrame( getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME );
									break;
								}
							} catch (NoSuchWindowException e)
							{
								throw new TestFrameworkException("NoSuchWindowException- wrong ID" + e.getLocalizedMessage());
							}
						}
					}


				} else if (p.getRegion().equals(Region.EAST))
				{
					throw new TestFrameworkException("not implemented yet");
					
				} else if (p.getRegion().equals(Region.WEST))
				{
					throw new TestFrameworkException("not implemented yet");
					
				} else if (p.getRegion().equals(Region.NORTH))
				{
					throw new TestFrameworkException("not implemented yet");
				}
			}
		}
		saveButton.click();

	}

}
