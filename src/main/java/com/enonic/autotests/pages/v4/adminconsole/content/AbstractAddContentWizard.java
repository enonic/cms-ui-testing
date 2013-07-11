package com.enonic.autotests.pages.v4.adminconsole.content;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;

public class AbstractAddContentWizard extends AbstractAdminConsolePage
{

	public static final String GENERAL_TAB_LINK_XPATH = "//a[text()='Content']";

	// 1 input: draft offline: unsaved draft
	@FindBy(how = How.ID, using = "_comment")
	protected WebElement commentInput;

	// 2. description text area
	@FindBy(how = How.ID, using = "description")
	protected WebElement descriptionTextarea;

	// 3. key words input text
	@FindBy(how = How.ID, using = "keywords")
	protected WebElement keywordsInput;
	// 4. save button
	@FindBy(how = How.ID, using = "saveSplitButtonBottomleftButton")
	protected WebElement saveButton;
	//5. close wizard button
	@FindBy(how = How.ID, using = "closebtn")
	protected WebElement closeButton;

	/**
	 * Constructor
	 * 
	 * @param session
	 */
	public AbstractAddContentWizard( TestSession session )
	{
		super(session);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(GENERAL_TAB_LINK_XPATH)));

	}
}
