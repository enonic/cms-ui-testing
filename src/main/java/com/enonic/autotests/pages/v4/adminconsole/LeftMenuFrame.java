package com.enonic.autotests.pages.v4.adminconsole;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Page;
import com.enonic.autotests.pages.v4.adminconsole.content.ContentsTableFrame;
import com.enonic.autotests.pages.v4.adminconsole.content.ContentRepositoriesTableFrame;
import com.enonic.autotests.pages.v4.adminconsole.content.RepositoriesListFrame;
import com.enonic.autotests.pages.v4.adminconsole.contenttype.ContentTypesFrame;
import com.enonic.autotests.pages.v4.adminconsole.site.SectionContentsTablePage;
import com.enonic.autotests.pages.v4.adminconsole.site.SiteInfoPage;
import com.enonic.autotests.pages.v4.adminconsole.site.SiteMenuItemsTablePage;
import com.enonic.autotests.pages.v4.adminconsole.site.SitesTableFrame;
import com.enonic.autotests.services.PageNavigatorV4;
import com.enonic.autotests.utils.TestUtils;

/**
 * Page object for Left Menu Frame.
 * 
 */
public class LeftMenuFrame extends Page
{

	private String CONTENTFOLDER_EXPANDER_XPATH = "//a[@id='openBranch-categories-']/img[contains(@src,'%s')]";
	private String SITEFOLDER_EXPANDER_XPATH = "//a[descendant::span[@id='menuitemText' and contains(.,'Sites')]]/../../td/a/img[contains(@src,'%s')]";
	private String SITENAME_LINK_XPATH = "//a[descendant::span[contains(@id,'menuitemText') and contains(.,'%s')]]";
	private String SITE_EXPANDER_IMG_XPATH =  SITENAME_LINK_XPATH + "/../../td/a/img";
		
	private String SITE_MENU_LINK_XPATH = SITENAME_LINK_XPATH + "/../../following-sibling::tr//span[@id='menuitemText' and contains(.,'Menu')]";
	private String SITE_MENU_EXPANDER_IMG_XPATH =  SITE_MENU_LINK_XPATH + "/../../../td/a/img";
	
	/** locator for section menu item */
	private String SITE_MENU_ITEM_XPATH =  SITENAME_LINK_XPATH + "/../../following-sibling::tr//span[contains(@id,'menuitemText') and contains(.,'%s')]";
	

	// 1) select <tr> which contains all categories 2) select the child, which contains the Category Name 3) select first element <td> - this is a '+/-'  link with icon
	private String CATEGORY_MENU_ITEM = "//tr[@id='id-categories']/child::td[2]//table[@class='menuItem' and descendant::tr[1]//span[text()='%s']]";
	private String CATEGORY_EXPANDER_IMG_XPATH = CATEGORY_MENU_ITEM + "//td[1]/a/img";
	private String CATEGORY_XPATH = CATEGORY_MENU_ITEM + "//td[2]/a";

	

	public static String CONTENT_TYPES_LOCATOR_XPATH = "//a[text()='Content types']";
	public static String SITES_LOCATOR_XPATH = "//a/span[@id='menuitemText' and contains(.,'Sites')]";
	public static String CONTENT_LOCATOR_XPATH = "//a[@target='mainFrame' and descendant::img[contains(@src,'images/icon_folder.gif')]]//span[text()='Content']";

	
	/**
	 * @param session
	 */
	public LeftMenuFrame( TestSession session )
	{
		super(session);
	}
	
	
	/**
	 * Clicks by 'Sites' link and open frame with table of sites.
	 * @param testSession
	 * @return
	 */
	public SitesTableFrame openSitesTableFrame(TestSession testSession)
	{
		SitesTableFrame sitesframe = new SitesTableFrame(testSession);
		PageNavigatorV4.clickMenuItemAndSwitchToRightFrame(testSession, SITES_LOCATOR_XPATH);
		sitesframe.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return sitesframe;

	}
	
	
	/**
	 * @param testSession
	 * @return
	 */
	public ContentTypesFrame openContentTypesFrame(TestSession testSession)
	{
		ContentTypesFrame frame = new ContentTypesFrame(testSession);
		PageNavigatorV4.clickMenuItemAndSwitchToRightFrame(testSession, CONTENT_TYPES_LOCATOR_XPATH);
		frame.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return frame;

	}

