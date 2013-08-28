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
import com.enonic.autotests.services.PageNavigatorV4;
import com.enonic.autotests.services.RepositoryService;
import com.enonic.autotests.testdata.contenttype.ContentConvertor;
import com.enonic.autotests.utils.Person;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.utils.UserEvent;
import com.enonic.autotests.utils.XmlReader;

public class ImportContentWithBlockGroup extends BaseTest
{
	/** configurations of Content Types */
	/** mode=xml; format="dd-MM-yyyy"; Added "block name='Events' " */
	private final String PERSON_EVENTS_CFG = "test-data/contenttype/person-block.xml";
	/** mode =xml; Added purge="true" for EVENTS */
	private final String PERSON_BLOCK_EVENTS_PURGE_CFG = "test-data/contenttype/person-events-purge.xml";
	private final String PERSON_CONTENT_PURGE_ARCHIVE_CFG = "test-data/contenttype/person-content-purge-archive.xml";
	private final String PERSON_CONTENT_PURGE_DELETE_CFG = "test-data/contenttype/person-content-purge-delete.xml";
	
	/** resources to IMPORT */
	private String IMPORT_PERSONS_BLOCK_GROUP_XML_FILE = "test-data/contentimport/persons-block-groups.xml";
	private String IMPORT_PERSONS_BLOCK_GROUP_UPDATE_XML_FILE = "test-data/contentimport/persons-block-groups-update.xml";
	private String IMPORT_PERSONS_BLOCK_GROUP_PURGE_UPDATE_XML_FILE = "test-data/contentimport/persons-block-groups-purge-update.xml";	
	/** delete one person from XML formatted source*/
	private String IMPORT_PERSONS_CONTENT_PURGE_XML_FILE = "test-data/contentimport/person-content-purge.xml";

	private ContentTypeService contentTypeService = new ContentTypeService();
	private RepositoryService repositoryService = new RepositoryService();
	private ContentService contentService = new ContentService();

	/** this content type has configured  "block name='Events' "; and date has the next format format="dd-MM-yyyy" */
	private String PERSON_CTYPE_NAME = "Person-events";
	
	/** key for saving a Category in the Session */
	private String IMPORT_CATEGORY_BLOCK_GROUPS_KEY = "category_event_block_groups";
	/** key for saving a Content Type  in the Session */
	private String PERSON_CTYPE_KEY = "person-events-ctype";
	
	
	private String EVENT_VALUE_PURGE_TEST = "04-07-1895";
	private String PERSON_EVENT_PURGE_TEST = "Fridtjof Nansen";
	private String EVENT_NAME_PURGE_TEST = "North pole almost reached";
	private String CONTENT_PURGE_PERSON_NAME = "Eva Helene Sars";


	private void createContentTypes()
	{
		ContentType personType = new ContentType();
		String contentTypeName = PERSON_CTYPE_NAME +Math.abs(new Random().nextInt());
		personType.setName(contentTypeName);
		personType.setContentHandler(ContentHandler.CUSTOM_CONTENT);
		personType.setDescription("person content type");
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(PERSON_EVENTS_CFG);
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
		repository.setName("personEvents" + Math.abs(new Random().nextInt()));
		repositoryService.createContentRepository(getTestSession(), repository);

		// create category for 'Events Block group'
		ContentCategory catBlock = new ContentCategory();
		catBlock.setContentTypeName(((ContentType)getTestSession().get(PERSON_CTYPE_KEY)).getName());
		catBlock.setName("importPersonEvents");
		String[] parentNames2 = { repository.getName() };
		catBlock.setParentNames(parentNames2);
		getTestSession().put(IMPORT_CATEGORY_BLOCK_GROUPS_KEY, catBlock);
		repositoryService.addCategory(getTestSession(), catBlock);
		logger.info("FINISHED: set up settings");

	}

	@Test(dependsOnMethods = "settings", description = "Importing into block groups,Mapping input fields of type date, format='dd-MM-yyyy' ")
	public void importingIntoBlockGroups()
	{
		logger.info("case-info:Importing into block groups.  Date Format used: format='dd-MM-yyyy'");
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(IMPORT_CATEGORY_BLOCK_GROUPS_KEY);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };

		// 1. import XML formatted resource:
		ContentsTableFrame table = contentService.doImportContent(getTestSession(), "person-import-xml", IMPORT_PERSONS_BLOCK_GROUP_XML_FILE, pathToCategory);
		List<String> namesActual = table.getContentNames();
		XmlReader xmlReader = new XmlReader();
		// 2. gets expected persons with events from the XML
		List<Person> personsFromXML = xmlReader.getPersons(IMPORT_PERSONS_BLOCK_GROUP_XML_FILE);
		// 3. Verify: the number of imported persons:
		Assert.assertTrue(personsFromXML.size() == namesActual.size(), "the number of persons from UI and expected number of persons are not equals!");

