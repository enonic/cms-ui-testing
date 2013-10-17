package com.enonic.autotests.services;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.userstores.User;
import com.enonic.autotests.pages.adminconsole.LeftMenuFrame;
import com.enonic.autotests.pages.adminconsole.content.AbstractContentTableView;
import com.enonic.autotests.pages.adminconsole.content.ContentWithTinyMCEWizard;
import com.enonic.autotests.pages.adminconsole.userstores.UsersTableFrame;
import com.enonic.autotests.utils.TestUtils;

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
		TestUtils.getInstance().saveScreenshot(testSession);
		return usersFrame;
	}
	/**
	 * Opens content with type, that contains HTML-editor and check 'edit html' button.
	 * <br> if user is expert contributor, this button should be enabled.
	 * @param testSession
	 * @param content
	 * @return
	 */
	public boolean isPresentEditHtmlButton(TestSession testSession, Content<?> content)
	{
		AbstractContentTableView tableViewFrame = PageNavigator.openContentsTableView( testSession, content.getParentNames() );
		tableViewFrame.doStartEditContent(content.getDisplayName());
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		TestUtils.getInstance().saveScreenshot(testSession);
		return wizard.isPresentEditHtmlButton();
		
		
	}
}
