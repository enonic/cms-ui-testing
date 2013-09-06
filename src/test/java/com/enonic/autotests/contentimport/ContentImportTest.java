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
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.utils.XmlReader;

/**
 * System test for add Content to section-pages.
 * 
 */
public class ContentImportTest extends BaseTest
{
	/**
	 * there two modes: mode =xml and mode =csv, so this content type configured
	 * for importing in XML and CSV mode
	 */
	private final String PERSON_CFG = "test-data/contenttype/person.xml";

	/** mode=xml; relatedcontenttype='person-related' " */
	private final String PERSON_RELATED_CFG = "test-data/contenttype/person-related-content.xml";
	private final String PERSON_BASE64_CFG = "test-data/contenttype/person-base64.xml";
	private final String PERSON_APPROVED_STATUS_CFG = "test-data/contenttype/person-ct-status-approved.xml";

	/** this XML file contains a list of persons for importing */
	private String IMPORT_PERSONS_XML = "test-data/contentimport/persons.xml";

	/** this CSV file contains a list of persons for importing */
	private String IMPORT_PERSONS_CSV_FILE = "test-data/contentimport/persons.csv";
	private String IMPORT_PERSONS_BASE64_FILE = "test-data/contentimport/persons-base64.xml";
	private String IMPORT_PERSONS_APPROVED_STATUS_FILE = "test-data/contentimport/persons-approved.xml";
	private String IMPORT_PERSONS_RELATED_CONTENT_XML_FILE = "test-data/contentimport/persons-related-content.xml";

	private ContentTypeService contentTypeService = new ContentTypeService();
	private RepositoryService repositoryService = new RepositoryService();
	private ContentService contentService = new ContentService();

	/** key for saving a Content Type in the Session */
	private String PERSON_CTYPE_KEY = "person-events-ctype";
	/** key for saving a Category in the Session */
	private String IMPORT_CATEGORY_KEY = "category_for_import";

	/** key for saving a Category in the Session */
	private String IMPORT_CATEGORY_RELATED_KEY = "category_for_related_import";

	/**
	 * performs preconditions for all tests:
	 */
	private void createContentTypesForImport()
	{
		ContentType personType = new ContentType();
		String contentTypeName = "Person" + Math.abs(new Random().nextInt());
		personType.setName(contentTypeName);
		personType.setContentHandler(ContentHandler.CUSTOM_CONTENT);
		personType.setDescription("person content type");
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(PERSON_CFG);
		String personCFG = TestUtils.getInstance().readConfiguration(in);
		personType.setConfiguration(personCFG);
		ContentTypesFrame frame = contentTypeService.createContentType(getTestSession(), personType);
		boolean isCreated = frame.verifyIsPresent(contentTypeName);
		if (!isCreated)
		{
			Assert.fail("Content Type was not created!");
		}
		getTestSession().put(PERSON_CTYPE_KEY, personType);

		ContentType personType2 = new ContentType();
		String contentTypeName2 = "person-related";
		personType2.setName(contentTypeName2);
		personType2.setContentHandler(ContentHandler.CUSTOM_CONTENT);
		personType2.setDescription("person content type");
		InputStream in2 = ContentConvertor.class.getClassLoader().getResourceAsStream(PERSON_RELATED_CFG);
		String personRelatedCFG2 = TestUtils.getInstance().readConfiguration(in2);
		personType2.setConfiguration(personRelatedCFG2);
		contentTypeService.createContentType(getTestSession(), personType2);
		logger.info("New content type  was created name: " + contentTypeName2);

	}

	@Test(description = "set up: create content types: Person")
	public void settingsForImport()
	{
		logger.info("@@@@@@@@@@@@@@@@ TEST STARTED name:  ContentImportTest   "+ "set up: create content types: Person ");
		logger.info("create content types and categories for ContentImportTest ");
		// 1.create content types
		createContentTypesForImport();
		// 2. create content repositories:
		ContentRepository repository = new ContentRepository();
		repository.setName("importTest" + Math.abs(new Random().nextInt()));
		repositoryService.createContentRepository(getTestSession(), repository);

		ContentCategory newCategory = new ContentCategory();
		newCategory.setContentTypeName(getContentTypeName());
		newCategory.setName("importCategory");
		String[] parentNames = { repository.getName() };
		newCategory.setParentNames(parentNames);
		getTestSession().put(IMPORT_CATEGORY_KEY, newCategory);
		repositoryService.addCategory(getTestSession(), newCategory);
		boolean isCreated = repositoryService.findCategoryByPath(getTestSession(), newCategory.getName(), newCategory.getParentNames());
		if (!isCreated)
		{
			Assert.fail("category was not created!");
		}

		ContentCategory ctegoryRelated = new ContentCategory();
		ctegoryRelated.setContentTypeName("person-related");
		ctegoryRelated.setName("importRelatedContent");
		String[] parentNames2 = { repository.getName() };
		ctegoryRelated.setParentNames(parentNames2);
		getTestSession().put(IMPORT_CATEGORY_RELATED_KEY, ctegoryRelated);
		repositoryService.addCategory(getTestSession(), ctegoryRelated);
		isCreated = repositoryService.findCategoryByPath(getTestSession(), ctegoryRelated.getName(), ctegoryRelated.getParentNames());
		if (!isCreated)
		{
			Assert.fail("category was not created!");
		}
		logger.info("$$$$$$$$$ FINISHED: set up settings");

	}

