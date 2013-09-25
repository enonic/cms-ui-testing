package com.enonic.autotests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.model.ContentHandler;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.model.FileContentInfo;
import com.enonic.autotests.model.site.SectionMenuItem;
import com.enonic.autotests.model.site.Site;
import com.enonic.autotests.model.site.Site.AllowedPageTypes;
import com.enonic.autotests.pages.v4.adminconsole.content.AbstractContentTableView;
import com.enonic.autotests.pages.v4.adminconsole.content.ContentsTableFrame;
import com.enonic.autotests.pages.v4.adminconsole.content.RepositoriesListFrame;
import com.enonic.autotests.pages.v4.adminconsole.site.SectionContentsTablePage;
import com.enonic.autotests.pages.v4.adminconsole.site.SiteMenuItemsTablePage;
import com.enonic.autotests.pages.v4.adminconsole.site.SitesTableFrame;
import com.enonic.autotests.providers.ContentRepositoryProvider;
import com.enonic.autotests.services.ContentService;
import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.services.RepositoryService;
import com.enonic.autotests.services.SiteService;
import com.enonic.autotests.testdata.content.AbstractContentXml;
import com.enonic.autotests.testdata.content.ContentRepositoryXml;
import com.enonic.autotests.testdata.contenttype.ContentConvertor;

public class ContentRepositoryTests extends BaseTest
{

	private final String REPOSITORY_LIST = "repository_list";
	private final String IMAGE_CONTENT = "image_content";
	private final String FILE_CONTENT = "file_content";
	private final String TEST_SITE = "test_site";
	private final String TEST_SECTION_FILE_CTYPE = "test_section";
	private final String TEST_ORDEREDSECTION_FILE_CTYPE = "test_section_ordered";
	private final String EDITTEST_CONTENT_NAME = "edited.jpg";
	
	private String SECTION_CONTENT_ADD_NAME ="sectioncontent.jpg";
	private String SECTION_CONTENT_PUBLISH_NAME ="publishcontent.jpg";
	
	private final String TEST_REPO_NAME =  "notopcategory";
	private final String TEST_FILE_CATEGORY_NAME =  "filesCategory";
	private final String TEST_PUBLISH_FILE_CATEGORY_NAME =  "publishtest";

	private ContentTypeService contentTypeService = new ContentTypeService();
	private RepositoryService repositoryService = new RepositoryService();
	private ContentService contentService = new ContentService();
	private SiteService siteService = new SiteService();

	@Test(description="set up: create content types: Image and File")
	public  void settingsForContentRepository()
	{
		logger.info("### TESTS STARTED:  ContentRepositoryTests");
		logger.info("checks for the existance  of Content type, creates new content type if it does not exist");
		ContentType imagesType = new ContentType();
		imagesType.setName("Image");
		imagesType.setContentHandler(ContentHandler.IMAGES);
		imagesType.setDescription("content repository test");
		boolean isExist = contentTypeService.findContentType(getTestSession(), "Image");
		if (!isExist)
		{
			contentTypeService.createContentType(getTestSession(), imagesType);
			logger.info("New content type with 'Images' handler was created");
		}

		ContentType filesType = new ContentType();
		filesType.setName("File");
		filesType.setContentHandler(ContentHandler.FILES);
		filesType.setDescription("content repository test");
		isExist = contentTypeService.findContentType(getTestSession(), "File");
		if (!isExist)
		{
			contentTypeService.createContentType(getTestSession(), filesType);
			logger.info("New content type with 'Files' handler was created");
		}

		getTestSession().put(REPOSITORY_LIST, new ArrayList<ContentRepository>());
		logger.info("### FINISHED: settings ForContentRepositoryTest ");
	}

