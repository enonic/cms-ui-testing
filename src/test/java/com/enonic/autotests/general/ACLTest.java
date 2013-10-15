package com.enonic.autotests.general;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.BaseTest;
import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.model.ContentHandler;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.model.ImageContentInfo;
import com.enonic.autotests.model.userstores.AclEntry;
import com.enonic.autotests.model.userstores.AclEntry.AvailableOperations;
import com.enonic.autotests.model.userstores.AclEntry.PrincipalType;
import com.enonic.autotests.model.userstores.BuiltInGroups;
import com.enonic.autotests.model.userstores.User;
import com.enonic.autotests.pages.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.pages.adminconsole.content.ContentsTableFrame;
import com.enonic.autotests.services.AccountService;
import com.enonic.autotests.services.ContentService;
import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.services.PageNavigator;
import com.enonic.autotests.services.RepositoryService;
import com.enonic.autotests.utils.TestUtils;

public class ACLTest extends BaseTest
{

	private AccountService accountService = new AccountService();
	private ContentTypeService contentTypeService = new ContentTypeService();

	private RepositoryService repositoryService = new RepositoryService();

	private ContentService contentService = new ContentService();
	private final String PASSWORD = "1q2w3e";
	private final String CONTRIBUTOR_USER_KEY = "contributor_key";
	private final String CONTRIBUTOR_CATEGORY_KEY = "contributor_cat_key";
	private final String CONTENT_NAME = "contentacl";

	@Test(description = "add user to  check AÑL-contributor")
	public void createUserTest()
	{
		User user = new User();
		user.setPassword(PASSWORD);
		String name = "contrib" + Math.abs(new Random().nextInt());
		user.setName(name);
		user.setEmail(name + "@mail.com");
		accountService.addUser(getTestSession(), user);
		getTestSession().put(CONTRIBUTOR_USER_KEY, user);
	}

	@Test(description = "add user to Contrubutors group", dependsOnMethods = "createUserTest")
	public void addUserToContributorsTest()
	{
		User contrubutor = (User) getTestSession().get(CONTRIBUTOR_USER_KEY);
		List<String> groups = new ArrayList<>();
		groups.add(BuiltInGroups.CONTRIBUTORS.getValue());
		contrubutor.setGroups(groups);

		accountService.editUser(getTestSession(), contrubutor.getName(), contrubutor);
	}

	@Test(description = "add catrgory and content, admin browse not allowed", dependsOnMethods = "addUserToContributorsTest")
	public void addContentAndGrantAccessTest()
	{
		ContentType imagesType = new ContentType();
		String contentTypeName = "Image";
		imagesType.setName(contentTypeName);
		imagesType.setContentHandler(ContentHandler.IMAGES);
		imagesType.setDescription("images ctype");
		boolean isExist = contentTypeService.findContentType(getTestSession(), contentTypeName);
		if (!isExist)
		{
			contentTypeService.createContentType(getTestSession(), imagesType);
			logger.info("New content type with 'Images' handler was created");
		}

		ContentRepository repository = new ContentRepository();
		repository.setName("acltest" + Math.abs(new Random().nextInt()));
		repositoryService.createContentRepository(getTestSession(), repository);
		// getTestSession().put(EDITOR_REPOSITORY_KEY, repository);

		ContentCategory category = new ContentCategory();
		category.setContentTypeName(contentTypeName);
		category.setName("contrib");
		String[] parentNames = { repository.getName() };
		category.setParentNames(parentNames);
		List<AclEntry> aclEntries = new ArrayList<>();
		AclEntry entry = new AclEntry();
		String principalName = ((User) getTestSession().get(CONTRIBUTOR_USER_KEY)).getName();
		entry.setPrincipalName(principalName);
		
		entry.setType(PrincipalType.USER);
		List<String> operations = new ArrayList<>();
		operations.add(AvailableOperations.READ.getUiValue());
		entry.setPermissions(operations);
		entry.setAllow(true);
		aclEntries.add(entry);
		category.setAclEntries(aclEntries);
		getTestSession().put(CONTRIBUTOR_CATEGORY_KEY, category);
		repositoryService.addCategory(getTestSession(), category);

		String pathToFile = "test-data/contentrepository/test.jpg";
		Content<ImageContentInfo> content = new Content<>();
		String[] pathToContent = new String[] { repository.getName(), category.getName() };
		content.setParentNames(pathToContent);
		ImageContentInfo contentTab = new ImageContentInfo();
		contentTab.setPathToFile(pathToFile);
		contentTab.setDescription("image for import test");
		content.setContentTab(contentTab);
		content.setDisplayName(CONTENT_NAME);
		content.setContentHandler(ContentHandler.IMAGES);
		contentService.addimageContent(getTestSession(), content);

	} 

	@Test(description = "Verify that user with no admin-browse is not able to browse category", dependsOnMethods = "addContentAndGrantAccessTest")
	public void verifyBrowseCategory1()
	{
		User contrubutor = (User) getTestSession().get(CONTRIBUTOR_USER_KEY);
		getTestSession().setUser(contrubutor);
		PageNavigator.navgateToAdminConsole(getTestSession());
		ContentCategory category = (ContentCategory) getTestSession().get(CONTRIBUTOR_CATEGORY_KEY);
		boolean result = repositoryService.isCategoryPresent(getTestSession(), category.getName(), category.getParentNames());

		Assert.assertFalse(result, "user able to browse category!");

	}
	
	@Test(description = "change the category by adding admin-browse for user", dependsOnMethods = "verifyBrowseCategory1")
	public void editCaregoryAddAdminBrowsePermissionTest()
	{
		ContentCategory categoryToEdit = (ContentCategory) getTestSession().get(CONTRIBUTOR_CATEGORY_KEY);
		
		AclEntry entry = new AclEntry();
		String principalName = ((User) getTestSession().get(CONTRIBUTOR_USER_KEY)).getName();
		entry.setPrincipalName(principalName);
		
		entry.setType(PrincipalType.USER);
		List<String> operations = new ArrayList<>();
		operations.add(AvailableOperations.BROWSE.getUiValue());
		entry.setPermissions(operations);
		entry.setAllow(true);
		categoryToEdit.getAclEntries().add(entry);
		getTestSession().setUser(null);
		repositoryService.editCategory(getTestSession(), categoryToEdit);		

	}

	@Test(description = "Verify that user is allowed to browse category", dependsOnMethods = "editCaregoryAddAdminBrowsePermissionTest")
	public void verifyBrowseCategory2()
	{
		User contrubutor = (User) getTestSession().get(CONTRIBUTOR_USER_KEY);
		getTestSession().setUser(contrubutor);
		ContentCategory category = (ContentCategory) getTestSession().get(CONTRIBUTOR_CATEGORY_KEY);
		boolean result = repositoryService.isCategoryPresent(getTestSession(), category.getName(), category.getParentNames());

		ContentsTableFrame frame = repositoryService.findCategoryInContentAndOpen(getTestSession(), category);
		result = frame.isContentPresentInTable(CONTENT_NAME);
		TestUtils.getInstance().saveScreenshot(getTestSession());
		Assert.assertTrue(result, "content was not found!");

	}

}
