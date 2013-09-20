package com.enonic.autotests.services;

import java.util.ArrayList;
import java.util.List;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.model.ImageContentInfo;
import com.enonic.autotests.pages.v4.adminconsole.content.AbstractContentTableView;
import com.enonic.autotests.pages.v4.adminconsole.content.AlignmentText;
import com.enonic.autotests.pages.v4.adminconsole.content.ContentWithTinyMCEWizard;
import com.enonic.autotests.pages.v4.adminconsole.content.SpecialCharacters;

/**
 * Service for TinyMCE HTML WYSIWYG editor.
 *
 */
public class TinyMCEService
{
	/**
	 * Inserts a table and verify: the tag 'table' with 'height' and 'width' present in HTML code
	 * 
	 * @param testSession
	 * @param category
	 */
	public void verifyInsertTable(TestSession testSession, ContentCategory category)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, getPathToCategory(category));
		tableViewFrame.doStartAddContent();
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.verifyInsertTable();
	}
	public void verifyChangeColorText(TestSession testSession, ContentCategory category)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, getPathToCategory(category));
		tableViewFrame.doStartAddContent();
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.verifyChangeColorText();
	}
	
	/**
	 * @param testSession
	 * @param category
	 */
	public void verifyChangeBackgroundColorText(TestSession testSession, ContentCategory category)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, getPathToCategory(category));
		tableViewFrame.doStartAddContent();
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.verifyChangeBackgroundColorText();
	}
	/**
	 * @param testSession
	 * @param category
	 */
	public void verifyInsertSpecialCharacters(TestSession testSession, ContentCategory category, SpecialCharacters character)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, getPathToCategory(category));
		tableViewFrame.doStartAddContent();
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.verifyInsertSpecialCharacters(character);
	}
	
	
	public void verifyDeletion(TestSession testSession, ContentCategory category)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, getPathToCategory(category));
		tableViewFrame.doStartAddContent();
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.verifyDeletion();
	}
	public void verifyInsertion(TestSession testSession, ContentCategory category)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, getPathToCategory(category));
		tableViewFrame.doStartAddContent();
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.verifyInsertion();
	}
	public void verifyInsertBlockQuote(TestSession testSession, ContentCategory category)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, getPathToCategory(category));
		tableViewFrame.doStartAddContent();
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.verifyInsertBlockQuote();
	}
	public void verifyInsertAbbreviation(TestSession testSession, ContentCategory category)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, getPathToCategory(category));
		tableViewFrame.doStartAddContent();
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.verifyInsertAbbreviation();
	}
	public void verifyInsertAcronym(TestSession testSession, ContentCategory category)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, getPathToCategory(category));
		tableViewFrame.doStartAddContent();
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.verifyInsertAcronym();
	}
	public void verifyInsertCitation(TestSession testSession, ContentCategory category)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, getPathToCategory(category));
		tableViewFrame.doStartAddContent();
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.verifyInsertCitation();
	}
	public void verifyInsertSuperscript(TestSession testSession, ContentCategory category)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, getPathToCategory(category));
		tableViewFrame.doStartAddContent();
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.verifyInsertSuperscript();
	}
	public void verifyInsertSubscript(TestSession testSession, ContentCategory category)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, getPathToCategory(category));
		tableViewFrame.doStartAddContent();
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.verifyInsertSubscript();
	}
	public void verifyInsertBulletedList(TestSession testSession, ContentCategory category)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, getPathToCategory(category));
		tableViewFrame.doStartAddContent();
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.verifyInsertBulletedList();
	}
	public void verifyInsertNumberedList(TestSession testSession, ContentCategory category)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, getPathToCategory(category));
		tableViewFrame.doStartAddContent();
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.verifyInsertNumberedList();
	}
	public void verifyIncreaseDecreaseIndents(TestSession testSession, ContentCategory category)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, getPathToCategory(category));
		tableViewFrame.doStartAddContent();
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.verifyIncreaseDecreaseIndents();
	}
	public void verifyUndoRedo(TestSession testSession, ContentCategory category)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, getPathToCategory(category));
		tableViewFrame.doStartAddContent();
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.verifyUndoRedo();
	}

	/**
	 * Inserts an Image and verify: image is present in HTML code
	 * 
	 * @param testSession
	 * @param category
	 */
	public void verifyInsertImage(TestSession testSession, ContentCategory category,Content<ImageContentInfo> contentToInsert)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, getPathToCategory(category));
		tableViewFrame.doStartAddContent();
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.verifyInsertImage(contentToInsert);
	}

	/**
	 * Inserts a text with style: bold, italic. And verify: text is present in HTML code.
	 * 
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

	/**
	 * Inserts a text, adds alignment. And verify: text with correct alignment is present in HTML code.
	 * @param testSession
	 * @param category
	 * @param align
	 */
	public void verifyTextAlignment(TestSession testSession, ContentCategory category, AlignmentText align)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, getPathToCategory(category));
		tableViewFrame.doStartAddContent();
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.verifyTextAlignment(align);
	}

	/**
	 * @param testSession
	 * @param category
	 */
	public void verifyAddAnchorInText(TestSession testSession, ContentCategory category)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, getPathToCategory(category));
		tableViewFrame.doStartAddContent();
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.verifyAnchorInText();
	}

	/**
	 * @param testSession
	 * @param category
	 */
	public void verifyAddHorizontalLine(TestSession testSession, ContentCategory category)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, getPathToCategory(category));
		tableViewFrame.doStartAddContent();
		ContentWithTinyMCEWizard wizard = new ContentWithTinyMCEWizard(testSession);
		wizard.verifyHorizontalLine();
	}

	/**
	 * @param testSession
	 * @param category
	 */
	public void verifyLinkUnlink(TestSession testSession, ContentCategory category)
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
		for (String name : catParents)
		{
			path.add(name);
		}
		path.add(category.getName());
		String[] ar = new String[path.size()];
		return path.toArray(ar);
	}

}