    @Test(dependsOnMethods = "settingsForContentRepository", description = "create new Content Repository with specified Content Type", dataProvider = "createContentRepositoryPositive", dataProviderClass = ContentRepositoryProvider.class)
	public void createRepositoryTest(ContentRepositoryXml contentRepoXML)
	{
    	logger.info("#### STARTED: createRepositoryTest ");
		logger.info(contentRepoXML.getCaseInfo());
		// add new content repository and save it in the test-session.
		ContentRepository cRepository = ContentConvertor.convertXmlDataToContentRepository(contentRepoXML);
		if (contentRepoXML.getTopCategory() != null)
		{
			String contentTypeName = contentRepoXML.getTopCategory().getContentType().getName();
			cRepository.setContentTypeName(contentTypeName);
		}

		cRepository.setName(contentRepoXML.getName() + Math.abs(new Random().nextInt()));

		// 1. create a new Repository: click by "Content" link in the left frame, and press the 'New'. 'Add category Wizard' should appears
		RepositoriesListFrame repositorIesListFrame = repositoryService.createContentRepository(getTestSession(), cRepository);

		// 2. verify is present in the table, located on the LeftFrame:
		boolean isCreated = repositorIesListFrame.verifyIsRepositoryPresentedInTable(cRepository.getName());
		Assert.assertTrue(isCreated, "new created repository was not found in the table!");
		// 3. put to the session for using in the next test.
		List<ContentRepository> repositoryList = (List) getTestSession().get(REPOSITORY_LIST);
		// add new created repository to arrayList and the save in the session:
		repositoryList.add(cRepository);
		logger.info("$$$$ FINISHED: createRepositoryTest ");

	}

    @Test(description = "add category(Content Type == 'Files') to the repository, repsitory has no a TopCategory", dependsOnMethods = "createRepositoryTest")
	public void testAddCategoryToRepository()
	{
    	logger.info("#### STARTED: add category with 'Files' type  to the repository, repsitory has no a TopCategory  Repository name is  "+TEST_REPO_NAME);
		ContentRepository repository = findRepositoryByName(TEST_REPO_NAME);
		
		//1. build new category with content type === "File"
		ContentCategory newcategory = new ContentCategory();
		newcategory.setName(TEST_FILE_CATEGORY_NAME);
		newcategory.setContentTypeName("File");
		newcategory.setDescription("Files category.");
		String[] pathName = { repository.getName() };
		newcategory.setParentNames(pathName);
		
		//2. add category with content type === "File"
		repositoryService.addCategory(getTestSession(), newcategory);
		//3.verify: category created
		boolean isCreated = repositoryService.isCategoryPresent(getTestSession(), TEST_FILE_CATEGORY_NAME, pathName);
		Assert.assertTrue(isCreated, "new added category was not found!" + newcategory.getName());
		logger.info("$$$$ FINISHED: testAddCategoryToRepository ");

	}
	  
	/**
	 * Case info:
	 * <br>Create Category in admin,
	 * <br>add content to the category, verify: 
	 * <br>Content visible in category view
	 * 
	 **/
    @Test(dependsOnMethods = "createRepositoryTest", dataProvider = "addContent", dataProviderClass = ContentRepositoryProvider.class)
	public void addContentToCategoryTest(AbstractContentXml xmlContent)
	{
		logger.info("#### STARTED  "+ xmlContent.getCaseInfo());
		Content<?> content = ContentConvertor.convertXmlDataToContent(xmlContent);
        //1. find repository for test in the testsession :
		ContentRepository repository = findRepositoryByHandler( xmlContent.getContentHandler());
		String ctypeName = repository.getContentTypeName();
		ContentCategory category = extractCategoryFromTestData(ctypeName, repository.getCategories());
		String[] pathName = new String[] { repository.getName() };
		category.setParentNames(pathName);
		// 2. add category to the Content-Repository
		repositoryService.addCategory(getTestSession(), category);
		String[] pathToContent = new String[] { repository.getName(), category.getName() };
		content.setParentNames(pathToContent);

		// 3. add content to the category.
		AbstractContentTableView frame = contentService.addContent(getTestSession(), repository, content);
		// 4. verify content is present in the table.(Content visible in the category view)
		boolean result = frame.isContentPresentInTable(content.getDisplayName());
		logger.info("case-info: Content visible in category view:" + result);
		Assert.assertTrue(result, "new added content was not found");
		
		
		logger.info("addContentToCategoryTest finished" );

		//5. put objects to the test-session for re use.
		if(content.getContentHandler().equals(ContentHandler.IMAGES))
		{
			getTestSession().put(IMAGE_CONTENT, content);	
		}
		if(content.getContentHandler().equals(ContentHandler.FILES))
		{
			getTestSession().put(FILE_CONTENT, content);	
		}
		
		logger.info("$$$$ FINISHED: addContentToCategoryTest ");

	}

