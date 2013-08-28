package com.enonic.autotests.pages.v4.adminconsole.content;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.AddContentException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.ContentHandler;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.utils.TestUtils;

/**
 * Base class for frames, that contains table with content: 'Content View'
 *
 */
public abstract class AbstractContentTableView extends AbstractAdminConsolePage
{
	// drop down menu, Content Item:
	public static final String CREATE_CONTENT_MENU_BUTTON_XPATH = "//a[@class='cms-menu-item-icon-create-content' ]";
	// drop down menu, Category Item
	public static final String CREATE_CATEGORY_BUTTON_XPATH = "//a[@class='cms-menu-item-icon-folder']";
	
	@FindBy(xpath = CREATE_CONTENT_MENU_BUTTON_XPATH)
	private WebElement menuItemAddContent;

	@FindBy(how = How.ID, using = "cmdNewMenuButtonbutton")
	protected  WebElement buttonNew;

	protected String CONTENT_NAME_XPATH = "//tr[contains(@class,'tablerowpainter_')]//td[contains(@class,'browsetablecell')]//div[contains(@style,'font-weight: bold') and text()='%s']";
	private String CONTENT_ROW = "//tr[contains(@class,'tablerowpainter') and descendant::div[contains(@style,'font-weight: bold') and text()='%s']]";
	protected String EDIT_CONTENT_LINK = CONTENT_ROW + "//img[@src='images/icon_edit.gif']";
	protected String DELETE_CONTENT_LINK = CONTENT_ROW + "//img[@src='images/icon_delete.gif']";
	protected String PUBLISH_CONTENT_LINK = CONTENT_ROW + "//img[@src='images/icon_content_publish.gif']";
	protected String MOVE_CONTENT_LINK =  CONTENT_ROW + "//img[@src='images/icon_content_move.gif']";

	protected String SELECT_CONTENT_CHECKBOX = CONTENT_ROW + "/td/input[@name='batch_operation']";
	/**
	 * @param session
	 */
	public AbstractContentTableView( TestSession session )
	{
		super(session);
		
	}
	
	/**
	 * @param pathName content name with absolute path name.
	 */
	public <T> IContentWizard<T> openEditContentWizard(Content<T> content)
	{
		String nameXpath = String.format(EDIT_CONTENT_LINK, content.getDisplayName() );
		boolean isEditButtonPresent = TestUtils.getInstance().waitAndFind(By.xpath(nameXpath), getDriver());
		if (!isEditButtonPresent)
		{
			throw new AddContentException("'Edit Content' link was not found");
		}
		findElement(By.xpath(nameXpath)).click();
		return updateOrCreateWizardFactory(content.getContentHandler());
	}
	
	/**
	 * @return
	 */
	public CreateCategoryWizard openAddCategoryWizard()
	{
		// 1. click by "New" button and show 'add content' and 'add category' menu-items:
		buttonNew.click();
		List<WebElement> elems = getDriver().findElements(By.xpath(CREATE_CATEGORY_BUTTON_XPATH));
		if (elems.size() == 0)
		{
			throw new TestFrameworkException("add Category Button was not found!");
		}
		elems.get(0).click();
		CreateCategoryWizard wizard = new CreateCategoryWizard(getSession());
		wizard.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return wizard;
	}
	/**
	 * Opens CreateContentWizardPage page on the Right Frame.
	 * 
	 * @return {@link IContentWizard} instance.
	 */
	public IContentWizard openAddContentWizardPage(ContentRepository cRepository)
	{
		// 1. click by "New" button and show 'add content' and 'add category' menu-items:
		buttonNew.click();
		boolean isAddContentButtonShowed = TestUtils.getInstance().waitAndFind(By.xpath(CREATE_CONTENT_MENU_BUTTON_XPATH), getDriver());
		if (!isAddContentButtonShowed)
		{
			throw new AddContentException("'New Content-'Menu item was not found");
		}
		// 2.click by: '(content-type) Content' and open the "add content-wizard":
		menuItemAddContent.click();
		// 3. verify if wizard opened:
		String xpath = String.format("//a[text()='%s']", cRepository.getName());
		boolean isTitleLoaded = TestUtils.getInstance().waitAndFind(By.xpath(xpath), getDriver());
		if (!isTitleLoaded)
		{
			throw new AddContentException("Create Content Wizard was not opened! Repository: " + cRepository.getName());
		}

		return updateOrCreateWizardFactory(cRepository.getTopCategory().getContentType().getContentHandler());
	}
	public AddImageContentWizard openAddImageContentWizardPage(String repoName )
	{
		// 1. click by "New" button and show 'add content' and 'add category' menu-items:
		buttonNew.click();
		boolean isAddContentButtonShowed = TestUtils.getInstance().waitAndFind(By.xpath(CREATE_CONTENT_MENU_BUTTON_XPATH), getDriver());
		if (!isAddContentButtonShowed)
		{
			throw new AddContentException("'New Content-'Menu item was not found");
		}
		// 2.click by: '(content-type) Content' and open the "add content-wizard":
		menuItemAddContent.click();
		// 3. verify if wizard opened:
		String xpath = String.format("//a[text()='%s']", repoName);
		boolean isTitleLoaded = TestUtils.getInstance().waitAndFind(By.xpath(xpath), getDriver());
		if (!isTitleLoaded)
		{
			throw new AddContentException("Create Content Wizard was not opened! Repository: " + repoName);
		}

		return new AddImageContentWizard(getSession());
	}
	/**
	 * @param displayName
	 * @return
	 */
	public boolean findContentInTableByName(String displayName)
	{
		String contentXpath = String.format(CONTENT_NAME_XPATH, displayName);
		boolean isPresent = TestUtils.getInstance().waitAndFind(By.xpath(contentXpath), getDriver());
		return isPresent;
	}
	/**
	 * @ContentHandler handler
	 * @return
	 */
	public IContentWizard updateOrCreateWizardFactory(ContentHandler handler)
	{
		switch (handler)
		{
		case FILES:
			return new AddFileContentWizard(getSession());

		case IMAGES:
			return new AddImageContentWizard(getSession());

		case CUSTOM_CONTENT:
			//TODO NOT implemented
			return null;
		default:
			return null;
		}
	}


}
