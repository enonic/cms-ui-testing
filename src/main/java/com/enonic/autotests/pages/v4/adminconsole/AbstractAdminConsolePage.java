package com.enonic.autotests.pages.v4.adminconsole;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Page;

public abstract class AbstractAdminConsolePage extends Page
{

	public static final String REFRESH_IMAGE_XPATH = "//img[contains(@src,'images/action_refresh_blue.gif')]";
	public static String LEFT_FRAME_NAME = "leftFrame";
	public static String MAIN_FRAME_NAME = "mainFrame";

	public static final String TITLE = "Enonic CMS - Administration";

	/**
	 * The constructor
	 * 
	 * @param session
	 */
	public AbstractAdminConsolePage( TestSession session )
	{
		super(session);
	}

	abstract public void waituntilPageLoaded(long timeout);

}