	/**
	 * Case-info: Importing from an XML formatted source
	 */
	@Test(dependsOnMethods = "settingsForImport", description = "Importing from an XML formatted source")
	public void importingFromXMLTest()
	{
		logger.info("#### TEST STARTED:Importing from an XML formatted source");
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(IMPORT_CATEGORY_KEY);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };
		// 1. import from an XML formatted resource
		ContentsTableFrame table = contentService.doImportContent(getTestSession(), "person-import-xml", IMPORT_PERSONS_XML, pathToCategory);
		logger.info("file: " + IMPORT_PERSONS_XML + "has imported");

		List<String> namesActual = table.getContentNames();
		for (String name : namesActual)
		{
			logger.info("person with name: " + name + "has imported");
		}
		logger.info("file: " + IMPORT_PERSONS_XML + "has imported");
		XmlReader xmlReader = new XmlReader();
		// 2. read XML file and gets expected person names:
		List<String> expected = xmlReader.readNodeValue(IMPORT_PERSONS_XML, "person", "name");
		Assert.assertTrue(namesActual.containsAll(expected), "some persons were not imported!");
		logger.info("$$$$$ FINISHED:'Importing from an XML formatted source' ");
	}

	@Test(dependsOnMethods = "settingsForImport", description = "Import content from an CVS formatted source")
	public void importingFromCSVTest()
	{
		logger.info("TEST STARTED:Import content from an CVS formatted source");
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(IMPORT_CATEGORY_KEY);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };
		// 1. import from an CSV formatted resource
		ContentsTableFrame table = contentService.doImportContent(getTestSession(), "person-import-csv", IMPORT_PERSONS_CSV_FILE, pathToCategory);
		List<String> namesActual = table.getContentNames();
		// 2. read CSV file and gets expected person names:
		List<String> expected = ImportUtils.getPersonNamesFromCSV(IMPORT_PERSONS_CSV_FILE);
		Assert.assertTrue(namesActual.containsAll(expected), "some persons were not imported!");
		logger.info("$$$$$$$$$ FINISHED:'Import content from an CVS formatted source' ");
	}

	/**
	 * Set the <import .. status="2"> in import. <br>
	 * Verify content gets the approved status
	 */
	@Test(description = "Content status test: set approve status in import block from configuration of a  Content Type", dependsOnMethods = "importingFromCSVTest")
	public void importAndSetApproveStatusTest()
	{
		logger.info("#### STARTED: Set the <import .. status='2'> in import");
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(IMPORT_CATEGORY_KEY);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };
		ContentsTableFrame table = (ContentsTableFrame) PageNavigatorV4.openContentsTableView(getTestSession(), pathToCategory);
		// delete all content
		table.doDeleteAllContent();

		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(PERSON_APPROVED_STATUS_CFG);
		String approvedStatusCFG = TestUtils.getInstance().readConfiguration(in);
		contentTypeService.editContentType(getTestSession(), getContentTypeName(), approvedStatusCFG);

		table = contentService.doImportContent(getTestSession(), "person-import-xml", IMPORT_PERSONS_APPROVED_STATUS_FILE, pathToCategory);
		List<String> namesActual = table.getContentNames();
		for (String name : namesActual)
		{
			logger.info("person with name:" + name + "was is present in the table");
		}

		XmlReader xmlReader = new XmlReader();

		List<String> expectedPersonNames = xmlReader.readNodeValue(IMPORT_PERSONS_APPROVED_STATUS_FILE, "person", "name");
		Assert.assertTrue(namesActual.containsAll(expectedPersonNames), "some persons were not imported!");

		// verify status:
		ContentStatus status = table.getContentStatus(namesActual.get(0));
		Assert.assertTrue(status.equals(ContentStatus.APPROVED), "expected status and actual are not equals!");
		logger.info("$$$$$$$$$ FINISHED:  importAndSetApproveStatusTest ");
	}

	// @Test(description = "Import source where entries in source points to other entries in source as related", dependsOnMethods="importingFromCSVTest")
	public void relatedContentInternalDependencyTest()
	{
		logger.info("T#### STARTED:Import source where entries in source points to other entries in source as related,");
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(IMPORT_CATEGORY_KEY);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };
		ContentsTableFrame table = (ContentsTableFrame) PageNavigatorV4.openContentsTableView(getTestSession(), pathToCategory);
	}

	@Test(dependsOnMethods = "importingFromCSVTest", description = "Add entry of type uploadfile, specify file-data (base64-encoded binary data) in import source (XML only) ")
	public void uploadfileTest()
	{
		logger.info("#### STARTED:Add entry of type uploadfile, specify file-data (base64-encoded binary data) in import source (XML only) ");
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(IMPORT_CATEGORY_KEY);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };
		// 1. edit the person contenttype: add base64
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(PERSON_BASE64_CFG);
		String base64CFG = TestUtils.getInstance().readConfiguration(in);
		contentTypeService.editContentType(getTestSession(), getContentTypeName(), base64CFG);

		// 2. import from an XML formatted resource with specified base64 file.
		ContentsTableFrame table = contentService.doImportContent(getTestSession(), "person-import-xml", IMPORT_PERSONS_BASE64_FILE, pathToCategory);
		List<String> namesActual = table.getContentNames();
		for (String name : namesActual)
		{
			logger.info("person with name:" + name + "was is present in the table");
		}
		// 3. verify: person exist in the table
		XmlReader xmlReader = new XmlReader();

		List<String> expectedPersonNames = xmlReader.readNodeValue(IMPORT_PERSONS_BASE64_FILE, "person", "name");
		boolean isContains = namesActual.containsAll(expectedPersonNames);
		Assert.assertTrue(isContains, "new persons were not found in the table!");
		// 4. open content and verify input with name 'filename_image' is present
		Assert.assertTrue(ImportUtils.isImageLinkPresent(getSessionDriver(), table, "Paolo Veronese"),"image link not found on the person-info page, but should be present!");
		logger.info("$$$$$$$$$$$$$$ FINISHED:'upload file Test (base64-encoded binary data)' ");
	}

	// TODO implement Keep content

	/**
	 * The key representing a content in an Enonic CMS installation is usually not known to an import source. 
	 * <br>For an import source to be able to refer to a related content, it needs to provide a value of an input field that uniquely identifies a content. 
	 * <br> This is done by setting the relatedcontenttype and relatedfield settings on the mapping configuration.
	 */
	@Test(description = "Mapping input fields of type relatedcontent", dependsOnMethods = "settingsForImport")
	public void mappingInputFieldsRelatedcontent()
	{
		logger.info("### STARTED: Mapping input fields of type relatedcontent");
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(IMPORT_CATEGORY_RELATED_KEY);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };
		ContentsTableFrame table = (ContentsTableFrame) PageNavigatorV4.openContentsTableView(getTestSession(), pathToCategory);
		table.doDeleteAllContent();

		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(PERSON_RELATED_CFG);
		String relatedContentCFG = TestUtils.getInstance().readConfiguration(in);
		contentTypeService.editContentType(getTestSession(), getContentTypeName(), relatedContentCFG);
		// 1. import XML formatted source with related persons
		table = contentService.doImportContent(getTestSession(), "person-import-xml", IMPORT_PERSONS_RELATED_CONTENT_XML_FILE, pathToCategory);

		// 2. gets all person names from web-page:
		List<String> namesActual = table.getContentNames();

		XmlReader xmlReader = new XmlReader();
		// 3. read XML file and gets expected person names:
		List<String> expectedPersonNames = xmlReader.readNodeValue(IMPORT_PERSONS_RELATED_CONTENT_XML_FILE, "person", "name");
		// 4. verify that expected and actual list of persons are equal.
		Assert.assertTrue(namesActual.containsAll(expectedPersonNames), "expected names and actual are not equals!");
		// verify children: click by person- "Fridtjof Nansen" and open person info
		List<String> childrenNames = ImportUtils.getChildPersons(getSessionDriver(), table, "Fridtjof Nansen");
		Assert.assertTrue(childrenNames.size() > 0, "No any person has been found, but should be two child persons for 'Fridtjof Nansen'");
		logger.info("$$$$$$$$$$$ FINISHED: Mapping input fields of type relatedcontent");

	}
	// -------------------------------------------------------------------------------------------------

	private String getContentTypeName()
	{
		ContentType ct = (ContentType) getTestSession().get(PERSON_CTYPE_KEY);
		return ct.getName();
	}
}
