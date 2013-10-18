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
import com.enonic.autotests.model.userstores.User;
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
	//private final String EXP_CONTRIBUTOR_CATEGORY_KEY = "exp_contributor_cat_key";
	private final String EXP_CONTRIBUTOR_CONTENT_KEY = "exp_contributor_content_key";
	private final String CONTENT_NAME = "expertcontent";
	
	private final String TINY_MCE_CFG = "test-data/contenttype/tiny-editor.xml";

	@Test(description = "add user to  check AÑL- expert contributor")
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
		logger.info("FINISHED ###:   user with name:"+exp.getName() +" was added  to the Expert Contributors group");
	}
	


	@Test(description = "add catrgory and content, admin browse  allowed", dependsOnMethods = "addUserToExpertContributorsTest")
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
		// 4. set ACL(Read permission, but not browse) for Category:
		List<AclEntry> catAclEntries = new ArrayList<>();
		AclEntry categoryAclEntry = new AclEntry();
		String principalName = ((User) getTestSession().get(EXP_CONTRIBUTOR_USER_KEY)).getName();
		categoryAclEntry.setPrincipalName(principalName);
		
		categoryAclEntry.setType(PrincipalType.USER);
		List<String> categoryPerm = new ArrayList<>();
		categoryPerm.add(CategoryAvailableOperations.READ.getUiValue());
		categoryPerm.add(CategoryAvailableOperations.BROWSE.getUiValue());
		categoryAclEntry.setPermissions(categoryPerm);
		categoryAclEntry.setAllow(true);
		catAclEntries.add(categoryAclEntry);
		category.setAclEntries(catAclEntries);
		//getTestSession().put(EXP_CONTRIBUTOR_CATEGORY_KEY, category);
		
		//5. create category in the just added repository
		repositoryService.addCategory(getTestSession(), category);
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
		
		List<String> contentPermissions = new ArrayList<>();
		contentPermissions.add(ContentAvailableOperations.READ.getUiValue());
		contentPermissions.add(ContentAvailableOperations.UPDATE.getUiValue());
		contentAclEntry.setPermissions(contentPermissions);
		
		contentAclEntry.setAllow(true);
		contentAclEntries.add(contentAclEntry);
		content.setAclEntries(contentAclEntries);
		
		contentService.addContentWithEditor(getTestSession(), content);
		getTestSession().put(EXP_CONTRIBUTOR_CONTENT_KEY, content);
		logger.info("content was added to the categry with name: " + content.getDisplayName());
		
		logger.info("finished $$$: Setup category with content. Give user read access  but not admin-browse on category");

	} 

	
	@Test(description="Expert Contributors should be able to edit HTML fields, both in full screen mode, and in a popup window that gives direct access to the HTML-code..", dependsOnMethods ="addContentAndGrantAccessTest")
	public void verifyEditHtmlButtonTest()
	{
		logger.info("STARTED#### verify that 'Expert Contributors' should be able to edit HTML fields");
		User expert = (User) getTestSession().get(EXP_CONTRIBUTOR_USER_KEY);
		getTestSession().setUser(expert);
		Content<ContentWithEditorInfo>  content = (Content<ContentWithEditorInfo>)getTestSession().get(EXP_CONTRIBUTOR_CONTENT_KEY);
		boolean result = accountService.isPresentEditHtmlButton(getTestSession(), content);
		Assert.assertTrue(result,"'edit html' button should be present on the edit content page!");
		logger.info("Finished $$$$ verified that 'Expert Contributors' should be able to edit HTML fields");
	}

}