	/**
	 * Case info:
	 * 
	 * Create content in admin, verify: 
	 * <br>Content searchable
	 * 
	 **/
	@Test(dependsOnMethods = "addContentToCategoryTest", description = "Create content in admin, verify: Content searchable")
	public void searchContentTest()
	{
		logger.info("### STARTED: create content and verify: Content  searchable ");
		Content<?> content = (Content<?>) getTestSession().get(IMAGE_CONTENT);
		// 1. Type the display name of content and press the 'Search' button
		List<String> contentNames = contentService.doSearchContentByName(getTestSession(), content.getDisplayName());
		// 2. verify Content searchable, and content present in the table :
		boolean result = contentNames.contains(content.getDisplayName());
		logger.info("case-info: Content searchable: " + result);
		Assert.assertTrue(result, "new added content was not found, search content does not work");
		logger.info("$$$$ FINISHED: searchContentTest ");
	}

	/**
	 * Case info:
	 * 
	 * Update content in admin, verify
     * <br> Content visible in category view
     * <br> Content (new values) searchable 
	 * 
	 **/
    @Test(description = "change content's name and search ",dependsOnMethods="searchContentTest")
	public void updateContentNameAndSearchTest()
	{
    	logger.info("##### STARTED: update content in admin and checkout:Content visible in category view,Content (new values) searchable ");
		Content<?> content = (Content<?>) getTestSession().get(IMAGE_CONTENT);
		// 1. change name of content.
		contentService.editContent(getTestSession(), content, EDITTEST_CONTENT_NAME);
		// 2.search content with new name:
		List<String> contentNames = contentService.doSearchContentByName(getTestSession(), content.getDisplayName());
		// 3. verify Content searchable, and content present in the table :
		boolean result = contentNames.contains(EDITTEST_CONTENT_NAME);
		logger.info("case-info: Content searchable: " + result);
		Assert.assertTrue(result, "new added content was not found, search content does not work");
		logger.info("$$$$$$ FINISHED: updateContentNameAndSearchTest ");
	}

	/**
	 * Case info:
	 * 
	 * Delete (both one and multiple (bulk)) content in admin, verify 
	 * <br>-Content not present in category view 
	 * <br>-Content not searchable
	 * 
	 **/
	@Test(description = "delete content and verify it in the table", dependsOnMethods = "updateContentNameAndSearchTest")
	public void deleteContentFromCategory()
	{
		logger.info("#### STARTED :Delete  content in admin, verify -Content not present in category view ");
		Content<?> content = (Content<?>) getTestSession().get(IMAGE_CONTENT);
		content.setDisplayName(EDITTEST_CONTENT_NAME);
		ContentsTableFrame contentTableFrame = contentService.deleteContentfromCategory(getTestSession(), content);
		boolean isPresent = contentTableFrame.isContentPresentInTable(content.getDisplayName());
		Assert.assertFalse(isPresent, String.format("content with name %s should be deleted ", content.getDisplayName()));
		logger.info("$$$$$ FINISHED: deleteContentFromCategory ");

	}
	
	    

