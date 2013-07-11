package com.enonic.autotests.pages.v4.adminconsole.contenttype;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.ContentTypeException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsoleWizardPage;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.utils.TextTransfer;
import com.enonic.autotests.validation.contenttype.ContentTypeValidateHelper;

/**
 * Wizard page for creating of new 'Content Type'.
 * 
 */
public class ContentTypeWizardPage extends AbstractAdminConsoleWizardPage
{

	private static String GET_CONFIGURATION_SCRIPT = "return document.getElementsByTagName('iframe')[0].contentDocument.body.innerHTML;";

	private static String CLEAR_CONFIGURATION_SCRIPT = "document.getElementsByTagName('iframe')[0].contentDocument.body.innerHTML = '';";

	public static String CUSTOM_CONTENT_HANDLER_NAME = "Custom content";

	public static String CONFIGURATION_TEXTAREA_TAGNAME = "iframe";

	public static String CONTENT_HANDLER_SELCET_NAME = "contenthandlerkey";

	public static final String ERROR_IMAGE_XPATH = "//img[contains(@src,'images/form-error.png')]";
	public static final String ERROR_MESSAGE_XPATH = "//img[contains(@src,'images/form-error.png')]/ancestor::span";

	public static final String CONTENT_TYPES_TABLE_ROWS_XPATH = "//td[@class='browsetablecell']";


	@FindBy(how = How.ID, using = "name")
	private WebElement nameInput;

	@FindBy(how = How.NAME, using = "description")
	private WebElement descriptionTextArea;

	@FindBy(how = How.XPATH, using = "//button[text()='Cancel']")
	private WebElement cancelButton;

	/**
	 * The Constructor
	 * 
	 * @param session
	 */
	public ContentTypeWizardPage( TestSession session )
	{
		super(session);

	}


	/**
	 * This method types test data and press the "Save" button.
	 * 
	 * @param ctype
	 *            {@link ContentType} instance.
	 */
	public void doTypeDataAndSave(ContentType ctype)
	{
		nameInput.sendKeys(ctype.getName());
		if (ctype.getDescription() == null)
		{
			throw new IllegalArgumentException("The Content Type's name should be not null!");
		}
		if (ctype.getDescription() != null)
		{
			getLogger().debug("new 'Content type' creation. Content handler's description: " + ctype.getDescription());
			descriptionTextArea.sendKeys(ctype.getDescription());
		}

		if (ctype.getContentHandler() == null)
		{
			throw new IllegalArgumentException("Content Type's handler name should be not null!");
		}
		String contentHandlerName = ctype.getContentHandler().getName();
		getLogger().debug("new 'Content type' creating. Content handler's name: " + contentHandlerName);
		TestUtils.getInstance().selectByText(getSession(), By.name(CONTENT_HANDLER_SELCET_NAME), contentHandlerName);

		// if content handler equls "Custom content", so need to fill the  configuration area:
		if (ctype.getContentHandler().getName().equals(CUSTOM_CONTENT_HANDLER_NAME))
		{
			
			if (ctype.getConfiguration() != null && !ctype.getConfiguration().isEmpty())
			{
				
				getLogger().debug("new 'Content type' creation. Content handler's configuration: " + contentHandlerName);
				
					clearConfigurationTextArea(getSession().getDriver());
					WebElement confTextArea = getSession().getDriver().findElement(By.tagName(CONFIGURATION_TEXTAREA_TAGNAME));
					confTextArea.sendKeys(ctype.getConfiguration());				
				
			}
		}
		saveButton.click();
		checkAlerts(getSession());
		checkErrorMessages(getSession());

		// boolean isPageStillPresent =
		// TestUtils.getInstance().checkIfDisplayed(By.id(ContentTypeWizardPage.TAB1_ID),
		// getSession().getDriver());
		// if(isPageStillPresent){
		// throw new
		// ContentTypeException("Button 'Save' was pressed but new 'Content Type' was not created! And error message did not appear ");
		// }
		//

	}


