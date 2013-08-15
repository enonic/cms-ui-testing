package com.enonic.autotests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.services.RepositoryService;
import com.enonic.autotests.services.SiteService;

/**
 * Preparations: delete all sites and repositories.
 *
 */
public class DeleteAllSitesAndContentTests extends BaseTest
{
	private SiteService siteService = new SiteService();
	
	private RepositoryService repositoryService = new RepositoryService();

	@Test(description="Delete all items from 'Sites' folder")
	public  void deleteSites()
	{
		siteService.delteAllSites(getTestSession());
		
		List<String> list = siteService.getAllSiteNames(getTestSession());
		Assert.assertTrue(list.size() == 0, "Folder 'Sites' is not empty");
		logger.info("all sites were deleted");
	}
	
	@Test(description="Delete all items from 'Content' folder ")
	public  void deleteAllContent()
	{
		List<String> allNames = repositoryService.getAllRepositoryNames(getTestSession());
		for(String repoName: allNames)
		{
			repositoryService.deleteRepository(getTestSession(), repoName);
		}
		logger.info("all content were deleted");
		
	}
}
