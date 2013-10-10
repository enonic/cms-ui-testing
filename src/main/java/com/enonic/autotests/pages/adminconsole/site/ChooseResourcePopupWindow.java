package com.enonic.autotests.pages.adminconsole.site;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Page;
import com.enonic.autotests.pages.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.services.PageNavigator;

public class ChooseResourcePopupWindow extends Page
{

	public static final String CHOOSE_RESOURCE_POPUP_WINDOW_TITLE = "Choose resource";
	public static final String LEFT_FRAME_NAME_IN_POPUP_WINDOW = "list";
	public static String ROOT_RESOURCE_IN_POPUP_WINDOW_XPATH = "//tr[@id='id-resources']/td/table/tbody/tr/td[2]/a/span[contains(.,'%s')]";
	public static String RESOURCE_EXPANDER = "/../../../td[1]//img[contains(@src,'plus.png')]";


	/**
	 * @param session
	 */
	public ChooseResourcePopupWindow( TestSession session )
	{
		super(session);
	}

	/**
	 * @param path
	 * @param resName
	 */
	public void doChooseResource(String[] path, String resName)
	{
		Set<String> allWindows = getDriver().getWindowHandles();
		if (!allWindows.isEmpty())
		{
			String whandle = getDriver().getWindowHandle();
			for (String windowId : allWindows)
			{
				try
				{
					//switch to POPUP-WINDOW
					if (getDriver().switchTo().window(windowId).getTitle().contains(CHOOSE_RESOURCE_POPUP_WINDOW_TITLE))
					{
						//switch to LEFT-FRAME in the POPUP-WINDOW
						PageNavigator.switchToFrame( getSession(), LEFT_FRAME_NAME_IN_POPUP_WINDOW );
						String rootResourceSpanXpath = String.format(ROOT_RESOURCE_IN_POPUP_WINDOW_XPATH, path[0]);
                        //finds a root-Folder with name is 'path[0]'
						String expanderXpath = rootResourceSpanXpath + RESOURCE_EXPANDER;
						if (findElements(By.xpath(expanderXpath)).size() > 0)
						{
							//expands a root-Folder with name is 'path[0]'
							getDriver().findElements(By.xpath(expanderXpath)).get(0).click();
						}
						//expand child folders: 
						for (int i = 1; i < path.length; i++)
						{
							String childFolderNameXpath = rootResourceSpanXpath + String.format("/../../../../..//span[contains(.,'%s')]", path[i]);
							String expander = childFolderNameXpath + RESOURCE_EXPANDER;
							if (findElements(By.xpath(expander)).size() > 0)
							{   //expands a folder if '+' is present near the folder
								findElements(By.xpath(expander)).get(0).click();
							} else
							{
                                // else clicks by folder and opens right Frame for  selecting a 'xsl'- page 
								findElements(By.xpath(childFolderNameXpath)).get(0).click();
							}
						}

						PageNavigator.switchToFrame( getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME );
						// finds resource in the table: 
						String pageNameXpath = String.format("//td[contains(@title,'%s')]", resName);
						List<WebElement> res = findElements(By.xpath(pageNameXpath));
						res.get(0).click();
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
	}
	
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath(String.format("//title[contains(.,'%s')]",CHOOSE_RESOURCE_POPUP_WINDOW_TITLE))));
		
	}
}