	/**
	 * @return
	 */
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
	public ContentRepositoriesTableFrame openRepositoryViewFrame(String repoName)
	{
		PageNavigatorV4.switchToFrame(getSession(), AbstractAdminConsolePage.LEFT_FRAME_NAME);
		// 1. expand a 'Content' folder
		expandContentFolder();

		String xpathString = String.format("//tr[@id='id-categories']/child::td[2]//table[@class='menuItem']//tr[1]//span[text()='%s']", repoName);
		// 2. Try to Find Repository by Name and click:
		TestUtils.getInstance().clickByLocator(By.xpath(xpathString), getDriver());

		PageNavigatorV4.switchToFrame(getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME);
		ContentRepositoriesTableFrame view = new ContentRepositoriesTableFrame(getSession());
		view.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		String repoNameXpath = String.format("//h1/a[text()='%s']", repoName);
		List<WebElement> elems = findElements(By.xpath(repoNameXpath));
		if (elems.size() == 0)
		{
			throw new TestFrameworkException("The name of repository should be present! Name:" + repoName);
		}
		return view;

	}
	/**
	 * Expands 'Sites' folder in Admin Console and click by site-name and opens Info page.
	 * 
	 * @param siteName site for opening.
	 * @return info-page for site, which contains buttons 'Delete', 'Edit', 'Open in ICE' .....
	 */
	public SiteInfoPage openSiteInfoPage(String siteName)
	{
		PageNavigatorV4.switchToFrame(getSession(), AbstractAdminConsolePage.LEFT_FRAME_NAME);
		//1. expand 'Sites' folder
		expandSitesFolder();
		//2.click by site
		String siteXpath = String.format(SITENAME_LINK_XPATH, siteName);
		boolean isSitePresent = TestUtils.getInstance().waitAndFind(By.xpath(siteXpath), getDriver());
		if(!isSitePresent)
		{
			throw new TestFrameworkException("Site with name: "+ siteName+ " was not found!");
		}
		findElement(By.xpath(siteXpath)).click();
        //3. switch to 'Right Frame'
		PageNavigatorV4.switchToFrame(getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME);
		SiteInfoPage info = new SiteInfoPage(getSession());
		info.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return info;
	}
	/**
	 * @param siteName
	 * @return
	 */
	public SiteMenuItemsTablePage openSiteMenuItems(String siteName)
	{
		PageNavigatorV4.switchToFrame(getSession(), AbstractAdminConsolePage.LEFT_FRAME_NAME);
		//1. expand 'Sites' folder
		expandSitesFolder();
		//2. expand site folder
		String siteExpanderXpath = String.format(SITE_EXPANDER_IMG_XPATH, siteName);
		//expandFolder(siteExpanderXpath);
		TestUtils.getInstance().expandFolder(getSession(), siteExpanderXpath);
		//3. click by 'Menu' link, located under the Site.
		String menuXpath = String.format(SITE_MENU_LINK_XPATH, siteName);
		boolean isPresentMenuLink = TestUtils.getInstance().waitAndFind(By.xpath(menuXpath), getDriver());
		if(!isPresentMenuLink)
		{
			throw new TestFrameworkException("Menu link for Site "+siteName +" was not found!, probably wrong xpath! ");
		}
		//4.click by Menu:
		findElement(By.xpath(menuXpath)).click();
		PageNavigatorV4.switchToFrame(getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME);
		SiteMenuItemsTablePage siteMenuItems = new SiteMenuItemsTablePage(getSession());
		return siteMenuItems;
	}
	
	/**
	 * @param siteName
	 * @param sectionName
	 * @return
	 */
	public SectionContentsTablePage openSiteSection(String siteName,String sectionName)
	{
		PageNavigatorV4.switchToFrame(getSession(), AbstractAdminConsolePage.LEFT_FRAME_NAME);
		//1. expand 'Sites' folder
		expandSitesFolder();
		//2. expand site folder
		String siteExpanderXpath = String.format(SITE_EXPANDER_IMG_XPATH, siteName);
		TestUtils.getInstance().expandFolder(getSession(), siteExpanderXpath);
		//3. expand Menu in the site:
		String siteMenuExpanderXpath = String.format(SITE_MENU_EXPANDER_IMG_XPATH, siteName);
		TestUtils.getInstance().expandFolder(getSession(), siteMenuExpanderXpath);
		
		String siteMenuItem = String.format(SITE_MENU_ITEM_XPATH, siteName, sectionName);
		boolean isMenuItemPresent = TestUtils.getInstance().waitAndFind(By.xpath(siteMenuItem), getDriver());
		if(!isMenuItemPresent)
		{
			throw new TestFrameworkException("Menu Item does not exist or wrong xpath");
		}
		findElement(By.xpath(siteMenuItem)).click();
		
		PageNavigatorV4.switchToFrame(getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME);
		SectionContentsTablePage contentTablePage = new SectionContentsTablePage(getSession());
		contentTablePage.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		contentTablePage.switchToViewContent();
		return contentTablePage;
	}

