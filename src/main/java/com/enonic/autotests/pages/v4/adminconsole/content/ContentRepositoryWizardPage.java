package com.enonic.autotests.pages.v4.adminconsole.content;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.ContentRepositoryException;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.ContentRepository.TopCategory;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsoleWizardPage;
import com.enonic.autotests.pages.v4.adminconsole.contenttype.ContentTypeWizardPage;
import com.enonic.autotests.utils.TestUtils;

/**
 * Wizard, which creates new Content Repository.
 * 
 * 08.04.2013
 */
public class ContentRepositoryWizardPage extends AbstractAdminConsoleWizardPage {

	public static final String DEFAULT_LANGUAGE = "English";
	public static String DEFAULT_LANGUAGE_SELCET_NAME = "languagekey";
	public static String TOP_CATEGORY_SELCET_NAME = "categorycontenttypekey";

	@FindBy(how = How.ID, using = "availablect")
	protected WebElement allowedTypesSelect;

	@FindBy(how = How.ID, using = "name")
	protected WebElement nameInput;

	/**
	 * The constructor.
	 * 
	 * @param session
	 */
	public ContentRepositoryWizardPage(TestSession session) {
		super(session);

	}

	public void doTypeDataAndSave(ContentRepository cRepository) {
		if (cRepository.getName() == null) {
			throw new IllegalArgumentException("The Content Repository name must not be null!");
		}
		getLogger().debug("new 'Content type' creation. The Content Repository's name: " + cRepository.getName());
		nameInput.sendKeys(cRepository.getName());

		addContentTypes(cRepository.getAlowedTypes());

		String defLanguage = cRepository.getDefaultLanguage();
		if (defLanguage == null || (defLanguage != null && defLanguage.isEmpty())) {
			defLanguage = DEFAULT_LANGUAGE;
		}
		getLogger().debug("new 'Content Repository' creation. The Default language is : " + defLanguage);
		TestUtils.getInstance().selectByText(getSession(), By.name(DEFAULT_LANGUAGE_SELCET_NAME), defLanguage);
		TopCategory topCat = cRepository.getTopCategory();
		if (topCat != null) {
			String type = topCat.getType();
			if (type != null) {
				TestUtils.getInstance().selectByText(getSession(), By.name(TOP_CATEGORY_SELCET_NAME), type);
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
				builder.doubleClick(option).build().perform();
				isFound = true;
				break;
			}

		}
		if (!isFound) {
			throw new ContentRepositoryException("The " + optionText + " content type was not found among available content types        ");
		}

	}

	/**
	 * @param session
	 */
	@Override
	public void verifyWizardOpened(TestSession session) {
		// TODO add check for presence of another TABS
		TestUtils.getInstance().waitUntilVisible(session, By.id(ContentTypeWizardPage.TAB1_ID));
	}

}
