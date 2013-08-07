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
import com.enonic.autotests.model.Section;
import com.enonic.autotests.model.Site;
import com.enonic.autotests.model.Site.AllowedPageTypes;
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
	private final String EDITTEST_CONTENT_NAME = "edited.gif";
	
	private final String TEST_REPO_NAME =  "notopcategory";
	private final String TEST_FILE_CATEGORY_NAME =  "filesCategory";

	private ContentTypeService contentTypeService = new ContentTypeService();
	private RepositoryService repositoryService = new RepositoryService();
	private ContentService contentService = new ContentService();
	private SiteService siteService = new SiteService();

	@Test(description="set up: create content types: Image and File")
	public  void settings()
	{
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
	}

    @Test(dependsOnMethods = "settings", description = "create new Content Repository with specified Content Type", dataProvider = "createContentRepositoryPositive", dataProviderClass = ContentRepositoryProvider.class)
	public void createRepositoryTest(ContentRepositoryXml contentRepoXML)
	{
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

	}

    @Test(description = "add category(Content Type == 'Files') to the repository, repsitory has no a TopCategory", dependsOnMethods = "createRepositoryTest")
	public void testAddCategoryToRepository()
	{
    	logger.info("case-info: add category with 'Files' type  to the repository, repsitory has no a TopCategory  Repository name is  "+TEST_REPO_NAME);
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
		boolean isCreated = repositoryService.findCategoryByPath(getTestSession(), TEST_FILE_CATEGORY_NAME, pathName);
		Assert.assertTrue(isCreated, "new added category was not found!" + newcategory.getName());

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
		logger.info(xmlContent.getCaseInfo());
		Content<?> content = ContentConvertor.convertXmlDataToContent(xmlContent);
        //1. find repository for test in the testsession :
		ContentRepository repository = findRepositoryByHandler( xmlContent.getContentHandler());
		String ctypeName = repository.getContentTypeName();
		ContentCategory category = extractCategoryFromTestData(ctypeName, repository.getCategories());
		String[] pathName = new String[] { repository.getName() };
		category.setParentNames(pathName);
		// 2. add category to the Content-Reposotry
		repositoryService.addCategory(getTestSession(), category);
		String[] pathToContent = new String[] { repository.getName(), category.getName() };
		content.setParentNames(pathToContent);

		// 3. add content to the category.
		AbstractContentTableView frame = contentService.addContent(getTestSession(), repository, content);
		// 4. verify content is present in the table.(Content visible in the category view)
		boolean result = frame.findContentInTableByName(content.getDisplayName());
		logger.info("case-info: Content visible in category view:" + result);
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
		
		Assert.assertTrue(result, "new added content was not found");

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
		logger.info("case-info: create content and verify: Content  searchable ");
		Content<?> content = (Content<?>) getTestSession().get(IMAGE_CONTENT);
		// 1. Type the display name of content and press the 'Search' button
		List<String> contentNames = contentService.doSearchContentByName(getTestSession(), content.getDisplayName());
		// 2. verify Content searchable, and content present in the table :
		boolean result = contentNames.contains(content.getDisplayName());
		logger.info("case-info: Content searchable: " + result);
		Assert.assertTrue(result, "new added content was not found, search content does not work");
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
    	logger.info("case-info: update content in admin and checkout:Content visible in category view,Content (new values) searchable ");
		Content<?> content = (Content<?>) getTestSession().get(IMAGE_CONTENT);
		// 1. change name of content.
		contentService.editContent(getTestSession(), content, EDITTEST_CONTENT_NAME);
		// 2.search content with new name:
		List<String> contentNames = contentService.doSearchContentByName(getTestSession(), content.getDisplayName());
		// 3. verify Content searchable, and content present in the table :
		boolean result = contentNames.contains(EDITTEST_CONTENT_NAME);
		logger.info("case-info: Content searchable: " + result);
		Assert.assertTrue(result, "new added content was not found, search content does not work");
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
		Content<?> content = (Content<?>) getTestSession().get(IMAGE_CONTENT);
		content.setDisplayName(EDITTEST_CONTENT_NAME);
		ContentsTableFrame contentTableFrame = contentService.deleteContentfromCategory(getTestSession(), content);
		boolean isPresent = contentTableFrame.findContentInTableByName(content.getDisplayName());
		Assert.assertFalse(isPresent, String.format("content with name %s should be deleted ", content.getDisplayName()));

	}
	
	    

	 /**
	  * Case info:
	  * 
	  * Move (both one and multiple (bulk)) content in admin, verify:
	  * <br>-Content visible in new category view
	  * <br>-Content searchable in new category
	  * 
	  **/
	@Test(dependsOnMethods = "addContentToCategoryTest")
	public void moveContentTest()
	{
		Content<?> content = (Content<?>) getTestSession().get(FILE_CONTENT);
		ContentRepository repository = findRepositoryByName(TEST_REPO_NAME);
		ContentsTableFrame table = contentService.moveContent(getTestSession(), content, repository.getName(), TEST_FILE_CATEGORY_NAME);
		boolean result = table.findContentInTableByName(content.getDisplayName());
		Assert.assertFalse(result,"content was not moved! content name"+content.getDisplayName());
		
		table = contentService.openCategory(getTestSession(), repository.getName(),TEST_FILE_CATEGORY_NAME);
		result = table.findContentInTableByName(content.getDisplayName());
		Assert.assertTrue(result,"content not present in the destination folder! content name"+content.getDisplayName());
	}
	
	/**
	 * Case info:
	 * 
	 * Empty all content from category. Open category, and delete by pushing "Delete" 	
	 */
	@Test(description="Empty all content from category. Open category, and delete by pushing 'Delete'",dependsOnMethods = "addContentToCategoryTest")
	public void deleteAllContentAndDeleteCategory()
	{
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
		boolean isCreated = repositoryService.findCategoryByPath(getTestSession(), newcategory.getName(), pathName);
		Assert.assertTrue(isCreated, "new added category was not found!" + newcategory.getName());

		ContentsTableFrame table = contentService.openCategory(getTestSession(), repository.getName(),newcategory.getName());
		table.doDeleteAllContent();
		table.doDeleteEmptyCategory();
		boolean ispresent = repositoryService.findCategoryByPath(getTestSession(), newcategory.getName(), pathName);
		Assert.assertFalse(ispresent, "new added category was not found!" + newcategory.getName());
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
	}
	
	/**
	 * Case-info: Edit site, allow Section page type.
	 */
	@Test(dependsOnMethods ="createNewSiteTest",description = "edit site and allow section page type")
	public void allowSectionPageTypeTest()
	{
		logger.info("Case-info: Edit site, allow Section page type.");
		Site site = (Site)getTestSession().get(TEST_SITE);	
		AllowedPageTypes[] allowedPageTypes = {AllowedPageTypes.SECTION};
		site.setAllowedPageTypes(allowedPageTypes );
		siteService.editSite(getTestSession(), site.getDispalyName(), site);
		logger.info("Edit site-test finished,  Section page type allowed.");
	}
	/**
	 * Case-info: add to Site new section menu item.
	 */
	@Test(dependsOnMethods ="allowSectionPageTypeTest",description ="add to Site new section menu item")
	public void addSectionTest()
	{
		logger.info("case-info: add to Site new section menu item ");
		Site site = (Site)getTestSession().get(TEST_SITE);			
		Section section = new Section();
		section.setDisplayName("section1");
		section.setShowInMenu(true);
		section.setMenuName("section1");
		List<String> list = new ArrayList<>();
		list.add("File");
		section.setAvailableContentTypes(list );
		SiteMenuItemsTablePage siteItems = siteService.addSection(getTestSession(), site.getDispalyName(), section );
		boolean result = siteItems.verifyIsPresent(section.getDisplayName());
		Assert.assertTrue(result,"section was not found in the table!");
		getTestSession().put(TEST_SECTION_FILE_CTYPE, section);	
		
	}
	/**
	 * Case info:
	 * 
	 * Add content to section in admin, verify:
	 * <br> -content visible in section view. 	
	 */
	@Test(dependsOnMethods ="addSectionTest")
	public void addContentToSectionTest()
	{
		Section section = (Section)getTestSession().get(TEST_SECTION_FILE_CTYPE);
		Site site = (Site)getTestSession().get(TEST_SITE);
		
		Content<FileContentInfo> content = (Content<FileContentInfo>)getTestSession().get(FILE_CONTENT);
		ContentRepository repository = findRepositoryByHandler( content.getContentHandler().toString());
		
		Content<FileContentInfo> sectionContent = content.cloneContent();
		sectionContent.setDisplayName("sectioncontent.gif");
		
//		ContentCategory newcategory = new ContentCategory();
//		newcategory.setName(TEST_FILE_CATEGORY_NAME);
//		newcategory.setContentTypeName("File");
//		newcategory.setDescription("Files category.");
//		String[] categoryPathName = { repository.getName() };
//		String[] contentPathName = { repository.getName(), newcategory.getName() };
//		newcategory.setParentNames(categoryPathName);
//		
//		sectionContent.setParentNames(contentPathName);
//		//2. add category with content type === "File"
//		repositoryService.addCategory(getTestSession(), newcategory);
//		contentService.addContent(getTestSession(), repository, sectionContent);
		addCategoryAndContent(sectionContent, repository);
		
		SectionContentsTablePage table = siteService.addContentToSection(getTestSession(), site.getDispalyName(), section, sectionContent);
		boolean result = table.verifyIsPresent(sectionContent.getDisplayName());
		Assert.assertTrue(result,"content was not added to Section!");
		
	}
	
	// ==================================================================================================
	/**
	 * Creates preconditions for addContentToSectionTest.
	 * @param sectionContent
	 * @param repository
	 */
	private void addCategoryAndContent(Content<FileContentInfo>sectionContent,ContentRepository repository)
	{
		
		
		ContentCategory newcategory = new ContentCategory();
		newcategory.setName(TEST_FILE_CATEGORY_NAME);
		newcategory.setContentTypeName("File");
		newcategory.setDescription("Files category.");
		String[] categoryPathName = { repository.getName() };
		String[] contentPathName = { repository.getName(), newcategory.getName() };
		newcategory.setParentNames(categoryPathName);
		sectionContent.setParentNames(contentPathName);
		// add category with content type === "File"
		repositoryService.addCategory(getTestSession(), newcategory);
		contentService.addContent(getTestSession(), repository, sectionContent);
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
