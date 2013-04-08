package com.enonic.autotests.pages.v4.adminconsole;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.utils.TestUtils;

public abstract class AbstractAdminConsoleWizardPage extends AbstractAdminConsolePage{

	public static String TAB1_ID = "tab-pane-1";
	
	@FindBy(how = How.NAME, using = "lagre")
	protected  WebElement saveButton;
	
	/**
	 * The constructor.
	 * @param session
	 */
	public AbstractAdminConsoleWizardPage(TestSession session) {
		super(session);
		
	}
	public abstract void verifyWizardOpened(TestSession session);
	
	/**
	 * @param session
	 */
	public String getAlertMessage(TestSession session) {
		if (!TestUtils.getInstance().alertIsPresent(getSession())) {
			getLogger().debug("alert was not present during creation the Content type");
			return null;
		} else {
			Alert alert = getSession().getDriver().switchTo().alert();
			String msg = alert.getText();
			alert.dismiss();
			return msg;
		}
	}

}
