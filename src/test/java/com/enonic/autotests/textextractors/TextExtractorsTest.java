package com.enonic.autotests.textextractors;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jsoup.Jsoup;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.BaseTest;
import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.model.ContentHandler;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.model.FileContentInfo;
import com.enonic.autotests.pages.v4.adminconsole.content.AbstractContentTableView;
import com.enonic.autotests.pages.v4.adminconsole.content.ContentIndexes;
import com.enonic.autotests.pages.v4.adminconsole.content.search.ContentSearchParams;
import com.enonic.autotests.pages.v4.adminconsole.content.search.SearchWhere;
import com.enonic.autotests.services.ContentService;
import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.services.RepositoryService;
import com.enonic.autotests.services.SearchService;
import com.enonic.autotests.testdata.contenttype.ContentConvertor;
import com.enonic.autotests.utils.XmlReader;

public class TextExtractorsTest extends BaseTest
{
	private final String TEXT_FOR_EXTRACTING = "text extractor test";
	private final String NOR_TEXT_FOR_EXTRACTING = "i dag en kjølig tåket dag";
	private ContentTypeService contentTypeService = new ContentTypeService();
	private RepositoryService repositoryService = new RepositoryService();
	private ContentService contentService = new ContentService();
	private SearchService searchService = new SearchService();
	
	private String CONTENT_LIST_KEY = "content_list";

	private final String FILES_CATEGORY_KEY = "files_category";
	private final String REPOSITORY_KEY = "repository_key";

	private final String FILE_CTYPE_NAME = "File";

	private static final String XML_EXTRACTOR_TESTDATA = "test-data/extractor/xml-extract.xml";
	private static final String HTML_EXTRACTOR_TESTDATA = "test-data/extractor/htmltestdata.html";
	private static final String TXT_EXTRACTOR_TESTDATA = "test-data/extractor/text.txt";
	private static final String PDF_EXTRACTOR_TESTDATA = "test-data/extractor/pdf.pdf";
	private static final String XLS_EXTRACTOR_TESTDATA = "test-data/extractor/excel.xls";
	private static final String DOC_EXTRACTOR_TESTDATA = "test-data/extractor/word.doc";
	private static final String PPT_EXTRACTOR_TESTDATA = "test-data/extractor/ppt.ppt";
	private static final String RTF_EXTRACTOR_TESTDATA = "test-data/extractor/rtf.rtf";
	private static final String ODT_EXTRACTOR_TESTDATA = "test-data/extractor/odt.odt";

	private static final String TXT_NOR_EXTRACTOR_TESTDATA = "test-data/extractor/nor.txt";
	private static final String TXT_CYR_EXTRACTOR_TESTDATA = "test-data/extractor/cyrillic.txt";
	
	

	/**
	 * Create File content type.
	 */

	private void createContentType()
	{
		logger.info("setup: create content type: with the Files Content Handler");
		ContentType filesType = new ContentType();
		filesType.setName(FILE_CTYPE_NAME);
		filesType.setContentHandler(ContentHandler.FILES);
		filesType.setDescription("content repository test");
		boolean isExist = contentTypeService.findContentType(getTestSession(), FILE_CTYPE_NAME);
		if (!isExist)
		{
			contentTypeService.createContentType(getTestSession(), filesType);
			logger.info("New content type with 'Files' handler was created");
		}

	}

	@Test(description = "Create a category for storing content with the Files Content Handler ")
	public void setup()
	{
		logger.info("#########  STARTED setup for  XML Extractor tests");
		logger.info("set up: create content types: Person and Image");
		// 1.create content types
		createContentType();
		// 2. create content repositories:
		ContentRepository repository = new ContentRepository();
		repository.setName("extractorTest" + Math.abs(new Random().nextInt()));
		repositoryService.createContentRepository(getTestSession(), repository);
		getTestSession().put(REPOSITORY_KEY, repository);

		// create category for upload files:
		ContentCategory fileCategory = new ContentCategory();
		fileCategory.setContentTypeName(FILE_CTYPE_NAME);
		fileCategory.setName("files");
		String[] parentNames2 = { repository.getName() };
		fileCategory.setParentNames(parentNames2);

		repositoryService.addCategory(getTestSession(), fileCategory);
		getTestSession().put(FILES_CATEGORY_KEY, fileCategory);
		getTestSession().put(CONTENT_LIST_KEY, new ArrayList<String>());
		logger.info("$$$$ FINISHED: setup  for  Extractor tests");

	}

