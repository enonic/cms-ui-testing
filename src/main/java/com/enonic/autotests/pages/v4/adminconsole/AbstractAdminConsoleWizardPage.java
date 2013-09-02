package com.enonic.autotests.pages.v4.adminconsole;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.ContentTypeException;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.utils.TestUtils;

public abstract class AbstractAdminConsoleWizardPage extends AbstractAdminConsolePage{
	
	public static final String GENERAL_TAB_LINK_XPATH = "//a[text()='General']";
	
	@FindBy(how = How.NAME, using = "lagre")
	protected  WebElement saveButton;
	
	/**
	 * The constructor.
	 * @param session
	 */
	public AbstractAdminConsoleWizardPage(TestSession session) {
		super(session);
		
	}
	
	

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(GENERAL_TAB_LINK_XPATH)));
		
	}
	
	/**
	 * @param session
	 */
	public String getAlertMessage(TestSession session) {
		if (!TestUtils.getInstance().alertIsPresent(getSession(), 1l)) {
			getLogger().debug("alert was not present during creation");
			return null;
		} else {
			Alert alert = getSession().getDriver().switchTo().alert();
			String msg = alert.getText();
			alert.dismiss();
			return msg;
		}
	}
	
	/**
	 * @param session
	 */
	public void checkAlerts(TestSession session)
	{
		if (!TestUtils.getInstance().alertIsPresent(getSession(), 1l))
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

}
