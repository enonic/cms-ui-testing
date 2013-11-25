package com.enonic.autotests.pages.adminconsole.content;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.ContentRepositoryException;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.ContentRepository.TopCategory;
import com.enonic.autotests.pages.adminconsole.AbstractAdminConsoleWizardPage;
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
}
