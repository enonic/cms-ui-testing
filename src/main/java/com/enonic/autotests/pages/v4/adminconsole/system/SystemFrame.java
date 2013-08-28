package com.enonic.autotests.pages.v4.adminconsole.system;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.utils.TestUtils;

/**
 * Path: Admin / System Page Object for 'System Frame'. Version 4.7
 * 
 */
public class SystemFrame extends AbstractAdminConsolePage
{                   
	private String REMOVE_BUTTON_XPATH = "//button[text()='Remove Deleted Content From Database']";
	
	private String PAGE_TITLE_XPATH = "//h1/a[text()='System']";

	/**
	 * The constructor .
	 * 
	 * @param session
	 */
	public SystemFrame( TestSession session )
	{
		super(session);
	}
	
	/**
	 * Clicks by 'Remove Content' button and confirm deleting.
	 */
	public void doDeleteRemovedContent()
	{
		List<WebElement> elems = findElements(By.xpath(REMOVE_BUTTON_XPATH));
		if(elems.size() == 0)
		{
			throw new TestFrameworkException("Remove button was not found on the System page!");
		}
		elems.get(0).click();
		boolean isAlertPresent = TestUtils.getInstance().alertIsPresent(getSession(), 1l);
		if (isAlertPresent)
		{
			Alert alert = getDriver().switchTo().alert();
			// Get the Text displayed on Alert:
			String textOnAlert = alert.getText();
			getLogger().info("Deleting of contents fromthe Datasource, alert message:" + textOnAlert);
			// Click OK button, by calling accept() method of Alert Class:
			alert.accept();
		}
		
	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(PAGE_TITLE_XPATH)));

	}

}
