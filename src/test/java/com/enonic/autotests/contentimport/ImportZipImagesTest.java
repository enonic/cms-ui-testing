package com.enonic.autotests.contentimport;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.io.FilenameUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.BaseTest;
import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.pages.adminconsole.content.ContentsTableFrame;

public class ImportZipImagesTest extends BaseTest
{
	private String TEST_REPOSITORY_KEY = "test_repository_key";
	private String IMAGE_CATEGORY_KEY = "image_category_key";
	private String IMPORT_IMAGES_ZIP = "test-data/contentimport/import-img.zip";
	
	@Test
	public void setup()
	{
		createImageCType();
		createRepository();
		createCategoryForImages();
	}

	@Test(dependsOnMethods = "setup",description = "Go to an image archive and click import. Add a zip-file with images, and verify that all images are imported to the archive. ")
	public void importZipImagesTest() throws IOException
	{
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(IMAGE_CATEGORY_KEY);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };
		
		
		ContentsTableFrame tableUI = contentService.doImportZipFile(getTestSession(), IMPORT_IMAGES_ZIP , false, pathToCategory);
		ZipFile zipfile = getZipFile(IMPORT_IMAGES_ZIP);
		Assert.assertTrue(isExistsOnUI(zipfile, tableUI), "expected content names and actual names are not equal!");
		logger.info("file: " + IMPORT_IMAGES_ZIP + "has imported");
	}

	
	
	private ZipFile getZipFile(String zipFileName) throws ZipException, IOException
	{
		URL dirURL = this.getClass().getClassLoader().getResource(IMPORT_IMAGES_ZIP);
		File file = null;
		try
		{
			file = new File(dirURL.toURI());
		} catch (URISyntaxException e)
		{

		}
		return new ZipFile(file);
	}
	
	private boolean isExistsOnUI(ZipFile zipFile, ContentsTableFrame table) throws ZipException, IOException
	{ 
		boolean result = true;
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements())
		{
			ZipEntry inputEntry = (ZipEntry) entries.nextElement();

			result &= table.isContentPresentInTable(FilenameUtils.removeExtension(inputEntry.getName()));
		}
		zipFile.close();
		return result;
	}
	
	private void createRepository()
	{
		ContentRepository repository = new ContentRepository();
		repository.setName("images" + Math.abs(new Random().nextInt()));
		repositoryService.createContentRepository(getTestSession(), repository);
		getTestSession().put(TEST_REPOSITORY_KEY, repository);

	}
	
	private void createCategoryForImages()
	{
		ContentCategory newCategory = new ContentCategory();
		newCategory.setContentTypeName(IMAGE_CONTENTTYPE_NAME);
		newCategory.setName("importCategory");
		ContentRepository repository = (ContentRepository)getTestSession().get(TEST_REPOSITORY_KEY);
		String[] parentNames = { repository.getName() };
		newCategory.setParentNames(parentNames);
		getTestSession().put(IMAGE_CATEGORY_KEY, newCategory);
		repositoryService.addCategory(getTestSession(), newCategory);
	}
	

}
