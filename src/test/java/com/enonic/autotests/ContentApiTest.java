package com.enonic.autotests;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.model.ContentHandler;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.services.ContentService;
import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.services.RepositoryService;
import com.enonic.autotests.testdata.contenttype.ContentConvertor;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.cms.api.client.ClientException;
import com.enonic.cms.api.client.ClientFactory;
import com.enonic.cms.api.client.RemoteClient;
import com.enonic.cms.api.client.model.ContentDataInputUpdateStrategy;
import com.enonic.cms.api.client.model.CreateCategoryParams;
import com.enonic.cms.api.client.model.CreateContentParams;
import com.enonic.cms.api.client.model.DeleteCategoryParams;
import com.enonic.cms.api.client.model.DeleteContentParams;
import com.enonic.cms.api.client.model.UpdateContentParams;
import com.enonic.cms.api.client.model.content.ContentDataInput;
import com.enonic.cms.api.client.model.content.ContentStatus;
import com.enonic.cms.api.client.model.content.GroupInput;
import com.enonic.cms.api.client.model.content.HtmlAreaInput;
import com.enonic.cms.api.client.model.content.SelectorInput;
import com.enonic.cms.api.client.model.content.TextAreaInput;
import com.enonic.cms.api.client.model.content.TextInput;
import com.enonic.cms.api.client.model.content.UrlInput;

public class ContentApiTest extends BaseTest
{

	private ContentTypeService contentTypeService = new ContentTypeService();
	private RepositoryService repositoryService = new RepositoryService();
	private ContentService contentService = new ContentService();

	private final String ARTICLE_CFG = "test-data/contenttype/article.xml";
	private String CONTENTTYPE_NAME = "article";
	private String REMOTE_CLIENT_KEY = "client_remote";
	private String REPOSITORY_API_TEST_KEY = "repository_clientapi";
	private String TESTAPI_CATEGORY_KEY = "cat_api_key";
	private String REPOSITORY_KEYVALUE_KEY = "repository_key_value";
	private String ARTICLE_CONTENT_KEY = "article_content_key";
	private final String CATEGORY_NAME = "apitest";
	private final String UPDATED_ARTICLE_NAME = "UpdatedArticle";

	@Test(description = "create article content type.")
	public void setup()
	{
		logger.info("#### STARTED: setup: create article content type and Content Repository for article-content");
		String clientUrl = getTestSession().getBaseUrl().substring(0, getTestSession().getBaseUrl().indexOf("admin")) + "rpc/bin";
		logger.info("clientUrl: " + clientUrl);
		RemoteClient client = ClientFactory.getRemoteClient(clientUrl);

		logger.info("try to login: login= admin, password=password");
		client.login("admin", "password");
		getTestSession().put(REMOTE_CLIENT_KEY, client);
		ContentType articleType = new ContentType();
		articleType.setName(CONTENTTYPE_NAME);
		articleType.setContentHandler(ContentHandler.CUSTOM_CONTENT);
		articleType.setDescription("article content type");
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(ARTICLE_CFG);
		String articleCFG = TestUtils.getInstance().readConfiguration(in);
		articleType.setConfiguration(articleCFG);
		// 1. create Article content type:
		boolean isExist = contentTypeService.findContentType(getTestSession(), CONTENTTYPE_NAME);
		if (!isExist)
		{
			contentTypeService.createContentType(getTestSession(), articleType);
			logger.info("New content type : 'Article' ");
		}

		// 2. create content repositories:
		ContentRepository repository = new ContentRepository();
		String repoName = "contentapi" + Math.abs(new Random().nextInt());
		repository.setName(repoName);
		repositoryService.createContentRepository(getTestSession(), repository);
		logger.info("ContentRepository : " + repoName + " was created!");
		Integer repoKey = repositoryService.getRepositoryKey(getTestSession(), repoName);
		getTestSession().put(REPOSITORY_API_TEST_KEY, repository);
		getTestSession().put(REPOSITORY_KEYVALUE_KEY, repoKey);
		logger.info("### FINISHED: setup ");
	}

