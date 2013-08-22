package com.enonic.autotests;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.seleniumemulation.GetLocation;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.model.ContentHandler;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.ContentRepository.TopCategory;
import com.enonic.autotests.model.ContentType;
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

/**
 * System test for add Content to section-pages.
 * 
 */
public class ContentImportTest extends BaseTest
{
	/** there two modes: mode =xml and mode =csv */
	private final String PERSON_CFG = "test-data/contenttype/person.xml";
	
	/** mode=xml; format="dd-MM-yyyy"; Added "block name='Events' " */
	private final String PERSON_BLOCK_CFG = "test-data/contenttype/person-block.xml";
	
	/** mode=xml; relatedcontenttype='person-related' " */
	private final String PERSON_RELATED_CFG = "test-data/contenttype/person-related-content.xml";
	private final String PERSON_BASE64_CFG = "test-data/contenttype/person-base64.xml";
	
	/** mode =xml; Added purge="true" for EVENTS */
	private final String PERSON_BLOCK_EVENTS_PURGE_CFG = "test-data/contenttype/person-events-purge.xml";
			
	/** this content type has configured block "block name='Events' ";  format="dd-MM-yyyy" */
	private String PERSON_BLOCK_CTYPE_NAME = "Person-block";
	private String PERSON_RELATED_CTYPE_NAME = "person-related";
	
	/** this content type configured for importing in XML and CSV mode */
	private String PERSON_CTYPE_NAME = "Person";
	
	/** this XML file contains a list of persons for importing */
	private String IMPORT_PERSONS_XML = "test-data/contentimport/persons.xml";
	
	/** this CSV file contains a list of persons for importing */
	private String IMPORT_PERSONS_CSV_FILE = "test-data/contentimport/persons.csv";
	
	private String IMPORT_PERSONS_BASE64_FILE = "test-data/contentimport/persons-base64.xml";
	
	private String IMPORT_PERSONS_BLOCK_GROUP_XML_FILE = "test-data/contentimport/persons-block-groups.xml";
	private String IMPORT_PERSONS_BLOCK_GROUP_UPDATE_XML_FILE = "test-data/contentimport/persons-block-groups-update.xml";
	private String IMPORT_PERSONS_BLOCK_GROUP_PURGE_UPDATE_XML_FILE = "test-data/contentimport/persons-block-groups-purge-update.xml";
	private String IMPORT_PERSONS_RELATED_CONTENT_XML_FILE = "test-data/contentimport/persons-related-content.xml";
	

	private ContentTypeService contentTypeService = new ContentTypeService();
	private RepositoryService repositoryService = new RepositoryService();
	private ContentService contentService = new ContentService();
	private String IMPORT_CATEGORY = "category_for_import";
	
	private String IMPORT_CATEGORY_BLOCK_GROUPS = "category_block_groups";
	private String IMPORT_CATEGORY_RELATED_CONTENT = "related_content";
	
	private String EVENT_NAME_PURGE_TEST = "North pole almost reached";
	
	private String EVENT_VALUE_PURGE_TEST = "04-07-1895";
	private String PERSON_EVENT_PURGE_TEST = "Fridtjof Nansen";

