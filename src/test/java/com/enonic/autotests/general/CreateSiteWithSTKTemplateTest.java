package com.enonic.autotests.general;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.BaseTest;
import com.enonic.autotests.model.site.PageMenuItem;
import com.enonic.autotests.model.site.PageMenuItem.PageMenuItemPortlet;
import com.enonic.autotests.model.site.PageMenuItem.Region;
import com.enonic.autotests.model.site.PageTemplate;
import com.enonic.autotests.model.site.Portlet;
import com.enonic.autotests.model.site.STKResource;
import com.enonic.autotests.model.site.Site;
import com.enonic.autotests.pages.v4.adminconsole.site.SiteMenuItemsTablePage;
import com.enonic.autotests.pages.v4.adminconsole.site.SitePortletsTablePage;
import com.enonic.autotests.pages.v4.adminconsole.site.SitesTableFrame;
import com.enonic.autotests.services.SiteService;

public class CreateSiteWithSTKTemplateTest extends BaseTest
{
	private SiteService siteService = new SiteService();
	private final String SITE_STK_KEY = "site_key";
	private final String PAGE_MENU_ITEM_KEY = "pmenuitem_key";

	private final String PAGE_MENU_ITEM_NAME = "pageitem";
	private final String PAGE_TEMPLATE_NAME = "test";

	private final String SAMPLE_PORTLET_NAME = "portlet1";
	private final String TEXT_IN_PORTLET = "My first module";
	private final String TEXT_PAGE_ITEM = "My first headline";

	@Test(description = "create new site and verify: site present in the table")
	public void createNewSiteTest()
	{
		logger.info("create new site and verify: site present in the table.");
		Site site = new Site();
		String siteName = "siteSTK" + Math.abs(new Random().nextInt());
		site.setDispalyName(siteName);
		site.setLanguage("English");
		SitesTableFrame table = siteService.createSite(getTestSession(), site);
		boolean result = table.verifyIsPresent(site.getDispalyName());
		Assert.assertTrue(result, "new site was not found in the table");
		getTestSession().put(SITE_STK_KEY, site);
		logger.info(" $$$$  FINISHED: CreateSiteWithSTKTemplateTest- site created: " + siteName);
	}

	@Test(description = "Create a page template", dependsOnMethods = "createNewSiteTest")
	public void createPageTemplateTest()
	{
		logger.info("#### Create a page template.");
		Site site = (Site) getTestSession().get(SITE_STK_KEY);
		PageTemplate template = new PageTemplate();
		template.setName("test");

		STKResource stylesheet = new STKResource();
		stylesheet.setName("page.xsl");
		stylesheet.setPath("modules", "theme-sample-site");
		template.setStylesheet(stylesheet);
		siteService.addPageTemplate(getTestSession(), site.getDispalyName(), template);
		logger.info("$$$$ FINISHED:  Page template was created. name : " + template.getName());
	}

	@Test(description = "Create a menu item which will use the page template", dependsOnMethods = "createPageTemplateTest")
	public void addMenuItemTest()
	{
		logger.info("#### Create a menu item which will use the page template.");
		Site site = (Site) getTestSession().get(SITE_STK_KEY);

		PageMenuItem menuItem = PageMenuItem.with().displayName(PAGE_MENU_ITEM_NAME).menuName(PAGE_MENU_ITEM_NAME).showInMenu(true)
				.pageTemplateName(PAGE_TEMPLATE_NAME).siteNmae(site.getDispalyName()).build();
		SiteMenuItemsTablePage table = siteService.addPageMenuItem(getTestSession(), menuItem);
		getTestSession().put(PAGE_MENU_ITEM_KEY, menuItem);

		boolean isPresent = table.verifyIsPresent(menuItem.getDisplayName());
		Assert.assertTrue(isPresent, "new menu item was not found in table, item: " + menuItem.getDisplayName());
		logger.info("$$$$ FINISHED:  Create a menu item.");
	}

