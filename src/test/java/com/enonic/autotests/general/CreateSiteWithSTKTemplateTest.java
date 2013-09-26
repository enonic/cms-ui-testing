package com.enonic.autotests.general;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.BaseTest;
import com.enonic.autotests.model.site.PageMenuItem;
import com.enonic.autotests.model.site.PageTemplate;
import com.enonic.autotests.model.site.Portlet;
import com.enonic.autotests.model.site.STKResource;
import com.enonic.autotests.model.site.Site;
import com.enonic.autotests.pages.v4.adminconsole.site.SitePortletsTablePage;
import com.enonic.autotests.pages.v4.adminconsole.site.SitesTableFrame;
import com.enonic.autotests.services.SiteService;

public class CreateSiteWithSTKTemplateTest extends BaseTest
{
	private SiteService siteService = new SiteService();
	private final String SITE_STK_KEY = "site_key";

	@Test(description = "create new site and verify: site present in the table")
	public void createNewSiteTest()
	{
		logger.info("create new site and verify: site present in the table.");
		Site site = new Site();
		String siteName = "siteSTK" + Math.abs(new Random().nextInt());
		site.setDispalyName(siteName);
		site.setLanguage("English");
		SitesTableFrame table = siteService.createSite(getTestSession(), site );
		boolean result = table.verifyIsPresent(site.getDispalyName());
		Assert.assertTrue(result,"new site was not found in the table");		
		getTestSession().put(SITE_STK_KEY, site);	
		logger.info(" $$$$  FINISHED: CreateSiteWithSTKTemplateTest- site created: "+siteName);
	}
	@Test(description ="Create a page template", dependsOnMethods = "createNewSiteTest")
	public void createPageTemplateTest()
	{
		logger.info("#### Create a page template.");
		Site site = (Site)getTestSession().get(SITE_STK_KEY);	
		PageTemplate template = new PageTemplate();
		template.setName("test");
		//template.setType(type)
		STKResource stylesheet = new STKResource();
		stylesheet.setName("page.xsl");
		stylesheet.setPath("modules","theme-sample-site");
		template.setStylesheet(stylesheet );
		siteService.addPageTemplate(getTestSession(), site.getDispalyName(), template );
		logger.info("$$$$ FINISHED:  Page template was created. name : " +template.getName());
	}
	
	@Test(description ="Create a menu item which will use the page template", dependsOnMethods = "createPageTemplateTest")
	public void addMenuItemTest()
	{
		logger.info("#### Create a menu item which will use the page template.");
		Site site = (Site)getTestSession().get(SITE_STK_KEY);	
		
		PageMenuItem menuItem = PageMenuItem.with().displayName("pageItem").menuName("pageItem").showInMenu(true).pageTemplateName("test").build();
		siteService.addPageMenuItem(getTestSession(), site.getDispalyName(), menuItem );
		//TODO verify is created
		logger.info("$$$$ FINISHED:  Create a menu item.");
	}
	
	@Test(description ="Edit site and specify path to resources.", dependsOnMethods = "createPageTemplateTest")
	public void editSiteAndSpecifyPathToResources()
	{
		logger.info("### Edit site and specify path to resources.");
		Site site = (Site)getTestSession().get(SITE_STK_KEY);	
		String pathToPublicResources = "/_public/theme-sample-site";
		String pathToInternalResources = "/config/sample-site";

        STKResource  deviceClassification = new STKResource();
        deviceClassification.setPath("modules","library-stk","resolvers");
        deviceClassification.setName("device-classification.xsl");
        site.setDeviceClassification(deviceClassification);
		site.setPathToPublicResources(pathToPublicResources );
		site.setPathToInternalResources(pathToInternalResources);
		siteService.editSite(getTestSession(), site.getDispalyName(), site);
		logger.info("$$$$ FINISHED:  Edit site and specify path to resources.");
	}
	
	@Test(description ="open site and verify a title", dependsOnMethods = "createPageTemplateTest")
	public void openSiteInACETest()
	{
		logger.info("#### open site and verify a title");
		Site site = (Site)getTestSession().get(SITE_STK_KEY);	
		
		boolean result = siteService.doOpenInICEAndVerify(getTestSession(), site.getDispalyName());
		Assert.assertTrue(result,"Open in ICE was failed!");
		logger.info("$$$$ FINISHED : site sucessfully opened!");
	} 
	
	@Test(description ="Create  a portlet", dependsOnMethods = "openSiteInACETest")
	public void addPortletTest()
	{
		logger.info("#### Create  a portlet");
		Site site = (Site)getTestSession().get(SITE_STK_KEY);	
		
		Portlet portlet = new Portlet();
		portlet.setName("Portlet1");
		STKResource stylesheet =  new STKResource();
		stylesheet.setName("sample-module.xsl");
		stylesheet.setPath("modules","module-sample-site");
		portlet.setStylesheet(stylesheet);
		SitePortletsTablePage table = siteService.addPortlet(getTestSession(), site.getDispalyName(), portlet );
		//boolean result = table.verifyIsPresent(portlet.getName());
		//Assert.assertTrue(result,"");
		//TODO verify is created
		logger.info("$$$$ FINISHED : Portlet Created!");
	} 
	
}
