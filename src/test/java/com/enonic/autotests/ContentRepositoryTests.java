package com.enonic.autotests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.model.ContentHandler;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.pages.v4.adminconsole.content.RepositoriesListFrame;
import com.enonic.autotests.providers.ContentRepositoryProvider;
import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.services.RepositoryService;
import com.enonic.autotests.testdata.content.AbstractContentXml;
import com.enonic.autotests.testdata.content.ContentRepositoryXml;
import com.enonic.autotests.testdata.contenttype.ContentConvertor;

public class ContentRepositoryTests extends BaseTest
{

	private final String REPOSITORY_LIST = "repository_list";

	private ContentTypeService contentTypeService = new ContentTypeService();
	private RepositoryService repositoryService = new RepositoryService();

	@Test
	public synchronized void settings()
	{

		ContentType imagesType = new ContentType();
		imagesType.setName("Image");
		imagesType.setContentHandler(ContentHandler.IMAGES);
		imagesType.setDescription("content repository test");
		boolean isExist = contentTypeService.findContentType(getTestSession(), "Image");
		if (!isExist)
		{
			contentTypeService.createContentType(getTestSession(), imagesType);
		}

		ContentType filesType = new ContentType();
		filesType.setName("File");
		filesType.setContentHandler(ContentHandler.FILES);
		filesType.setDescription("content repository test");
		isExist = contentTypeService.findContentType(getTestSession(), "File");
		if (!isExist)
		{
			contentTypeService.createContentType(getTestSession(), filesType);
		}

		getTestSession().put(REPOSITORY_LIST, new ArrayList<ContentRepository>());
	}

	@Test(dependsOnMethods = "settings", description = "create new Content Repository with specified Content Type", dataProvider = "createContentRepositoryPositive", dataProviderClass = ContentRepositoryProvider.class)
	public void createContentRepository(ContentRepositoryXml contentRepoXML)
	{
		logger.info(contentRepoXML.getCaseInfo());
		ContentRepository cRepository = ContentConvertor.convertXmlDataToContentRepository(contentRepoXML);
		if (contentRepoXML.getTopCategory() != null)
		{
			String contentTypeName = contentRepoXML.getTopCategory().getContentType().getName();
			cRepository.setContentTypeName(contentTypeName);
		}

		cRepository.setName(contentRepoXML.getName() + Math.abs(new Random().nextInt()));

		// 1. create a new Repository: click by "Content" link in the left frame, and press the 'New'. 'Add category Wizard' should appears
		RepositoriesListFrame repositorIesListFrame = repositoryService.createContentRepository(getTestSession(), cRepository);

		// 2. verify is present in the table, located on the LeftFrame:
		boolean isCreated = repositorIesListFrame.verifyIsRepositoryPresentedInTable(cRepository.getName());
		Assert.assertTrue(isCreated, "new created repository was not found in the table!");
		// 3. put to the session for using in the next test.
		List<ContentRepository> repositoryList = (List) getTestSession().get(REPOSITORY_LIST);
		repositoryList.add(cRepository);

	}

	@Test(description="add category to the repository, repsitory has no a TopCategory", dependsOnMethods="createContentRepository")
	public void testAddCategory()
	{

		List<ContentRepository> repositoryList = (List) getTestSession().get(REPOSITORY_LIST);
		logger.info("add category to the repository. repository name is  " + repositoryList.get(0).getName());
		ContentRepository repository = findRepositoryByName("nocategory");
		ContentCategory newcategory = new ContentCategory();
		newcategory.setName("testCategory");
		newcategory.setContentTypeName("File");
		newcategory.setDescription("Files category.");
		newcategory.setParentName(repository.getName());

		repositoryService.addCategory(getTestSession(), newcategory);
	}

	///==================================================================================================

	@Test(dependsOnMethods = "createContentRepository", dataProvider = "addContent", dataProviderClass = ContentRepositoryProvider.class)
	public void addContentTest(AbstractContentXml xmlContent)
	{
		Content<?> content = ContentConvertor.convertXmlDataToContent(xmlContent);
		List<ContentRepository> repositoryList = (List) getTestSession().get(REPOSITORY_LIST);

		ContentRepository repository = findRepositoryByHandler(repositoryList, xmlContent.getContentHandler());

		String ctypeName = repository.getContentTypeName();
		ContentCategory category = findCategoryByContentTypeName(ctypeName, repository.getCategories());
		repositoryService.addCategory(getTestSession(), category, repository.getName());
		repositoryService.addContent(getTestSession(), repository, category.getName(), content);
		repositoryService.doSearchContentRelatedToRepository(getTestSession(), xmlContent.getDisplayName(), repository.getName());
		//TODO check content in the table.
	}

	


	private ContentCategory findCategoryByContentTypeName(String ctypeName, List<ContentCategory> categories)
	{
		for (ContentCategory cc : categories)
		{
			if (cc.getContentTypeName().equals(ctypeName))
			{
				return cc;
			}
		}
		return null;
	}

	private ContentRepository findRepositoryByHandler(List<ContentRepository> repositoryList, String expected)
	{
		for (ContentRepository repo : repositoryList)
		{
			if (repo.getTopCategory() == null)
			{
				continue;
			}
			if (repo.getTopCategory().getContentType().getContentHandler().getName().equals(expected))
			{
				return repo;
			}

		}

		return null;
	}

	private ContentRepository findRepositoryByName(String expectedName)
	{
		List<ContentRepository> repositoryList = (List) getTestSession().get(REPOSITORY_LIST);
		for (ContentRepository repo : repositoryList)
		{
			if (repo.getName().contains(expectedName))
			{
				return repo;
			}

		}

		return null;
	}

	
}
