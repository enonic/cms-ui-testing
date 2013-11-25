package com.enonic.autotests.contentindex;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.FileContentInfo;
import com.enonic.autotests.model.site.SectionMenuItem;
import com.enonic.autotests.model.site.Site;
import com.enonic.autotests.model.site.Site.AllowedPageTypes;
import com.enonic.autotests.pages.adminconsole.content.RepositoriesListFrame;
import com.enonic.autotests.pages.adminconsole.site.SectionContentsTablePage;
import com.enonic.autotests.pages.adminconsole.site.SiteMenuItemsTablePage;
import com.enonic.autotests.pages.adminconsole.site.SitesTableFrame;
import com.enonic.autotests.services.SiteService;

public class ContentIndexPart2 extends BaseContentIndexTest
{
	private final String SITE_KEY = "test_site";
	private final String SECTION_KEY = "test_section";
	private final String ORDERED_SECTION_KEY = "test_section_ordered";
	
	private final String CI_REPO_KEY = "repo_contentindex";

	private String SECTION_CONTENT_NAME = "sectioncontent.jpg";
	private final String FILE_CATEGORY_NAME = "filesCategory";

	private String SECTION_CONTENT_PUBLISH_NAME = "publishcontent.jpg";

	private final String PATH_TO_FILE = "test-data/contentrepository/test.jpg";

	private SiteService siteService = new SiteService();

	@Test(description = "set up: create content types: Image and File")
	public void settings()
	{
		doSettings();
	}

	@Test(description = "create a repository", dependsOnMethods = "settings")
	public void createRepository()
	{
		ContentRepository repository = new ContentRepository();
		String repoName = "cindex" + Math.abs(new Random().nextInt());
		repository.setName(repoName);
		RepositoriesListFrame repositoriesListFrame = repositoryService.createContentRepository(getTestSession(), repository);

		boolean isCreated = repositoriesListFrame.verifyIsRepositoryPresentedInTable(repoName);
		Assert.assertTrue(isCreated, "new created repository was not found in the table!");

		logger.info("ContentRepository : " + repoName + " was created!");
		getTestSession().put(CI_REPO_KEY, repository);
	}

	@Test(description = "create new site and verify: site present in the table", dependsOnMethods = "settings")
	public void createNewSiteTest()
	{
		logger.info("Case-info: create new site and verify: site present in the table.");
		Site site = new Site();
		String siteName = "sectiontest" + Math.abs(new Random().nextInt());
		site.setDispalyName(siteName);
		site.setLanguage("English");
		SitesTableFrame table = siteService.createSite(getTestSession(), site);
		boolean result = table.verifyIsPresent(site.getDispalyName());
		Assert.assertTrue(result, "new site was not found in the table");
		getTestSession().put(SITE_KEY, site);
		logger.info("FINISHED: createNewSiteTest ");
	}

	/**
	 * Case-info: Edit site, allow Section page type.
	 */
	@Test(dependsOnMethods = "createNewSiteTest", description = "edit site and allow section page type")
	public void allowSectionPageTypeTest()
	{
		logger.info("##### SATRTED: Edit site, allow Section page type.");
		Site site = (Site) getTestSession().get(SITE_KEY);
		AllowedPageTypes[] allowedPageTypes = { AllowedPageTypes.SECTION };
		site.setAllowedPageTypes(allowedPageTypes);
		siteService.editSite(getTestSession(), site.getDispalyName(), site);
		logger.info("$$$$$ FINISHED: Edit site-test ,  Section page type allowed.");
	}

	/**
	 * Case-info: add new section menu item to the Site .
	 */
	@Test(dependsOnMethods = "allowSectionPageTypeTest", description = "add to Site new section menu item")
	public void addSectionTest()
	{
		logger.info("##### STARTED : add to Site new section menu item ");
		Site site = (Site) getTestSession().get(SITE_KEY);
		SectionMenuItem section = new SectionMenuItem();
		section.setDisplayName("section1");
		section.setShowInMenu(true);
		section.setMenuName("section1");
		section.setSiteName(site.getDispalyName());
		List<String> list = new ArrayList<>();
		list.add("File");
		section.setAvailableContentTypes(list);
		// 1. try to add a new section to Site:
		SiteMenuItemsTablePage siteItems = siteService.addSectionMenuItem(getTestSession(), site.getDispalyName(), section);
		// 2. verify: section present
		boolean result = siteItems.verifyIsPresent(section.getDisplayName());
		Assert.assertTrue(result, "section was not found in the table!");
		logger.info("TEST FINISHED: add to Site new section menu item ");
		// put new created section to the session.
		getTestSession().put(SECTION_KEY, section);
		logger.info("$$$$$ FINISHED: addSectionTest.");

	}

