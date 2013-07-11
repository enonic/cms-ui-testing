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
import com.enonic.autotests.model.ContentHandler;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.utils.TestUtils;

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

	/**
	 * @param session
	 */
	public AbstractContentTableView( TestSession session )
	{
		super(session);
		
	}
	public CreateCategoryWizard openAddCategoryWizard()
	{
		// 1. click by "New" button and show 'add content' and 'add category' menu-items:
		buttonNew.click();
		List<WebElement> elems = getSession().getDriver().findElements(By.xpath(CREATE_CATEGORY_BUTTON_XPATH));
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
	 * @return {@link AddNewContentWizardPage} instance.
	 */
	public IAddContentToRepository openAddContentWizardPage(ContentRepository cRepository)
	{
		// 1. click by "New" button and show 'add content' and 'add category' menu-items:
		buttonNew.click();
		boolean isAddContentButtonShowed = TestUtils.getInstance().waitAndFind(By.xpath(CREATE_CONTENT_MENU_BUTTON_XPATH),
				getSession().getDriver());
		if (!isAddContentButtonShowed)
		{
			throw new AddContentException("'New Content-'Menu item was not found");
		}
		// 2.click by: '(content-type) Content' and open the "add content-wizard":
		menuItemAddContent.click();
		// 3. verify if wizard opened:
		String xpath = String.format("//a[text()='%s']", cRepository.getName());
		boolean isTitleLoaded = TestUtils.getInstance().waitAndFind(By.xpath(xpath), getSession().getDriver());
		if (!isTitleLoaded)
		{
			throw new AddContentException("Create Content Wizard was not opened! Repository: " + cRepository.getName());
		}

		return addContentWizardFactory(cRepository);
	}
	/**
	 * @param cRepository
	 * @return
	 */
	public IAddContentToRepository addContentWizardFactory(ContentRepository cRepository)
	{
		ContentHandler handler = cRepository.getTopCategory().getContentType().getContentHandler();
		switch (handler)
		{
		case FILES:
			return new AddFileContentWizard(getSession());

		case IMAGES:
			return new AddImageContentWizard(getSession());

		case CUSTOM_CONTENT:
			return new AddFileContentWizard(getSession());
		default:
			return null;
		}
	}


}