		// 4. Open all persons and Verify that all events are present on the page. and Verify:the Order of imported block group entries- The imported
		// block group entries will be ordered as they appear from top to bottom in the XML source.
		for (Person personFromXml : personsFromXML)
		{
			List<UserEvent> uiEvents = ImportUtils.getEventsFromUI(getSessionDriver(), table, personFromXml.getName());
			boolean result = personFromXml.getEvents().equals(uiEvents);
			Assert.assertTrue(result, "Importing into block groups test failed, because expexted events and actual are not equals!");
		}
		logger.info("TEST-FINISHED:'Importing into block groups' ");

	}

	/**
	 * Case-info: Updating block group entries. <br>
	 * Updates content that was imported in the 'importingIntoBlockGroups'
	 * method. <br>
	 * Mapping input fields of type date : <mapping src="4" format="dd.MM.yyyy"
	 * dest="date-of-birth"/>
	 */
	@Test(dependsOnMethods = "importingIntoBlockGroups", description = "Updating block group entries, sync='person-no'.There are a  new person and a new event in the XML formatted source. ")
	public void updatingBlockGroupEntriesTest()
	{
		logger.info("case-info:Updating block group entries, sync='person-no'. There are a  new person and a new event in the XML formatted source. ");
		logger.info("case-info:Mapping input fields of type date");
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(IMPORT_CATEGORY_BLOCK_GROUPS_KEY);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };
		// 1. update content: new event was added for first person and new person added:
		ContentsTableFrame table = contentService.doImportContent(getTestSession(), "person-import-xml", IMPORT_PERSONS_BLOCK_GROUP_UPDATE_XML_FILE, pathToCategory);
		List<String> namesActual = table.getContentNames();
		XmlReader xmlReader = new XmlReader();
		// 2. gets EXPECTED persons with events from the XML
		List<Person> personsFromXML = xmlReader.getPersons(IMPORT_PERSONS_BLOCK_GROUP_UPDATE_XML_FILE);
		boolean isNewPersonAdded = namesActual.size() == personsFromXML.size();
		Assert.assertTrue(isNewPersonAdded,
				"Content-import was performed  and new person should be added, but the number of persons from UI and expected number of persons are not equals!");
		for (Person personFromXML : personsFromXML)
		{
			List<UserEvent> eventsFromUI = ImportUtils.getEventsFromUI(getSessionDriver(), table, personFromXML.getName());
			boolean result = eventsFromUI.equals(personFromXML.getEvents());
			Assert.assertTrue(result, "'Updating block group entries' test failed, because expexted events and actual are not equals!");
		}
		logger.info("TEST-FINISHED:'Importing into block groups' ");
	}

	

	/**
	 * Case-info: Updating block group entries. By default, existing block group entries in a content not existing in the import source, 
	 * are not removed. Removing can be enabled by giving the purge attribute on the block group element the value "true". 
	 * When purge is set to true, any existing block group entries in the content that are not in the import source will be removed.
	 */
	@Test(description = "Block group purge setting. Sets purge to 'true' ", dependsOnMethods = "updatingBlockGroupEntriesTest")
	public void blockPurgeTrueTest()

	{
		logger.info("case-info:Block group purge setting. Sets purge to 'true' ");
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(PERSON_BLOCK_EVENTS_PURGE_CFG);
		String purgeTrueCFG = TestUtils.getInstance().readConfiguration(in);
		// 1. change content type's configuration: add purge="true" for EVENTS
		contentTypeService.editContentType(getTestSession(), getContentTypeName(), purgeTrueCFG);
		logger.info("Content type: " + getContentTypeName() + "Was edited. Removing of block group entries enabled ");

		// GET_EVENTS_BEFORE_ from session:
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(IMPORT_CATEGORY_BLOCK_GROUPS_KEY);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };
		ContentsTableFrame table = (ContentsTableFrame) PageNavigatorV4.openContentsTableView(getTestSession(), pathToCategory);
		// 2. get events before import and update content
		logger.info("try to get all events from UI for Person with name:" + PERSON_EVENT_PURGE_TEST);
		List<UserEvent> eventsBefore = ImportUtils.getEventsFromUI(getSessionDriver(), table, PERSON_EVENT_PURGE_TEST);
		logger.info("there are: " + eventsBefore.size() + " for the  Person with name: " + PERSON_EVENT_PURGE_TEST);

		UserEvent eventsBeforeUpdate = new UserEvent();
		eventsBeforeUpdate.setName(EVENT_NAME_PURGE_TEST);
		eventsBeforeUpdate.setValue(EVENT_VALUE_PURGE_TEST);
		Assert.assertTrue(eventsBefore.contains(eventsBeforeUpdate), "before Importing: event with name:" + EVENT_NAME_PURGE_TEST + "should be present on the page");

		// 3. import and update content: "North pole almost reached" event should be removed for 'Fridtjof Nansen' person
		table = contentService.doImportContent(getTestSession(), "person-import-xml", IMPORT_PERSONS_BLOCK_GROUP_PURGE_UPDATE_XML_FILE, pathToCategory);
		logger.info("import of XML formatted source finished. Content was updated: filename " + IMPORT_PERSONS_BLOCK_GROUP_PURGE_UPDATE_XML_FILE);
		// verify that
		List<UserEvent> eventsAfterImport = ImportUtils.getEventsFromUI(getSessionDriver(), table, PERSON_EVENT_PURGE_TEST);
		Assert.assertFalse(eventsAfterImport.contains(eventsBeforeUpdate), "event with name:" + EVENT_NAME_PURGE_TEST+ " still present on the page, but should be removed!");
		logger.info("TEST-FINISHED:'Block group purge setting' ");

	}
	@Test(description = "Purge Content. Sets purge to 'archive' ", dependsOnMethods = "blockPurgeTrueTest")
	public void contentPurgeArchiveTest()

	{
		logger.info("case-info:Purge Content. Sets purge to 'archive' ");
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(PERSON_CONTENT_PURGE_ARCHIVE_CFG);
		String contentPurgeArchiveCFG = TestUtils.getInstance().readConfiguration(in);
		// 1. change content type's configuration: add purge="true" for EVENTS
		contentTypeService.editContentType(getTestSession(), getContentTypeName(), contentPurgeArchiveCFG);
		logger.info("Content type: " + getContentTypeName() + "Was edited. Purge Content. Purge=='archive' ");

		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(IMPORT_CATEGORY_BLOCK_GROUPS_KEY);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };
		ContentsTableFrame table = (ContentsTableFrame) PageNavigatorV4.openContentsTableView(getTestSession(), pathToCategory);
		// 2. get status before import 
		ContentStatus statusBeforeImport = table.getContentStatus(CONTENT_PURGE_PERSON_NAME);
		Assert.assertTrue(statusBeforeImport.equals(ContentStatus.DRAFT),"expected status and actual are not equals!");

		

		// 3. import and update content: person with name  "Eva Helene Sars" should be archived in category:
		table = contentService.doImportContent(getTestSession(), "person-import-xml", IMPORT_PERSONS_CONTENT_PURGE_XML_FILE, pathToCategory);
		logger.info("import of XML formatted source finished. Content was updated: filename " + IMPORT_PERSONS_CONTENT_PURGE_XML_FILE);
		ContentStatus statusAfterImport = table.getContentStatus(CONTENT_PURGE_PERSON_NAME);
		Assert.assertTrue(statusAfterImport.equals(ContentStatus.ARCHIVED),"expected status and actual are not equals!");
			
		logger.info("TEST-FINISHED: Purge Content. Sets purge to 'archive' ");

	}
	// TODO implement Keep content
	
	@Test(description = "Purge Content. Sets purge to 'delete' ", dependsOnMethods = "contentPurgeArchiveTest")
	public void contentPurgeDeleteTest()

	{
		logger.info("case-info:Purge Content. Sets purge to 'archive' ");
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(PERSON_CONTENT_PURGE_DELETE_CFG);
		String contentPurgeDeleteCFG = TestUtils.getInstance().readConfiguration(in);
		// 1. change content type's configuration: add purge="delete" for persons
		contentTypeService.editContentType(getTestSession(), getContentTypeName(), contentPurgeDeleteCFG);
		logger.info("Content type: " + getContentTypeName() + "Was edited. Purge Content. Purge=='delete' ");

		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(IMPORT_CATEGORY_BLOCK_GROUPS_KEY);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };
		ContentsTableFrame table = (ContentsTableFrame) PageNavigatorV4.openContentsTableView(getTestSession(), pathToCategory);
		// 2. finds person in the table before a import
		boolean isPresentPersonBeforeImport = table.findContentInTableByName(CONTENT_PURGE_PERSON_NAME);
		Assert.assertTrue(isPresentPersonBeforeImport,"the person with name: "+ CONTENT_PURGE_PERSON_NAME + "is present in the table!");	

		// 3. import and update content: person with name  "Eva Helene Sars" should be deleted from a category:
		table = contentService.doImportContent(getTestSession(), "person-import-xml", IMPORT_PERSONS_CONTENT_PURGE_XML_FILE, pathToCategory);
		boolean isPresentPersonAfterImport = table.findContentInTableByName(CONTENT_PURGE_PERSON_NAME);
		Assert.assertFalse(isPresentPersonAfterImport,"the person with name: "+ CONTENT_PURGE_PERSON_NAME + "should be deleted from the table!");		
		logger.info("TEST-FINISHED: Purge Content. Sets purge to 'delete' ");

	}
	private String getContentTypeName()
	{
		ContentType ct = (ContentType)getTestSession().get(PERSON_CTYPE_KEY);
		return ct.getName();
	}
}
