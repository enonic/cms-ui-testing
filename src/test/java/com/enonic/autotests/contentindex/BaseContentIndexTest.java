package com.enonic.autotests.contentindex;

import java.util.List;

import com.enonic.autotests.BaseTest;
import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.model.ContentHandler;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.model.FileContentInfo;
import com.enonic.autotests.services.ContentService;
import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.services.RepositoryService;
import com.enonic.autotests.services.SiteService;

public class BaseContentIndexTest extends BaseTest
{
	 protected ContentTypeService contentTypeService = new ContentTypeService();

	 protected RepositoryService repositoryService = new RepositoryService();

	 protected ContentService contentService = new ContentService();
	

	 protected SiteService siteService = new SiteService();
	 
	 public void  doSettings()
	 {
		 logger.info( "### TESTS STARTED:  ContentRepositoryTests" );
	        logger.info( "checks for the existance  of Content type, creates new content type if it does not exist" );
	        ContentType imagesType = new ContentType();
	        imagesType.setName( "Image" );
	        imagesType.setContentHandler( ContentHandler.IMAGES );
	        imagesType.setDescription( "content repository test" );
	        boolean isExist = contentTypeService.findContentType( getTestSession(), "Image" );
	        if ( !isExist )
	        {
	            contentTypeService.createContentType( getTestSession(), imagesType );
	            logger.info( "New content type with 'Images' handler was created" );
	        }

	        ContentType filesType = new ContentType();
	        filesType.setName( "File" );
	        filesType.setContentHandler( ContentHandler.FILES );
	        filesType.setDescription( "content repository test" );
	        isExist = contentTypeService.findContentType( getTestSession(), "File" );
	        if ( !isExist )
	        {
	            contentTypeService.createContentType( getTestSession(), filesType );
	            logger.info( "New content type with 'Files' handler was created" );
	        }

	        logger.info( "### FINISHED: settings Content Index " );
	 }
	 
		/**
		 * Creates preconditions for addContentToSectionTest.
		 * 
		 * @param sectionContents
		 * @param repository
		 */
		public void addCategoryAndContent(List<Content<FileContentInfo>> sectionContents, ContentRepository repository, String categoryName)
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
			for (Content<FileContentInfo> content : sectionContents)
			{
				content.setParentNames(contentPathName);
				contentService.addFileContent(getTestSession(), content);
			}

		}
}
