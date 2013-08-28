package com.enonic.autotests.contentimport;

import java.io.InputStream;
import java.util.List;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.BaseTest;
import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.model.ContentHandler;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.pages.v4.adminconsole.content.ContentStatus;
import com.enonic.autotests.pages.v4.adminconsole.content.ContentsTableFrame;
import com.enonic.autotests.pages.v4.adminconsole.contenttype.ContentTypesFrame;
import com.enonic.autotests.services.ContentService;
import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.services.RepositoryService;
import com.enonic.autotests.testdata.contenttype.ContentConvertor;
import com.enonic.autotests.utils.TestUtils;

public class ImportContentUpdateStrategyTest extends BaseTest
{
	private ContentTypeService contentTypeService = new ContentTypeService();
	private RepositoryService repositoryService = new RepositoryService();
	private ContentService contentService = new ContentService();
	
	private final String PERSON_UPDATE_APPROVE_CFG = "test-data/contenttype/person-strategy-update-approve.xml";
	private final String PERSON_UPDATE_ARCHIVE_CFG = "test-data/contenttype/person-strategy-update-archive.xml";
	private final String PERSON_UPDATE_DRAFT_CFG = "test-data/contenttype/person-strategy-update-draft.xml";
	private final String PERSON_UPDATE_KEEP_STATUS_CFG = "test-data/contenttype/person-strategy-update-keep-status.xml";
	
	private final String IMPORT_PERSONS_XML_FILE = "test-data/contentimport/persons.xml";
	private final String IMPORT_UPDATE_PERSONS_XML_FILE = "test-data/contentimport/persons-for-approve.xml";
	
	/** key for saving a Content Type  in the Session */
	private String PERSON_CTYPE_KEY = "person-ctype";
	/** key for saving a Content Type  in the Session */
	private String CATEGORY_STARTEGY_TEST_KEY = "category_startegy";

	private void createContentTypes()
	{
		ContentType personType = new ContentType();
		String contentTypeName = "update-strategy" +Math.abs(new Random().nextInt());
		personType.setName(contentTypeName);
		personType.setContentHandler(ContentHandler.CUSTOM_CONTENT);
		personType.setDescription("person content type");
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(PERSON_UPDATE_APPROVE_CFG);
		String personCFG = TestUtils.getInstance().readConfiguration(in);
		personType.setConfiguration(personCFG);
		ContentTypesFrame frame = contentTypeService.createContentType(getTestSession(), personType);
		boolean isCreated = frame.verifyIsPresent(contentTypeName);
		if (!isCreated)
		{
			Assert.fail("error during creation of a content type!");
		}
        getTestSession().put(PERSON_CTYPE_KEY, personType);
		logger.info("New content type  was created name: " + contentTypeName);

	}

	@Test(description = "set up: create content types: Person")
	public void settings()
	{
		logger.info("checks for the existance  of Content type, creates new content type if it does not exist");
		// 1.create content types
		createContentTypes();
		// 2. create content repositories:
		ContentRepository repository = new ContentRepository();
		repository.setName("update-strategy" + Math.abs(new Random().nextInt()));
		repositoryService.createContentRepository(getTestSession(), repository);

		// create category for 'Events Block group'
		ContentCategory cat = new ContentCategory();
		cat.setContentTypeName(((ContentType)getTestSession().get(PERSON_CTYPE_KEY)).getName());
		cat.setName("strategyTest");
		String[] parentNames2 = { repository.getName() };
		cat.setParentNames(parentNames2);
		getTestSession().put(CATEGORY_STARTEGY_TEST_KEY, cat);
		repositoryService.addCategory(getTestSession(), cat);
		logger.info("FINISHED: set up settings");

	}
	
	@Test(description = "UPDATE-AND-APPROVE-CONTENT strategy used",dependsOnMethods="settings")
	public void updateAndApproveContentTest()
	{
		logger.info("case-info:Update content, UPDATE-AND-APPROVE-CONTENT strategy used.  ");
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(CATEGORY_STARTEGY_TEST_KEY);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };

