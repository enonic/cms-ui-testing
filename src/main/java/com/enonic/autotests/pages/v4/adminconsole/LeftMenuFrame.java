package com.enonic.autotests.pages.v4.adminconsole;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Page;
import com.enonic.autotests.pages.v4.adminconsole.content.CategoryViewFrame;
import com.enonic.autotests.pages.v4.adminconsole.content.ContentRepositoryViewFrame;
import com.enonic.autotests.pages.v4.adminconsole.content.RepositoriesListFrame;
import com.enonic.autotests.pages.v4.adminconsole.contenttype.ContentTypesFrame;
import com.enonic.autotests.services.PageNavigatorV4;
import com.enonic.autotests.utils.TestUtils;

/**
 * Representation of the Left Menu Frame
 * 
 */
public class LeftMenuFrame extends Page
{

	private String CONTENTFOLDER_EXPANDER_XPATH = "//a[@id='openBranch-categories-']/img[contains(@src,'%s')]";

	// 1) select <tr> which contains all categories 2) select the child, which contains the Category Name 3) select first element <td> - this is a '+/-'  link with icon
	private String CATEGORY_MENU_ITEM = "//tr[@id='id-categories']/child::td[2]//table[@class='menuItem' and descendant::tr[1]//span[text()='%s']]";
	private String CATEGORY_EXPANDER_IMG_XPATH = CATEGORY_MENU_ITEM + "//td[1]/a/img";
	private String CATEGORY_XPATH = CATEGORY_MENU_ITEM + "//td[2]/a";

	

	public static String CONTENT_TYPES_LOCATOR_XPATH = "//a[text()='Content types']";
	public static String CONTENT_LOCATOR_XPATH = "//a[@target='mainFrame' and descendant::img[contains(@src,'images/icon_folder.gif')]]//span[text()='Content']";

	
	/**
	 * @param session
	 */
	public LeftMenuFrame( TestSession session )
	{
		super(session);
	}
	public ContentTypesFrame openContentTypesFrame(TestSession testSession)
	{
		ContentTypesFrame frame = new ContentTypesFrame(testSession);
		PageNavigatorV4.clickMenuItemAndSwitchToRightFrame(testSession, CONTENT_TYPES_LOCATOR_XPATH);
		frame.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return frame;

	}

	public RepositoriesListFrame openRepositoriesTableFrame()
	{
		PageNavigatorV4.clickMenuItemAndSwitchToRightFrame(getSession(), CONTENT_LOCATOR_XPATH);
		RepositoriesListFrame frame = new RepositoriesListFrame(getSession());
		frame.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return frame;

	}

	/**
	 * Expand the "Content" folder, click by RepositoryName, open repository
	 * view and click by 'New-Category' button
	 * 
	 * @param session
	 * @param repoName
	 * @return
	 */
	public ContentRepositoryViewFrame openRepositoryViewFrame(String repoName)
	{

		String whandle = getSession().getDriver().getWindowHandle();
		getSession().getDriver().switchTo().window(whandle);
		getSession().getDriver().switchTo().frame(AbstractAdminConsolePage.LEFT_FRAME_NAME);
		// 1. expand a 'Content' folder
		expandContentFolder();

		String xpathString = String.format("//tr[@id='id-categories']/child::td[2]//table[@class='menuItem']//tr[1]//span[text()='%s']", repoName);
		// 2. Try to Find Repository by Name and click:
		TestUtils.getInstance().clickByLocator(By.xpath(xpathString), getSession().getDriver());

		getSession().getDriver().switchTo().window(whandle);
		getSession().getDriver().switchTo().frame(AbstractAdminConsolePage.MAIN_FRAME_NAME);
		ContentRepositoryViewFrame view = new ContentRepositoryViewFrame(getSession());
		view.waituntilPageLoaded(2L);
		String repoNameXpath = String.format("//h1/a[text()='%s']", repoName);
		List<WebElement> elems = getSession().getDriver().findElements(By.xpath(repoNameXpath));
		if (elems.size() == 0)
		{
			throw new TestFrameworkException("The name of repository should be present! Name:" + repoName);
		}
		return view;

	}

	/**
	 * Expands the "Content" folder in the 'Left Menu', expands
	 * Repository-folder, select a Category and opens a 'Category-View'
	 * 
	 * @param session
	 * @param names
	 * @return
	 */
	public CategoryViewFrame openCategoryViewFrame(String... names)
	{
		String whandle = getSession().getDriver().getWindowHandle();
		getSession().getDriver().switchTo().window(whandle);
		getSession().getDriver().switchTo().frame(AbstractAdminConsolePage.LEFT_FRAME_NAME);
		// 1. check out if Expanded or Rolled up the 'Content' folder: try to  find '+' icon near the 'Content' link in the left-menu:
		expandContentFolder();

		// 2. find required category for add content: find Repository and expand it.
		for (int i = 0; i < names.length - 1; i++)
		{
			expandCategory(names[i]);
		}

		// 3. click by category and open category-view, where 'New/add' button should present.
		String categoryName = names[names.length - 1];
		String categoryXpath = String.format(CATEGORY_XPATH, categoryName);
		boolean isCategoryPresent = TestUtils.getInstance().waitAndFind(By.xpath(categoryXpath), getSession().getDriver());
		if (!isCategoryPresent)
		{
			throw new TestFrameworkException("category with name" + categoryName);
		}

		getSession().getDriver().findElement(By.xpath(categoryXpath)).click();
		getSession().getDriver().switchTo().window(whandle);
		getSession().getDriver().switchTo().frame(AbstractAdminConsolePage.MAIN_FRAME_NAME);

		CategoryViewFrame view = new CategoryViewFrame(getSession());
		view.waituntilPageLoaded(2L);
		return view;
	}

	/**
	 * Click by '+' icon and expands a category or repository.
	 * 
	 * @param repoOrCategoryName
	 */
	private void expandCategory(String repoOrCategoryName)
	{
		String expanderXpath = String.format(CATEGORY_EXPANDER_IMG_XPATH, repoOrCategoryName);
		List<WebElement> elems = getSession().getDriver().findElements(By.xpath(expanderXpath));
		if (elems.size() == 0)
		{
			throw new TestFrameworkException("Expander for folder: " + repoOrCategoryName + " is wrong or this folder has no one category!");
		}
		// check if category has + expander:
		WebElement img = elems.get(0);
		if (img.getAttribute("src").contains(AppConstants.PLUS_ICON_PNG))
		{
			elems.get(0).click();
		} else if (img.getAttribute("src").contains(AppConstants.MINUS_ICON_PNG))
		{
			getLogger().info("the category with name " + repoOrCategoryName + " already expanded");
		}
	}

	private void expandContentFolder()
	{
		String expanderPlusXpath = String.format(CONTENTFOLDER_EXPANDER_XPATH, AppConstants.PLUS_ICON_PNG);
		boolean isRolledUp = TestUtils.getInstance().waitAndFind(By.xpath(expanderPlusXpath), getSession().getDriver());
		if (isRolledUp)
		{
			// 1. Expand the 'Content' folder:
			TestUtils.getInstance().clickByLocator(By.xpath(expanderPlusXpath), getSession().getDriver());
		}
	}

}
