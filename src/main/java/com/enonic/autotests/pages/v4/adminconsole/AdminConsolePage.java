package com.enonic.autotests.pages.v4.adminconsole;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Page;

public class AdminConsolePage extends Page {

	public AdminConsolePage(TestSession session) {
		super(session);

	}

	public static String LEFT_FRAME_CLASSNAME = "leftframe";
	public static String LEFT_FRAME_NAME = "leftFrame";
	public static String MAIN_FRAME_NAME = "mainFrame";

	public static final String TITLE = "Enonic CMS - Administration";

	@Override
	public String getTitle() {
		return TITLE;
	}

}
