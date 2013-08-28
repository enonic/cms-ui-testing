package com.enonic.autotests.services;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.v4.adminconsole.LeftMenuFrame;
import com.enonic.autotests.pages.v4.adminconsole.system.SystemFrame;

public class SystemService
{
	/**
	 * @param session
	 */
	public void doRemoveDeletetContentFromDataBase(TestSession session)
	{
		PageNavigatorV4.navgateToAdminConsole(session);
		LeftMenuFrame menu = new LeftMenuFrame(session);
		SystemFrame sysFrame = menu.openSystemPage(session);
		sysFrame.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		sysFrame.doDeleteRemovedContent();
	}
}
