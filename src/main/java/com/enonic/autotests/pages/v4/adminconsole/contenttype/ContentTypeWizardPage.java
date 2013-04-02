package com.enonic.autotests.pages.v4.adminconsole.contenttype;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.TestUtils;
import com.enonic.autotests.exceptions.ContentTypeException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.validation.contenttype.ContentTypeValidateHelper;

/**
 * Wizard page for creating of new 'Content Type'.
 * 
 */
public class ContentTypeWizardPage extends AbstractAdminConsolePage {
	private static String GET_CONFIGURATION_SCRIPT = "return document.getElementsByTagName('iframe')[0].contentDocument.body.innerHTML;";
	private static String CLEAR_CONFIGURATION_SCRIPT = "document.getElementsByTagName('iframe')[0].contentDocument.body.innerHTML = '';";
	public static String CUSTOM_CONTENT_HANDLER_NAME = "Custom content";
	public static String TAB1_ID = "tab-pane-1";
	public static String CONFIGURATION_TEXTAREA_TAGNAME = "iframe";

	public static String CONTENT_HANDLER_SELCET_NAME = "contenthandlerkey";

	public static final String ERROR_IMAGE_XPATH = "//img[contains(@src,'images/form-error.png')]";
	public static final String ERROR_MESSAGE_XPATH = "//img[contains(@src,'images/form-error.png')]/ancestor::span";

	@FindBy(how = How.XPATH, using = ERROR_MESSAGE_XPATH)
	private WebElement errorMessage;

	@FindBy(how = How.NAME, using = "lagre")
	private WebElement saveButton;

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
	public ContentTypeWizardPage(TestSession session) {
		super(session);

	}

	/**
	 * This method types test data and press the "Save" button.
	 * 
	 * @param ctype
	 *            {@link ContentType} instance.
	 */
	public void doTypeDataAndSave(ContentType ctype) {
		nameInput.sendKeys(ctype.getName());
		if (ctype.getDescription() == null) {
			throw new IllegalArgumentException("The Content Type's name should be not null!");
		}
		if (ctype.getDescription() != null) {
			getLogger().debug("new 'Content type' creating. Content handler's description: " + ctype.getDescription());
			descriptionTextArea.sendKeys(ctype.getDescription());
		}

		if (ctype.getContentHandler() == null) {
			throw new IllegalArgumentException("Content Type's handler name should be not null!");
		}
		String contentHandlerName = ctype.getContentHandler();
		getLogger().debug("new 'Content type' creating. Content handler's name: " + contentHandlerName);
		TestUtils.getInstance().selectByText(getSession(), By.name(CONTENT_HANDLER_SELCET_NAME), contentHandlerName);

		if (ctype.getContentHandler().equals(CUSTOM_CONTENT_HANDLER_NAME)) {
			if (ctype.getConfiguration() != null && !ctype.getConfiguration().isEmpty()) {
				getLogger().debug("new 'Content type' creation. Content handler's configuration: " + contentHandlerName);
				editConfigurationTextArea(getSession().getDriver(), ctype.getConfiguration());
			}
		}
		saveButton.click();
		checkAlerts(getSession());
		checkErrorMessages(getSession());

	}

	/**
	 * @param session
	 */
	private void checkAlerts(TestSession session) {
		if (!TestUtils.getInstance().alertIsPresent(getSession())) {
			getLogger().debug("alert was not present during creation the Content type");
		} else {
			getLogger().setDriver(session.getDriver()).error("alert was present, error during creating the Content Type", session);
			Alert alert = getSession().getDriver().switchTo().alert();
			String errorMessage = alert.getText();
			alert.accept();
			throw new ContentTypeException("error during creation the Content Type:" + errorMessage);
		}
	}

	/**
	 * Check error message above the  name field. 
	 * @param session
	 */
	private void checkErrorMessages(TestSession session) {
		if (TestUtils.getInstance().checkIfDisplayed(By.xpath(ERROR_IMAGE_XPATH), getSession().getDriver())) {
			getLogger().error("Error image and Error message appears during creation the content type:", session);
			TestUtils.getInstance().saveScreenshot(getSession());
			String message = errorMessage.getText();
			throw new ContentTypeException(message);
		}
	}

	/**
	 * 
	 * 
	 * @param driver
	 */
	private void clearConfigurationTextArea(WebDriver driver) {
		getLogger().debug("try to clear configuration text area, findByTagName:" + CONFIGURATION_TEXTAREA_TAGNAME);
		Object cfgBefore = ((JavascriptExecutor) getSession().getDriver()).executeScript(GET_CONFIGURATION_SCRIPT);
		boolean result = ContentTypeValidateHelper.validateConfigurationContent(cfgBefore, ContentTypeValidateHelper.CONTENTTYPE_TAG,
				ContentTypeValidateHelper.INDEXPARAMETERS_TAG);
		if (!result) {
			
		}
		getLogger().debug("content before cleaning:" + cfgBefore);
		((JavascriptExecutor) getSession().getDriver()).executeScript(CLEAR_CONFIGURATION_SCRIPT);
		Object cfgAfterClean = ((JavascriptExecutor) getSession().getDriver()).executeScript(GET_CONFIGURATION_SCRIPT);
		System.out.println(cfgAfterClean.toString());
		if(!cfgAfterClean.toString().equals("<br>")){
			throw new TestFrameworkException("ContentType wizard, configuration 'Text area' was not cleared!");
		}

	}

	/**
	 * Fills 'configuration' text area with test data.
	 * 
	 * @param driver
	 * @param configuration
	 */
	private void editConfigurationTextArea(WebDriver driver, String configuration) {
		clearConfigurationTextArea(driver);
		getLogger().debug("try to find configuration text area, findByTagName:" + CONFIGURATION_TEXTAREA_TAGNAME);
		WebElement confTextArea = getSession().getDriver().findElement(By.tagName(CONFIGURATION_TEXTAREA_TAGNAME));
		getLogger().debug("element was found:" + confTextArea.getTagName());
		confTextArea.sendKeys(configuration);
	}

	/**
	 * Performs Click by "Cancel" button in the New Content Type wizard.
	 */
	public void doCancel() {
		cancelButton.click();
		getLogger().debug("ContentTypeWizardPage, 'Cancel' button was pressed");
		
		// ContentTypesFrame should appear, frame's name should be is " Admin/Content Types":
		TestUtils.getInstance().waitUntilVisible(getSession(), By.xpath(ContentTypesFrame.FRAME_NAME_XPATH));
		getLogger().debug("ContentTypeWizardPage, was canceled.");
	}

}
