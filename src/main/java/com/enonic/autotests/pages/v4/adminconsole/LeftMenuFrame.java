package com.enonic.autotests.pages.v4.adminconsole;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Page;
import com.enonic.autotests.pages.v4.adminconsole.content.RepositoriesListFrame;
import com.enonic.autotests.pages.v4.adminconsole.contenttype.ContentTypesFrame;
import com.enonic.autotests.utils.TestUtils;

public class LeftMenuFrame extends Page{
	
	@FindBy(how = How.ID, using = "openBranch-categories-")
	protected WebElement expandContentLink;
	
	/**
	 * @param session
	 */
	public LeftMenuFrame(TestSession session) {
		super(session);
	}
	public static String CONTENT_TYPES_LOCATOR_XPATH = "//a[text()='Content types']";
	public static String CONTENT_LOCATOR_XPATH = "//span[text()='Content']";
	//

	public ContentTypesFrame openContentTypesFrame(TestSession testSession) {
		ContentTypesFrame frame = new ContentTypesFrame(testSession);
		frame.open(CONTENT_TYPES_LOCATOR_XPATH);
		TestUtils.getInstance().waitUntilVisible(testSession, By.xpath(ContentTypesFrame.CONTENT_TYPES_FRAME_NAME_XPATH));
		return frame;

	}
	public RepositoriesListFrame openContentFrame(TestSession testSession) {
		RepositoriesListFrame frame = new RepositoriesListFrame(testSession);
		frame.open(CONTENT_LOCATOR_XPATH);
		// check for exists, frame name should be is "Content"
		TestUtils.getInstance().waitUntilVisible(testSession, By.xpath(RepositoriesListFrame.CONTENT_FRAME_NAME_XPATH));
		return frame;

	}
	
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}
}
