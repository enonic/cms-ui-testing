package com.enonic.autotests.services;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.NoSuchWindowException;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.site.MenuItem;
import com.enonic.autotests.model.site.PageMenuItem;
import com.enonic.autotests.model.site.PageTemplate;
import com.enonic.autotests.model.site.Portlet;
import com.enonic.autotests.model.site.SectionMenuItem;
import com.enonic.autotests.model.site.Site;
import com.enonic.autotests.pages.v4.adminconsole.LeftMenuFrame;
import com.enonic.autotests.pages.v4.adminconsole.site.AddPageMenuItemWizardPage;
import com.enonic.autotests.pages.v4.adminconsole.site.AddPortletWizardPage;
import com.enonic.autotests.pages.v4.adminconsole.site.AddSectionMenuItemWizardPage;
import com.enonic.autotests.pages.v4.adminconsole.site.SectionContentsTablePage;
import com.enonic.autotests.pages.v4.adminconsole.site.SiteInfoPage;
import com.enonic.autotests.pages.v4.adminconsole.site.SiteMenuItemsTablePage;
import com.enonic.autotests.pages.v4.adminconsole.site.SitePortletsTablePage;
import com.enonic.autotests.pages.v4.adminconsole.site.SiteTemplatesPage;
import com.enonic.autotests.pages.v4.adminconsole.site.SitesTableFrame;

/**
 *   Site service 
 *
 */
