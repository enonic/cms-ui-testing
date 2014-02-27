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
import com.enonic.autotests.model.userstores.AclEntry.CategoryAvailableOperations;
import com.enonic.autotests.model.userstores.AclEntry.ContentAvailableOperations;
import com.enonic.autotests.model.userstores.AclEntry.PrincipalType;
import com.enonic.autotests.model.userstores.BuiltInGroups;
import com.enonic.autotests.model.userstores.PermissionOperation;
import com.enonic.autotests.model.userstores.User;
import com.enonic.autotests.pages.adminconsole.LeftMenuFrame;
import com.enonic.autotests.pages.adminconsole.content.ContentsTableFrame;
import com.enonic.autotests.services.AccountService;
import com.enonic.autotests.services.PageNavigator;
import com.enonic.autotests.utils.TestUtils;

/**
 * Tests for Developer's 'Access rights'
 *
 */
public class DeveloperAclTest extends BaseTest
{
	
	private AccountService accountService = new AccountService();
	
	private final String PASSWORD = "1q2w3e";
	private final String DEV_USER_KEY = "dev_contributor_key";
	private final String DEV_CONTRIBUTOR_CONTENT_KEY = "dev_content_key";
	private final String CONTENT_NAME = "devcontent";
	private final String TINY_MCE_CFG = "test-data/contenttype/tiny-editor.xml";
	private final String DEVELOPER_CATEGORY_KEY = "dev_cat_key";

	@Test(description = "add user to  check 'Access rights' for 'Developer'")
	public void createUserTest()
	{
		User user = new User();
		user.setPassword(PASSWORD);
		String name = "developer" + Math.abs(new Random().nextInt());
		user.setName(name);
		user.setEmail(name + "@mail.com");
		accountService.addUser(getTestSession(), user);
		getTestSession().put(DEV_USER_KEY, user);
	}
	

	@Test(description = "add user to Developers group", dependsOnMethods = "createUserTest")
	public void addUserToDevelopersTest()
	{
		logger.info("STARTED###:  add user to the  Developers group");
		User dev = (User) getTestSession().get(DEV_USER_KEY);
		List<String> groups = new ArrayList<>();
		groups.add(BuiltInGroups.DEVELOPERS.getValue());
		dev.setGroups(groups);

		accountService.editUser(getTestSession(), dev.getName(), dev);
		logger.info("FINISHED ###:   user with name:"+dev.getName() +" was added  to the Expert Contributors group");
	}
	


