package com.enonic.autotests.contentindex;

import java.util.List;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.model.ContentHandler;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.FileContentInfo;
import com.enonic.autotests.model.ImageContentInfo;
import com.enonic.autotests.model.site.Site;
import com.enonic.autotests.model.site.Site.AllowedPageTypes;
import com.enonic.autotests.pages.adminconsole.content.AbstractContentTableView;
import com.enonic.autotests.pages.adminconsole.content.ContentsTableFrame;
import com.enonic.autotests.pages.adminconsole.content.RepositoriesListFrame;
import com.enonic.autotests.pages.adminconsole.site.SitesTableFrame;

public class ContentIndexPart1
    extends BaseContentIndexTest
{

    private final String IMAGE_CONTENT = "image_content";

    private final String FILE_CONTENT_KEY = "file_content";

 
    private final String EDITTEST_CONTENT_NAME = "edited.jpg";

    private final String TEST_FILE_CATEGORY_NAME = "filesCategory";
    private final String IMAGE_CATEGORY_NAME = "imagesCategory";

    
    
    private final String TEST_REPO_KEY = "testrepo";
    private final String FILES_CATEGORY_KEY = "files_cat";
    private final String IMAGES_CATEGORY_KEY = "images_cat";
    
    private final String PATH_TO_FILE = "test-data/contentrepository/test.jpg";
    private final String PATH_TO_IMAGE = "test-data/contentrepository/test.jpg";

    @Test(description = "set up: create content types: Image and File")
    public void settingsForContentRepository()
    {
        doSettings();
    }
    
    
    @Test(description = "create a repository",dependsOnMethods  ="settingsForContentRepository")
    public void createRepositoryHaveNoContentType()
    {
    	ContentRepository repository = new ContentRepository();
		String repoName = "testrepo" + Math.abs(new Random().nextInt());
		repository.setName(repoName);
		RepositoriesListFrame repositoriesListFrame = repositoryService.createContentRepository(getTestSession(), repository);
		
		boolean isCreated = repositoriesListFrame.verifyIsRepositoryPresentedInTable(repoName );
	    Assert.assertTrue( isCreated, "new created repository was not found in the table!" );
	        
		logger.info("ContentRepository : " + repoName + " was created!");
		getTestSession().put(TEST_REPO_KEY, repository);
    }


    @Test(description = "add category(Content Type == 'Files') to the repository, parent category have no content type.",
          dependsOnMethods = "createRepositoryHaveNoContentType")
    public void testAddFileCategoryToRepository()
    {
    	ContentRepository repository = (ContentRepository)getTestSession().get(TEST_REPO_KEY );
        logger.info(
            "#### STARTED: add category with 'Files' type  to the repository, top category have no content type.  Repository name is  " +
            		repository.getName() );
        

        //1. build new category with content type === "File"
        String[] parents = {repository.getName()};
        ContentCategory newcategory = ContentCategory.with().name(TEST_FILE_CATEGORY_NAME).contentTypeName("File" ).parentNames(parents).description( "Files category.").build();

        //2. add category with content type === "File"
        repositoryService.addCategory( getTestSession(), newcategory );
        //3.verify: category created
        boolean isCreated = repositoryService.isCategoryPresent( getTestSession(), TEST_FILE_CATEGORY_NAME, parents );
        Assert.assertTrue( isCreated, "new added category was not found!" + newcategory.getName() );
        getTestSession().put(FILES_CATEGORY_KEY, newcategory);
        logger.info( "$$$$ FINISHED: test Add Category With 'Files' handler To Repository " );

    }
    
    @Test(description = "add category(Content Type == 'Images') to the repository, parent category have no content type.",
            dependsOnMethods = "createRepositoryHaveNoContentType")
      public void testAddImagesCategoryToRepository()
      {
    	ContentRepository repository = (ContentRepository)getTestSession().get(TEST_REPO_KEY );
          logger.info(
              "#### STARTED: add category with 'Files' type  to the repository, top category have no content type.  Repository name is  " +
            		  repository.getName() );
          

          //1. build new category with content type === "Image"
          String[] parents = {repository.getName()};
          ContentCategory newcategory = ContentCategory.with().name(IMAGE_CATEGORY_NAME).contentTypeName("Image" ).parentNames(parents).description( "Images category.").build();

          //2. add category with content type === "Image"
          repositoryService.addCategory( getTestSession(), newcategory );
          //3.verify: category created
          boolean isCreated = repositoryService.isCategoryPresent( getTestSession(), IMAGE_CATEGORY_NAME, parents );
          Assert.assertTrue( isCreated, "new added category was not found!" + newcategory.getName() );
          getTestSession().put(IMAGES_CATEGORY_KEY, newcategory);
          logger.info( "$$$$ FINISHED: test Add Category with 'Images' handler To Repository " );

      }
    /**
     * Case info:
     * <br>add File content to the category, verify:
     * <br>Content visible in category view
     */
    @Test(dependsOnMethods = "testAddFileCategoryToRepository")
    public void addFileContentToCategory()
    {
    	ContentCategory filescategory = (ContentCategory)getTestSession().get(FILES_CATEGORY_KEY);
    	
    	Content<FileContentInfo> content = new Content<>();
		String[] parents = new String[] { filescategory.getParentNames()[0], filescategory.getName() };
		content.setParentNames(parents);
		FileContentInfo contentTab = new FileContentInfo();
		contentTab.setPathToFile(PATH_TO_FILE);
		contentTab.setDescription("file content");
		content.setContentTab(contentTab);
		content.setDisplayName("file.jpg");
		content.setContentHandler(ContentHandler.FILES);
		AbstractContentTableView frame = contentService.addFileContent(getTestSession(), content);
    	boolean result = frame.isContentPresentInTable( content.getDisplayName() );
    	Assert.assertTrue(result, "Content with name" + content.getDisplayName() +" was not added!");
    	getTestSession().put( FILE_CONTENT_KEY, content );
    	logger.info( "$$$$ FINISHED: tests 'add File Content To a Category' " );
    	
    }
    
    /**
     * Case info:
     * <br>add Image content to the category, verify:
     * <br>Content visible in category view
     */
    @Test(dependsOnMethods = "testAddImagesCategoryToRepository")
    public void addImageContentToCategory()
    {
    	ContentCategory filescategory = (ContentCategory)getTestSession().get(IMAGES_CATEGORY_KEY);
    	
    	Content<ImageContentInfo> content = new Content<>();
		String[] parents = new String[] { filescategory.getParentNames()[0], filescategory.getName() };
		content.setParentNames(parents);
		ImageContentInfo contentTab = new ImageContentInfo();
		contentTab.setPathToFile(PATH_TO_IMAGE);
		contentTab.setDescription("image content");
		content.setContentTab(contentTab);
		content.setDisplayName("image.jpg");
		content.setContentHandler(ContentHandler.IMAGES);
		AbstractContentTableView frame = contentService.addImageContent(getTestSession(), content);
    	boolean result = frame.isContentPresentInTable( content.getDisplayName() );
    	Assert.assertTrue(result, "Content with name" + content.getDisplayName() +" was not added!");
    	getTestSession().put( IMAGE_CONTENT, content );
    	logger.info( "$$$$ FINISHED: tests 'add Image Content to a Category' " );
    	
    }


   
  
    /**
     * Case info:
     * <p/>
     * Create content in admin, verify:
     * <br>Content searchable
     */
    @Test(dependsOnMethods = "addImageContentToCategory", description = "Create content in admin, verify: Content searchable")
    public void searchContentTest()
    {
        logger.info( "### STARTED: create content and verify: Content  searchable " );
        Content<?> content = (Content<?>) getTestSession().get( IMAGE_CONTENT );
        // 1. Type the display name of content and press the 'Search' button
        List<String> contentNames = contentService.doSearchContentByName( getTestSession(), content.getDisplayName() );
        // 2. verify Content searchable, and content present in the table :
        boolean result = contentNames.contains( content.getDisplayName() );
        logger.info( "case-info: Content searchable: " + result );
        Assert.assertTrue( result, "new added content was not found, search content does not work" );
        logger.info( "$$$$ FINISHED: searchContentTest " );
    }

    /**
     * Case info:
     * <p/>
     * Update content in admin, verify
     * <br> Content visible in category view
     * <br> Content (new values) searchable
     */
    @Test(description = "change content's name and search ", dependsOnMethods = "searchContentTest")
    public void updateContentNameAndSearchTest()
    {
        logger.info(
            "##### STARTED: update content in admin and checkout:Content visible in category view,Content (new values) searchable " );
        Content<?> content = (Content<?>) getTestSession().get( IMAGE_CONTENT );
        // 1. change name of content.
        contentService.editContent( getTestSession(), content, EDITTEST_CONTENT_NAME );
        // 2.search content with new name:
        List<String> contentNames = contentService.doSearchContentByName( getTestSession(), content.getDisplayName() );
        // 3. verify Content searchable, and content present in the table :
        boolean result = contentNames.contains( EDITTEST_CONTENT_NAME );
        logger.info( "case-info: Content searchable: " + result );
        Assert.assertTrue( result, "new added content was not found, search content does not work" );
        logger.info( "$$$$$$ FINISHED: updateContentNameAndSearchTest " );
    }

    /**
     * Case info:
     * <p/>
     * Delete (both one and multiple (bulk)) content in admin, verify
     * <br>-Content not present in category view
     * <br>-Content not searchable
     */
    @Test(description = "delete content and verify it in the table", dependsOnMethods = "updateContentNameAndSearchTest")
    public void deleteContentFromCategory()
    {
        logger.info( "#### STARTED :Delete  content in admin, verify -Content not present in category view " );
        Content<?> content = (Content<?>) getTestSession().get( IMAGE_CONTENT );
        content.setDisplayName( EDITTEST_CONTENT_NAME );
        ContentsTableFrame contentTableFrame = contentService.deleteContentfromCategory( getTestSession(), content );
        boolean isPresent = contentTableFrame.isContentPresentInTable( content.getDisplayName() );
        Assert.assertFalse( isPresent, String.format( "content with name %s should be deleted ", content.getDisplayName() ) );
        logger.info( "$$$$$ FINISHED: delete Content From a Category " );

    }

    /**
     * Case info:
     * <p/>
     * Move (both one and multiple (bulk)) content in admin, verify:
     * <br>-Content visible in new category view
     * <br>-Content searchable in new category
     */
    @Test(dependsOnMethods = "addFileContentToCategory",
          description = "Move (both one and multiple (bulk)) content in admin, verify Content visible in new category view")
    public void moveContentTest()
    {
        logger.info( "#### STRATED: Move  content in admin, verify Content visible in new category view" );
        ContentRepository repository = (ContentRepository)getTestSession().get(TEST_REPO_KEY );
        String[] parents = {repository.getName()};
        ContentCategory newcategory = ContentCategory.with().name("movetest").contentTypeName("File" ).parentNames(parents).description( "Files category.").build();
        repositoryService.addCategory(getTestSession(), newcategory);
        
        Content<?> content = (Content<?>) getTestSession().get( FILE_CONTENT_KEY );
        String[] newParents = new String[] {repository.getName(), newcategory.getName()};
        ContentsTableFrame table = contentService.moveContent( getTestSession(), content, newParents );
        boolean result = table.isContentPresentInTable( content.getDisplayName() );
        Assert.assertFalse( result, "content was not moved! content name" + content.getDisplayName() );

        table = contentService.openCategory( getTestSession(), repository.getName(), "movetest" );
        result = table.isContentPresentInTable( content.getDisplayName() );
        Assert.assertTrue( result, "content not present in the destination folder! content name" + content.getDisplayName() );
        logger.info( "$$$$$ FINISHED: move Content Test " );
    }

 
    /**
     * Case info:
     * <p/>
     * Empty all content from category. Open category, and delete by pushing "Delete"
     */
    @Test(description = "Empty all content from category. Open category, and delete by pushing 'Delete'",
          dependsOnMethods = "createRepositoryHaveNoContentType")
    public void deleteAllContentAndDeleteCategory()
    {
        logger.info( "#### STARTED: Empty all content from category. Open category, and delete by pushing 'Delete'" );
        ContentRepository repository = (ContentRepository)getTestSession().get(TEST_REPO_KEY );
        ContentCategory catToDelete = new ContentCategory();
        catToDelete.setName( "categoryTodelete" );
        catToDelete.setContentTypeName( "File" );
        catToDelete.setDescription( "Files category. This category will be delete" );
        String[] pathName = {repository.getName()};
        catToDelete.setParentNames( pathName );

        //2. add category with content type === "File"
        repositoryService.addCategory( getTestSession(), catToDelete );
        
        Content<FileContentInfo> contentToDelete = new Content<>();
		String[] parents = new String[] { catToDelete.getParentNames()[0], catToDelete.getName() };
		contentToDelete.setParentNames(parents);
		FileContentInfo contentTab = new FileContentInfo();
		contentTab.setPathToFile(PATH_TO_FILE);
		contentTab.setDescription("file content");
		contentToDelete.setContentTab(contentTab);
		contentToDelete.setDisplayName("file.jpg");
		contentToDelete.setContentHandler(ContentHandler.FILES);
        
        contentService.addFileContent(getTestSession(), contentToDelete);
        //3.verify: category created
        boolean isCreated = repositoryService.isCategoryPresent( getTestSession(), catToDelete.getName(), pathName );
        Assert.assertTrue( isCreated, "new added category was not found!" + catToDelete.getName() );

        ContentsTableFrame table = contentService.openCategory( getTestSession(), repository.getName(), catToDelete.getName() );
        table.doDeleteAllContent();
        table.doDeleteEmptyCategory();
        boolean ispresent = repositoryService.isCategoryPresent( getTestSession(), catToDelete.getName(), pathName );
        Assert.assertFalse( ispresent, "new added category was not found!" + catToDelete.getName() );
        logger.info( "$$$$$FINISHED: deleteAllContentAndDeleteCategory " );
    }


  
    /**
     * Case info:
     * Delete site in admin , verify:
     * <br> -site present in the table.
     */
    @Test(description = "delete site test. Find site in admin console and delete")
    public void createAnddeleteSiteTest()
    {
        logger.info( "#### STARTED :delete site test. Find site in admin console and delete" );
        Site site = new Site();
        site.setDispalyName( "siteTodelete" );
        AllowedPageTypes[] allowedPageTypes = {AllowedPageTypes.SECTION, AllowedPageTypes.LABEL, AllowedPageTypes.URL};
        site.setLanguage( "English" );
        site.setAllowedPageTypes( allowedPageTypes );
        SitesTableFrame sitesPage = siteService.createSite( getTestSession(), site );
        boolean result = sitesPage.verifyIsPresent( site.getDispalyName() );
        Assert.assertTrue( result, "site was not created: " + site.getDispalyName() );

        siteService.deleteSite( getTestSession(), site.getDispalyName() );
        result = sitesPage.verifyIsPresent( site.getDispalyName() );
        Assert.assertFalse( result, "site was not deleted: " + site.getDispalyName() );
        logger.info( "$$$$ FINISHED: deleteSiteTest #########" );

    }

}