	 /**
	  * Case info:
	  * 
	  * Move (both one and multiple (bulk)) content in admin, verify:
	  * <br>-Content visible in new category view
	  * <br>-Content searchable in new category
	  * 
	  **/
	@Test(dependsOnMethods = "addContentToCategoryTest",description = "Move (both one and multiple (bulk)) content in admin, verify Content visible in new category view")
	public void moveContentTest()
	{
		logger.info("#### STRATED: Move  content in admin, verify Content visible in new category view");
		Content<?> content = (Content<?>) getTestSession().get(FILE_CONTENT);
		ContentRepository repository = findRepositoryByName(TEST_REPO_NAME);
		ContentsTableFrame table = contentService.moveContent(getTestSession(), content, repository.getName(), TEST_FILE_CATEGORY_NAME);
		boolean result = table.isContentPresentInTable(content.getDisplayName());
		Assert.assertFalse(result,"content was not moved! content name"+content.getDisplayName());
		
		table = contentService.openCategory(getTestSession(), repository.getName(),TEST_FILE_CATEGORY_NAME);
		result = table.isContentPresentInTable(content.getDisplayName());
		Assert.assertTrue(result,"content not present in the destination folder! content name"+content.getDisplayName());
		logger.info("$$$$$ FINISHED: moveContentTest ");
	}
	
	/**
	 * Case info:
	 * 
	 * Empty all content from category. Open category, and delete by pushing "Delete" 	
	 */
	@Test(description="Empty all content from category. Open category, and delete by pushing 'Delete'",dependsOnMethods = "addContentToCategoryTest")
	public void deleteAllContentAndDeleteCategory()
	{
		logger.info("#### STARTED: Empty all content from category. Open category, and delete by pushing 'Delete'");
		ContentRepository repository = findRepositoryByName(TEST_REPO_NAME);
		ContentCategory newcategory = new ContentCategory();
		newcategory.setName("categoryTodelete");
		newcategory.setContentTypeName("File");
		newcategory.setDescription("Files category.");
		String[] pathName ={ repository.getName() };
		newcategory.setParentNames(pathName);
		
		//2. add category with content type === "File"
		repositoryService.addCategory(getTestSession(), newcategory);
		//3.verify: category created
		boolean isCreated = repositoryService.isCategoryPresent(getTestSession(), newcategory.getName(), pathName);
		Assert.assertTrue(isCreated, "new added category was not found!" + newcategory.getName());

		ContentsTableFrame table = contentService.openCategory(getTestSession(), repository.getName(),newcategory.getName());
		table.doDeleteAllContent();
		table.doDeleteEmptyCategory();
		boolean ispresent = repositoryService.isCategoryPresent(getTestSession(), newcategory.getName(), pathName);
		Assert.assertFalse(ispresent, "new added category was not found!" + newcategory.getName());
		logger.info("$$$$$FINISHED: deleteAllContentAndDeleteCategory ");
	}
	
	
	@Test(description = "create new site and verify: site present in the table",dependsOnMethods="addContentToCategoryTest")
	public void createNewSiteTest()
	{
		logger.info("Case-info: create new site and verify: site present in the table.");
		Site site = new Site();
		String siteName = "site" + Math.abs(new Random().nextInt());
		site.setDispalyName(siteName);
		site.setLanguage("English");
		SitesTableFrame table = siteService.createSite(getTestSession(), site );
		boolean result = table.verifyIsPresent(site.getDispalyName());
		Assert.assertTrue(result,"new site was not found in the table");		
		getTestSession().put(TEST_SITE, site);	
		logger.info("FINISHED: createNewSiteTest ");
	}
	
