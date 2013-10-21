package com.enonic.autotests.general.acl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.BaseTest;
import com.enonic.autotests.model.userstores.BuiltInGroups;
import com.enonic.autotests.model.userstores.User;
import com.enonic.autotests.pages.adminconsole.LeftMenuFrame;
import com.enonic.autotests.services.AccountService;
import com.enonic.autotests.services.ContentService;
import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.services.PageNavigator;
import com.enonic.autotests.services.RepositoryService;

public class UserStoreAdministratorTest extends BaseTest
{

	private AccountService accountService = new AccountService();
	private ContentTypeService contentTypeService = new ContentTypeService();

	private RepositoryService repositoryService = new RepositoryService();

	private ContentService contentService = new ContentService();
	private final String PASSWORD = "1q2w3e";
	private final String STORE_ADMIN_USER_KEY = "sadmin_contributor_key";
	private final String STORE_ADMIN_CONTENT_KEY = "sadmin_content_key";
	private final String CONTENT_NAME = "storeadmcontent";
	private final String TINY_MCE_CFG = "test-data/contenttype/tiny-editor.xml";
	private final String STORE_ADMIN_CATEGORY_KEY = "sadmin_cat_key";

	@Test(description = "add user to  check 'Access rights' for 'Developer'")
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

	@Test(description = "add user to Userstore Administrators group", dependsOnMethods = "createUserTest")
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

}
