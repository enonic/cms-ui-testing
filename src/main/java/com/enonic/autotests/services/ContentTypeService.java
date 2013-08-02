package com.enonic.autotests.services;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.pages.v4.adminconsole.LeftMenuFrame;
import com.enonic.autotests.pages.v4.adminconsole.contenttype.ContentTypesFrame;

public class ContentTypeService
{

	/**
	 * @param testSession
	 * @param ctype
	 * @return
	 */
	public ContentTypesFrame createContentType(TestSession testSession, ContentType ctype)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame menu = new LeftMenuFrame(testSession);
		ContentTypesFrame frame = menu.openContentTypesFrame(testSession);
		frame.createContentType(ctype);
				
		return new ContentTypesFrame(testSession);

	}
	/**
	 * @param testSession
	 * @param typename
	 * @return
	 */
	public boolean findContentType(TestSession testSession, String typename)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame menu = new LeftMenuFrame(testSession);
		ContentTypesFrame frame = menu.openContentTypesFrame(testSession);
		return frame.verifyIsPresent(typename);
		

	}

	/**
	 * @param testSession
	 * @param contentTypeName
	 * @return
	 */
	public ContentTypesFrame deleteContentType(TestSession testSession, String contentTypeName)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame menu = new LeftMenuFrame(testSession);
		ContentTypesFrame frame = menu.openContentTypesFrame(testSession);
		frame.deleteContentType(contentTypeName);
		return new ContentTypesFrame(testSession);
	}

	public void editContentType(TestSession testSession, String name)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		// TODO add implementation.

	}
}