	/**
	 * Case-info: Edit site, allow Section page type.
	 */
	@Test(dependsOnMethods ="createNewSiteTest",description = "edit site and allow section page type")
	public void allowSectionPageTypeTest()
	{
		logger.info("##### SATRTED: Edit site, allow Section page type.");
		Site site = (Site)getTestSession().get(TEST_SITE);	
		AllowedPageTypes[] allowedPageTypes = {AllowedPageTypes.SECTION};
		site.setAllowedPageTypes(allowedPageTypes );
		siteService.editSite(getTestSession(), site.getDispalyName(), site);
		logger.info("$$$$$ FINISHED: Edit site-test ,  Section page type allowed.");
	}
	/**
	 * Case-info: add new section menu item to the  Site .
	 */
	@Test(dependsOnMethods ="allowSectionPageTypeTest",description ="add to Site new section menu item")
	public void addSectionTest()
	{
		logger.info("##### STARTED : add to Site new section menu item ");
		Site site = (Site)getTestSession().get(TEST_SITE);			
		SectionMenuItem section = new SectionMenuItem();
		section.setDisplayName("section1");
		section.setShowInMenu(true);
		section.setMenuName("section1");
		section.setSiteName(site.getDispalyName());
		List<String> list = new ArrayList<>();
		list.add("File");
		section.setAvailableContentTypes(list );
		//1. try to add a new section to Site:
		SiteMenuItemsTablePage siteItems = siteService.addSection(getTestSession(), site.getDispalyName(), section );
		//2. verify: section present
		boolean result = siteItems.verifyIsPresent(section.getDisplayName());
		Assert.assertTrue(result,"section was not found in the table!");
		logger.info("TEST FINISHED: add to Site new section menu item ");
		// put new created section to the session.
		getTestSession().put(TEST_SECTION_FILE_CTYPE, section);	
		logger.info("$$$$$ FINISHED: addSectionTest.");
		
	}
	/**
	 * Case-info: add new section menu item to the  Site .
	 */
	@Test(dependsOnMethods ="allowSectionPageTypeTest",description ="add new ordered section menu item to the  Site ")
	public void addOrderedSectionTest()
	{
		logger.info("#### STARTED: add new ordered section menu item to the  Site ");
		Site site = (Site)getTestSession().get(TEST_SITE);			
		SectionMenuItem section = new SectionMenuItem();
		section.setDisplayName("ordered");
		section.setShowInMenu(true);
		section.setMenuName("ordered");
		section.setOrdered(true);
		section.setSiteName(site.getDispalyName());
		List<String> list = new ArrayList<>();
		list.add("File");
		section.setAvailableContentTypes(list );
		//1. try to add a new section to Site:
		SiteMenuItemsTablePage siteItems = siteService.addSection(getTestSession(), site.getDispalyName(), section );
		//2. verify: section present
		boolean result = siteItems.verifyIsPresent(section.getDisplayName());
		Assert.assertTrue(result,"section was not found in the table!");
		logger.info("TEST FINISHED: add to Site new section menu item ");
		// put new created section to the session.
		getTestSession().put(TEST_ORDEREDSECTION_FILE_CTYPE, section);	
		logger.info("$$$$$ FINISHED: addOrderedSectionTest. #########");
		
	}
	/**
	 * Case-info: Publish  to ordered section in admin, verify: 
	 * <br>Content visible as published in section view
	 * <br>correct ordering  is maintained after save
	 */
	@Test(dependsOnMethods ="addOrderedSectionTest",description = "publish 2 file to ordered section")
	public void publishContentToOrderedSection()
	{
		
		logger.info("##### STARTED :publish two files to ordered section ");
		Content<FileContentInfo> content = (Content<FileContentInfo>)getTestSession().get(FILE_CONTENT);
		ContentRepository repository = findRepositoryByHandler( content.getContentHandler().toString());
		
		Content<FileContentInfo> sectionContent = content.cloneContent();
		sectionContent.setDisplayName(SECTION_CONTENT_PUBLISH_NAME);
		Content<FileContentInfo> sectionContent2 = content.cloneContent();
		sectionContent2.setDisplayName("content2.jpg");
		
		//1. preparation: create new category and add content to the category
		List<Content<FileContentInfo>> contents = new  ArrayList<>();
		contents.add(sectionContent);
		contents.add(sectionContent2);
		addCategoryAndContent(contents, repository,TEST_PUBLISH_FILE_CATEGORY_NAME);
		//2. get section from, that was created in previous tests: 'addSectionTest'
		SectionMenuItem section = (SectionMenuItem)getTestSession().get(TEST_ORDEREDSECTION_FILE_CTYPE);	
		
		//3. open category and press 'approve and publish'-button, (publish content to the section)
		contentService.doPublishContentToSection(getTestSession(), sectionContent, section);
		boolean isContentPublished =  siteService.isContentFromSectionPublished(getTestSession(), section,sectionContent.getDisplayName());
		Assert.assertTrue(isContentPublished, "content publishing failed, content not present in the section or content is not published!");
		
		contentService.doPublishContentToSectionAnMoveToEnd(getTestSession(), sectionContent2, section);
		List<String> actualContentNames = siteService.getContentNamesFromSection(getTestSession(), section);
		boolean isOrsered = verifyOrder(actualContentNames, sectionContent2.getDisplayName());
		
		Assert.assertTrue(isOrsered,"wrong order in the section page");
		logger.info("$$$$ FINISHED: publishContentToOrderedSection #########");
	}
	/**
	 * Verifies: list is ordered.
	 * @param orderedContent
	 * @param expectedLastContentName
	 * @return
	 */
	private boolean verifyOrder(List<String> actualContentNames,String expectedContentName)
	{
		// contentName was moved down, when it published, so this content should in the second row.
		return actualContentNames.get(1).equals(expectedContentName);
	}
	/**
	 * Case-info: Publish  to section in admin, verify: 
	 * <br>Content visible as published in section view
	 */
	@Test(dependsOnMethods ="addSectionTest",description = "publish content to section")
	public void publishContentToSection()
	{
		logger.info("#### STARTED :publish content to section ");
		Content<FileContentInfo> content = (Content<FileContentInfo>)getTestSession().get(FILE_CONTENT);
		ContentRepository repository = findRepositoryByHandler( content.getContentHandler().toString());
		
		Content<FileContentInfo> sectionContent = content.cloneContent();
		sectionContent.setDisplayName(SECTION_CONTENT_PUBLISH_NAME);
		List<Content<FileContentInfo>> contents = new  ArrayList<>();
		contents.add(sectionContent);
		//1. preparation: create new category and add content to the category
		addCategoryAndContent(contents, repository,TEST_PUBLISH_FILE_CATEGORY_NAME);
		//2. get section from, that was created in previous tests: 'addSectionTest'
		SectionMenuItem section = (SectionMenuItem)getTestSession().get(TEST_SECTION_FILE_CTYPE);	
		
		//3. open category and press 'approve and publish'-button, (publish content to the section)
		contentService.doPublishContentToSection(getTestSession(), sectionContent, section);
		boolean isContentPublished =  siteService.isContentFromSectionPublished(getTestSession(), section,sectionContent.getDisplayName());
		Assert.assertTrue(isContentPublished, "content publishing failed, content not present in the section or content is not published!");
		logger.info("$$$$$ FINISHED: publishContentToSection #########");
	}
	/**
	 * Case info:
	 * 
	 * Add content to section in admin, verify:
	 * <br> -content visible in section view. 	
	 */
	@Test(dependsOnMethods ="addSectionTest",description = "add content to section, verify: content visible in section view")
	public void addContentToSectionTest()
	{
		logger.info("#### STARTED :add content to section, verify: content visible in section view");
		SectionMenuItem section = (SectionMenuItem)getTestSession().get(TEST_SECTION_FILE_CTYPE);
		
		Content<FileContentInfo> content = (Content<FileContentInfo>)getTestSession().get(FILE_CONTENT);
		ContentRepository repository = findRepositoryByHandler( content.getContentHandler().toString());
		
		Content<FileContentInfo> sectionContent = content.cloneContent();
		sectionContent.setDisplayName(SECTION_CONTENT_ADD_NAME);
		List<Content<FileContentInfo>> contents = new  ArrayList<>();
		contents.add(sectionContent);
		//1. preparation: create new  category and add content to the category
		addCategoryAndContent(contents, repository,TEST_FILE_CATEGORY_NAME);
		//2.verify, that content visible in section view and try to add content to section:
		SectionContentsTablePage table = siteService.addContentToSection(getTestSession(), section, sectionContent);
		//3. verify: content present in the section:
		boolean result = table.verifyIsPresent(sectionContent.getDisplayName());
		Assert.assertTrue(result,"content was not added to Section!");
		logger.info("$$$$$ FINISHED:addContentToSectionTest#########");
		
	}
	
