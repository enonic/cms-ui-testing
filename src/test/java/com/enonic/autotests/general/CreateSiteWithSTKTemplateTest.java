package com.enonic.autotests.general;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.BaseTest;
import com.enonic.autotests.model.PageTemplate;
import com.enonic.autotests.model.Site;
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
	public void createPageTemplateForSite()
	{
		logger.info("Select 'Page templates' for the site you created in the previous step, and then click 'New'.");
		Site site = (Site)getTestSession().get(SITE_STK_KEY);	
		PageTemplate template = new PageTemplate();
		siteService.addPageTemplate(getTestSession(), site.getDispalyName(), template );
	}
	
}