public class SiteService
{
	/**
	 * Deletes all sites in admin.
	 * 
	 * @param testSession
	 */
	public void delteAllSites(TestSession testSession)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame menu = new LeftMenuFrame(testSession);
		SitesTableFrame sitesFrame = menu.openSitesTableFrame(testSession);
		sitesFrame.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		sitesFrame.doDeleteAll();
	}
	
	/**
	 * Returns the names of all sites.
	 * 
	 * @param testSession
	 * @return
	 */
	public List<String> getAllSiteNames(TestSession testSession)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame menu = new LeftMenuFrame(testSession);
		SitesTableFrame sitesFrame = menu.openSitesTableFrame(testSession);
		sitesFrame.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return sitesFrame.getAllSiteNames();
	}

	/**
	 * Clicks by 'New' button, opens site-wizard, populate a data and save new Site
	 * 
	 * @param testSession
	 * @param site new site.
	 * @return
	 */
	public SitesTableFrame createSite(TestSession testSession, Site site)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame menu = new LeftMenuFrame(testSession);
		SitesTableFrame sitesFrame = menu.openSitesTableFrame(testSession);
		sitesFrame.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		sitesFrame.doAddSite(site);		
		sitesFrame.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return sitesFrame;

	}
	
	/**
	 * Add a new section(menu item) to Site
	 * @param testSession
	 * @param siteName
	 * @param section
	 * @return
	 */
	public SiteMenuItemsTablePage addSectionMenuItem(TestSession testSession,String siteName,SectionMenuItem section)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		//1. expand 'Sites'folder, expand site and click by 'Menu' link 
		LeftMenuFrame leftmenu = new LeftMenuFrame(testSession);
	     // SiteMenuItemsTablePage contains all Menu Items for this site.
		SiteMenuItemsTablePage menuItemsPage = leftmenu.openSiteMenuItems(siteName);
		
		
		//2. click by 'New' button and select 'Section', open add section wizard:
		AddSectionMenuItemWizardPage sectionwizard = menuItemsPage.startAddNewSection();
		//3. populate data and save.
		sectionwizard.doTypeDataAndSave(section);
		menuItemsPage.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return menuItemsPage;
	}
	/**
	 * Adds a new 'Page' menu item
	 * 
	 * @param testSession
	 * @param siteName
	 * @param menuItem
	 * @return
	 */
	public SiteMenuItemsTablePage addPageMenuItem(TestSession testSession, PageMenuItem menuItem)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		//1. expand 'Sites'folder, expand site and click by 'Menu' link 
		LeftMenuFrame leftmenu = new LeftMenuFrame(testSession);
	     // SiteMenuItemsTablePage contains all Menu Items for this site.
		SiteMenuItemsTablePage menuItemsPage = leftmenu.openSiteMenuItems(menuItem.getSiteName());
		AddPageMenuItemWizardPage wizard = menuItemsPage.startAddNewPage(menuItem.getPageTemplateName());
		wizard.doTypeDataAndSave(menuItem);
		return menuItemsPage;
	}
	
	/**
	 * Adds a 'Page Template' to the Site
	 * 
	 * @param testSession
	 * @param siteName
	 * @param templ
	 * @return
	 */
	public SiteTemplatesPage addPageTemplate(TestSession testSession,String siteName, PageTemplate templ)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		//1. expand 'Sites'folder, expand site and click by 'Menu' link 
		LeftMenuFrame leftmenu = new LeftMenuFrame(testSession);
	     // SiteMenuItemsTablePage contains all Menu Items for this site.
		SiteTemplatesPage siteTemplPage = leftmenu.openSitePageTemplates(siteName);
		//2. click by 'New' button and select 'Section', open add section wizard:
		siteTemplPage.doCreatePageTemplate(templ);
		
		siteTemplPage.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return siteTemplPage;
	}
	

	/**
	 * Adds a portlet to a Site
	 * 
	 * @param testSession
	 * @param siteName
	 * @param portlet
	 * @return
	 */
	public SitePortletsTablePage addPortlet(TestSession testSession, Portlet portlet)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		//1. expand 'Sites'folder, expand site and click by 'Menu' link 
		LeftMenuFrame leftmenu = new LeftMenuFrame(testSession);
	     // SiteMenuItemsTablePage contains all Menu Items for this site.
		SitePortletsTablePage sitePortletsPage = leftmenu.openSitePortletsTable(portlet.getSiteName());
		//2. click by 'New' button and select 'Section', open add section wizard:
		sitePortletsPage.doCreatePortlet(portlet);
		
		sitePortletsPage.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return sitePortletsPage;
	}
	
	/**
	 * opens for edit a 'menu item' and changes a data.
	 * 
	 * @param testSession
	 * @param itemToUpdate
	 * @param itemNew
	 * @return
	 */
	public SiteMenuItemsTablePage editMenuItem(TestSession testSession, MenuItem itemToUpdate, MenuItem itemNew)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		//1. expand 'Sites'folder, expand site and click by 'Menu' link 
		LeftMenuFrame leftmenu = new LeftMenuFrame(testSession);
	     // SiteMenuItemsTablePage contains all Menu Items for this site.
		SiteMenuItemsTablePage siteMenuItems = leftmenu.openSiteMenuItems(itemToUpdate.getSiteName());
		siteMenuItems.doEditMenuItem(itemToUpdate, itemNew);

		return siteMenuItems;
	}


	/**
	 * Opens a section, clicks by 'Add' button and add new content to a section.
	 * 
	 * @param testSession
	 * @param section
	 * @param content content to add.
	 * @return
	 */
	public SectionContentsTablePage addContentToSection(TestSession testSession,SectionMenuItem section,Content<?> content)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame leftmenu = new LeftMenuFrame(testSession);
		SectionContentsTablePage sectionContentsTable = leftmenu.openSiteSection(section.getSiteName(), section.getDisplayName());
		String[] parents = content.getParentNames();
		sectionContentsTable.doAddContentToSection(parents,content.getDisplayName());
		sectionContentsTable.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return sectionContentsTable;
	}
	
	/**
	 * Check out a publish status of content.
	 * @param testSession
	 * @param section
	 * @param contentName
	 * @return true if content published, otherwise false.
	 */
	public boolean isContentFromSectionPublished(TestSession testSession, SectionMenuItem section,String contentName)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame leftmenu = new LeftMenuFrame(testSession);
		SectionContentsTablePage sectionContentsTable = leftmenu.openSiteSection(section.getSiteName(), section.getDisplayName());
		
		return sectionContentsTable.isContentPublished(contentName);
		
	}
	/**
	 * Gets all names of content from the section.
	 * 
	 * @param testSession
	 * @param section
	 * @return
	 */
	public List<String> getContentNamesFromSection(TestSession testSession, SectionMenuItem section)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame leftmenu = new LeftMenuFrame(testSession);
		SectionContentsTablePage sectionContentsTable = leftmenu.openSiteSection(section.getSiteName(), section.getDisplayName());
		return sectionContentsTable.getAllContentNames();		
	}
	
	/**
	 * Removes content from a section.
	 * 
	 * @param testSession
	 * @param siteName
	 * @param sectionName
	 * @param contentName
	 * @return
	 */
	public SectionContentsTablePage removeContentFromSection(TestSession testSession,String siteName,String sectionName,String contentName)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame leftmenu = new LeftMenuFrame(testSession);
		SectionContentsTablePage sectionContentsTable = leftmenu.openSiteSection(siteName, sectionName);
		sectionContentsTable.doRemoveContent(contentName);
		return sectionContentsTable;
	}
	
	/**
	 * @param testSession
	 * @param siteName
	 * @param sectionName
	 * @param contentName
	 * @return
	 */