	@Test(description = "add catrgory and content, admin browse  allowed", dependsOnMethods = "addUserToDevelopersTest")
	public void addContentAndGrantAccessTest()
	{
		logger.info("STARTED###:  Setup category with content. Give user read-update access ");
		// create content type with editor
		ContentType editorCtype = new ContentType();
		String contentTypeName = "TinyEditor";
		editorCtype.setName(contentTypeName);
		editorCtype.setContentHandler(ContentHandler.CUSTOM_CONTENT);
		editorCtype.setDescription("content type with html area tinyMCE");
		InputStream in = ContributorACLCategoryBrowseTest.class.getClassLoader().getResourceAsStream(TINY_MCE_CFG);
		String editorCFG = TestUtils.getInstance().readConfiguration(in);
		editorCtype.setConfiguration(editorCFG);
		
		boolean isExist = contentTypeService.findContentType(getTestSession(), contentTypeName);
		if (!isExist)
		{
			contentTypeService.createContentType(getTestSession(), editorCtype);
			logger.info("New content type with 'custom content' handler was created");
		}
				
        //2. create a repository
		ContentRepository repository = new ContentRepository();
		repository.setName("devrepo" + Math.abs(new Random().nextInt()));
		repositoryService.createContentRepository(getTestSession(), repository);
		logger.info("repository was created. repository  name: " + repository.getName());
		
		
        //3. start to add category to the repository
		ContentCategory category = new ContentCategory();
		category.setContentTypeName(contentTypeName);
		category.setName("cat");
		String[] parentNames = { repository.getName() };
		category.setParentNames(parentNames);
		// 4. set ACL('Read' permission and 'Browse' for Developer) to Category:
		List<AclEntry> catAclEntries = new ArrayList<>();
		AclEntry categoryAclEntry = new AclEntry();
		String principalName = ((User) getTestSession().get(DEV_USER_KEY)).getName();
		categoryAclEntry.setPrincipalName(principalName);
		
		categoryAclEntry.setType(PrincipalType.USER);
		List<PermissionOperation> categoryPerm = new ArrayList<>();
		categoryPerm.add(PermissionOperation.with().name(CategoryAvailableOperations.BROWSE.getUiValue()).allow(true).build());
		categoryPerm.add(PermissionOperation.with().name(CategoryAvailableOperations.READ.getUiValue()).allow(true).build());
		categoryAclEntry.setPermissions(categoryPerm);
		
		catAclEntries.add(categoryAclEntry);
		category.setAclEntries(catAclEntries);
		
		
		//5. create category in the just added repository
		repositoryService.addCategory(getTestSession(), category);
		getTestSession().put(DEVELOPER_CATEGORY_KEY,category);
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
		getTestSession().put(DEV_CONTRIBUTOR_CONTENT_KEY, content);
		logger.info("content was added to the categry with name: " + content.getDisplayName());
		
		logger.info("finished $$$:  Setup category with content. Give user read-update access ");

	} 
	@Test(description= "verify 'static resources' link is present in the menu", dependsOnMethods ="addUserToDevelopersTest")
	public void verifyStaticResLinkTest()
	{  
		logger.info("STARTED ###:  vverify 'static resources' link is present in the menu");
		//1 set user for login
		User developer = (User) getTestSession().get(DEV_USER_KEY);
		getTestSession().setUser(developer);
		//2. open console and verify: 'static resources' is present
		boolean isPresent = PageNavigator.isPresentMenuLink(getTestSession(), LeftMenuFrame.STATIC_RES_MENU_ITEM_XPATH);
		Assert.assertTrue(isPresent,"the link 'static resources should be present in the left menu'");
		logger.info("finished $$$:  verify 'static resources' link is present in the menu");
	}
	
	@Test(description= "verify 'Content types' link is present in the menu", dependsOnMethods ="addUserToDevelopersTest")
	public void verifyContentTypeLinkTest()
	{   
		logger.info("STARTED ###:  verify 'Content types' link is present in the menu");
		//1. set user for login
		User developer = (User) getTestSession().get(DEV_USER_KEY);
		getTestSession().setUser(developer);
		//2. open console and verify: 'content types' is present
		boolean isPresent = PageNavigator.isPresentMenuLink(getTestSession(), LeftMenuFrame.CONTENT_TYPES_MENU_ITEM_XPATH);
		Assert.assertTrue(isPresent,"the link 'Content types' should be present in the left menu");
		logger.info("finished $$$:  verify 'Content types' link is present in the menu");
	}
	
	@Test(description= "verify access to content with explicitly granted access", dependsOnMethods ="addContentAndGrantAccessTest")
	public void verifyAccessToContent()
	{   
		logger.info("STARTED ###:  verify access to content with explicitly granted access");
		//1. set user for login
		User developer = (User) getTestSession().get(DEV_USER_KEY);
		getTestSession().setUser(developer);
		ContentCategory category = (ContentCategory) getTestSession().get(DEVELOPER_CATEGORY_KEY);
		boolean result = repositoryService.isCategoryPresent(getTestSession(), category.getName(), category.getParentNames());

		ContentsTableFrame frame = repositoryService.findCategoryInContentAndOpen(getTestSession(), category);
		result = frame.isContentPresentInTable(CONTENT_NAME);
		TestUtils.getInstance().saveScreenshot(getTestSession());
		Assert.assertTrue(result, "content was not found!");
		logger.info("finished $$$:  verify access to content with explicitly granted access");
	}
}