	/**
	 * Case info:
	 * Delete site in admin , verify:
	 * <br> -site present in the table. 	
	 */
	@Test(description="delete site test. Find site in admin console and delete")
	public void createAnddeleteSiteTest()
	{
		logger.info("#### STARTED :delete site test. Find site in admin console and delete");
		Site site =  new Site();
		site.setDispalyName("siteTodelete");
		AllowedPageTypes[] allowedPageTypes = { AllowedPageTypes.SECTION, AllowedPageTypes.LABEL, AllowedPageTypes.URL };
		site.setLanguage("English");
		site.setAllowedPageTypes(allowedPageTypes );
		SitesTableFrame sitesPage = siteService.createSite(getTestSession(), site );
		boolean result = sitesPage.verifyIsPresent(site.getDispalyName());
		Assert.assertTrue(result,"site was not created: "+ site.getDispalyName());
		
		siteService.deleteSite(getTestSession(), site.getDispalyName());
		result = sitesPage.verifyIsPresent(site.getDispalyName());
		Assert.assertFalse(result,"site was not deleted: "+ site.getDispalyName());
		logger.info("$$$$ FINISHED: deleteSiteTest #########");
		
	}
	/**
	 * Case info:
	 * 
	 * Remove content from section, verify:
	 * <br> -content not present in section view. 	
	 */
	@Test(dependsOnMethods ="addContentToSectionTest",description = "Remove content from section, verify: content not present in section view")
	public void removeFromSectionTest()
	{
		logger.info("##### STARTED :add content to section, verify: content visible in section view");
		SectionMenuItem section = (SectionMenuItem)getTestSession().get(TEST_SECTION_FILE_CTYPE);
		Site site = (Site)getTestSession().get(TEST_SITE);
		SectionContentsTablePage table = siteService.removeContentFromSection(getTestSession(), site.getDispalyName(), section.getDisplayName(), SECTION_CONTENT_ADD_NAME);
		boolean result = table.verifyIsPresent(SECTION_CONTENT_ADD_NAME);
		Assert.assertFalse(result, "content was not removed from section, contentname:" +SECTION_CONTENT_ADD_NAME);
		logger.info("$$$$ FINISHED: Remove content from section");
	}
	
	
	/**
	 * Creates preconditions for addContentToSectionTest.
	 * @param sectionContent
	 * @param repository
	 */
	private void addCategoryAndContent(List<Content<FileContentInfo>>sectionContents, ContentRepository repository, String categoryName)
	{		
		ContentCategory newcategory = new ContentCategory();
		newcategory.setName(categoryName);
		newcategory.setContentTypeName("File");
		newcategory.setDescription("Files category.");
		String[] categoryPathName = { repository.getName() };
		String[] contentPathName = { repository.getName(), newcategory.getName() };
		newcategory.setParentNames(categoryPathName);
		
		// add category with content type === "File"
		repositoryService.addCategory(getTestSession(), newcategory);
		for(Content<FileContentInfo> content: sectionContents)
		{
			content.setParentNames(contentPathName);
			contentService.addContent(getTestSession(), repository, content);
		}
		
	}
	
	/**
	 * Finds category in the test-data
	 * 
	 * @param ctypeName
	 * @param categories
	 * @return
	 */
	private ContentCategory extractCategoryFromTestData(String ctypeName, List<ContentCategory> categories)
	{
		for (ContentCategory cc : categories)
		{
			if (cc.getContentTypeName().equals(ctypeName))
			{
				return cc;
			}
		}
		return null;
	}

	private ContentRepository findRepositoryByHandler( String expectedHandler)
	{
		List<ContentRepository> repositoryList = (List) getTestSession().get(REPOSITORY_LIST);
		for (ContentRepository repo : repositoryList)
		{
			if (repo.getTopCategory() == null)
			{
				continue;
			}
			if (repo.getTopCategory().getContentType().getContentHandler().toString().equals(expectedHandler))
			{
				return repo;
			}

		}

		return null;
	}

	private ContentRepository findRepositoryByName(String expectedName)
	{
		List<ContentRepository> repositoryList = (List) getTestSession().get(REPOSITORY_LIST);
		for (ContentRepository repo : repositoryList)
		{
			if (repo.getName().contains(expectedName))
			{
				return repo;
			}

		}

		return null;
	}

}
