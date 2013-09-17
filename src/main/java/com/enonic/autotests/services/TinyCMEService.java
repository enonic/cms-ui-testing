package com.enonic.autotests.services;

import java.util.ArrayList;
import java.util.List;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.pages.v4.adminconsole.content.AbstractContentTableView;
import com.enonic.autotests.pages.v4.adminconsole.content.AlignmentText;
import com.enonic.autotests.pages.v4.adminconsole.content.ContentWithTinyMCEWizard;

public class TinyCMEService
{
	/**
	 * @param testSession
	 * @param category
	 */
	public void verifyBoldItalic(TestSession testSession, ContentCategory category)
	{  
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, getPathToCategory(category));
		tableViewFrame.doStartAddContent();
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.verifyItalicBoldAndRemoveFormatting();
	}
	public void verifyTextAlignment (TestSession testSession, ContentCategory category,AlignmentText align)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, getPathToCategory(category));
		tableViewFrame.doStartAddContent();
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.verifyTextAlignment(align);
	}
	
	public void verifyAddAnchorInText (TestSession testSession, ContentCategory category)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, getPathToCategory(category));
		tableViewFrame.doStartAddContent();
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.verifyAnchorInText();
	}
	public void verifyAddHorizontalLine (TestSession testSession, ContentCategory category)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, getPathToCategory(category));
		tableViewFrame.doStartAddContent();
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.verifyHorizontalLine();
	}
	public void verifyLinkUnlink (TestSession testSession, ContentCategory category)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, getPathToCategory(category));
		tableViewFrame.doStartAddContent();
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.verifyLinkUnlink();
	}
	
	private String[] getPathToCategory(ContentCategory category)
	{
		List<String> path = new ArrayList<>();
		String[] catParents = category.getParentNames();
		for(String name: catParents)
		{
			path.add(name);
		}
		path.add(category.getName());
		String[] ar = new String[path.size()];
		return path.toArray(ar);
	}

}
