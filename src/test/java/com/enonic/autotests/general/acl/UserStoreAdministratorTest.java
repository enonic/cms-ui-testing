package com.enonic.autotests.general.acl;

import java.io.InputStream;
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
import com.enonic.autotests.model.ContentWithEditorInfo;
import com.enonic.autotests.model.userstores.AclEntry;
import com.enonic.autotests.model.userstores.BuiltInGroups;
import com.enonic.autotests.model.userstores.PermissionOperation;
import com.enonic.autotests.model.userstores.User;
import com.enonic.autotests.model.userstores.AclEntry.CategoryAvailableOperations;
import com.enonic.autotests.model.userstores.AclEntry.ContentAvailableOperations;
import com.enonic.autotests.model.userstores.AclEntry.PrincipalType;
import com.enonic.autotests.pages.adminconsole.LeftMenuFrame;
import com.enonic.autotests.pages.adminconsole.content.ContentsTableFrame;
import com.enonic.autotests.services.AccountService;
import com.enonic.autotests.services.ContentService;
import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.services.PageNavigator;
import com.enonic.autotests.services.RepositoryService;
import com.enonic.autotests.utils.TestUtils;

public class UserStoreAdministratorTest extends BaseTest
{

	private AccountService accountService = new AccountService();
	private ContentTypeService contentTypeService = new ContentTypeService();

	private RepositoryService repositoryService = new RepositoryService();

	private ContentService contentService = new ContentService();
	private final String PASSWORD = "1q2w3e";
	private final String STORE_ADMIN_USER_KEY = "sadmin_contributor_key";
	private final String CONTENT_NAME = "storeadmcontent";
	private final String TINY_MCE_CFG = "test-data/contenttype/tiny-editor.xml";
	private final String STORE_ADMIN_CATEGORY_KEY = "sadmin_cat_key";

	@Test(description = "add user to  check 'Access rights' for 'User Store Administrator'")
	public void createUserTest()
	{
		User user = new User();
		user.setPassword(PASSWORD);
		String name = "storeadmin" + Math.abs(new Random().nextInt());
		user.setName(name);
		user.setEmail(name + "@mail.com");
		accountService.addUser(getTestSession(), user);
		boolean result = accountService.isUserPresent(getTestSession(), user.getName());
		Assert.assertTrue(result,"new created user was not found in the table ");
		getTestSession().put(STORE_ADMIN_USER_KEY, user);
	}

	@Test(description = "add user to 'Userstore Administrators' group", dependsOnMethods = "createUserTest")
	public void addUserToUserAdministratorsTest()
	{
		logger.info("STARTED###:  add user to Userstore Administrators group");
		User sadmin = (User) getTestSession().get(STORE_ADMIN_USER_KEY);
		List<String> groups = new ArrayList<>();
		groups.add(BuiltInGroups.USER_STORE_ADMIN.getValue());
		sadmin.setGroups(groups);

		accountService.editUser(getTestSession(), sadmin.getName(), sadmin);
		logger.info("FINISHED ###:   user with name:" + sadmin.getName() + " was added  to the Userstore Administrators group");
	}
	
