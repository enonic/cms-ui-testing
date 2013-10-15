package com.enonic.autotests.pages.adminconsole.userstores;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Page;
import com.enonic.autotests.pages.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.services.PageNavigator;
import com.enonic.autotests.utils.TestUtils;

public class ChooseGroupPopupWindow  extends Page
{

	@FindBy(name = "batchButtonShim")
	private WebElement addButton;
	
	private String GROUP_NAME_XPATH ="//td[contains(@class,'browsetablecell') and child::input[@value='%s']]";
	public ChooseGroupPopupWindow( TestSession session )
	{
		super(session);
	}
	
	public void doChooseGroups(List<String> groupNames)
	{
		Set<String> allWindows = getDriver().getWindowHandles();
		if (!allWindows.isEmpty())
		{
			String whandle = getDriver().getWindowHandle();
			for (String windowId : allWindows)
			{

				try
				{
					if (getDriver().switchTo().window(windowId).getTitle().contains("http://"))
					{
						TestUtils.getInstance().selectByText(getSession(), By.xpath("//select[@name='userstorekey']"), "View: All");
						//1. select all groups to add:
						for(String name: groupNames)
						{
							boolean res = TestUtils.getInstance().waitAndFind(By.xpath(String.format(GROUP_NAME_XPATH,name)), getDriver());
							if(!res)
							{
								throw new TestFrameworkException("The Group with name: "+name + " was not found!");
							}
							getDriver().findElement(By.xpath(String.format(GROUP_NAME_XPATH,name))).click();
						}
						// 2. click by 'Add' button, close popup window
						getDriver().findElement(By.xpath("//button[text()='Add']")).click();
						

						// 3. switch to mainFrame again:
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

}
