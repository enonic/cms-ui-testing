package com.enonic.autotests.editor;

import java.io.InputStream;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.enonic.autotests.BaseTest;
import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.model.ContentHandler;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.pages.v4.adminconsole.content.AlignmentText;
import com.enonic.autotests.pages.v4.adminconsole.contenttype.ContentTypesFrame;
import com.enonic.autotests.services.ContentService;
import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.services.RepositoryService;
import com.enonic.autotests.services.TinyMCEService;
import com.enonic.autotests.testdata.contenttype.ContentConvertor;
import com.enonic.autotests.utils.TestUtils;

/**
 * Tests for content, that has TinyCME editor.
 * 
 */
public class EditorTest extends BaseTest
{
	private static final String EDITOR_REPOSITORY_KEY = "editor_repository_key";

	private static final String EDITOR_CATEGORY_KEY = "editor_category_key";

	private final String TINY_MCE_CFG = "test-data/contenttype/tiny-editor.xml";

	private ContentTypeService contentTypeService = new ContentTypeService();

	private RepositoryService repositoryService = new RepositoryService();

	private ContentService contentService = new ContentService();
	private TinyMCEService tinyMCEService = new TinyMCEService();

	@Test
	public void setup()
	{
		ContentType editorCtype = new ContentType();
		String contentTypeName = "Editor" + Math.abs(new Random().nextInt());
		editorCtype.setName(contentTypeName);
		editorCtype.setContentHandler(ContentHandler.CUSTOM_CONTENT);
		editorCtype.setDescription("content type with html area tinyMCE");
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(TINY_MCE_CFG);
		String editorCFG = TestUtils.getInstance().readConfiguration(in);
		editorCtype.setConfiguration(editorCFG);
		ContentTypesFrame frame = contentTypeService.createContentType(getTestSession(), editorCtype);
		boolean isCreated = frame.verifyIsPresent(contentTypeName);
		if (!isCreated)
		{
			Assert.fail("error during creation of a content type!");
		}

		ContentRepository repository = new ContentRepository();
		repository.setName("editor" + Math.abs(new Random().nextInt()));
		repositoryService.createContentRepository(getTestSession(), repository);
		getTestSession().put(EDITOR_REPOSITORY_KEY, repository);

		ContentCategory category = new ContentCategory();
		category.setContentTypeName(contentTypeName);
		category.setName("editorcategory");
		String[] parentNames = { repository.getName() };
		category.setParentNames(parentNames);
		getTestSession().put(EDITOR_CATEGORY_KEY, category);
		repositoryService.addCategory(getTestSession(), category);
	}

	@Test(description = "Format some text as bold anditalic", dependsOnMethods="setup")
	public void editingTextTest()
	{
		logger.info("### STARTED:");
		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyBoldItalic(getTestSession(), category);
		logger.info("$$$$ FINISHED: Format some text as bold anditalic");
	}

	 @Test(dataProvider ="parseAlignmentData",description = "Type text, mark them and click the different alignment buttons, center align, right align, full align and left align.", dependsOnMethods="setup")
	public void textAlignmentTest(AlignmentText alignment)
	{
		logger.info("### STARTED: test text alignment:" + alignment.getValue());
		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyTextAlignment(getTestSession(), category, alignment);
		logger.info("$$$$ FINISHED verify text with text alignment:" + alignment.getValue());
	}

	@DataProvider
	private Object[][] parseAlignmentData()
	{
		return new Object[][] {

		{ AlignmentText.LEFT }, { AlignmentText.RIGHT }, { AlignmentText.CENTER }, { AlignmentText.FULL } };
	}

	@Test(description = "Create an anchor some place in the text. Verify: an anchor symbol appears at that spot. ", dependsOnMethods = "setup")
	public void addAnchorTest()
	{
		logger.info("### STARTED: test add an anchor in the text");
		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyAddAnchorInText(getTestSession(), category);

		logger.info("$$$$ FINISHED verify text with text alignment:");
	}
	
	@Test(description = "Insert Horizontal line ",dependsOnMethods ="setup")
	public void addHorizontalLineTest()
	{
		logger.info("### STARTED: Insert Horizontal line ");
		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyAddHorizontalLine(getTestSession(), category);
		logger.info("$$$$ FINISHED Insert Horizontal line ");
	}

	 @Test(description="Creating a link in the text", dependsOnMethods = "setup")
	 public void linkAndUnlinkTest()
	{
		 logger.info("### STARTED: Creating a link in the text");
		 ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyLinkUnlink(getTestSession(), category);
		 logger.info("$$$$ FINISHED verify text with text alignment:");
		
	}

	@Test(description = "verify insert image", dependsOnMethods = "setup")
	public void addImageTest()
	{
		logger.info("### STARTED: verify insert image:");
		//1. create category and upload image.
		
		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		//2. add image to content:
		tinyMCEService.verifyInsertImage(getTestSession(), category);
		logger.info("$$$$ FINISHED verify insert image:");

	}
	@Test(description = "verify insert Table ", dependsOnMethods = "setup")
	public void insertTableTest()
	{
		logger.info("### STARTED: verify insert Table :");
		
		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyInsertTable(getTestSession(), category);
		logger.info("$$$$ FINISHED verify insert Table ");

	}

}