	/**
	 * Expands the "Content" folder in the 'Left Menu', expands
	 * Repository-folder, select a Category and opens a 'Category-View'
	 * 
	 * @param session
	 * @param names
	 * @return
	 */
	public ContentsTableFrame openCategoryViewFrame(String... names)
	{
		if(names.length == 0)
		{
			throw new TestFrameworkException("repository name or category name shhould be specified! ");
		}
		PageNavigatorV4.switchToFrame(getSession(), AbstractAdminConsolePage.LEFT_FRAME_NAME);
		// 1. check out if Expanded or Rolled up the 'Content' folder: try to  find '+' icon near the 'Content' link in the left-menu:
		expandContentFolder();

		// 2. find required category(repository) for add content: find Repository and expand it.
		for (int i = 0; i < names.length - 1; i++)
		{
			String catXpath = String.format(CATEGORY_EXPANDER_IMG_XPATH, names[i]);
			//expandFolder(catXpath);
			TestUtils.getInstance().expandFolder(getSession(),catXpath);
		}

		// 3. click by category and open category-view, where 'New/add' button should present.
		String categoryName = names[names.length - 1];
		String categoryXpath = String.format(CATEGORY_XPATH, categoryName);
		boolean isCategoryPresent = TestUtils.getInstance().waitAndFind(By.xpath(categoryXpath), getDriver());
		if (!isCategoryPresent)
		{
			throw new TestFrameworkException("category with name" + categoryName);
		}
		
		//4.click by categoryName:
		findElement(By.xpath(categoryXpath)).click();
		
		//5. switch to the main frame
		PageNavigatorV4.switchToFrame(getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME);
		ContentsTableFrame view = new ContentsTableFrame(getSession());
		view.waituntilPageLoaded(2L);
		return view;
	}
	
	/**
	 * @param categoryToSearch
	 * @param parentNames
	 * @return
	 */
	public WebElement findCategoryInContentFolder(String categoryToSearch, String... parentNames)
	{
		if(parentNames.length == 0)
		{
			throw new TestFrameworkException("repository name or category name shhould be specified! ");
		}
		// 1. check out if Expanded or Rolled up the 'Content' folder: try to  find '+' icon near the 'Content' link in the left-menu:
		expandContentFolder();

		// 2. find required category(repository) for add content: find Repository and expand it.
		for (int i = 0; i < parentNames.length; i++)
		{
			String categoryXpath = String.format(CATEGORY_EXPANDER_IMG_XPATH, parentNames[i]);
			TestUtils.getInstance().expandFolder(getSession(),categoryXpath);
		}

		// 3. click by category and open category-view, where 'New/add' button should present.		
		String categoryXpath = String.format(CATEGORY_XPATH, categoryToSearch);
		boolean isCategoryPresent = TestUtils.getInstance().waitAndFind(By.xpath(categoryXpath), getDriver());
		if (!isCategoryPresent)
		{
			return null;
		}else{
			return findElement(By.xpath(categoryXpath));
		}
		

	}


	/**
	 * Clicks by 'expand' icon and expands the 'Content' folder in the 'Left Menu' frame
	 */
	private void expandContentFolder()
	{
		String expanderPlusXpath = String.format(CONTENTFOLDER_EXPANDER_XPATH, AppConstants.PLUS_ICON_PNG);
		boolean isRolledUp = TestUtils.getInstance().waitAndFind(By.xpath(expanderPlusXpath), getDriver());
		if (isRolledUp)
		{
			// 1. Expand the 'Content' folder:
			TestUtils.getInstance().clickByLocator(By.xpath(expanderPlusXpath), getDriver());
		}
	}
	
	/**
	 * Clicks by 'expand' icon and expands the 'Sites' folder in the 'Left Menu' frame
	 */
	private void expandSitesFolder()
	{
		String expanderPlusXpath = String.format(SITEFOLDER_EXPANDER_XPATH, AppConstants.PLUS_ICON_PNG);
		boolean isRolledUp = TestUtils.getInstance().waitAndFind(By.xpath(expanderPlusXpath), getDriver());
		if (isRolledUp)
		{
			// 1. Expand the 'Content' folder:
			TestUtils.getInstance().clickByLocator(By.xpath(expanderPlusXpath), getDriver());
		}
	}

}
