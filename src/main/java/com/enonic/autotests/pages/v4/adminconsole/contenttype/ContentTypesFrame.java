package com.enonic.autotests.pages.v4.adminconsole.contenttype;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.ContentTypeException;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.utils.TestUtils;

/**
 * Path to Frame: Admin / Content types
 * 
 * Page Object for 'Content types' frame. Version 4.7
 * 
 * 02.04.2013
 */
public class ContentTypesFrame extends AbstractAdminConsolePage
{

	public static final String CONTENT_TYPES_FRAME_NAME_XPATH = "//a[text()='Content types']";
	public static final String CONTENT_TYPES_TABLE_NAME_XPATH = "//td[contains(@class,'browsetablecell'  ) and text()='%s']";
	public static final String CONTENT_TYPES_TABLE_DELETE_BUTTON_XPATH = "//tr/td[text()='%s']/following-sibling:: td[2]//a[@class='button_link'  and descendant::img[@src='images/icon_delete.gif']]";

	private final String CONTENT_TABLE_XPATH = "//table[@class='browsetable']";
	
	@FindBy(xpath = "//button[text()='New']")
	private WebElement buttonNew;

	/**
	 * The Constructor
	 * 
	 * @param session
	 *            {@link TestSession} instance
	 */
	public ContentTypesFrame( TestSession session )
	{
		super(session);

	}

	/**
	 * Finds a content type in the table, clicks by 'Delete' icon, confirms the  deletion and waits until frame will be loaded again.
	 * 
	 * @param contentTypeName
	 */
	public void deleteContentType(String contentTypeName)
	{
		// 1. find content type in the table and delete button for it.
		String nameXpath = String.format(CONTENT_TYPES_TABLE_DELETE_BUTTON_XPATH, contentTypeName);
		List<WebElement> elems = getSession().getDriver().findElements(By.xpath(nameXpath));
		if (elems.size() == 0)
		{
			new ContentTypeException("Error during deleting the contentType:" + contentTypeName + " wrong xpath or content does not exist!");
		}
		//2. click by delete button
		elems.get(0).click();
		
		//3. confirm deleting:
		boolean isAlertPresent = TestUtils.getInstance().alertIsPresent(getSession(),1l);
		if(isAlertPresent){
			  Alert alert = getSession().getDriver().switchTo().alert();		        
		        //Get the Text displayed on Alert:
		        String textOnAlert = alert.getText();
		        getLogger().info("Deleting of the contentr type, alert message:"+textOnAlert);
		        //Click OK button, by calling accept() method of Alert Class:
		        alert.accept();
		}
		waituntilPageLoaded(2l);
	}

	public void createContentType(ContentType ctype)
	{
		ContentTypeWizardPage wizardPage = openContentTypeWizard();
		
		wizardPage.doTypeDataAndSave(ctype);

		TestUtils.getInstance().waitUntilVisible(getSession(), By.xpath(CONTENT_TYPES_FRAME_NAME_XPATH),2l);
		getLogger().info("Content Type with name: " + ctype.getName() + " was created!");

	}

	/**
	 * Clicks by 'New' button and opens wizard.
	 * 
	 * @return
	 */
	private ContentTypeWizardPage openContentTypeWizard()
	{
		buttonNew.click();
		ContentTypeWizardPage wizard = new ContentTypeWizardPage(getSession());
		wizard.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return wizard;
	}

	/**
	 * Verifies that  content present in the table.
	 * 
	 * @param contentTypeName
	 * @return true if contentType is present in the table, otherwise false
	 */
	public boolean verifyIsPresent(String contentTypeName)
	{
		String contentTypeXpath = String.format(CONTENT_TYPES_TABLE_NAME_XPATH, contentTypeName);
		getSession().getDriver().manage().timeouts().implicitlyWait(AppConstants.IMPLICITLY_WAIT, TimeUnit.SECONDS);
		List<WebElement> elements = getSession().getDriver().findElements(By.xpath(contentTypeXpath));
		if (elements.size() == 0)
		{
			getLogger().info("content type with name  " + contentTypeName + "was not found!");
			return false;
		}

		getLogger().info("new Content Type was found in the Table! " + contentTypeName);
		return true;
	}
	
	/**
	 * @param timeout
	 */
	@Override
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CONTENT_TABLE_XPATH)));
		new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CONTENT_TYPES_FRAME_NAME_XPATH)));
		
	}

}
