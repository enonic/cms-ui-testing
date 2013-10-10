package com.enonic.autotests.contentimport;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.BaseTest;
import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.model.ContentHandler;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.model.ImageContentInfo;
import com.enonic.autotests.pages.adminconsole.content.AbstractContentTableView;
import com.enonic.autotests.pages.adminconsole.content.ContentsTableFrame;
import com.enonic.autotests.pages.adminconsole.contenttype.ContentTypesFrame;
import com.enonic.autotests.services.ContentService;
import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.services.RepositoryService;
import com.enonic.autotests.testdata.contenttype.ContentConvertor;
import com.enonic.autotests.utils.Person;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.utils.XmlReader;

public class AddEntryImageFileContent extends BaseTest
{

	private ContentTypeService contentTypeService = new ContentTypeService();
	
	private RepositoryService repositoryService = new RepositoryService();
	
	private ContentService contentService = new ContentService();

	private final String IMAGE_CTYPE_KEY = "image_ctype";

	private final String PERSON_IMAGE_CFG = "test-data/contenttype/person-image.xml";

	/** key for saving a Content Type in the Session */
	private String PERSON_CTYPE_KEY = "person-events-ctype";
	private String PERSON_CATEGORY_KEY = "person-category";
	private String IMAGES_CATEGORY_KEY = "images-category";
	private String REPOSITORY_KEY = "repository";
	private String PERSONS_WITH_IMAGE_TO_IMPORT = "test-data/contentimport/persons-mapped-image.xml";
	private String CONTENT_KEY = "content_key";

	/**
	 * Create Person and Image content types.
	 */
	private void createContentTypes()
	{
		ContentType imageCtype = new ContentType();
		String contentTypeName = "Image" + Math.abs(new Random().nextInt());
		imageCtype.setName(contentTypeName);
		imageCtype.setContentHandler(ContentHandler.IMAGES);
		imageCtype.setDescription("images content type");
        //1.  create IMAGE content type
		ContentTypesFrame frame = contentTypeService.createContentType(getTestSession(), imageCtype);
		boolean isCreated = frame.verifyIsPresent(contentTypeName);
		if (!isCreated)
		{
			Assert.fail("error during creation of a content type!");
		}
		getTestSession().put(IMAGE_CTYPE_KEY, imageCtype);

        //2. create Person content type with " <input name='face' type='image'>"
		ContentType personType = new ContentType();
		contentTypeName = "person" + Math.abs(new Random().nextInt());
		personType.setName(contentTypeName);
		personType.setContentHandler(ContentHandler.CUSTOM_CONTENT);
		personType.setDescription("person content type");
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(PERSON_IMAGE_CFG);
		String personCFG = TestUtils.getInstance().readConfiguration(in);
		personType.setConfiguration(personCFG);
		frame = contentTypeService.createContentType(getTestSession(), personType);
		isCreated = frame.verifyIsPresent(contentTypeName);
		if (!isCreated)
		{
			Assert.fail("error during creation of a content type!");
		}
		getTestSession().put(PERSON_CTYPE_KEY, personType);
		logger.info("New content type  was created name: " + contentTypeName);

	}

	@Test(description = "set up: create content types: Person and Image")
	public void settings()
	{
		logger.info("@@@@@@@@@@@@@@@@  STARTED settings for  AddEntryImageFileContent  ");
		logger.info("set up: create content types: Person and Image");
		// 1.create content types
		createContentTypes();
		// 2. create content repositories:
		ContentRepository repository = new ContentRepository();
		repository.setName("person" + Math.abs(new Random().nextInt()));
		repositoryService.createContentRepository(getTestSession(), repository);
		getTestSession().put(REPOSITORY_KEY, repository);

		ContentCategory personCategory = new ContentCategory();
		personCategory.setContentTypeName(((ContentType) getTestSession().get(PERSON_CTYPE_KEY)).getName());
		personCategory.setName("personWithImage");
		String[] parentNames = { repository.getName() };
		personCategory.setParentNames(parentNames);
		getTestSession().put(PERSON_CATEGORY_KEY, personCategory);
		repositoryService.addCategory(getTestSession(), personCategory);

		// create category for upload image:
		ContentCategory imageCategory = new ContentCategory();
		imageCategory.setContentTypeName(((ContentType) getTestSession().get(IMAGE_CTYPE_KEY)).getName());
		imageCategory.setName("images");
		String[] parentNames2 = { repository.getName() };
		imageCategory.setParentNames(parentNames2);
		getTestSession().put(IMAGES_CATEGORY_KEY, imageCategory);
		repositoryService.addCategory(getTestSession(), imageCategory);
		logger.info("$$$$ FINISHED: set up settings for  AddEntryImageFileContent");

	}

