package com.enonic.autotests.pages.v4.adminconsole.site;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.model.site.PageTemplate;
import com.enonic.autotests.model.site.PageType;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsoleWizardPage;
import com.enonic.autotests.services.PageNavigatorV4;
import com.enonic.autotests.utils.TestUtils;

/**
 * Page Object for 'Add Page Template Wizard' page.
 * 
 */
public class AddPageTemplateWizard extends AbstractAdminConsoleWizardPage
{
	private String ROOT_RESOURCE_IN_POPUP_WINDOW_XPATH = "//tr[@id='id-resources']/td/table/tbody/tr/td[2]/a/span[contains(.,'%s')]";
	private String RESOURCE_EXPANDER = "/../../../td[1]//img[contains(@src,'plus.png')]";
	private final String CHOOSE_STYLESHEET_POPUP_WINDOW_TITLE = "Choose resource";
	private final String LEFT_FRAME_NAME_IN_POPUP_WINDOW = "list";
	@FindBy(name = "name")
	private WebElement nameInput;

	@FindBy(xpath = "//span[contains(@class,'tab')]/a[text()='Page configuration']")
	private WebElement pageConfTabLink;

	@FindBy(name = "description")
	private WebElement descriptionTextArea;

	/**
	 * The constructor.
	 * 
	 * @param session
	 */
	public AddPageTemplateWizard( TestSession session )
	{
		super(session);

	}

	/**
	 * Types name, description, select a type and specify path to stylesheet.
	 * 
	 * @param template
	 */
	public void doTypeDataAndSave(PageTemplate template)
	{
		
		if (template.getName() == null)
		{
			throw new IllegalArgumentException("template name should be specified!");
		}
		//1. fill name input
		nameInput.sendKeys(template.getName());
		if (template.getDescription() != null)
		{
			//2. fill description input
			descriptionTextArea.sendKeys(template.getDescription());
		}
		if (template.getType() != null && template.getType().equals(PageType.DEFAULT))
		{
			//2. select a type
			TestUtils.getInstance().selectByText(getSession(), By.xpath("//slect[@name='type']"), template.getType().getValue());
		}
		//3.  Click by 'Page configuration' tab and select a stylesheet:
		pageConfTabLink.click();
		doSelectStylesheet(template.getStylesheet().getPath(), template.getStylesheet().getName());
		saveButton.click();
		checkAlerts();

	}

	/**specifies path to stylesheet.
	 * 
	 * @param path
	 * @param resName
	 */
	private void doSelectStylesheet(String[] path, String resName)
	{
		// try to find a 'Choose' a stylesheet button
		String selectStylesheetButtonXpath = "//button[@type='button' and @name='btnstylesheetkey']";
		boolean isPresent = TestUtils.getInstance().waitAndFind(By.xpath(selectStylesheetButtonXpath), getDriver());
		if (isPresent)
		{
			// click by 'Choose' a stylesheet button:(Browse)
			findElement(By.xpath(selectStylesheetButtonXpath)).click();
		}else
		{
			throw new TestFrameworkException("Button 'Choose' stylesheet was not found on the 'Page configuration' tab!");
		}
		Set<String> allWindows = getSession().getDriver().getWindowHandles();
		if (!allWindows.isEmpty())
		{
			String whandle = getSession().getDriver().getWindowHandle();
			for (String windowId : allWindows)
			{
				try
				{
					//switch to POPUP-WINDOW
					if (getDriver().switchTo().window(windowId).getTitle().contains(CHOOSE_STYLESHEET_POPUP_WINDOW_TITLE))
					{
						//switch to LEFT-FRAME in the POPUP-WINDOW
						PageNavigatorV4.switchToFrame(getSession(), LEFT_FRAME_NAME_IN_POPUP_WINDOW);
						String rootResourceSpanXpath = String.format(ROOT_RESOURCE_IN_POPUP_WINDOW_XPATH, path[0]);
                        //finds a root-Folder with name is 'path[0]'
						String expanderXpath = rootResourceSpanXpath + RESOURCE_EXPANDER;
						if (findElements(By.xpath(expanderXpath)).size() > 0)
						{
							//expands a root-Folder with name is 'path[0]'
							findElements(By.xpath(expanderXpath)).get(0).click();
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

						PageNavigatorV4.switchToFrame(getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME);
						// finds resource in the table: 
						String pageNameXpath = String.format("//td[contains(@title,'%s')]", resName);
						List<WebElement> ee = findElements(By.xpath(pageNameXpath));
						ee.get(0).click();
						// POPUP WINDOW CLOSED, need to switch to the main  window
						getSession().getDriver().switchTo().window(whandle);
						PageNavigatorV4.switchToFrame(getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME);
						break;
					}
				} catch (NoSuchWindowException e)
				{
					throw new TestFrameworkException("NoSuchWindowException- wrong ID" + e.getLocalizedMessage());
				}
			}
		}
	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(GENERAL_TAB_LINK_XPATH)));

	}

}