	/**
	 * performs preconditions for all tests: 
	 */
	private void createContentTypes()
	{
		ContentType personType = new ContentType();
		personType.setName(PERSON_CTYPE_NAME);
		personType.setContentHandler(ContentHandler.CUSTOM_CONTENT);
		personType.setDescription("person content type");
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(PERSON_CFG);
		String personCFG = TestUtils.getInstance().readConfiguration(in);
		personType.setConfiguration(personCFG);
		boolean isExist = contentTypeService.findContentType(getTestSession(), PERSON_CTYPE_NAME);
		if (!isExist)
		{
			ContentTypesFrame frame = contentTypeService.createContentType(getTestSession(), personType);
			frame.verifyIsPresent(PERSON_CTYPE_NAME);
			logger.info("New content type  was created name: " + PERSON_CTYPE_NAME);
		} else
		{
			contentTypeService.editContentType(getTestSession(), PERSON_CTYPE_NAME, personCFG);
			logger.info("Content type: " + PERSON_CTYPE_NAME + " Was edited. Default configuration's settings were saved");
		}
		ContentType personTypeBlock = new ContentType();
		personTypeBlock.setName(PERSON_BLOCK_CTYPE_NAME);
		personTypeBlock.setContentHandler(ContentHandler.CUSTOM_CONTENT);
		personTypeBlock.setDescription("person content type");
		in = ContentConvertor.class.getClassLoader().getResourceAsStream(PERSON_BLOCK_CFG);
		String personEventsBlockCFG = TestUtils.getInstance().readConfiguration(in);
		personTypeBlock.setConfiguration(personEventsBlockCFG);
		isExist = contentTypeService.findContentType(getTestSession(), PERSON_BLOCK_CTYPE_NAME);
		if (!isExist)
		{
			ContentTypesFrame frame = contentTypeService.createContentType(getTestSession(), personTypeBlock);
			frame.verifyIsPresent(PERSON_BLOCK_CTYPE_NAME);
			logger.info("New content type was created");
		} else
		{
			contentTypeService.editContentType(getTestSession(), PERSON_BLOCK_CTYPE_NAME, personEventsBlockCFG);
			logger.info("Content type: " + PERSON_BLOCK_CTYPE_NAME + " Was edited. Default configuration's settings were saved");
		}

		ContentType personRelatedct = new ContentType();
		personRelatedct.setName(PERSON_RELATED_CTYPE_NAME);
		personRelatedct.setContentHandler(ContentHandler.CUSTOM_CONTENT);
		personRelatedct.setDescription("Mapping input fields of type relatedcontent");
		in = ContentConvertor.class.getClassLoader().getResourceAsStream(PERSON_RELATED_CFG);
		String personRelatedCFG = TestUtils.getInstance().readConfiguration(in);
		personRelatedct.setConfiguration(personRelatedCFG);
		isExist = contentTypeService.findContentType(getTestSession(), PERSON_RELATED_CTYPE_NAME);
		if (!isExist)
		{
			ContentTypesFrame frame = contentTypeService.createContentType(getTestSession(), personRelatedct);
			frame.verifyIsPresent(PERSON_RELATED_CTYPE_NAME);
			logger.info("New content type was created: " + PERSON_RELATED_CTYPE_NAME);
		}

	}

	@Test(description = "set up: create content types: Person")
	public void settings()
	{
		logger.info("checks for the existance  of Content type, creates new content type if it does not exist");
		// 1.create content types
		createContentTypes();
		// 2. create content repositories:
		ContentRepository repository = new ContentRepository();
		repository.setName("importTest" + Math.abs(new Random().nextInt()));
		repositoryService.createContentRepository(getTestSession(), repository);

		ContentCategory newCategory = new ContentCategory();
		newCategory.setContentTypeName(PERSON_CTYPE_NAME);
		newCategory.setName("importCategory");
		String[] parentNames = { repository.getName() };
		newCategory.setParentNames(parentNames);
		getTestSession().put(IMPORT_CATEGORY, newCategory);
		repositoryService.addCategory(getTestSession(), newCategory);
        // create category for 'Block group'
		ContentCategory catBlock = new ContentCategory();
		catBlock.setContentTypeName(PERSON_BLOCK_CTYPE_NAME);
		catBlock.setName("importBlockGroup");
		String[] parentNames2 = { repository.getName() };
		catBlock.setParentNames(parentNames2);
		getTestSession().put(IMPORT_CATEGORY_BLOCK_GROUPS, catBlock);
		repositoryService.addCategory(getTestSession(), catBlock);
		
		// create category for related content
		ContentCategory catRelated = new ContentCategory();
		catRelated.setContentTypeName(PERSON_RELATED_CTYPE_NAME);
		catRelated.setName("importRelatedContent");
		String[] parentNames3 = { repository.getName() };
		catRelated.setParentNames(parentNames3);
		getTestSession().put(IMPORT_CATEGORY_RELATED_CONTENT, catRelated);
		repositoryService.addCategory(getTestSession(), catRelated);
		logger.info("FINISHED: set up settings");

	}

