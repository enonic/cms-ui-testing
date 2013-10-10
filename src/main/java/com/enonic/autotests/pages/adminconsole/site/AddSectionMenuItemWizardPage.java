package com.enonic.autotests.pages.adminconsole.site;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.model.site.SectionMenuItem;

/**
 * Page object for "Add new section" Menu Item wizard.
 *
 */
public class AddSectionMenuItemWizardPage extends AbstractMenuItemWizardPage<SectionMenuItem>
{
	
	@FindBy(name = "availablect")
	private WebElement avaiableContentTypeNames;
	
	@FindBy(name = "section_ordered")
	private WebElement orderedRadio;
	
	/**
	 *The Constructor
	 */
	public AddSectionMenuItemWizardPage( TestSession session )
	{
		super(session);
		
	}


	/**
	 * This method types test data and press the "Save" button.
	 * 
	 * @param ctype
	 *            {@link ContentType} instance.
	 */
	@Override
	public void doTypeDataAndSave(SectionMenuItem section)
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
		if(section.isOrdered())
		{
			orderedRadio.click();
		}
		List<String> expectedCTNames = section.getAvailableContentTypes();
		List<WebElement> allOptions = avaiableContentTypeNames.findElements(By.tagName("option"));
		for(WebElement opt: allOptions)
		{
			if(expectedCTNames.contains(opt.getText()))
			{
				opt.click();
				findElements(By.xpath("//button[child::img[contains(@src,'move_right')]]")).get(0).click();
			}
		}
		
//		for(String name: expectedCTNames)
//		{
//			TestUtils.getInstance().doubleClickActionByOption(getSession(), allOptions, name);
//		}
		saveButton.click();	
	}


	@Override
	public void doEdit(SectionMenuItem menuItemToUpdate, SectionMenuItem newMenuItem)
	{
		throw new TestFrameworkException("Not implemented yet!");
		
	}


	


}