	/**
	 * Case info:
	 * <p/>
	 * Add content to section in admin, verify: <br>
	 * -content visible in section view.
	 */
	@Test(dependsOnMethods = "addSectionTest", description = "add content to section, verify: content visible in section view")
	public void addContentToSectionTest()
	{
		logger.info("#### STARTED :add content to section, verify: content visible in section view");
		SectionMenuItem section = (SectionMenuItem) getTestSession().get(SECTION_KEY);

		// Content<FileContentInfo> content = (Content<FileContentInfo>)
		// getTestSession().get( FILE_CONTENT_KEY );
		ContentRepository repository = (ContentRepository) getTestSession().get(CI_REPO_KEY);

		Content<FileContentInfo> sectionContent = new Content<>();

		FileContentInfo contentTab = new FileContentInfo();
		contentTab.setPathToFile(PATH_TO_FILE);
		contentTab.setDescription("file content");
		sectionContent.setContentTab(contentTab);
		sectionContent.setDisplayName(SECTION_CONTENT_NAME);

		List<Content<FileContentInfo>> contents = new ArrayList<>();
		contents.add(sectionContent);
		// 1. preparation: create new category and add content to the category
		addCategoryAndContent(contents, repository, FILE_CATEGORY_NAME);
		// 2.verify, that content visible in section view and try to add content
		// to section:
		SectionContentsTablePage table = siteService.addContentToSection(getTestSession(), section, sectionContent);
		// 3. verify: content present in the section:
		boolean result = table.verifyIsPresent(sectionContent.getDisplayName());
		Assert.assertTrue(result, "content was not added to Section!");
		logger.info("$$$$$ FINISHED:addContentToSectionTest#########");

	}

	/**
	 * Case-info: Publish to section in admin, verify: <br>
	 * Content visible as published in section view
	 */
	@Test(dependsOnMethods = "addSectionTest", description = "publish content to section")
	public void publishContentToSection()
	{
		logger.info("#### STARTED :publish content to section ");
		ContentRepository repository = (ContentRepository) getTestSession().get(CI_REPO_KEY);

		Content<FileContentInfo> sectionContent = new Content<>();

		FileContentInfo contentTab = new FileContentInfo();
		contentTab.setPathToFile(PATH_TO_FILE);
		contentTab.setDescription("file content");
		sectionContent.setContentTab(contentTab);
		sectionContent.setDisplayName(SECTION_CONTENT_PUBLISH_NAME);
		String[] parentsNames = new String[] { repository.getName(), FILE_CATEGORY_NAME };
		sectionContent.setParentNames(parentsNames);

		// 1. preparation: create new category and add content to the category
		contentService.addFileContent(getTestSession(), sectionContent);
		
		// 2. get section from, that was created in previous tests: 'addSectionTest'
		SectionMenuItem section = (SectionMenuItem) getTestSession().get(SECTION_KEY);

		// 3. open category and press 'approve and publish'-button, (publish content to the section)
		contentService.doPublishContentToSection(getTestSession(), sectionContent, section);
		boolean isContentPublished = siteService.isContentFromSectionPublished(getTestSession(), section, sectionContent.getDisplayName());
		Assert.assertTrue(isContentPublished, "content publishing failed, content not present in the section or content is not published!");
		logger.info("$$$$$ FINISHED: publishContentToSection #########");
	}

	/**
	 * Case info:
	 * <p/>
	 * Remove content from section, verify: <br>
	 * -content not present in section view.
	 */
	@Test(dependsOnMethods = "addContentToSectionTest", description = "Remove content from section, verify: content not present in section view")
	public void removeFromSectionTest()
	{
		logger.info("##### STARTED :add content to section, verify: content visible in section view");
		SectionMenuItem section = (SectionMenuItem) getTestSession().get(SECTION_KEY);
		Site site = (Site) getTestSession().get(SITE_KEY);
		SectionContentsTablePage table = siteService.removeContentFromSection(getTestSession(), site.getDispalyName(), section.getDisplayName(),
				SECTION_CONTENT_NAME);
		boolean result = table.verifyIsPresent(SECTION_CONTENT_NAME);
		Assert.assertFalse(result, "content was not removed from section, contentname:" + SECTION_CONTENT_NAME);
		logger.info("$$$$ FINISHED: Remove content from section");
	}