	@Test(dependsOnMethods = "setup", description = "add content with Norwegian encoding", groups = "upload")
	public void addContentNorwegianEncodingTest()
	{
		logger.info("#########  STARTED: Test *.txt Text extraction, add content with Norwegian encoding");

		String displayName = "nor.txt";
		ContentRepository repository = (ContentRepository) getTestSession().get(REPOSITORY_KEY);
		ContentCategory ctegory = (ContentCategory) getTestSession().get(FILES_CATEGORY_KEY);

		Content<FileContentInfo> content = new Content<>();
		String[] pathToContent = new String[] { repository.getName(), ctegory.getName() };
		content.setParentNames(pathToContent);
		FileContentInfo contentTab = new FileContentInfo();
		contentTab.setPathToFile(TXT_NOR_EXTRACTOR_TESTDATA);
		contentTab.setDescription("file for extractor test");
		content.setContentTab(contentTab);
		content.setDisplayName(displayName);
		content.setContentHandler(ContentHandler.FILES);
		AbstractContentTableView table = contentService.addFileContent(getTestSession(), content);
		boolean result = table.findContentInTableByName(displayName);
		if (!result)
		{
			Assert.fail("file was not uploaded!");
		}
		Map<ContentIndexes, String> map = contentService.getContentIndexedValues(getTestSession(), content);
		String attachmentIndex = map.get(ContentIndexes.ATTACHMENT);

		Assert.assertTrue(attachmentIndex.contains(NOR_TEXT_FOR_EXTRACTING), "expected text was not found on the Source page in block 'Indexed Values'!");
		logger.info("#########  FINISHED: Test *.txt Text extraction, add content with Norwegian encoding");
	}

	@Test(dependsOnMethods = "setup", groups = "upload", description = "add *.txt content with Cyrillic encoding")
	public void addCyrillicEncodingTextTest() throws URISyntaxException, IOException
	{
		logger.info("#########  STARTED: Test *.txt Text extraction, add content with Cyrillic encoding  ");

		String displayName = "cyrillic.txt";
		ContentRepository repository = (ContentRepository) getTestSession().get(REPOSITORY_KEY);
		ContentCategory ctegory = (ContentCategory) getTestSession().get(FILES_CATEGORY_KEY);

		Content<FileContentInfo> content = new Content<>();
		String[] pathToContent = new String[] { repository.getName(), ctegory.getName() };
		content.setParentNames(pathToContent);
		FileContentInfo contentTab = new FileContentInfo();
		contentTab.setPathToFile(TXT_CYR_EXTRACTOR_TESTDATA);
		contentTab.setDescription("file for extractor test");
		content.setContentTab(contentTab);
		content.setDisplayName(displayName);
		content.setContentHandler(ContentHandler.FILES);
		AbstractContentTableView table = contentService.addFileContent(getTestSession(), content);
		boolean result = table.findContentInTableByName(displayName);
		if (!result)
		{
			Assert.fail("file was not uploaded!");
		}
		Map<ContentIndexes, String> map = contentService.getContentIndexedValues(getTestSession(), content);
		String attachmentIndex = map.get(ContentIndexes.ATTACHMENT);

		URL url = ContentConvertor.class.getClassLoader().getResource(TXT_CYR_EXTRACTOR_TESTDATA);
		File txtData = new File(url.toURI());
		String expectedTxtContent = Jsoup.parse(txtData, "UTF-8").text();
		Assert.assertTrue(attachmentIndex.equals(expectedTxtContent), "expected text was not found on the Source page in block 'Indexed Values'!");
		logger.info("#########  FINISHED: Test *.txt Text extraction, add content with Cyrillic encoding");
	}

	@Test(description = "Upload an xml file with some text. Verify that the attachment index contains text from the file.  ",groups = "upload", dependsOnMethods = "setup")
	public void xmlExtractorTest()
	{
		String displayName = "xmltestdata.xml";
		logger.info("#########  STARTED setup for  XML Extractor tests");

		ContentRepository repository = (ContentRepository) getTestSession().get(REPOSITORY_KEY);
		ContentCategory ctegory = (ContentCategory) getTestSession().get(FILES_CATEGORY_KEY);

		Content<FileContentInfo> content = new Content<>();
		String[] pathToContent = new String[] { repository.getName(), ctegory.getName() };
		content.setParentNames(pathToContent);
		FileContentInfo contentTab = new FileContentInfo();
		contentTab.setPathToFile(XML_EXTRACTOR_TESTDATA);
		contentTab.setDescription("file for extractor test");
		content.setContentTab(contentTab);
		content.setDisplayName(displayName);
		content.setContentHandler(ContentHandler.FILES);
		AbstractContentTableView table = contentService.addFileContent(getTestSession(), content);
		boolean result = table.findContentInTableByName(displayName);
		if (!result)
		{
			Assert.fail("file was not uploaded!");
		}
		Map<ContentIndexes, String> map = contentService.getContentIndexedValues(getTestSession(), content);
		String attachmentIndex = map.get(ContentIndexes.ATTACHMENT);
		XmlReader reader = new XmlReader();

		List<String> expectedValue = reader.readNodeValue(XML_EXTRACTOR_TESTDATA, "content", "displayName");
		if (expectedValue.size() == 0)
		{
			Assert.fail("error during parsing the XML test data");
		}
		Assert.assertTrue(attachmentIndex.contains(expectedValue.get(0)), "expected text was not found on the Source page in block 'Indexed Values'!");
		((List<String>)getTestSession().get(CONTENT_LIST_KEY)).add(displayName);
		logger.info("#########  FINISHED: Test *.xml Text extraction    ");
	}

