package com.enonic.autotests.pages.v4.adminconsole;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.TestUtils;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.pages.Page;
import com.enonic.autotests.pages.v4.adminconsole.wizards.ContentTypeWizardPage;

public class ContentTypesFrame extends Page {

	@FindBy(xpath = "//button[text()='New']")
	private WebElement buttonNew;

	@FindBy(xpath = "//a[text()='Content types']")
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
		getSession().getDriver().switchTo().frame(AdminConsolePage.LEFT_FRAME_NAME);

		By leftFrameContentTypes = By.xpath(LeftMenuFrame.CONTENT_TYPES_LOCATOR_XPATH);
		TestUtils.getInstance().clickByLocator(leftFrameContentTypes, getSession().getDriver());
		getSession().getDriver().switchTo().window(whandle);
		getSession().getDriver().switchTo().frame(AdminConsolePage.MAIN_FRAME_NAME);
		TestUtils.getInstance().waitUntilVisible(getSession(), By.xpath("//a[text()='Content types']"));
		
		//By by1 = By.xpath("//button[text()='New']");
		//getSession().getDriver().findElements(by1);
		
		// driver.switch.frame(driver.findElementByXpath("//iframe[contains(@src,'forsee')]"))
		
		
	}
	
	public void createContentType(ContentType ctype){
		buttonNew.click();
		TestUtils.getInstance().waitUntilVisible(getSession(), By.className(ContentTypeWizardPage.TAB1_CLASS_NAME));
	}

	@Override
	public String getTitle() {
		return AdminConsolePage.TITLE;
	}

}
