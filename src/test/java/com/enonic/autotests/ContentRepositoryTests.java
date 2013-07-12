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
import com.enonic.autotests.pages.v4.adminconsole.content.AbstractContentTableView;
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
		logger.info("add category to the repository. repsitory has no a TopCategory. Repository name is  " + repositoryList.get(0).getName());
		ContentRepository repository = findRepositoryByName("notopcategory");
		ContentCategory newcategory = new ContentCategory();
		newcategory.setName("testCategory");
		newcategory.setContentTypeName("File");
		newcategory.setDescription("Files category.");
		String[] names = {repository.getName()};
		newcategory.setParentNames(names);

		repositoryService.addCategory(getTestSession(), newcategory);
		boolean isCreated = repositoryService.findCategoryByPath(getTestSession(),"testCategory", names);
		Assert.assertTrue(isCreated,"new added category was not found!"+ newcategory.getName());
		
	}

	///==================================================================================================

	@Test(dependsOnMethods = "createContentRepository", dataProvider = "addContent", dataProviderClass = ContentRepositoryProvider.class)
	public void addContentTest(AbstractContentXml xmlContent)
	{
		Content<?> content = ContentConvertor.convertXmlDataToContent(xmlContent);
		List<ContentRepository> repositoryList = (List) getTestSession().get(REPOSITORY_LIST);

		ContentRepository repository = findRepositoryByHandler(repositoryList, xmlContent.getContentHandler());

		String ctypeName = repository.getContentTypeName();
		ContentCategory category = extractCategoryFromTestData(ctypeName, repository.getCategories());
		String[] parents = new String[]{repository.getName()};
		category.setParentNames(parents);
		repositoryService.addCategory(getTestSession(), category);
		String[] pathToContent = new String[]{repository.getName(),category.getName()};
		content.setParents(pathToContent );
		AbstractContentTableView frame = repositoryService.addContent(getTestSession(), repository, content);
		String fullContentName = buildFullContentName(xmlContent, pathToContent);
		boolean result = frame.verifyContentInTableByName(fullContentName );
		logger.info("case-info: Content visible in category view:"+result );
		List<String> contentNames = repositoryService.doSearchContentByName(getTestSession(), xmlContent.getDisplayName());
		
		String fullName = buildFullContentName(xmlContent, repository.getName(),category.getName());
		result = contentNames.contains(fullName);
		logger.info("case-info: Content searchable: "+ result);
		Assert.assertTrue(result,"new added content is not searchable");
	}

	
	private String buildFullContentName(AbstractContentXml xmlContent,String ...parentCategories)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("/");
		for(String s:parentCategories)
		{
			sb.append(s).append("/");
		}
		
		sb.append(xmlContent.getDisplayName());
		return sb.toString(); 
	}


	/**
	 * Finds category in the test-data
	 * 
	 * @param ctypeName
	 * @param categories
	 * @return
	 */
	private ContentCategory extractCategoryFromTestData(String ctypeName, List<ContentCategory> categories)
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