	@Test(description = "upload a image, the key of this image will use in Imported resource", dependsOnMethods = "settings")
	public void uploadImageAndReadKey()
	{
		logger.info("#### STARTED: upload image and gets KEY for this new resource");
		// jpg file to upload:
		String pathToFile = "test-data/contentimport/face.jpg";
		ContentRepository repository = (ContentRepository) getTestSession().get(REPOSITORY_KEY);
		ContentCategory ctegory = (ContentCategory) getTestSession().get(IMAGES_CATEGORY_KEY);

		Content<ImageContentInfo> content = new Content<>();
		String[] pathToContent = new String[] { repository.getName(), ctegory.getName() };
		content.setParentNames(pathToContent);
		ImageContentInfo contentTab = new ImageContentInfo();
		contentTab.setPathToFile(pathToFile);
		contentTab.setDescription("image for import test");
		content.setContentTab(contentTab);
		content.setDisplayName("face");
		content.setContentHandler(ContentHandler.IMAGES);
		//1. add image to category, category has 'IMAGES' content handler:
		AbstractContentTableView table = contentService.addimageContent(getTestSession(), content);
		//2. verify:Image is present in the table of content
		Assert.assertTrue(table.isContentPresentInTable("face"), "image was not uploaded!");
		//3. save in session a key of uploaded image 
		String key = contentService.getContentKeyPropery(getTestSession(), content);
		getTestSession().put(CONTENT_KEY, key);

		logger.info("$$$$ FINISHED: uploadImageAndReadKey");

	}

	/**
	 * For an import source to be able to refer an image content it needs to
	 * provide a content key that represents a content in an Enonic CMS
	 * installation. This means the import source must know the content key.
	 * 
	 * @throws IOException
	 * 
	 */
	@Test(dependsOnMethods = "uploadImageAndReadKey", description = "Importing content with mapped input fields of type image ")
	public void personWithImageImportTest() throws IOException
	{
		logger.info("#### STARTED:Importing content with mapped input fields of type image");
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(PERSON_CATEGORY_KEY);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };
		//1. get key from the session
		String key = (String) getTestSession().get(CONTENT_KEY);
        //2. set new key for resource
		StringBuilder sb = readAndChangeResource(key);
		File tmpfile = writeToFile("persons", sb);
		// 3. import XML formatted resource:
		logger.info("try to import file: "+ tmpfile.getAbsolutePath());
		ContentsTableFrame table = contentService.doImportTmpFileContent(getTestSession(), "person-import-xml", tmpfile, pathToCategory);
		
		List<String> namesActual = table.getContentNames();
		XmlReader xmlReader = new XmlReader();
		// 4. gets expected persons with events from the XML
		List<Person> personsFromXML = xmlReader.getPersons(PERSONS_WITH_IMAGE_TO_IMPORT);
		// 5. Verify: the number of imported persons:
		Assert.assertTrue(personsFromXML.size() == namesActual.size(), "the number of persons from UI and expected number of persons are not equals!");

		//6.  click by content and verify: image is present on the page
		logger.info("open for edit and verify: image is present on the page ");
		boolean isImagePresent = ImportUtils.checkContentKey(getSessionDriver(), table, "Fridtjof Nansen", key);
		Assert.assertTrue(isImagePresent, "image was not mapped");
		logger.info("$$$$ FINISHED:'Importing content with mapped input fields of type image' ");

	}

	/**
	 * Reads XML formatted resource and sets new Key for image resource
	 * @param imageKey
	 * @return
	 */
	private StringBuilder readAndChangeResource(String imageKey)
	{
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(PERSONS_WITH_IMAGE_TO_IMPORT);
		StringBuilder sb = new StringBuilder();
		Scanner scanner = new Scanner(in);
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			if (line.contains("face-key"))
			{
				int startindex = line.indexOf("face-key=");
				int length = "face-key=".length();
				String res = line.substring(0, startindex + length) + "\"" + imageKey + "\">";
				sb.append(res);
			} else
			{
				sb.append(line);
			}

		}
		scanner.close();
		return sb;
	}

	/**
	 * Creates file in the system tempDir and writes content to the file.
	 * @param fileName name of temp file
	 * @param sb
	 * @return new created file, this file will be uploaded to a category
	 * @throws IOException
	 */
	public File writeToFile(String fileName, StringBuilder sb) throws IOException
	{
		File tempDir = new File(System.getProperty("java.io.tmpdir"));
		File tempFile = File.createTempFile(fileName, ".tmp", tempDir);
		FileWriter fileWriter = new FileWriter(tempFile, true);
		System.out.println(tempFile.getAbsolutePath());
		BufferedWriter bw = new BufferedWriter(fileWriter);
		bw.write(sb.toString());
		bw.close();
		return tempFile;
	}
}