	/**
	 * Case-info: add new section menu item to the Site .
	 */
	@Test(dependsOnMethods = "allowSectionPageTypeTest", description = "add new ordered section menu item to the  Site ")
	public void addOrderedSectionTest()
	{
		logger.info("#### STARTED: add new ordered section menu item to the  Site ");
		Site site = (Site) getTestSession().get(SITE_KEY);
		SectionMenuItem section = new SectionMenuItem();
		section.setDisplayName("ordered");
		section.setShowInMenu(true);
		section.setMenuName("ordered");
		section.setOrdered(true);
		section.setSiteName(site.getDispalyName());
		List<String> list = new ArrayList<>();
		list.add("File");
		section.setAvailableContentTypes(list);
		// 1. try to add a new section to Site:
		SiteMenuItemsTablePage siteItems = siteService.addSectionMenuItem(getTestSession(), site.getDispalyName(), section);
		// 2. verify: section present
		boolean result = siteItems.verifyIsPresent(section.getDisplayName());
		Assert.assertTrue(result, "section was not found in the table!");

		// put new created section to the session.
		getTestSession().put(ORDERED_SECTION_KEY, section);
		logger.info("$$$$$ FINISHED: addOrderedSectionTest. #########");

	}

	/**
	 * Case-info: Publish to ordered section in admin, verify: <br>
	 * Content visible as published in section view <br>
	 * correct ordering is maintained after save
	 */
	 @Test(dependsOnMethods = "addOrderedSectionTest", description ="publish 2 file to ordered section")
	public void publishContentToOrderedSection()
	{
        String categoryName= "orderedtest";
		logger.info("##### STARTED :publish two files to ordered section ");
		ContentRepository repository = (ContentRepository) getTestSession().get(CI_REPO_KEY);

		Content<FileContentInfo> sectionContent1 = new Content<>();
		Content<FileContentInfo> sectionContent2 = new Content<>();
		sectionContent1.setDisplayName("contenttopublish1.jpg");
		String[] parentsNames = new String[]{repository.getName(),categoryName};
		sectionContent1.setParentNames(parentsNames );
		
		FileContentInfo contentTab = new FileContentInfo();
		contentTab.setPathToFile(PATH_TO_FILE);
		contentTab.setDescription("file content");
		sectionContent1.setContentTab(contentTab);
		sectionContent2.setContentTab(contentTab);
		
		sectionContent2.setDisplayName("contenttopublish2.jpg");
		sectionContent2.setParentNames(parentsNames );

		// 1. preparation: create new category and add content to the category
		List<Content<FileContentInfo>> contents = new ArrayList<>();
		contents.add(sectionContent1);
		contents.add(sectionContent2);
		addCategoryAndContent(contents, repository, categoryName );
		// 2. get section from, that was created in previous tests: 'addOrderedSectionTest'
		SectionMenuItem section = (SectionMenuItem) getTestSession().get(ORDERED_SECTION_KEY);

		// 3. open category and press 'approve and publish'-button, (publish content to the section)
		contentService.doPublishContentToSection(getTestSession(), sectionContent1, section);
		boolean isContentPublished = siteService.isContentFromSectionPublished(getTestSession(), section, sectionContent1.getDisplayName());
		Assert.assertTrue(isContentPublished, "content publishing failed, content not present in the section or content is not published!");

		contentService.doPublishContentToSectionAnMoveToEnd(getTestSession(), sectionContent2, section);
		List<String> actualContentNames = siteService.getContentNamesFromSection(getTestSession(), section);
		boolean isOrsered = verifyOrder(actualContentNames, sectionContent2.getDisplayName());

		Assert.assertTrue(isOrsered, "wrong order in the section page");
		logger.info("$$$$ FINISHED: publishContentToOrderedSection #########");
	}

	/**
	 * Verifies: list is ordered.
	 * 
	 * @param actualContentNames
	 * @param expectedContentName
	 * @return
	 */
	private boolean verifyOrder(List<String> actualContentNames, String expectedContentName)
	{
		// contentName was moved down, when it published, so this content should in the second row.
		return actualContentNames.get(1).equals(expectedContentName);
	}



}