	/**
	 * Case-info: Importing from an XML formatted source
	 */
	@Test(dependsOnMethods = "settings", description = "Importing from an XML formatted source")
	public void importingFromXMLTest()
	{

		logger.info("case-info:Importing from an XML formatted source");
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(IMPORT_CATEGORY);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };
		// 1. import from an XML formatted resource
		ContentsTableFrame table = contentService.doImportContent(getTestSession(), "person-import-xml", IMPORT_PERSONS_XML, pathToCategory);
		logger.info("file: "+ IMPORT_PERSONS_XML +"has imported");
		
		List<String> namesActual = table.getContentNames();
		for(String name :namesActual)
		{
			logger.info("person with name: "+ name +"has imported");
		}
		logger.info("file: "+ IMPORT_PERSONS_XML +"has imported");
		XmlReader xmlReader = new XmlReader();
		// 2. read XML file and gets expected person names:
		List<String> expected = xmlReader.readNodeValue(IMPORT_PERSONS_XML, "person", "name");
		Assert.assertTrue(verifyExported(namesActual, expected), "expected names and actual are not equals!");
		logger.info("TEST-FINISHED:'Importing from an XML formatted source' ");
	}

	@Test(dependsOnMethods = "settings", description = "Import content from an CVS formatted source")
	public void importingFromCSVTest()
	{
		logger.info("case-info:Import content from an CVS formatted source");
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(IMPORT_CATEGORY);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };
		// 1. import from an CSV formatted resource
		ContentsTableFrame table = contentService.doImportContent(getTestSession(), "person-import-csv", IMPORT_PERSONS_CSV_FILE, pathToCategory);
		List<String> namesActual = table.getContentNames();
		// 2. read CSV file and gets expected person names:
		List<String> expected = getPersonNamesFromCSV(IMPORT_PERSONS_CSV_FILE);
		Assert.assertTrue(verifyExported(namesActual, expected), "expected names and actual are not equals!");
		logger.info("TEST-FINISHED:'Import content from an CVS formatted source' ");
	}
	
	/**
	 * Set the <import .. status="2"> in import

      <br>Verify content gets the approved status

	 */
	@Test(description = "Content status test: set approve status in import block from configuration of a  Content Type", dependsOnMethods="")
	public void importAndSetApproveStatusTest()
	{
		logger.info("CASE-INFO: Set the <import .. status='2'> in import");


	}
	
	@Test(dependsOnMethods = "importingFromCSVTest", description = "Add entry of type uploadfile, specify file-data (base64-encoded binary data) in import source (XML only) ")
	public void uploadfileTest()
	{
		logger.info("case-info:Add entry of type uploadfile, specify file-data (base64-encoded binary data) in import source (XML only) ");
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(IMPORT_CATEGORY);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };
		 //1. edit the person contentype: add base64
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(PERSON_BASE64_CFG);
		String base64CFG = TestUtils.getInstance().readConfiguration(in);
		contentTypeService.editContentType(getTestSession(), PERSON_CTYPE_NAME, base64CFG);
		// 2. import from an XML formatted resource with specified base64 file.
		ContentsTableFrame table = contentService.doImportContent(getTestSession(), "person-import-xml", IMPORT_PERSONS_BASE64_FILE, pathToCategory);
		List<String> namesActual = table.getContentNames();
		for(String name: namesActual)
		{
			logger.info("person with name:"+ name+ "was is present in the table");
		}
		// 3. verify: person exist in the table 
		XmlReader xmlReader = new XmlReader();
		
		List<String> expectedPersonNames = xmlReader.readNodeValue(IMPORT_PERSONS_BASE64_FILE, "person", "name");
		boolean isContains = namesActual.containsAll(expectedPersonNames);
		Assert.assertTrue(isContains,"new persons were not found in the table!");
		//4. open content and verify input with name 'filename_image' is present
		Assert.assertTrue(isImageLinkPresent(table, "Paolo Veronese"), "image link not found on the person-info page, but should be present!");
		logger.info("TEST-FINISHED:'upload file Test (base64-encoded binary data)' ");
	}

	@Test(dependsOnMethods = "settings", description = "Importing into block groups,Mapping input fields of type date, format='dd-MM-yyyy' ")
	public void importingIntoBlockGroups()
	{
		logger.info("case-info:Importing into block groups.  Date Format used: format='dd-MM-yyyy'");
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(IMPORT_CATEGORY_BLOCK_GROUPS);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };
		// 1. import XML formatted resource:
		ContentsTableFrame table = contentService.doImportContent(getTestSession(), "person-import-xml", IMPORT_PERSONS_BLOCK_GROUP_XML_FILE,pathToCategory);
		List<String> namesActual = table.getContentNames();
		XmlReader xmlReader = new XmlReader();
		// 2. gets expected persons with events from the XML
		List<Person> personsFromXML = xmlReader.getPersons(IMPORT_PERSONS_BLOCK_GROUP_XML_FILE);
		//3. Verify: the number of imported persons:
		Assert.assertTrue(personsFromXML.size() == namesActual.size(),"the number of persons from UI and expected number of persons are not equals!");
		
		//4. Open all persons and Verify that all events are present on the page.
		//and  Verify:the Order of imported block group entries- The imported block group entries will be ordered as they appear from top to bottom in the XML source.
		for (Person personFromXml : personsFromXML)
		{
			
			List<UserEvent> uiEvents = getEventsFromUI(table, personFromXml.getName()) ;
			boolean result = personFromXml.getEvents().equals(uiEvents);
			Assert.assertTrue(result,"Importing into block groups test failed, because expexted events and actual are not equals!");
		}
		logger.info("TEST-FINISHED:'Importing into block groups' ");

	}
	/**
	 * Case-info: Updating block group entries.
	 * <br> Updates content that was imported in the 'importingIntoBlockGroups' method.
	 * <br>Mapping input fields of type date :   <mapping src="4" format="dd.MM.yyyy" dest="date-of-birth"/>
	 */
	@Test( dependsOnMethods = "importingIntoBlockGroups", description = "Updating block group entries, sync='person-no'.There are a  new person and a new event in the XML formatted source. ")
	public void updatingBlockGroupEntriesTest()
	{
		logger.info("case-info:Updating block group entries, sync='person-no'. There are a  new person and a new event in the XML formatted source. " );
		logger.info("case-info:Mapping input fields of type date");
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(IMPORT_CATEGORY_BLOCK_GROUPS);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };
		//1. update content: new event was added for first person and new person added: 
		ContentsTableFrame table = contentService.doImportContent(getTestSession(), "person-import-xml", IMPORT_PERSONS_BLOCK_GROUP_UPDATE_XML_FILE, pathToCategory);
		List<String> namesActual = table.getContentNames();
		XmlReader xmlReader = new XmlReader();
		// 2. gets EXPECTED persons with events from the XML
		List<Person> personsFromXML = xmlReader.getPersons(IMPORT_PERSONS_BLOCK_GROUP_UPDATE_XML_FILE);
		boolean isNewPersonAdded = namesActual.size() == personsFromXML.size();
		Assert.assertTrue(isNewPersonAdded, "Content-import was performed  and new person should be added, but the number of persons from UI and expected number of persons are not equals!");
		for (Person personFromXML : personsFromXML)
		{
			List<UserEvent> eventsFromUI = getEventsFromUI(table,personFromXML.getName());
			boolean result = eventsFromUI.equals(personFromXML.getEvents());
			Assert.assertTrue(result,"'Updating block group entries' test failed, because expexted events and actual are not equals!");
		}
		logger.info("TEST-FINISHED:'Importing into block groups' ");
	}
	//TODO implement Keep content

	/**
	 * Case-info: Updating block group entries.
	 * By default, existing block group entries in a content not existing in the import source, are not removed. Removing can be enabled by giving the purge attribute on the block group element the value "true".
       When purge is set to true, any existing block group entries in the content that are not in the import source will be removed.
	 */
	@Test(description="Block group purge setting", dependsOnMethods="updatingBlockGroupEntriesTest")
	public void blockGroupPurgeSettingTest()
	{
		logger.info("case-info:Block group purge setting.");
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(PERSON_BLOCK_EVENTS_PURGE_CFG);
		String purgeTrueCFG = TestUtils.getInstance().readConfiguration(in);
		//1. change content type's configuration: add purge="true" for EVENTS
		contentTypeService.editContentType(getTestSession(), PERSON_BLOCK_CTYPE_NAME, purgeTrueCFG);
		logger.info("Content type: "+PERSON_BLOCK_CTYPE_NAME+"Was edited. Removing of block group entries enabled ");
		
		//GET_EVENTS_BEFORE_ from session:		
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(IMPORT_CATEGORY_BLOCK_GROUPS);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };
		ContentsTableFrame table =(ContentsTableFrame)PageNavigatorV4.openContentsTableView(getTestSession(), pathToCategory);
		//2. get events before import and update content
		logger.info("try to get all events from UI for Person with name:"+ PERSON_EVENT_PURGE_TEST);
		List<UserEvent> eventsBefore = getEventsFromUI(table, PERSON_EVENT_PURGE_TEST);
		logger.info("there are: "+eventsBefore.size()+ " for the  Person with name: "+ PERSON_EVENT_PURGE_TEST);
		
		UserEvent eventToPurge = new UserEvent();
		eventToPurge.setName(EVENT_NAME_PURGE_TEST);
		eventToPurge.setValue(EVENT_VALUE_PURGE_TEST);
		Assert.assertTrue(eventsBefore.contains(eventToPurge), "before Importing: event with name:" +EVENT_NAME_PURGE_TEST+ "should be present on the page");
		
		//3. import and update content: "North pole almost reached" event should be  removed for 'Fridtjof Nansen' person 
		table = contentService.doImportContent(getTestSession(), "person-import-xml", IMPORT_PERSONS_BLOCK_GROUP_PURGE_UPDATE_XML_FILE, pathToCategory);
		logger.info("import of XML formatted source finished. Content was updated: filename "+ IMPORT_PERSONS_BLOCK_GROUP_PURGE_UPDATE_XML_FILE);
		// verify that 
		List<UserEvent> eventsAfterImport = getEventsFromUI(table, PERSON_EVENT_PURGE_TEST);
		Assert.assertFalse(eventsAfterImport.contains(eventToPurge), "event with name:" +EVENT_NAME_PURGE_TEST+ " still present on the page, but should be removed!");
		logger.info("TEST-FINISHED:'Block group purge setting' ");
		
	}

	// -------------------------------------------------------------------------------------------------
	/**
	 * The key representing a content in an Enonic CMS installation is usually not known to an import source. 
	 * <br>For an import source to be able to refer to a related content, it needs to provide a value of an input field that uniquely identifies a content. 
	 * <br>This is done by setting the relatedcontenttype and relatedfield settings on the mapping configuration.
	 */
	@Test(description = "Mapping input fields of type relatedcontent", dependsOnMethods="settings")
	public void mappingInputFieldsRelatedcontent()
	{
		logger.info("CASE-INFO: Mapping input fields of type relatedcontent");
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(IMPORT_CATEGORY_RELATED_CONTENT);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };
		//1. import XML formatted source with related persons
		ContentsTableFrame table = contentService.doImportContent(getTestSession(), "person-import-xml", IMPORT_PERSONS_RELATED_CONTENT_XML_FILE, pathToCategory);
		
		//2. gets all person names from web-page:
		List<String> namesActual = table.getContentNames();
		
		XmlReader xmlReader = new XmlReader();
		// 3. read XML file and gets expected person names:
		List<String> expectedPersonNames = xmlReader.readNodeValue(IMPORT_PERSONS_RELATED_CONTENT_XML_FILE, "person", "name");
		//4. verify that expected and actual list of persons are equal.
		Assert.assertTrue(verifyExported(namesActual, expectedPersonNames), "expected names and actual are not equals!");
		//verify children: click by person- "Fridtjof Nansen" and open person info
		List<String> childrenNames = getChildPersons(table, "Fridtjof Nansen");
		Assert.assertTrue(childrenNames.size()>0,"No any person has been found, but should be two child persons for 'Fridtjof Nansen'");
		logger.info("TEST-FINISHED: Mapping input fields of type relatedcontent");
		
	}
	private boolean verifyExported(List<String> namesActual, List<String> expected)
	{
		boolean result = true;
		for (String expectedname : expected)
		{
			result &= namesActual.contains(expectedname);
		}
		// result &= expected.size() == namesActual.size();
		return result;
	}

	/**
	 * Reads name from CSV formatted file. Name located in the second position.
	 * 
	 * <import mode="csv" name="person-import-csv" separator=";"> <mapping
	 * dest="person-no" src="1" /> <mapping dest="name" src="2" /> <mapping
	 * dest="date-of-birth" src="4" /> </import>
	 */
	private List<String> getPersonNamesFromCSV(String file)
	{
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(file);
		List<String> names = new ArrayList<>();
		Scanner scanner = new Scanner(in);
		while (scanner.hasNextLine())
		{
			String lineFromCSV = scanner.nextLine();
			String delims = ";";
			String[] tokens = lineFromCSV.split(delims);
			// name has second position in the csv-record!
			names.add(tokens[1]);

		}
		scanner.close();
		return names;
	}
	public  List<UserEvent>getEventsFromUI(ContentsTableFrame table, String contentName)
	{
		table.clickByNameAndOpenInfo(contentName);
		List<UserEvent> userEvents = new ArrayList<>();
		List<WebElement> eventNameElements = getTestSession().getDriver().findElements(By.xpath("//input[@name='event-name']"));
		List<WebElement> dateElements = getTestSession().getDriver().findElements(By.xpath("//input[@name='dateevent-date']"));
		UserEvent event;
		for(int i=0; i<eventNameElements.size();i++)
		{
			event = new UserEvent();
			event.setName(eventNameElements.get(i).getAttribute("value"));
			String dateFormatted = converDateFormat(dateElements.get(i).getAttribute("value"));
			event.setValue(dateFormatted);
			userEvents.add(event);
		}
		getTestSession().getDriver().findElement(By.xpath("//button[@name='closebtn']")).click();
		table.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return userEvents;
	}
	private  List<String> getChildPersons(ContentsTableFrame table, String contentName)
	{
		table.clickByNameAndOpenInfo(contentName);
		List<String> childList = new ArrayList<>();
		String childrenXpath = "//span[@class='relatedcontentsclass']";
		List<WebElement> children = getTestSession().getDriver().findElements(By.xpath(childrenXpath));
		for(WebElement ch:children)
		{
			childList.add(ch.getText());
		}
		return childList;
	}
	private boolean  isImageLinkPresent(ContentsTableFrame table, String contentName)
	{
		table.clickByNameAndOpenInfo(contentName);
		String imageLinkXpath = "//a[@name='image_link']";
		List<WebElement> elems = getTestSession().getDriver().findElements(By.xpath(imageLinkXpath));
		if(elems.size() >0){
			return true;
		}else 
		{
			return false;
		}
		
	}
	
	private String  converDateFormat(String date)
	{
		DateFormat dfRequired = new SimpleDateFormat("dd-MM-yyyy");  
		SimpleDateFormat parse = new SimpleDateFormat("dd.MM.yyyy");
		Date parsed =null;
		try
		{
			parsed = parse.parse(date);
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dfRequired.format(parsed); 
	}
}
