package com.enonic.autotests.pages.adminconsole.userstores;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.Alert;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.model.userstores.User;
import com.enonic.autotests.pages.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.services.PageNavigator;
import com.enonic.autotests.utils.TestUtils;

public class AddUserWizardPage extends  AbstractAdminConsolePage
{
	private final String USER_TAB_XPATH ="//span[@class='tab selected']/a[text()='User']";
	@FindBy(name = "uid_dummy")
	private WebElement nameInput;
	
	@FindBy(name = "password_dummy")
	private WebElement passwordInput;
	
	@FindBy(name = "password2_dummy")
	private WebElement passwordRepeatInput;
	
	@FindBy(name = "email")
	private WebElement emailInput;
	
	@FindBy(xpath = "//input[@type='button' and @value='Save']")
	private WebElement saveButton;
	
	
	public AddUserWizardPage( TestSession session )
	{
		super(session);
	}
	
	public void doTypeDataAndSave(User user)
	{
		nameInput.sendKeys(user.getName());
		passwordInput.sendKeys(user.getPassword());
		passwordRepeatInput.sendKeys(user.getPassword());
		emailInput.sendKeys(user.getEmail());
		saveButton.click();
		checkAlerts();
	}
	/**
	 * Updates user or add new Groups
	 * 
	 * @param user
	 */
	public void doEdit(User user)
	{
		
		if(!user.getEmail().equals(emailInput.getAttribute("value").trim()))
		{
			emailInput.sendKeys(user.getEmail());
		}
		
		
		doAddGroups(user.getGroups());
		//clicks by 'save' button.
		saveButton.click();
		checkAlerts();
		
		
	}
	
	private void  doAddGroups(List<String> groupNames)
	{
		//1. goto 'member of ...' tab 
		getDriver().findElement(By.xpath("//span[contains(@class,'tab')]/a[contains(.,'Member')]")).click();
		
		String addGroupsButtonXpath = "//button[@name= 'butAddAccesRightRow2']";
		boolean isPresent = TestUtils.getInstance().waitAndFind(By.xpath(addGroupsButtonXpath), getDriver());
		if(!isPresent)
		{
			throw new TestFrameworkException("button 'add' groups was not found");
		}
		//2. click by ADD(group) button:
		getDriver().findElement(By.xpath(addGroupsButtonXpath)).click();
		//3. select all groups to add and close popup window
		ChooseGroupPopupWindow popupWin = new ChooseGroupPopupWindow(getSession());
		popupWin.doChoosePrincipals(groupNames);
		
		
		
	}
	
	/**
	 * @param session
	 */
	public void checkAlerts()
	{
		if (!TestUtils.getInstance().alertIsPresent(getSession(), 2l))
		{
			getLogger().debug("alert was not present during creation");
		} else
		{
			Alert alert = getSession().getDriver().switchTo().alert();
			String msg = alert.getText();
			alert.dismiss();
			// alert.accept();
			throw new SaveOrUpdateException("error during creation the CMS-object:" + msg);
		}
	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(USER_TAB_XPATH)));
		
	}

}
