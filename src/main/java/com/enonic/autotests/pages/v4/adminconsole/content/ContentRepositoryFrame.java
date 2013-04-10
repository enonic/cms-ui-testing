package com.enonic.autotests.pages.v4.adminconsole.content;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.AddContentException;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.utils.TestUtils;

/**
 * Path to Frame: Content/'Repository Name'
 * <br>"New", "Search","Advanced search", "Edit Content Repository" buttons should be present on this Frame.
 * <br>This Frame contain a table with 'Content'.
 * <br>Page Object for 'Content Repository' frame. Version 4.7. 
 * 
 * 09.04.2013
 */
public class ContentRepositoryFrame extends AbstractAdminConsolePage{

	public static final String CREATE_CONTENT_MENU_BUTTON_XPATH = "//a[@class='cms-menu-item-icon-create-content' ]";
	public static String REPOSITORY_LINK_XPATH = "//a[contains(@id,'treeItem-category') and contains(@title,'%s')]";
	public static  String CONTENT_REPOSITORY_FRAME_NAME_XPATH = "//span[@id='titlename' and contains(.,'%s')]";
	
	@FindBy(how = How.ID, using = "cmdNewMenuButtonbutton")
	protected  WebElement buttonNew;
	
	@FindBy(xpath = "//button[text()='Remove content repository']")
	private WebElement buttonDelete;
	
	@FindBy(xpath = CREATE_CONTENT_MENU_BUTTON_XPATH)
	private WebElement buttonAddContent;
	
	
	/**
	 * The constructor.
	 * @param session {@link TestSession} instance.
	 */
	public ContentRepositoryFrame(TestSession session) {
		super(session);
		
	}
	
	/**
	 * Opens CreateContentWizardPage page on the Right Frame.
	 * 
	 * @return {@link CreateContentWizardPage}} instance.
	 */
	public CreateContentWizardPage openAddContentWizardPage(String repName){
		buttonNew.click();
		boolean isAddContentButtonShowed = TestUtils.getInstance().checkIfDisplayed(By.xpath(CREATE_CONTENT_MENU_BUTTON_XPATH), getSession().getDriver());
		if(!isAddContentButtonShowed){
			throw new AddContentException("'New Content-'Menu item was not found");
		}
		buttonAddContent.click();
		String xpath = String.format("//a[text()='%s']", repName);
		boolean isTitleLoaded = TestUtils.getInstance().checkIfDisplayed(By.xpath(xpath), getSession().getDriver());
		if(!isTitleLoaded){
			throw new AddContentException("Create Content Wizard was not opened! Repository: "+repName);
		}
		TestUtils.getInstance().saveScreenshot(getSession());
		return new CreateContentWizardPage(getSession());
	}
	
	@Override
	public void open(String xpathString){
		String whandle = getSession().getDriver().getWindowHandle();
		getSession().getDriver().switchTo().window(whandle);
		getSession().getDriver().switchTo().frame(AbstractAdminConsolePage.LEFT_FRAME_NAME);
		boolean toExpanded = TestUtils.getInstance().checkIfDisplayed(By.xpath("//a[@id='openBranch-categories-']/img[contains(@src,'javascript/images/Lplus.png')]"), getSession().getDriver());
		if(toExpanded){
			//1. Expand 'Content' folder
			String xpathExpandContent =  "//a[@id='openBranch-categories-']";
			TestUtils.getInstance().clickByLocator(By.xpath(xpathExpandContent), getSession().getDriver());
		}
		
		//2. Try to Find Repository by Name and click:
		TestUtils.getInstance().clickByLocator(By.xpath(xpathString), getSession().getDriver());

		getSession().getDriver().switchTo().window(whandle);
		getSession().getDriver().switchTo().frame(AbstractAdminConsolePage.MAIN_FRAME_NAME);
		
	}

}
