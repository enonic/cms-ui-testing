package com.enonic.autotests.services;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.userstores.User;
import com.enonic.autotests.pages.adminconsole.LeftMenuFrame;
import com.enonic.autotests.pages.adminconsole.userstores.UsersTableFrame;

public class AccountService
{
	/**
	 * Edits a user in admin console
	 * 
	 * @param testSession
	 * @param userToEdit
	 * @param user
	 * @return
	 */
	public UsersTableFrame editUser(TestSession testSession, String userToEdit, User user)
	{
		PageNavigator.navgateToAdminConsole(testSession);
		LeftMenuFrame menu = new LeftMenuFrame(testSession);
		UsersTableFrame usersFrame = menu.openUsersTableFrame(testSession);
		//selects a user in a table, opens Wizard page and populate new data
		usersFrame.doEditUser(userToEdit, user);		
		
		usersFrame.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return usersFrame;
	}
	/**
	 * Adds a new user in admin console
	 * 
	 * @param testSession
	 * @param user
	 * @return
	 */
	public UsersTableFrame addUser(TestSession testSession, User user)
	{
		PageNavigator.navgateToAdminConsole(testSession);
		LeftMenuFrame menu = new LeftMenuFrame(testSession);
		UsersTableFrame usersFrame = menu.openUsersTableFrame(testSession);
		usersFrame.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		//clicks by "New", opens AddUserWizard page and populate  data: username, password, email... Than clicks by 'Save' button
		usersFrame.doAddUser(user);		
		
		usersFrame.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return usersFrame;
	}
}