		// 1. import XML formatted resource: sync='person-no', two persons should be imported.
		ContentsTableFrame table = contentService.doImportContent(getTestSession(), "person-import-xml", IMPORT_PERSONS_XML_FILE, pathToCategory);
		//2. import and update content- change both persons.
		table = contentService.doImportContent(getTestSession(), "person-import-xml", IMPORT_UPDATE_PERSONS_XML_FILE, pathToCategory);
		//3. verify that APPROVED status for both persons.
		List<String> namesActual = table.getContentNames();
		for(String name: namesActual)
		{
			ContentStatus status = table.getContentStatus(name);
			Assert.assertTrue(status.equals(ContentStatus.APPROVED), "the actual status and expected are not equals!");
		}
	  
	}
	
	@Test(description = "UPDATE-AND-ARCHIVE-CONTENT strategy used",dependsOnMethods="updateAndApproveContentTest")
	public void updateAndArchiveContentTest()
	{
		logger.info("case-info:Update content, UPDATE-AND-ARCHIVE-CONTENT strategy used.  ");
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(CATEGORY_STARTEGY_TEST_KEY);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };

		//1. change a strategy in the 'Content Type' 
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(PERSON_UPDATE_ARCHIVE_CFG);
		String archiveStrategyCFG = TestUtils.getInstance().readConfiguration(in);
		// 1. change content type's configuration: add purge="true" for EVENTS
		contentTypeService.editContentType(getTestSession(), getContentTypeName(), archiveStrategyCFG);
		logger.info("Content type: " + getContentTypeName() + "Was edited. Strategy changed to UPDATE-AND-ARCHIVE-CONTENT");
		//2. import and update content- change both persons, therefore status should be changed to ARCHIVED:
		ContentsTableFrame table = contentService.doImportContent(getTestSession(), "person-import-xml", IMPORT_PERSONS_XML_FILE, pathToCategory);
		
	
		//3. verify that APPROVED status for both persons.
		List<String> namesActual = table.getContentNames();
		for(String name: namesActual)
		{
			ContentStatus status = table.getContentStatus(name);
			Assert.assertTrue(status.equals(ContentStatus.ARCHIVED), "the actual status and expected are not equals!");
		}
	  
	}
	
	@Test(description = "UPDATE-CONTENT-DRAFT strategy used",dependsOnMethods="updateAndArchiveContentTest")
	public void updateAndDraftContentTest()
	{
		logger.info("case-info:Update content, UPDATE-AND-ARCHIVE-CONTENT strategy used.  ");
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(CATEGORY_STARTEGY_TEST_KEY);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };

		//1. change a strategy in the 'Content Type' 
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(PERSON_UPDATE_DRAFT_CFG);
		String archiveStrategyCFG = TestUtils.getInstance().readConfiguration(in);
		// 1. change content type's configuration: add purge="true" for EVENTS
		contentTypeService.editContentType(getTestSession(), getContentTypeName(), archiveStrategyCFG);
		logger.info("Content type: " + getContentTypeName() + "Was edited. Strategy changed to UPDATE-CONTENT-DRAFT");
		//2. import and update content- change both persons, therefore status should be changed to ARCHIVED:
		ContentsTableFrame table = contentService.doImportContent(getTestSession(), "person-import-xml", IMPORT_UPDATE_PERSONS_XML_FILE, pathToCategory);
		
	
		//3. verify that APPROVED status for both persons.
		List<String> namesActual = table.getContentNames();
		for(String name: namesActual)
		{
			ContentStatus status = table.getContentStatus(name);
			Assert.assertTrue(status.equals(ContentStatus.DRAFT), "the actual status and expected are not equals!");
		}
	  
	}
	
	@Test(description = "UPDATE-CONTENT-KEEP-STATUS strategy used",dependsOnMethods="updateAndDraftContentTest")
	public void updateAndKeepStatusTest()
	{
		logger.info("case-info:Update content,UPDATE-CONTENT-KEEP-STATUS strategy used.  ");
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(CATEGORY_STARTEGY_TEST_KEY);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };

		//1. change a strategy in the 'Content Type' 
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(PERSON_UPDATE_KEEP_STATUS_CFG);
		String keepStatusCFG = TestUtils.getInstance().readConfiguration(in);
		// 1. change content type's configuration: add purge="true" for EVENTS
		contentTypeService.editContentType(getTestSession(), getContentTypeName(), keepStatusCFG);
		logger.info("Content type: " + getContentTypeName() + "Was edited. Strategy changed to UPDATE-CONTENT-KEEP-STATUS");
		//2. import and update content- change both persons, therefore status should be changed to ARCHIVED:
		ContentsTableFrame table = contentService.doImportContent(getTestSession(), "person-import-xml", IMPORT_PERSONS_XML_FILE, pathToCategory);
		
	
		//3. verify that DRAFT status for both persons, because KEEP STATUS that was received in previous test
		List<String> namesActual = table.getContentNames();
		for(String name: namesActual)
		{
			ContentStatus status = table.getContentStatus(name);
			Assert.assertTrue(status.equals(ContentStatus.DRAFT), "the actual status and expected are not equals!");
		}
	  
	}
	
	private String getContentTypeName()
	{
		ContentType ct = (ContentType)getTestSession().get(PERSON_CTYPE_KEY);
		return ct.getName();
	}

}
