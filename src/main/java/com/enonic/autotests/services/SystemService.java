package com.enonic.autotests.services;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.adminconsole.LeftMenuFrame;
import com.enonic.autotests.pages.adminconsole.system.SystemFrame;

public class SystemService
{
	/**
	 * @param session
	 */
	public void doRemoveDeletetContentFromDataBase(TestSession session)
	{
		PageNavigator.navgateToAdminConsole( session );
		LeftMenuFrame menu = new LeftMenuFrame(session);
		SystemFrame sysFrame = menu.openSystemPage(session);
		sysFrame.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		sysFrame.doDeleteRemovedContent();
	}
}
