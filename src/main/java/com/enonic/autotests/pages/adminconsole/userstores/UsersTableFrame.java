package com.enonic.autotests.pages.adminconsole.userstores;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Alert;
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
	private String USERNAME_XPATH = "//tr[contains(@class,'tablerowpainter')]//td[2][contains(@class,'browsetablecell') and contains(.,'%s')]";
	private String DELETE_USER_ICON_XPATH = "//tr[contains(@class,'tablerowpainter') and descendant::td[contains(.,'%s')]]//img[contains(@src,'icon_delete.gif')]";

	@FindBy(xpath = "//button[text()='New']")
	protected WebElement newButton;

	public UsersTableFrame( TestSession session )
	{
		super(session);
	}

	/**
	 * Adds new user.s
	 * 
	 * @param user
	 */
	public void doAddUser(User user)
	{
		newButton.click();
		AddUserWizardPage wizard = new AddUserWizardPage(getSession());
		wizard.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		wizard.doTypeDataAndSave(user);

	}

	public boolean isUserPresent(String userName)
	{
		String usernameXpath = String.format(USERNAME_XPATH, userName);
		boolean isPresent = TestUtils.getInstance().waitAndFind(By.xpath(usernameXpath), getDriver());
		return isPresent;
	}

	public void doEditUser(String userName, User user)
	{
		String usernameXpath = String.format(USERNAME_XPATH, user.getName());
		boolean isPresent = TestUtils.getInstance().waitAndFind(By.xpath(usernameXpath), getDriver());
		if (!isPresent)
		{
			throw new TestFrameworkException("user not found: " + user.getName());
		}
		// finds user by name in the table, clicks by it and opens Wizard page
		getDriver().findElement(By.xpath(usernameXpath)).click();
		AddUserWizardPage wizard = new AddUserWizardPage(getSession());
		// populate new data and clicks by 'Save' button.
		wizard.doEdit(user);

	}

	/**
	 * Clicks by 'delete' icon and deletes a user.
	 * 
	 * @param userName
	 */
	public void doDeleteUser(String userName)
	{
		String deleteUserIconXpath = String.format(DELETE_USER_ICON_XPATH, userName);
		boolean isPresent = TestUtils.getInstance().waitAndFind(By.xpath(deleteUserIconXpath), getDriver());
		if (!isPresent)
		{
			throw new TestFrameworkException("user not found: " + userName);
		}
		getDriver().findElement(By.xpath(deleteUserIconXpath)).click();
		boolean isAlertPresent = TestUtils.getInstance().alertIsPresent(getSession(), 2l);
		if (isAlertPresent)
		{
			Alert alert = getDriver().switchTo().alert();
			// Get the Text displayed on Alert:
			String textOnAlert = alert.getText();
			getLogger().info("User will be deleted, alert message:" + textOnAlert);
			// Click OK button
			alert.accept();
		}
	}

	/**
	 * Gets all strings(user names) from a table of users.
	 * 
	 * @return
	 */
	public List<String> getUserNames()
	{
		List<String> names = new ArrayList<>();
		List<WebElement> elems = getDriver().findElements(By.xpath("//tr[contains(@class,'tablerowpainter')]//td[2]"));
		for (WebElement elem : elems)
		{

			names.add(elem.getText().trim());
		}
		return names;
	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(TITLE_USERS_FRAME_XPATH)));

	}

}