//	public SectionContentsTablePage changePublishStatusInSection(TestSession testSession,String siteName,String sectionName,String contentName)
//	{
//		PageNavigatorV4.navgateToAdminConsole(testSession);
//		LeftMenuFrame leftmenu = new LeftMenuFrame(testSession);
//		SectionContentsTablePage sectionContentsTable = leftmenu.openSiteSection(siteName, sectionName);
//		//String[] parents = content.getParentNames();
//		sectionContentsTable.doChange
//		return sectionContentsTable;
//	}
	/**
	 * Clicks by site's name and opens 'Site Edit Wizard', types new data and clicks by 'Save' button.
	 * 
	 * @param testSession
	 * @param siteName site to edit
	 * @param newSite new site.
	 * @return page with table of all Sites.
	 */
	public SitesTableFrame editSite(TestSession testSession, String siteName, Site newSite)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame menu = new LeftMenuFrame(testSession);
		SitesTableFrame sitesFrame = menu.openSitesTableFrame(testSession);
		sitesFrame.doEditSite(siteName, newSite);
		sitesFrame.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return sitesFrame;
	}
	
	/**
	 * Clicks by site's name and opens 'edit portlet' wizard page, go to 'Datasource' tab and click by 'preview datasource' button.
	 * 
	 * @param testSession
	 * @param portlet portlet with datasource
	 * @return datasource content.
	 */
	public String getPreviewDatasourceContent(TestSession testSession,  Portlet portlet)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame leftmenu = new LeftMenuFrame(testSession);
		SitePortletsTablePage sitePortletsPage = leftmenu.openSitePortletsTable(portlet.getSiteName());
		AddPortletWizardPage wizard = sitePortletsPage.openPortletForEdit(portlet.getName());
		return wizard.getPreviewDatasourceContent();		
	}
	/**
	 * Opens site in ICE and get sources, verify: expected text is present in sources.
	 * 
	 * @param testSession
	 * @param siteName
	 * @param text
	 * @return
	 */
	public boolean doOpenInICEAndVerifyText(TestSession testSession, String siteName,String text)
	{
		boolean result = false;
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame menu = new LeftMenuFrame(testSession);
		
		SiteInfoPage siteInfoPage = menu.openSiteInfoPage( siteName);
		siteInfoPage.doOpenSiteInICE();	
		Set<String> allWindows = testSession.getDriver().getWindowHandles();
		if (!allWindows.isEmpty())
		{
			for (String windowId : allWindows)
			{
				try
				{
					//try to find and switch to POPUP-WINDOW:
					if (testSession.getDriver().switchTo().window(windowId).getTitle().contains(siteName))
					{
						String source = testSession.getDriver().getPageSource();
						if(source.contains(text))
						{
							result =true;
						}
						
					}
				} catch (NoSuchWindowException e)
				{
					throw new TestFrameworkException("NoSuchWindowException- wrong ID" + e.getLocalizedMessage());
				}
			}
		}
		return result;
	}
	
	/**
	 * Deletes a site.
	 * 
	 * @param testSession
	 * @param siteName
	 * @return {@link SitesTableFrame} instance,
	 */
	public SitesTableFrame deleteSite(TestSession testSession, String siteName)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame menu = new LeftMenuFrame(testSession);
		SiteInfoPage siteInfo = menu.openSiteInfoPage( siteName);
		siteInfo.doDeleteSite();
		SitesTableFrame sitesTable = new SitesTableFrame(testSession);
		sitesTable.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return sitesTable;
	}
}
