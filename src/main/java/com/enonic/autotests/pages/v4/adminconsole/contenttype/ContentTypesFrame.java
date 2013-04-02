package com.enonic.autotests.pages.v4.adminconsole.contenttype;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.TestUtils;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.pages.v4.adminconsole.LeftMenuFrame;

/**
 * Path: Admin / Content types
 *
 * Page Object for 'Content types' frame. Version 4.7
 *
 * 02.04.2013
 */
public class ContentTypesFrame extends AbstractAdminConsolePage {

	public static final String FRAME_NAME_XPATH = "//a[text()='Content types']";

	@FindBy(xpath = "//button[text()='New']")
	private WebElement buttonNew;

	@FindBy(xpath = FRAME_NAME_XPATH)
	private WebElement contentTypesLink;// Admin/Content Types

	/**
	 * The Constructor
	 * 
	 * @param session
	 *            {@link TestSession} instance
	 */
	public ContentTypesFrame(TestSession session) {
		super(session);

	}

	public void open() {
		String whandle = getSession().getDriver().getWindowHandle();
		getSession().getDriver().switchTo().window(whandle);
		getSession().getDriver().switchTo().frame(AbstractAdminConsolePage.LEFT_FRAME_NAME);

		By leftFrameContentTypes = By.xpath(LeftMenuFrame.CONTENT_TYPES_LOCATOR_XPATH);
		TestUtils.getInstance().clickByLocator(leftFrameContentTypes, getSession().getDriver());

		getSession().getDriver().switchTo().window(whandle);
		getSession().getDriver().switchTo().frame(AbstractAdminConsolePage.MAIN_FRAME_NAME);

		// check for exists, frame name should be is " Admin/Content Types"
		TestUtils.getInstance().waitUntilVisible(getSession(), By.xpath(FRAME_NAME_XPATH));

	}

	public void createContentType(ContentType ctype) {
		buttonNew.click();
		TestUtils.getInstance().waitUntilVisible(getSession(), By.id(ContentTypeWizardPage.TAB1_ID));
		ContentTypeWizardPage wizardPage = new ContentTypeWizardPage(getSession());

		wizardPage.doTypeDataAndSave(ctype);
		
		TestUtils.getInstance().waitUntilVisible(getSession(), By.xpath(FRAME_NAME_XPATH));
		getLogger().info("Content Type with name: " + ctype.getName() + "was created!");

	}

	// driver.findElement(By.xpath("//table[@class='basic-table']/tbody/tr[3]/td/input")).click();
}