	/**
	 * create category in API(article content type)
	 */
	@Test(description = "create category in API", dependsOnMethods = "setup")
	public void createCategoryInApi()
	{
		logger.info("#### STARTED: create category in API");
		String categoryName = CATEGORY_NAME;
		RemoteClient client = (RemoteClient) getTestSession().get(REMOTE_CLIENT_KEY);
		int parentCategoryKey = (Integer) getTestSession().get(REPOSITORY_KEYVALUE_KEY);
		CreateCategoryParams params = new CreateCategoryParams();
		params.parentCategoryKey = parentCategoryKey;
		params.contentTypeName = CONTENTTYPE_NAME;
		params.name = categoryName;
		Integer categoryKey = client.createCategory(params);
		logger.info("category with key= " + categoryKey + " was created!");

		ContentRepository repo = (ContentRepository) getTestSession().get(REPOSITORY_API_TEST_KEY);
		boolean isCreated = repositoryService.isCategoryPresent(getTestSession(), categoryName, repo.getName());
		Assert.assertTrue(isCreated, "Category was not found! Error during creation of category in API!");
		getTestSession().put(TESTAPI_CATEGORY_KEY, categoryKey);
		logger.info("$$$$ FINISHED: create category in API");
	}

	/**
	 * Create content in API, verify: Content visible in category view. Content
	 * searchable
	 */
	@Test(description = "Create content in API, add article contet to category, created in API", dependsOnMethods = "createCategoryInApi")
	public void addContentToCategory()
	{
		logger.info("#### STARTED: Create content in API");
		String articleContentName = "firstArticle";
		int articleKey = createArticleContent(articleContentName);
		logger.info("article with key= " + articleKey + " was created");
		// verify: Content searchable
		List<String> result = contentService.doSearchContentByName(getTestSession(), articleContentName);
		logger.info("Content searchable: " + result.contains(articleContentName));
		Assert.assertTrue(result.contains(articleContentName), "Content created with content-api was not found!");
		getTestSession().put(ARTICLE_CONTENT_KEY, articleKey);

		createArticleContent("secondArticle");
		logger.info("$$$$ FINISHED: Create content in API");
	}

	/**
	 * Update content in API, verify Content visible in category view Content
	 * (new values) searchable.
	 */
	@Test(description = "Update content in API", dependsOnMethods = "addContentToCategory")
	public void testUpdateContent()
	{
		logger.info("#### STARTED: Update content in API");
		RemoteClient client = (RemoteClient) getTestSession().get(REMOTE_CLIENT_KEY);

		UpdateContentParams params = new UpdateContentParams();
		Integer firstContentKey = (Integer) getTestSession().get(ARTICLE_CONTENT_KEY);
		params.contentKey = firstContentKey;
		params.changeComment = "comment changed";
		params.createNewVersion = true;
		params.publishFrom = new Date();
		params.publishTo = null;
		params.status = ContentStatus.STATUS_APPROVED;
		params.setAsCurrentVersion = true;
		params.updateStrategy = ContentDataInputUpdateStrategy.REPLACE_ALL;

		ContentDataInput data = new ContentDataInput(CONTENTTYPE_NAME);
		data.add(new TextInput("heading", UPDATED_ARTICLE_NAME));
		GroupInput group = data.addGroup("Related link");
		group.add(new UrlInput("url", "http://www.enonic.no"));
		group.add(new TextInput("description", "Alle vet hvem Enonic er!  Ingen behov for ytterligere forklaring!"));
		group.add(new SelectorInput("destination", "current"));
		data.add(new TextAreaInput("preface", "preface text changed"));
		data.add(new HtmlAreaInput("text", "text changed"));
		params.contentData = data;
		// update content:
		client.updateContent(params);

		// verify searchable:
		List<String> result = contentService.doSearchContentByName(getTestSession(), UPDATED_ARTICLE_NAME);

		logger.info("Content searchable: " + result.contains(UPDATED_ARTICLE_NAME));
		Assert.assertTrue(result.contains(UPDATED_ARTICLE_NAME), "updated Content  with content-api was not found!");
		ContentRepository repository = (ContentRepository) getTestSession().get(REPOSITORY_API_TEST_KEY);
		String[] parents = { repository.getName(), CATEGORY_NAME };
		com.enonic.autotests.pages.v4.adminconsole.content.ContentStatus status = contentService.getContentStatus(getTestSession(),
				UPDATED_ARTICLE_NAME, parents);
		Assert.assertTrue(status.equals(com.enonic.autotests.pages.v4.adminconsole.content.ContentStatus.PUBLISHED) || status.equals(com.enonic.autotests.pages.v4.adminconsole.content.ContentStatus.PENDING), "expected and actual status are not equals!");
		logger.info("$$$$ FINISHED: Update content in API");

	}

