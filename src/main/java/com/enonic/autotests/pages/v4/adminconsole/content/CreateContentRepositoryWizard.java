package com.enonic.autotests.pages.v4.adminconsole.content;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.Select;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.ContentRepositoryException;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.ContentRepository.TopCategory;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsoleWizardPage;
import com.enonic.autotests.utils.TestUtils;

/**
 * Wizard, which creates new Content Repository.
 * 
 * 08.04.2013
 */
public class CreateContentRepositoryWizard extends AbstractAdminConsoleWizardPage {

	public static final String DEFAULT_LANGUAGE = "English";
	public static String DEFAULT_LANGUAGE_SELCET_NAME = "languagekey";
	public static String TOP_CATEGORY_SELCET_NAME = "categorycontenttypekey";

	@FindBy(how = How.ID, using = "availablect")
	protected WebElement allowedTypesSelect;

	@FindBy(how = How.ID, using = "name")
	protected WebElement nameInput;

	@FindBy(how = How.NAME, using = "languagekey")
	protected WebElement languageSelect;

	@FindBy(how = How.ID, using = "contenttypekey")
	protected WebElement contentTypesSelect;
	
	@FindBy(how = How.ID, using = "categorycontenttypekey")
	protected WebElement contentTypeSelect;
	

	/**
	 * The constructor.
	 * 
	 * @param session
	 */
	public CreateContentRepositoryWizard(TestSession session) {
		super(session);

	}

	/**
	 * Types test-data and press button "Save".
	 * @param cRepository
	 */
	public void doTypeDataAndSave(ContentRepository cRepository) {
		if (cRepository.getName() == null) {
			throw new IllegalArgumentException("The Content Repository name must not be null!");
		}
		getLogger().debug("new 'Content type' creation. The Content Repository's name: " + cRepository.getName());
		nameInput.sendKeys(cRepository.getName());

		//addContentTypes(cRepository.getSelectedTypes());

		String defLanguage = cRepository.getDefaultLanguage();
		if (defLanguage == null || (defLanguage != null && defLanguage.isEmpty())) {
			defLanguage = DEFAULT_LANGUAGE;
		}
		getLogger().debug("new 'Content Repository' creation. The Default language is : " + defLanguage);
		TestUtils.getInstance().selectByText(getSession(), By.name(DEFAULT_LANGUAGE_SELCET_NAME), defLanguage);
		TopCategory topCat = cRepository.getTopCategory();
		if (topCat != null) {
			String contentTypeName = topCat.getContentType().getName();
			if (contentTypeName != null) {
				TestUtils.getInstance().selectByText(getSession(), By.name(TOP_CATEGORY_SELCET_NAME), contentTypeName);
			}

		}

		saveButton.click();
		String alertMessage = getAlertMessage(getSession());
		if (alertMessage != null) {
			// save screenshot and log:
			getLogger().error(alertMessage, getSession());
			throw new ContentRepositoryException("Error during creation of Content Repository: " + alertMessage);
		}

	}

	private void addContentTypes(List<String> typesToAdd) {
		for (String type : typesToAdd) {
			doubleClickActionByOption(type);
		}

	}

	/**
	 * @param optionText
	 */
	private void doubleClickActionByOption(String optionText) {
		List<WebElement> allOptions = allowedTypesSelect.findElements(By.tagName("option"));
		boolean isFound = false;
		for (WebElement option : allOptions) {
			getLogger().debug(String.format("option was found : %s", option.getText()));
			if (option.getText().equals(optionText)) {
				Actions builder = new Actions(getSession().getDriver());
				builder.doubleClick(option).perform();
				isFound = true;
				break;
			}

		}
		if (!isFound) {
			throw new ContentRepositoryException("The " + optionText + " content type was not found among available content types        ");
		}

	}

	private List<String> getSelectedContentTypes() {
		List<WebElement> allOptions = contentTypesSelect.findElements(By.tagName("option"));
		List<String> selectedContentTypes = new ArrayList<String>();
		for (WebElement option : allOptions) {
			selectedContentTypes.add(option.getText());
		}

		return selectedContentTypes;

	}


	/**
	 * @param expected
	 * @return true if expected test-data equals actual data, otherwise false.
	 */
	public boolean verifyData(ContentRepository expected) {
		boolean result = true;
		String actualName = nameInput.getAttribute("value");
		result &= expected.getName().equals(actualName);
		if(!expected.getName().equals(actualName)){
			getLogger().error("expected name is:"+expected.getName()+" but actual is: "+actualName, getSession());
		}else{
			
			getLogger().info("Expected and actual value are equals. Expected name is:"+expected.getName()+" and  actual is: "+actualName);
		}
		String actualLanguage =  new Select(languageSelect).getFirstSelectedOption().getText();
		result &= expected.getDefaultLanguage().equals(actualLanguage);
		
		if(!expected.getDefaultLanguage().equals(actualLanguage)){
			getLogger().error("expected language is:"+expected.getDefaultLanguage()+" but actual is: "+actualLanguage, getSession());
		}else{
			
			getLogger().info("Expected and actual value are equals. Expected Language is:"+expected.getDefaultLanguage()+" and  actual is: "+actualLanguage);
		}
		List<String> actualSelectedContentTypes = getSelectedContentTypes();
		result &= actualSelectedContentTypes.equals(expected.getSelectedTypes());
		return result;
	}
}
