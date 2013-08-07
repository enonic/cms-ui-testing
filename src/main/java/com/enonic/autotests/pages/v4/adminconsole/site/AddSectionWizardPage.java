package com.enonic.autotests.pages.v4.adminconsole.site;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.model.Section;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.utils.TestUtils;

/**
 * Page object for "Add new section" Menu Item wizard.
 *
 */
public class AddSectionWizardPage extends AbstractAdminConsolePage
{

	private final String GENERAL_TAB_NAME_XPATH = "//a[text()='General']";
	
	@FindBy(name = "displayname")
	private WebElement displaynameInput;
	
	@FindBy(name = "menu-name")
	private WebElement menunameInput;
	
	@FindBy(name = "visibility")
	private WebElement showInMenuCheckBox;
	
	
	@FindBy(name = "availablect")
	private WebElement avaiableContentTypeNames;
	
	@FindBy(name = "save")
	private WebElement saveButton;
	
	/**
	 *The Constructor
	 */
	public AddSectionWizardPage( TestSession session )
	{
		super(session);
		
	}


	/**
	 * This method types test data and press the "Save" button.
	 * 
	 * @param ctype
	 *            {@link ContentType} instance.
	 */
	public void doTypeDataAndSave(Section section)
	{
		String dispalayName = section.getDisplayName();
		if(dispalayName == null )
		{
			throw new IllegalArgumentException("display name should not be null !");
		}
		displaynameInput.sendKeys(dispalayName);
		String menuname = section.getMenuName();
		if(menuname !=null )
		{
			menunameInput.sendKeys(menuname);
		}
		boolean isShow = section.isShowInMenu();
		if((!showInMenuCheckBox.isSelected() && isShow) || (!isShow && showInMenuCheckBox.isSelected()))
		{
			showInMenuCheckBox.click();
		}
		List<WebElement> allOptions = avaiableContentTypeNames.findElements(By.tagName("option"));
		List<String> expectedCTNames = section.getAvailableContentTypes();
		for(String name: expectedCTNames)
		{
			TestUtils.getInstance().doubleClickActionByOption(getSession(), allOptions, name);
		}
		saveButton.click();
		
		
	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(GENERAL_TAB_NAME_XPATH)));
		
	}

}