	/**
	 * Delete content in API, verify Content not present in category view,
	 * Content not searchable
	 */
	@Test(description = "Delete content in API, verify: Content not searchable ", dependsOnMethods = "testUpdateContent")
	public void testDeleteContent()
	{
		logger.info("#### STARTED: Delete content in API, +verify: Content not searchable");
		RemoteClient client = (RemoteClient) getTestSession().get(REMOTE_CLIENT_KEY);
		Integer firstContentKey = (Integer) getTestSession().get(ARTICLE_CONTENT_KEY);
		DeleteContentParams params = new DeleteContentParams();
		params.contentKey = firstContentKey;
		client.deleteContent(params);

		// verify searchable:
		List<String> result = contentService.doSearchContentByName(getTestSession(), UPDATED_ARTICLE_NAME);
		Assert.assertTrue(result.isEmpty(), "content with name: " + UPDATED_ARTICLE_NAME + " was found but it should be deleted ");
		logger.info("$$$$ FINISHED: Delete content in API");

	}

	/**
	 * Try to delete a category with content, but set the includeContent parameter to false.
	 *  Verify: An Exception is thrown No changes to the category or its content.
	 */
	@Test(dependsOnMethods = "testDeleteContent", expectedExceptions = ClientException.class, description = "Try to delete a category with content, but set the includeContent parameter to false.")
	public void deleteCategoryTest()
	{
		logger.info("#### STARTED: delete a category with content, but set the includeContent parameter to false");
		RemoteClient client = (RemoteClient) getTestSession().get(REMOTE_CLIENT_KEY);
		DeleteCategoryParams params = new DeleteCategoryParams();
		int key = (Integer) getTestSession().get(TESTAPI_CATEGORY_KEY);
		params.key = key;
		params.includeContent = false;
       // ClientException should be thrown:
		client.deleteCategory(params);
		
		
	}

	/**
	 * Try to delete a category with content, but set the includeContent
	 * parameter to false. Verify: An Exception is thrown No changes to the
	 * category or its content.
	 */
	@Test(dependsOnMethods = "deleteCategoryTest", description = "Try to delete a category with content, now set includeContent parameter to true. ")
	public void deleteCategoryWithContentTest()
	{
		logger.info("#### STARTED: delete a category with content, but set the includeContent parameter to true");
		RemoteClient client = (RemoteClient) getTestSession().get(REMOTE_CLIENT_KEY);
		DeleteCategoryParams params = new DeleteCategoryParams();
		int key = (Integer) getTestSession().get(TESTAPI_CATEGORY_KEY);
		params.key = key;
		params.includeContent = true;

		client.deleteCategory(params);
		ContentRepository repository = (ContentRepository) getTestSession().get(REPOSITORY_API_TEST_KEY);
		boolean isPresent = repositoryService.isCategoryPresent(getTestSession(), CATEGORY_NAME, repository.getName());
		Assert.assertFalse(isPresent, "Category with name:" + CATEGORY_NAME + " should be deleted from the repository:" + repository.getName());
		logger.info("$$$$ FINISHED: Try to delete a category with content, now set includeContent parameter to true");
	}

	private int createArticleContent(String articleName)
	{

		RemoteClient client = (RemoteClient) getTestSession().get(REMOTE_CLIENT_KEY);
		Integer categoryKey = (Integer) getTestSession().get(TESTAPI_CATEGORY_KEY);
		CreateContentParams cparams = new CreateContentParams();
		cparams.categoryKey = categoryKey;
		cparams.publishTo = null;
		cparams.status = ContentStatus.STATUS_DRAFT;
		cparams.changeComment = "article content";

		ContentDataInput data = new ContentDataInput(CONTENTTYPE_NAME);
		data.add(new TextInput("heading", articleName));
		data.add(new TextAreaInput("preface", "tttratara"));
		data.add(new HtmlAreaInput("text", "first article"));
		cparams.contentData = data;
		return client.createContent(cparams);

	}

}