	/**
	 * Check error message above the name field.
	 * 
	 * @param session
	 */
	private void checkErrorMessages(TestSession session)
	{
		if (TestUtils.getInstance().waitAndFind(By.xpath(ERROR_IMAGE_XPATH), getSession().getDriver()))
		{
			getLogger().error("Error image and Error message appears during creation the content type:", session);
			TestUtils.getInstance().saveScreenshot(getSession());
			String message = null;
			List<WebElement> elems = getSession().getDriver().findElements(By.xpath(ERROR_MESSAGE_XPATH));
			if(elems.size() > 0)
			{
				message = elems.get(0).getText();
			}
			elems = getSession().getDriver().findElements(By.xpath("//td[@class= 'warning-message']"));
			if(elems.size() > 0)
			{
				message = elems.get(0).getText();
			}
			if(message == null)
			{
				message = "error during saving a content type";
			}
			throw new ContentTypeException(message);
		}
	}

	/**
	 * 
	 * 
	 * @param driver
	 */
	private void clearConfigurationTextArea(WebDriver driver)
	{
		getLogger().debug("try to clear configuration text area, findByTagName:" + CONFIGURATION_TEXTAREA_TAGNAME);
		Object cfgBefore = ((JavascriptExecutor) getSession().getDriver()).executeScript(GET_CONFIGURATION_SCRIPT);
		boolean result = ContentTypeValidateHelper.validateConfigurationContent(cfgBefore, ContentTypeValidateHelper.CONTENTTYPE_TAG,
				ContentTypeValidateHelper.INDEXPARAMETERS_TAG);
		if (!result)
		{
			// TODO: Implement bad validation result.
		}
		getLogger().debug("content before cleaning:" + cfgBefore);
		((JavascriptExecutor) getSession().getDriver()).executeScript(CLEAR_CONFIGURATION_SCRIPT);
		Object cfgAfterClean = ((JavascriptExecutor) getSession().getDriver()).executeScript(GET_CONFIGURATION_SCRIPT);

		if (!cfgAfterClean.toString().equals("<br>"))
		{
			throw new TestFrameworkException("ContentType wizard, configuration 'Text area' was not cleared!");
		}

	}

	/**
	 * Performs Click by "Cancel" button in the New Content Type wizard.
	 */
	public void doCancel()
	{
		cancelButton.click();
		getLogger().debug("ContentTypeWizardPage, 'Cancel' button was pressed");

		// ContentTypesFrame should appear, frame's name should be is
		// " Admin/Content Types":
		TestUtils.getInstance().waitUntilVisible(getSession(), By.xpath(ContentTypesFrame.CONTENT_TYPES_FRAME_NAME_XPATH),AppConstants.PAGELOAD_TIMEOUT);
		getLogger().debug("ContentTypeWizardPage, was canceled.");
	}

	/**
	 * @param name
	 * @return
	 */
	public boolean verifyIsCreated(String name)
	{
		List<WebElement> elements = getSession().getDriver().findElements(By.xpath(CONTENT_TYPES_TABLE_ROWS_XPATH));
		for (WebElement el : elements)
		{
			if (name.equals(el.getText().trim()))
			{
				return true;
			}
		}
		return false;
	}

	public boolean verifyData(ContentType expected)
	{
		boolean result = true;
		String actualHandler = getSession().getDriver().findElement(By.name(CONTENT_HANDLER_SELCET_NAME)).getText().trim();
		// String actualHandler = contentHandlerInput.getText().trim();
		String actualName = nameInput.getText().trim();
		result &= actualHandler.equals(expected.getContentHandler().getName().trim());
		getLogger().debug("expected content handler is: " + expected.getContentHandler().getName().trim() + " actual is :" + actualHandler);
		expected.getName();
		getLogger().debug("expected content name is: " + expected.getName().trim() + " actual is :" + actualName);
		expected.getConfiguration();
		expected.getDescription();
		return result;
	}
	


}
