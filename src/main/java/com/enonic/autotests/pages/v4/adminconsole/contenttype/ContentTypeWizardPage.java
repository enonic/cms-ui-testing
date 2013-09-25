package com.enonic.autotests.pages.v4.adminconsole.contenttype;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsoleWizardPage;
import com.enonic.autotests.utils.TestUtils;

/**
 * Wizard page for creating of new 'Content Type'.
 * 
 */
public class ContentTypeWizardPage extends AbstractAdminConsoleWizardPage
{

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
	 * @throws
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

		// if content handler equals "Custom content", so need to fill the configuration area:
		if (ctype.getContentHandler().getName().equals(CUSTOM_CONTENT_HANDLER_NAME))
		{

			if (ctype.getConfiguration() != null && !ctype.getConfiguration().isEmpty())
			{
				setContentTypeConfiguration(ctype.getConfiguration());
			}
		}
		saveButton.click();
		checkAlerts();
		checkErrorMessages();
	}

	/**
	 * Changes configuration of content type and clicks by "Save" button.
	 * 
	 * @param cfg
	 */
	public void updateConfiguration(String cfg)
	{
		if (cfg != null && !cfg.isEmpty())
		{
			setContentTypeConfiguration(cfg);

		} else
		{
			getLogger().warning("wrong configuration");
		}
		saveButton.click();
		checkAlerts();

	}

	/**
	 * Sets value to the CodeMirror editor.
	 * @param cfg
	 */
	private void setContentTypeConfiguration(String cfg)
	{
		((JavascriptExecutor) getSession().getDriver()).executeScript("window.moduleCodeArea.codeMirror.setValue(arguments[0])", cfg);
	}

	/**
	 * Check error message above the name field.
	 * 
	 */
	private void checkErrorMessages()
	{
		if (TestUtils.getInstance().waitAndFind(By.xpath(ERROR_IMAGE_XPATH), getSession().getDriver()))
		{
			getLogger().error("Error image and Error message appears during creation the content type:", getSession());
			TestUtils.getInstance().saveScreenshot(getSession());
			String message = null;
			List<WebElement> elems = getSession().getDriver().findElements(By.xpath(ERROR_MESSAGE_XPATH));
			if (elems.size() > 0)
			{
				message = elems.get(0).getText();
			}
			elems = getSession().getDriver().findElements(By.xpath("//td[@class= 'warning-message']"));
			if (elems.size() > 0)
			{
				message = elems.get(0).getText();
			}
			if (message == null)
			{
				message = "error during saving a content type";
			}
			throw new SaveOrUpdateException(message);
		}
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
