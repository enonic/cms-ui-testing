package com.enonic.autotests.pages.v4.adminconsole.contenttype;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
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
public class ContentTypesFrame extends AbstractAdminConsolePage {

	public static final String CONTENT_TYPES_FRAME_NAME_XPATH = "//a[text()='Content types']";
	public static final String CONTENT_TYPES_TABLE_NAME_TD_XPATH = "//td[@class='browsetablecell' or @class='browsetablecell row-last'][1]";
	public static final String CONTENT_TYPES_TABLE_DELETE_TD_XPATH = "";

	@FindBy(xpath = "//button[text()='New']")
	private WebElement buttonNew;

	

	/**
	 * The Constructor
	 * 
	 * @param session
	 *            {@link TestSession} instance
	 */
	public ContentTypesFrame(TestSession session) {
		super(session);

	}



	public void createContentType(ContentType ctype) {
		ContentTypeWizardPage wizardPage = openContentTypeWizard();
		wizardPage.verifyWizardOpened(getSession());
		wizardPage.doTypeDataAndSave(ctype);
		
		TestUtils.getInstance().waitUntilVisible(getSession(), By.xpath(CONTENT_TYPES_FRAME_NAME_XPATH));
		getLogger().info("Content Type with name: " + ctype.getName() + " was created!");

	}

	private ContentTypeWizardPage openContentTypeWizard(){
		buttonNew.click();
		return new ContentTypeWizardPage(getSession());
	}
	
	public boolean verifyIsCreated(String name){
		List<WebElement> elements = getSession().getDriver().findElements(By.xpath(CONTENT_TYPES_TABLE_NAME_TD_XPATH));

		for(WebElement el: elements){
			if(name.equals(el.getText().trim())){
				getLogger().info("new Content Type was found in the Table! "+ el.getText());
				TestUtils.getInstance().saveScreenshot(getSession());
				return true;
			}
		}
		getLogger().info("new Content Type was not found in the Table! "+ name);
		return false;
	}
}
