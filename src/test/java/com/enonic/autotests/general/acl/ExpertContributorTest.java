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
import com.enonic.autotests.pages.adminconsole.content.ContentsTableFrame;
import com.enonic.autotests.services.AccountService;
import com.enonic.autotests.services.ContentService;
import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.services.RepositoryService;
import com.enonic.autotests.utils.TestUtils;

public class ExpertContributorTest extends BaseTest
{
	private AccountService accountService = new AccountService();
	private ContentTypeService contentTypeService = new ContentTypeService();

	private RepositoryService repositoryService = new RepositoryService();

	private ContentService contentService = new ContentService();
	private final String PASSWORD = "1q2w3e";
	private final String EXP_CONTRIBUTOR_USER_KEY = "exp_contributor_key";
	private final String EXP_CONTRIBUTOR_CATEGORY_KEY = "exp_contributor_cat_key";
	private final String EXP_CONTRIBUTOR_CONTENT_KEY = "exp_contributor_content_key";
	private final String CONTENT_NAME = "expertcontent";
	
	private final String TINY_MCE_CFG = "test-data/contenttype/tiny-editor.xml";

	@Test(description = "add user to  check ACL- expert contributor")
	public void createUserTest()
	{
		User user = new User();
		user.setPassword(PASSWORD);
		String name = "expertc" + Math.abs(new Random().nextInt());
		user.setName(name);
		user.setEmail(name + "@mail.com");
		accountService.addUser(getTestSession(), user);
		getTestSession().put(EXP_CONTRIBUTOR_USER_KEY, user);
	}
	

	@Test(description = "add user to Expert Contrubutors group", dependsOnMethods = "createUserTest")
	public void addUserToExpertContributorsTest()
	{
		logger.info("STARTED###:  add user to the Expert Contributors group");
		User exp = (User) getTestSession().get(EXP_CONTRIBUTOR_USER_KEY);
		List<String> groups = new ArrayList<>();
		groups.add(BuiltInGroups.EXPERT_CONTRIBUTORS.getValue());
		exp.setGroups(groups);

		accountService.editUser(getTestSession(), exp.getName(), exp);
		logger.info("FINISHED ###:   user with name:"+exp.getName() +" was added  to the 'Expert Contributors' group");
	}
	


	@Test(description = "add category and content, 'admin browse' and 'read' are allowed", dependsOnMethods = "addUserToExpertContributorsTest")
	public void addContentAndGrantAccessTest()
	{
		logger.info("STARTED###:  Setup category with content. Give user read update access ");
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
		repository.setName("expertc" + Math.abs(new Random().nextInt()));
		repositoryService.createContentRepository(getTestSession(), repository);
		logger.info("repository was created. repository  name: " + repository.getName());
		
		
        //3. start to add category to the repository
		ContentCategory category = new ContentCategory();
		category.setContentTypeName(contentTypeName);
		category.setName("cat");
		String[] parentNames = { repository.getName() };
		category.setParentNames(parentNames);
		// 4. set ACL('Read' and 'browse') for Category:
		List<AclEntry> catAclEntries = new ArrayList<>();
		AclEntry categoryAclEntry = new AclEntry();
		String principalName = ((User) getTestSession().get(EXP_CONTRIBUTOR_USER_KEY)).getName();
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
		getTestSession().put(EXP_CONTRIBUTOR_CATEGORY_KEY, category);
		
		logger.info("category was added. category  name: " + category.getName());
		
       // 6. add  content to the category
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
		getTestSession().put(EXP_CONTRIBUTOR_CONTENT_KEY, content);
		logger.info("content was added to the categry with name: " + content.getDisplayName());
		
		logger.info("finished $$$: Setup category with content. Give user 'read' and 'admin-browse' access  but not on category");

	} 
	
	@Test(description = "Verify that category is visible and content present in this category", dependsOnMethods = "addContentAndGrantAccessTest")
	public void verifyCategoryAndContentPresent()
	{
		logger.info("Verify that category is visible and content present in this category");
		User user = (User) getTestSession().get(EXP_CONTRIBUTOR_USER_KEY );
		getTestSession().setUser(user);
		ContentCategory category = (ContentCategory) getTestSession().get(EXP_CONTRIBUTOR_CATEGORY_KEY);
		boolean result = repositoryService.isCategoryPresent(getTestSession(), category.getName(), category.getParentNames());

		
		Assert.assertTrue(result, String.format("category with name %s was not found!",category.getName()));
		
		ContentsTableFrame frame = repositoryService.findCategoryInContentAndOpen(getTestSession(), category);
		result = frame.isContentPresentInTable(CONTENT_NAME);
		TestUtils.getInstance().saveScreenshot(getTestSession());
		Assert.assertTrue(result, "content was not found!");
		logger.info("Finished $$$$ Verify that category is visible and content present in this category");

	} 

	
	@Test(description="Expert Contributors should be able to edit HTML fields, both in full screen mode, and in a popup window that gives direct access to the HTML-code..", dependsOnMethods ="addContentAndGrantAccessTest")
	public void verifyEditHtmlButtonTest()
	{
		logger.info("STARTED#### verify that 'Expert Contributors' should be able to edit HTML fields");
		User expert = (User) getTestSession().get(EXP_CONTRIBUTOR_USER_KEY);
		getTestSession().setUser(expert);
		Content<ContentWithEditorInfo>  content = (Content<ContentWithEditorInfo>)getTestSession().get(EXP_CONTRIBUTOR_CONTENT_KEY);
		boolean result = accountService.isPresentEditHtmlButton(getTestSession(), content);
		Assert.assertTrue(result,"'edit html' button should be present on the 'edit content wizard' page!");
		logger.info("Finished $$$$ verified that 'Expert Contributors' should be able to edit HTML fields");
	}

}