	@Test(description = "Upload an html file with some text. Verify that the attachment index contains text from the file.  ", groups = "upload", dependsOnMethods = "setup")
	public void htmlExtractorTest() throws IOException, URISyntaxException
	{
		String displayName = "htmltestdata.html";
		logger.info("#########  STARTED setup for  HTML Extractor tests");

		ContentRepository repository = (ContentRepository) getTestSession().get(REPOSITORY_KEY);
		ContentCategory ctegory = (ContentCategory) getTestSession().get(FILES_CATEGORY_KEY);

		Content<FileContentInfo> content = new Content<>();
		String[] pathToContent = new String[] { repository.getName(), ctegory.getName() };
		content.setParentNames(pathToContent);
		FileContentInfo contentTab = new FileContentInfo();
		contentTab.setPathToFile(HTML_EXTRACTOR_TESTDATA);
		contentTab.setDescription("file for extractor test");
		content.setContentTab(contentTab);
		content.setDisplayName(displayName);
		content.setContentHandler(ContentHandler.FILES);
		AbstractContentTableView table = contentService.addFileContent(getTestSession(), content);
		boolean result = table.findContentInTableByName(displayName);
		if (!result)
		{
			Assert.fail("file was not uploaded!");
		}
		Map<ContentIndexes, String> map = contentService.getContentIndexedValues(getTestSession(), content);
		String attachmentIndex = map.get(ContentIndexes.ATTACHMENT);

		URL url = ContentConvertor.class.getClassLoader().getResource(HTML_EXTRACTOR_TESTDATA);
		File htmlData = new File(url.toURI());
		String expectedHTMLContent = Jsoup.parse(htmlData, "UTF-8").text();

		Assert.assertTrue(expectedHTMLContent.equals(attachmentIndex), "expected and actual indexed values are not equals!");
		((List<String>)getTestSession().get(CONTENT_LIST_KEY)).add(displayName);
		logger.info("#########  FINISHED: Test *.html Text extraction    ");
	}

	@Test(description = "Upload an Plain Text file with some text. Verify that the attachment index contains text from the file.  ", groups = "upload", dependsOnMethods = "setup")
	public void plainTextExtractorTest() throws IOException, URISyntaxException
	{
		String displayName = "text.txt";
		logger.info("#########  STARTED: Test Plain Text extraction ");

		ContentRepository repository = (ContentRepository) getTestSession().get(REPOSITORY_KEY);
		ContentCategory ctegory = (ContentCategory) getTestSession().get(FILES_CATEGORY_KEY);

		Content<FileContentInfo> content = new Content<>();
		String[] pathToContent = new String[] { repository.getName(), ctegory.getName() };
		content.setParentNames(pathToContent);
		FileContentInfo contentTab = new FileContentInfo();
		contentTab.setPathToFile(TXT_EXTRACTOR_TESTDATA);
		contentTab.setDescription("file for extractor test");
		content.setContentTab(contentTab);
		content.setDisplayName(displayName);
		content.setContentHandler(ContentHandler.FILES);
		AbstractContentTableView table = contentService.addFileContent(getTestSession(), content);
		boolean result = table.findContentInTableByName(displayName);
		if (!result)
		{
			Assert.fail("file was not uploaded!");
		}
		Map<ContentIndexes, String> map = contentService.getContentIndexedValues(getTestSession(), content);
		String attachmentIndex = map.get(ContentIndexes.ATTACHMENT);

		URL url = ContentConvertor.class.getClassLoader().getResource(TXT_EXTRACTOR_TESTDATA);
		File txtData = new File(url.toURI());
		String expectedTxtContent = Jsoup.parse(txtData, "UTF-8").text();

		Assert.assertTrue(expectedTxtContent.equals(attachmentIndex), "expected and actual indexed values are not equals!");
		((List<String>)getTestSession().get(CONTENT_LIST_KEY)).add(displayName);
		logger.info("#########  FINISHED: Test *.txt Text extraction    ");
	}

	@Test(description = "advanced search with text extractor plugin", dependsOnGroups ="upload")
	public void advancedSearchTest()
	{
		ContentSearchParams params = ContentSearchParams.with().searcText(TEXT_FOR_EXTRACTING).where(SearchWhere.ATTACHMENTS).build();
		List<String> contentNames = searchService.doAdvancedSearch(getTestSession(), params);
		List<String> expectedListNames = (List<String> )getTestSession().get(CONTENT_LIST_KEY);
		boolean result = contentNames.containsAll(expectedListNames);
		Assert.assertTrue(result, "'Advanced Search' failed some files were not found!");
		
	}

}