	@Test(description = "Edit site and specify path to resources.", dependsOnMethods = "createPageTemplateTest")
	public void editSiteAndSpecifyPathToResources()
	{
		logger.info("### Edit site and specify path to resources.");
		Site site = (Site) getTestSession().get(SITE_STK_KEY);
		String pathToPublicResources = "/_public/theme-sample-site";
		String pathToInternalResources = "/config/sample-site";

		STKResource deviceClassification = new STKResource();
		deviceClassification.setPath("modules", "library-stk", "resolvers");
		deviceClassification.setName("device-classification.xsl");
		site.setDeviceClassification(deviceClassification);
		site.setPathToPublicResources(pathToPublicResources);
		site.setPathToInternalResources(pathToInternalResources);
		siteService.editSite(getTestSession(), site.getDispalyName(), site);
		logger.info("$$$$ FINISHED:  Edit site and specify path to resources.");
	}

	@Test(description = "open site and verify a text", dependsOnMethods = "createPageTemplateTest")
	public void openSiteInACETest()
	{
		logger.info("#### open site and verify a text");
		Site site = (Site) getTestSession().get(SITE_STK_KEY);

		boolean result = siteService.doOpenInICEAndVerifyText(getTestSession(), site.getDispalyName(), TEXT_PAGE_ITEM);
		Assert.assertTrue(result, "Open in ICE was failed!");
		logger.info("$$$$ FINISHED : site sucessfully opened! Text was verified");
	}

	@Test(description = "Create  a portlet", dependsOnMethods = "openSiteInACETest")
	public void addPortletTest()
	{
		logger.info("#### Create  a portlet");
		Site site = (Site) getTestSession().get(SITE_STK_KEY);

		Portlet portlet = new Portlet();
		portlet.setName(SAMPLE_PORTLET_NAME);
		STKResource stylesheet = new STKResource();
		stylesheet.setName("sample-module.xsl");
		stylesheet.setPath("modules", "module-sample-site");
		portlet.setStylesheet(stylesheet);
		portlet.setSiteName(site.getDispalyName());
		SitePortletsTablePage table = siteService.addPortlet(getTestSession(), portlet);
		boolean result = table.verifyIsPresent(portlet.getName());
		Assert.assertTrue(result, "Portlet with name: " + portlet.getName() + " was not found in the table!");
		logger.info("$$$$ FINISHED : Portlet Created!");
	}

	@Test(description = "Edit page menu item: add portlet to the page configuration.", dependsOnMethods = "addPortletTest")
	public void editPageMenuItemTest()
	{
		logger.info("#### Put the new portlet into the 'center' region.");
		PageMenuItem itemToUpdate = (PageMenuItem) getTestSession().get(PAGE_MENU_ITEM_KEY);

		PageMenuItem itemNew = itemToUpdate.cloneItem();
		List<PageMenuItemPortlet> portlets = new ArrayList<>();
		PageMenuItemPortlet itemPortlet = new PageMenuItemPortlet();
		itemPortlet.setPortletName(SAMPLE_PORTLET_NAME);
		itemPortlet.setRegion(Region.CENTER);
		portlets.add(itemPortlet);
		itemNew.setPortlets(portlets);
		// specify Region Center
		siteService.editMenuItem(getTestSession(), itemToUpdate, itemNew);
		logger.info("$$$$ FINISHED : portlet was added into the 'center' region.");
	}

	@Test(description = " open site and verify a TEXT from a portlet", dependsOnMethods = "editPageMenuItemTest")
	public void openSiteInACE2Test()
	{
		logger.info("#### open site and verify a TEXT from a portlet");
		Site site = (Site) getTestSession().get(SITE_STK_KEY);

		boolean result = siteService.doOpenInICEAndVerifyText(getTestSession(), site.getDispalyName(), TEXT_IN_PORTLET);
		Assert.assertTrue(result, "text was not found in page!");
		logger.info("$$$$ FINISHED : open site and verify a TEXT from a portlet");
	}

}
