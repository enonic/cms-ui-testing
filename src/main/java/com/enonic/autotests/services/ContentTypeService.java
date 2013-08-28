package com.enonic.autotests.services;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.pages.v4.adminconsole.LeftMenuFrame;
import com.enonic.autotests.pages.v4.adminconsole.contenttype.ContentTypesFrame;

/**
 * 
 *Content type service.
 */
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
	
	/**
	 * Deletes all sites in admin.
	 * 
	 * @param testSession
	 */
	public void delteAllContentTypes(TestSession testSession)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame menu = new LeftMenuFrame(testSession);
		ContentTypesFrame ctypesFrame = menu.openContentTypesFrame(testSession);
		ctypesFrame.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		ctypesFrame.doDeleteAll();
	}

	/**
	 * Clicks by 'Content type', opens for edit it  and changes a configuration of content type.
	 * @param testSession
	 * @param ctypeName
	 * @param cfg
	 */
	public void editContentType(TestSession testSession, String ctypeName, String cfg)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame menu = new LeftMenuFrame(testSession);
		ContentTypesFrame ctypesFrame = menu.openContentTypesFrame(testSession);
		ctypesFrame.doChangeConfiguration(ctypeName, cfg);

	}
}