	@Test(description= "verify 'Userstores' link is present in the menu, when user logged in to admin console", dependsOnMethods ="addUserToUserAdministratorsTest")
	public void verifyUserStoreLinkTest()
	{   
		logger.info("STARTED ###:  verify 'Userstores' link is present in the menu");
		//1. set user for login
		User developer = (User) getTestSession().get(STORE_ADMIN_USER_KEY);
		getTestSession().setUser(developer);
		//2. open console and verify: 'Userstores' is present
		boolean isPresent = PageNavigator.isPresentMenuLink(getTestSession(), LeftMenuFrame.USERSTORES_MENU_ITEM_XPATH);
		Assert.assertTrue(isPresent,"the link 'Userstores' should be present in the left menu");
		logger.info("finished $$$:  verify 'Userstores' link is present in the menu");
	}
	
	
	@Test(description = "Setup category with  'read' and  'admin-browse' on category", dependsOnMethods = "addUserToUserAdministratorsTest")
	public void addContentAndGrantAccessTest()
	{
		logger.info("STARTED###: Setup category with  'read' and  'admin-browse' on category");
		// create content type with editor		
		String contentTypeName = "TinyEditor";
	
		InputStream in = ContributorACLCategoryBrowseTest.class.getClassLoader().getResourceAsStream(TINY_MCE_CFG);
		String editorCFG = TestUtils.getInstance().readConfiguration(in);
		ContentType editorCtype =  ContentType.with().name(contentTypeName).description("content type with html area tinyMCE")
				.contentHandler(ContentHandler.CUSTOM_CONTENT).configuration(editorCFG).build();
		boolean isExist = contentTypeService.findContentType(getTestSession(), contentTypeName);
		if (!isExist)
		{
			contentTypeService.createContentType(getTestSession(), editorCtype);
			logger.info("New content type with 'custom content' handler was created");
		}
				
        //2. create a repository
		ContentRepository repository = new ContentRepository();
		repository.setName("sadmrepo" + Math.abs(new Random().nextInt()));
		repositoryService.createContentRepository(getTestSession(), repository);
		logger.info("repository was created. repository  name: " + repository.getName());
		
		
        //3. start to add category to the repository
		ContentCategory category = new ContentCategory();
		category.setContentTypeName(contentTypeName);
		category.setName("cat");
		String[] parentNames = { repository.getName() };
		category.setParentNames(parentNames);
		// 4. set ACL('Read' permission and 'Browse' for 'User Store Admin') to Category:
		List<AclEntry> catAclEntries = new ArrayList<>();
		AclEntry categoryAclEntry = new AclEntry();
		String principalName = ((User) getTestSession().get(STORE_ADMIN_USER_KEY)).getName();
		categoryAclEntry.setPrincipalName(principalName);
		
		categoryAclEntry.setType(PrincipalType.USER);
		List<PermissionOperation> categoryPerm = new ArrayList<>();
		PermissionOperation op1 = PermissionOperation.with().allow(true).name(CategoryAvailableOperations.BROWSE.getUiValue()).build();
		PermissionOperation op2 = PermissionOperation.with().allow(true).name(CategoryAvailableOperations.READ.getUiValue()).build();
		
		categoryPerm.add(op1);
		categoryPerm.add(op2);
		categoryAclEntry.setPermissions(categoryPerm);
		
		catAclEntries.add(categoryAclEntry);
		category.setAclEntries(catAclEntries);
		
		
		//5. create category in the just added repository
		repositoryService.addCategory(getTestSession(), category);
		getTestSession().put(STORE_ADMIN_CATEGORY_KEY,category);
		logger.info("category was added. category  name: " + category.getName());
		
       // 6. add  content with editor to the category
		String[] pathToContent = new String[] { repository.getName(), category.getName() };

		Content<ContentWithEditorInfo> content = new Content<>();
		content.setDisplayName(CONTENT_NAME);
		content.setParentNames(pathToContent);
		ContentWithEditorInfo contentTab = new ContentWithEditorInfo();
		contentTab.setHtmlareaText("test text");
		content.setContentTab(contentTab);
		content.setContentHandler(ContentHandler.CUSTOM_CONTENT);
		
		List<AclEntry> contentAclEntries = new ArrayList<>();
		AclEntry contentAclEntry = new AclEntry();
		contentAclEntry.setPrincipalName(principalName);		
		contentAclEntry.setType(PrincipalType.USER);
		
		
		List<PermissionOperation> contentPermissions = new ArrayList<>();
		contentPermissions.add(PermissionOperation.with().name(ContentAvailableOperations.READ.getUiValue()).allow(true).build());
		contentPermissions.add(PermissionOperation.with().name(ContentAvailableOperations.UPDATE.getUiValue()).allow(true).build());
		contentAclEntry.setPermissions(contentPermissions);
		
		contentAclEntries.add(contentAclEntry);
		content.setAclEntries(contentAclEntries);
		
		contentService.addContentWithEditor(getTestSession(), content);
		logger.info("content was added to the categry with name: " + content.getDisplayName());
		
		logger.info("finished $$$:  Setup category with  'read' and  'admin-browse' on category ");
	} 
	
	@Test(description = "Verify that category is visible and content present in this category", dependsOnMethods = "addContentAndGrantAccessTest")
	public void verifyCategoryPresent()
	{
		logger.info("Verify that category is visible and content present in this category");
		User user = (User) getTestSession().get(STORE_ADMIN_USER_KEY );
		getTestSession().setUser(user);
		ContentCategory category = (ContentCategory) getTestSession().get(STORE_ADMIN_CATEGORY_KEY);
		boolean result = repositoryService.isCategoryPresent(getTestSession(), category.getName(), category.getParentNames());

		
		Assert.assertTrue(result, String.format("category with name %s was not found!",category.getName()));
		
		ContentsTableFrame frame = repositoryService.findCategoryInContentAndOpen(getTestSession(), category);
		result = frame.isContentPresentInTable(CONTENT_NAME);
		TestUtils.getInstance().saveScreenshot(getTestSession());
		Assert.assertTrue(result, "content was not found!");
		logger.info("Finished $$$$ Verify that category is visible and content present in this category");

	}
	

}
