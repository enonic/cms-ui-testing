package com.enonic.autotests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.services.RepositoryService;
import com.enonic.autotests.services.SiteService;
import com.enonic.autotests.services.SystemService;

/**
 * Preparations: delete all sites, repositories and Content types.
 * 
 */
public class DeleteAllSitesAndContentTests extends BaseTest
{
	private SiteService siteService = new SiteService();

	private RepositoryService repositoryService = new RepositoryService();
	private SystemService systemService = new SystemService();
	private ContentTypeService contentTypeService = new ContentTypeService();

	@Test(description = "Delete all items from 'Sites' folder")
	public void deleteSites()
	{
		logger.info("@@@@@@@@@@@@@@@@ TEST: deleteSites started  @@@@@@@@@@@@@@@@");
		siteService.delteAllSites(getTestSession());

		List<String> list = siteService.getAllSiteNames(getTestSession());
		Assert.assertTrue(list.size() == 0, "Folder 'Sites' is not empty");
		logger.info("$$$$$ all sites were deleted");
	}

	@Test(description = "Delete all items from 'Content' folder ", dependsOnMethods = "deleteSites")
	public void deleteAllContent()
	{
		logger.info("### TEST: deleteAllContent");
		List<String> allNames = repositoryService.getAllRepositoryNames(getTestSession());
		for (String repoName : allNames)
		{
			repositoryService.deleteRepository(getTestSession(), repoName);
		}
		logger.info("$$$$$$ all content were deleted");

	}

	@Test(description = "Remove Deleted Content From Database ", dependsOnMethods = "deleteAllContent")
	public void removeDeletedContentFromDatabaseTest()
	{
		logger.info("#### TEST: removeDeletedContentFromDatabaseTest");
		systemService.doRemoveDeletetContentFromDataBase(getTestSession());
		logger.info("Content were removed From Database");
		logger.info("$$$$$ TEST: removeDeletedContentFromDatabaseTest");

	}

	@Test(description = "Delete all Content Types", dependsOnMethods = "removeDeletedContentFromDatabaseTest")
	public void deleteAllContentTypes()
	{
		logger.info("#### TEST: deleteAllContentTypes");
		contentTypeService.delteAllContentTypes(getTestSession());
		logger.info("$$$$$$$$ ALL FINISHED:DeleteAllSitesAndContentTests, all content types were deleted");

	}
}
