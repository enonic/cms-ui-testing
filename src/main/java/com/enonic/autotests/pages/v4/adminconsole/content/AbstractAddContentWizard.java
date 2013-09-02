package com.enonic.autotests.pages.v4.adminconsole.content;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.Content;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;

public abstract class AbstractAddContentWizard<T> extends AbstractAdminConsolePage implements IContentWizard<T>
{

	public static final String GENERAL_TAB_LINK_XPATH = "//a[text()='Content']";

	// 1 input: draft offline: unsaved draft
	@FindBy(how = How.ID, using = "_comment")
	protected WebElement commentInput;

	// 2. description text area
	@FindBy(how = How.NAME, using = "description")
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
	
	@FindBy(how = How.ID, using = "name")
	protected WebElement nameInput;
	
	private final String PROPERTIES_TAB_LINK = "//span[contains(@class,'tab')]/a[text()='Properties']";
	private final String SOURCE_TAB_LINK = "//span[contains(@class,'tab')]/a[text()='Source']";
	

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
		new WebDriverWait(getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(GENERAL_TAB_LINK_XPATH)));

	}
	@Override
	public String getContentKey()
	{
		getDriver().findElement(By.xpath(PROPERTIES_TAB_LINK)).click();
		ContentPropertiesTab propTab = new ContentPropertiesTab(getSession());
		propTab.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return propTab.getKeyValue();
		
	}
	@Override
	public Map<ContentIndexes,String> getIndexedValues()
	{
		getDriver().findElement(By.xpath(SOURCE_TAB_LINK)).click();
		ContentSourceTab sourceTab = new ContentSourceTab(getSession());
		sourceTab.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return sourceTab.getIndexedVaues();
	}


	@Override
	public abstract  void typeDataAndSave(Content<T> content);
	
}
