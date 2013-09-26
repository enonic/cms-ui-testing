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
		ChooseResourcePopupWindow popup = new ChooseResourcePopupWindow(getSession());
		popup.doChooseResource(path, resName);
	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(GENERAL_TAB_LINK_XPATH)));

	}

}
