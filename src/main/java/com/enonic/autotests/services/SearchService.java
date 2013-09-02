package com.enonic.autotests.services;

import java.util.List;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.v4.adminconsole.LeftMenuFrame;
import com.enonic.autotests.pages.v4.adminconsole.content.RepositoriesListFrame;
import com.enonic.autotests.pages.v4.adminconsole.content.search.ContentSearchParams;

public class SearchService
{

	/**
	 * Click by 'Advanced Search' button, types a text and select file attachments 
	 * @param testSession
	 * @param text text to search.
	 * @return  a list of file names that contain the text.
	 */
	public List<String> doAdvancedSearch(TestSession testSession, ContentSearchParams params )
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame menu = new LeftMenuFrame(testSession);
		RepositoriesListFrame frame = menu.openRepositoriesTableFrame();
		return frame.doContentAdvancedSearch(params);
		
	}
}
