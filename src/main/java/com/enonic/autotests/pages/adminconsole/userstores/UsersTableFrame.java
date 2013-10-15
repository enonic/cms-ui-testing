package com.enonic.autotests.pages.adminconsole.userstores;

import org.openqa.selenium.By;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.model.userstores.User;
import com.enonic.autotests.pages.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.utils.TestUtils;

public class UsersTableFrame extends AbstractAdminConsolePage
{
	private final String TITLE_USERS_FRAME_XPATH = "//h1/a[text()='Users']";
	private String USERNAME_XPATH ="//tr[contains(@class,'tablerowpainter')]//td[2][contains(@class,'browsetablecell') and contains(.,'%s')]";

	@FindBy(xpath= "//button[text()='New']")
	protected WebElement newButton;
	
	public UsersTableFrame( TestSession session )
	{
		super(session);
	}
	
	public void doAddUser(User user )
	{
		newButton.click();
		AddUserWizardPage wizard = new AddUserWizardPage(getSession());
		wizard.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		wizard.doTypeDataAndSave(user);
		
	}
	public void doEditUser(String userName, User user)
	{
		String usernameXpath = String.format(USERNAME_XPATH, user.getName());
		boolean isPresent = TestUtils.getInstance().waitAndFind(By.xpath(usernameXpath), getDriver());
		if(!isPresent)
		{
			throw new TestFrameworkException("user not found: "+ user.getName());
		}
		//finds user by name in the table, clicks by it and opens Wizard page
		getDriver().findElement(By.xpath(usernameXpath)).click();
		AddUserWizardPage wizard = new AddUserWizardPage(getSession());
		// populate new data and clicks by 'Save' button. 
		wizard.doEdit(user);
		//wizard.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		
	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(TITLE_USERS_FRAME_XPATH)));
		
	}

}
