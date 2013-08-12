package com.enonic.autotests.services;

import java.util.List;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.Section;
import com.enonic.autotests.model.Site;
import com.enonic.autotests.pages.v4.adminconsole.LeftMenuFrame;
import com.enonic.autotests.pages.v4.adminconsole.site.AddSectionWizardPage;
import com.enonic.autotests.pages.v4.adminconsole.site.SectionContentsTablePage;
import com.enonic.autotests.pages.v4.adminconsole.site.SiteInfoPage;
import com.enonic.autotests.pages.v4.adminconsole.site.SiteMenuItemsTablePage;
import com.enonic.autotests.pages.v4.adminconsole.site.SitesTableFrame;

/**
 *   Site service 
 *
 */
public class SiteService
{

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
		sitesFrame.waituntilPageLoaded(2l);
		sitesFrame.doAddSite(site);		
		sitesFrame.waituntilPageLoaded(2l);
		return sitesFrame;

	}
	
	public SiteMenuItemsTablePage addSection(TestSession testSession,String siteName,Section section)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		//1. expand 'Sites'folder, expand site and click by 'Menu' link 
		LeftMenuFrame leftmenu = new LeftMenuFrame(testSession);
	     // SiteMenuItemsTablePage contains all Menu Items for this site.
		SiteMenuItemsTablePage siteMenuItems = leftmenu.openSiteMenuItems(siteName);
		
		
		//2. click by 'New' button and select 'Section', open add section wizard:
		AddSectionWizardPage sectionwizard = siteMenuItems.startAddNewSection();
		//3. populate data and save.
		sectionwizard.doTypeDataAndSave(section);
		siteMenuItems.waituntilPageLoaded(2l);
		return siteMenuItems;
	}
	
	
	/**
	 * Opens a section, clicks by 'Add' button and add new content to a section.
	 * @param testSession
	 * @param section
	 * @param content content to add.
	 * @return
	 */
	public SectionContentsTablePage addContentToSection(TestSession testSession,Section section,Content<?> content)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame leftmenu = new LeftMenuFrame(testSession);
		SectionContentsTablePage sectionContentsTable = leftmenu.openSiteSection(section.getSiteName(), section.getDisplayName());
		String[] parents = content.getParentNames();
		sectionContentsTable.doAddContentToSection(parents,content.getDisplayName());
		sectionContentsTable.waituntilPageLoaded(2l);
		return sectionContentsTable;
	}
	
	/**
	 * Check out a publish status of content.
	 * @param testSession
	 * @param section
	 * @param contentName
	 * @return true if content published, otherwise false.
	 */
	public boolean isContentFromSectionPublished(TestSession testSession, Section section,String contentName)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame leftmenu = new LeftMenuFrame(testSession);
		SectionContentsTablePage sectionContentsTable = leftmenu.openSiteSection(section.getSiteName(), section.getDisplayName());
		
		return sectionContentsTable.isContentPublished(contentName);
		
	}
	/**
	 * Gets all names of contents from section
	 * 
	 * @param testSession
	 * @param section
	 * @return
	 */
	public List<String> getContentNamesFromSection(TestSession testSession, Section section)
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
		sitesFrame.waituntilPageLoaded(2l);
		return sitesFrame;
	}
	
	/**
	 * Deletes site.
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
