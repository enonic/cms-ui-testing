package com.enonic.autotests.general;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.BaseTest;
import com.enonic.autotests.model.site.PageMenuItem;
import com.enonic.autotests.model.site.PageTemplate;
import com.enonic.autotests.model.site.PageTemplate.TemplateStylesheet;
import com.enonic.autotests.model.site.Site;
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
		logger.info("CreateSiteWithSTKTemplateTest- site created: "+siteName);
	}
	@Test(description ="Create a page template", dependsOnMethods = "createNewSiteTest")
	public void createPageTemplateTest()
	{
		logger.info("Select 'Page templates' for the site you created in the previous step, and then click 'New'.");
		Site site = (Site)getTestSession().get(SITE_STK_KEY);	
		PageTemplate template = new PageTemplate();
		template.setName("test");
		//template.setType(type)
		TemplateStylesheet stylesheet = new TemplateStylesheet();
		stylesheet.setName("page.xsl");
		stylesheet.setPath("modules","theme-sample-site");
		template.setStylesheet(stylesheet );
		siteService.addPageTemplate(getTestSession(), site.getDispalyName(), template );
	}
	
	@Test(description ="Create a menu item which will use the page template", dependsOnMethods = "createPageTemplateTest")
	public void addMenuItemTest()
	{
		logger.info("Create a menu item which will use the page template.");
		Site site = (Site)getTestSession().get(SITE_STK_KEY);	
		
		PageMenuItem menuItem = PageMenuItem.with().displayName("pageItem").menuName("pageItem").showInMenu(true).pageTemplateName("test").build();
		siteService.addPageMenuItem(getTestSession(), site.getDispalyName(), menuItem );
	}
	
	//@Test(description ="Create a menu item which will use the page template", dependsOnMethods = "createPageTemplateTest")
	public void editSiteAndSpecifyPathToResources()
	{
		logger.info("Create a menu item which will use the page template.");
		Site site = (Site)getTestSession().get(SITE_STK_KEY);	
		String pathToPublicResources = "/_public/theme-sample-site";
		String pathToInternalResources = "/config/sample-site";
		String devClassification = "/modules/library-stk/resolvers/device-classification.xsl";

		site.setPathToPublicResources(pathToPublicResources );
		site.setPathToInternalResources(pathToInternalResources);
		siteService.editSite(getTestSession(), site.getDispalyName(), site);
	}
	
}
