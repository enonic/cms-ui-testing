package com.enonic.autotests.general;

import java.io.InputStream;
import java.util.ArrayList;

import java.util.List;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.BaseTest;
import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.model.ContentHandler;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.model.userstores.AclEntry;
import com.enonic.autotests.model.userstores.BuiltInGroups;
import com.enonic.autotests.model.userstores.User;
import com.enonic.autotests.pages.adminconsole.contenttype.ContentTypesFrame;
import com.enonic.autotests.services.AccountService;
import com.enonic.autotests.services.ContentService;
import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.services.PageNavigator;
import com.enonic.autotests.services.RepositoryService;
import com.enonic.autotests.testdata.contenttype.ContentConvertor;
import com.enonic.autotests.utils.TestUtils;

public class ACLTest extends BaseTest
{

	private AccountService accountService = new AccountService();
	private ContentTypeService contentTypeService = new ContentTypeService();

	private RepositoryService repositoryService = new RepositoryService();

	private ContentService contentService = new ContentService();
	private final String PASSWORD = "1q2w3e";
	private final String CONTRIBUTOR_KEY = "contributor_key";
	
	private final String TINY_MCE_CFG = "test-data/contenttype/tiny-editor.xml";
	
	@Test
	public void setup()
	{
		ContentType editorCtype = new ContentType();
		String contentTypeName = "Editor" + Math.abs(new Random().nextInt());
		editorCtype.setName(contentTypeName);
		editorCtype.setContentHandler(ContentHandler.CUSTOM_CONTENT);
		editorCtype.setDescription("content type with html area tinyMCE");
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(TINY_MCE_CFG);
		String editorCFG = TestUtils.getInstance().readConfiguration(in);
		editorCtype.setConfiguration(editorCFG);
		ContentTypesFrame frame = contentTypeService.createContentType(getTestSession(), editorCtype);
		boolean isCreated = frame.verifyIsPresent(contentTypeName);
		if (!isCreated)
		{
			Assert.fail("error during creation of a content type!");
		}

		ContentRepository repository = new ContentRepository();
		repository.setName("acltest" + Math.abs(new Random().nextInt()));
		repositoryService.createContentRepository(getTestSession(), repository);
		//getTestSession().put(EDITOR_REPOSITORY_KEY, repository);

		ContentCategory category = new ContentCategory();
		category.setContentTypeName(contentTypeName);
		category.setName("contrib");
		String[] parentNames = { repository.getName() };
		category.setParentNames(parentNames);
		List<AclEntry> aclEntries = new ArrayList<>();
//		AclEntry e = new AclEntry();
//		e.setPrincipalName(principalName);
//		e.setType(type)
//		aclEntries.add(e )
		category.setAclEntries(aclEntries );
		//getTestSession().put(EDITOR_CATEGORY_KEY, category);
		repositoryService.addCategory(getTestSession(), category);
		
		
		ContentCategory cat2 = new ContentCategory();
		cat2.setContentTypeName(contentTypeName);
		cat2.setName("expertcontrib");		
		category.setParentNames(parentNames);
		

	}

	@Test(description = "add user to  check AÑL-contributor")
	public void createContributorTest()
	{
		User user = new User();
		user.setPassword(PASSWORD);
		String name = "contrib" + Math.abs(new Random().nextInt());
		user.setName(name);
		user.setEmail(name + "@mail.com");
		accountService.addUser(getTestSession(), user);
		getTestSession().put(CONTRIBUTOR_KEY, user);
	}

	@Test(description = "add user to Contrubutors group", dependsOnMethods = "createContributorTest")
	public void editContributorUserTest()
	{
		User contrubutor = (User) getTestSession().get(CONTRIBUTOR_KEY);
		List<String> groups = new ArrayList<>();
		groups.add(BuiltInGroups.CONTRIBUTORS.getValue());
		contrubutor.setGroups(groups);

		accountService.editUser(getTestSession(), contrubutor.getName(), contrubutor);
	}

	@Test(description = "add user to Contrubutors group", dependsOnMethods = "createContributorTest")
	public void addContentAndGrantAccessTest()
	{
		User contrubutor = (User) getTestSession().get(CONTRIBUTOR_KEY);
		List<String> groups = new ArrayList<>();
		groups.add(BuiltInGroups.CONTRIBUTORS.getValue());
		contrubutor.setGroups(groups);

		accountService.editUser(getTestSession(), contrubutor.getName(), contrubutor);
	}

	@Test(description = "add user to Contrubutors group", dependsOnMethods = "addContentAndGrantAccessTest")
	public void loginContributorUserTest()
	{
		User contrubutor = (User) getTestSession().get(CONTRIBUTOR_KEY);
		getTestSession().setUser(contrubutor);
		PageNavigator.navgateToAdminConsole(getTestSession());
		// accountService.editUser(getTestSession(), contrubutor.getName(),
		// contrubutor );
	}
}
