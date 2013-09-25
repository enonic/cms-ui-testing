package com.enonic.autotests.pages.v4.adminconsole.site;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.site.MenuItem;
import com.enonic.autotests.model.site.SectionMenuItem;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;

public abstract class AbstractMenuItemWizardPage<T> extends AbstractAdminConsolePage
{
	protected  final String GENERAL_TAB_NAME_XPATH = "//a[text()='General']";
	
	@FindBy(name = "displayname")
	protected WebElement displaynameInput;
	
	@FindBy(name = "menu-name")
	protected WebElement menunameInput;
	
	@FindBy(name = "visibility")
	protected WebElement showInMenuCheckBox;
	
	@FindBy(name = "save")
	protected WebElement saveButton;

	/**
	 * The constructor.
	 * 
	 * @param session
	 */
	public AbstractMenuItemWizardPage( TestSession session )
	{
		super(session);
		
	}
	
	abstract public void doTypeDataAndSave(T menuItem);

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(GENERAL_TAB_NAME_XPATH)));
		
	}
	

}
