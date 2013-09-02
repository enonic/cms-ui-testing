package com.enonic.autotests.services;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.testng.Assert;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.FileContentInfo;
import com.enonic.autotests.model.ImageContentInfo;
import com.enonic.autotests.model.Section;
import com.enonic.autotests.pages.v4.adminconsole.LeftMenuFrame;
import com.enonic.autotests.pages.v4.adminconsole.content.AbstractContentTableView;
import com.enonic.autotests.pages.v4.adminconsole.content.AddFileContentWizard;
import com.enonic.autotests.pages.v4.adminconsole.content.AddImageContentWizard;
import com.enonic.autotests.pages.v4.adminconsole.content.ContentIndexes;
import com.enonic.autotests.pages.v4.adminconsole.content.ContentsTableFrame;
import com.enonic.autotests.pages.v4.adminconsole.content.IContentWizard;
import com.enonic.autotests.pages.v4.adminconsole.content.PersonImportWizardPage;
import com.enonic.autotests.pages.v4.adminconsole.content.RepositoriesListFrame;

public class ContentService
{
	/**
	 * Finds all content in the 'Content' folder of Left Menu.
	 * <br>return list of full names: 'RepsitoryName/CategoryName/../ContentName'
	 * @param testSession
	 * @param contentName
	 * @return list of full names
	 */
	public  List<String> doSearchContentByName(TestSession testSession, String contentName)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame menu = new LeftMenuFrame(testSession);
		RepositoriesListFrame frame = menu.openRepositoriesTableFrame();
		return frame.doSearchContent(contentName);
		
	}
	
	/**
	 * Imports content to the Category.
	 * 
	 * @param testSession
	 * @param importName
	 * @param fileName
	 * @param categoryPath
	 * @return
	 */
	public ContentsTableFrame doImportContent(TestSession testSession,String importName,String fileName,String... categoryPath)
	{		
		ContentsTableFrame tableOfContent = (ContentsTableFrame)PageNavigatorV4.openContentsTableView(testSession, categoryPath );
		//1. clicks by 'Import' button.
		tableOfContent.startImportContent();
		// check if import name equals: import-person-xml or import-person-csv
		if(importName.contains("person"))
		{
			PersonImportWizardPage page = new PersonImportWizardPage(testSession);
			page.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		//2. choose	 a file and press "Import" button, press the "Back" button as well and waits until Table of content appears.
			page.doImportFromFile(importName, fileName);
			
			tableOfContent.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		}else{
		   System.out.println("not implemented for import name:"+importName);
		   Assert.fail("not implemented for import name:"+importName);
		}
		
		return tableOfContent;
	}
	public ContentsTableFrame doImportTmpFileContent(TestSession testSession,String importName,File tmp,String... categoryPath)
	{		
		ContentsTableFrame tableOfContent = (ContentsTableFrame)PageNavigatorV4.openContentsTableView(testSession, categoryPath );
		//1. clicks by 'Import' button.
		tableOfContent.startImportContent();
		// check if import name equals: import-person-xml or import-person-csv
		if(importName.contains("person"))
		{
			PersonImportWizardPage page = new PersonImportWizardPage(testSession);
			page.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		//2. choose	 a file and press "Import" button, press the "Back" button as well and waits until Table of content appears.
			page.doImportFromTmpFile(importName, tmp);
			
			tableOfContent.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		}else{
		   System.out.println("not implemented for import name:"+importName);
		   Assert.fail("not implemented for import name:"+importName);
		}
		
		return tableOfContent;
	}
	
	/**
	 * Opens category by name, find a content and publishes content to Section.
	 * 
	 * @param testSession
	 * @param content
	 * @param section
	 */
	public void doPublishContentToSection(TestSession testSession,Content<?> content,Section section)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);		
		ContentsTableFrame contentTableFrame = (ContentsTableFrame)PageNavigatorV4.openContentsTableView(testSession, content.getParentNames());
		contentTableFrame.doPublishToSection(content.getDisplayName(), section);
		contentTableFrame.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
	}
	public void doPublishContentToSectionAnMoveToEnd(TestSession testSession,Content<?> content,Section section)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);		
		ContentsTableFrame contentTableFrame = (ContentsTableFrame)PageNavigatorV4.openContentsTableView(testSession, content.getParentNames());
		contentTableFrame.doPublishToSectionAndMoveToEnd(content.getDisplayName(), section);
		contentTableFrame.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
	}
	/**
	 * Open Category and add content. 
	 * 
	 * @param testSession
	 * @param cRepository
	 * @param content
	 * @return
	 */
	public <T> AbstractContentTableView addContent(TestSession testSession, ContentRepository cRepository, Content<T> content)//
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, content.getParentNames());
		IContentWizard<T> wizard = tableViewFrame.openAddContentWizardPage(cRepository);
		wizard.typeDataAndSave(content);
		tableViewFrame.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return tableViewFrame;
	}
	
	public <T> String getContentKeyPropery(TestSession testSession, Content<T> content)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, content.getParentNames());
		IContentWizard<?> wizard = tableViewFrame.openEditContentWizard(content);
		return wizard.getContentKey();
		
	}
	public <T> Map<ContentIndexes,String> getContentIndexedValues(TestSession testSession, Content<T> content)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, content.getParentNames());
		IContentWizard<?> wizard = tableViewFrame.openEditContentWizard(content);
		
		return wizard.getIndexedValues();
		
	}
	
	public AbstractContentTableView addimageContent(TestSession testSession,  Content<ImageContentInfo> content)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, content.getParentNames());
		AddImageContentWizard wizard = tableViewFrame.openAddImageContentWizardPage(content.getParentNames()[0]);
		wizard.typeDataAndSave(content);
		tableViewFrame.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return tableViewFrame;
		
	}
	public AbstractContentTableView addFileContent(TestSession testSession,  Content<FileContentInfo> content)
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, content.getParentNames());
		AddFileContentWizard wizard = tableViewFrame.openAddFileContentWizardPage(content.getParentNames()[0]);
		wizard.typeDataAndSave(content);
		tableViewFrame.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return tableViewFrame;
		
		

	}
	/**
	 * Find category and content in the repository, change name and save content.  
	 * 
	 * @param session
	 * @param content content to update.
	 * @param newName new name of content.
	 */
	public <T> void  editContent(TestSession session, Content<T> content,String newName)
	{
		doSearchContentByName(session, content.getDisplayName());
		
		ContentsTableFrame contentsTable = new ContentsTableFrame(session);
		IContentWizard<T> dd = contentsTable.openEditContentWizard(content);
		content.setDisplayName(newName);
	    dd.typeDataAndSave(content);
	}
	
	/**
	 * Opens a Category, shows the table of content, finds content and select it.
	 * <br>clicks by 'Delete' icon, confirm and deletes content from the category.
	 * 
	 * @param session
	 * @param displayName
	 */
	public ContentsTableFrame  deleteContentfromCategory(TestSession session, Content<?> content)
	{
		String[] parentNames = content.getParentNames();
		String contentDisplayName = content.getDisplayName();
		if(parentNames.length == 1)
		{
			throw new IllegalArgumentException("path to content should contains repository name and category name, array-size should be more than 1 ");
		}
		ContentsTableFrame contentTableFrame = (ContentsTableFrame)PageNavigatorV4.openContentsTableView(session, parentNames);
		contentTableFrame.doDeleteContent(contentDisplayName);
		return contentTableFrame;
		
	}
	/**
	 * Expands 'Content' folder in the 'Left Menu',expands a content-repository finds Category and clicks by it. 
	 * 
	 * @param session
	 * @param destinationNames Path name.
	 * @return {@link ContentsTableFrame} instance and shows table of content.  
	 */
	public ContentsTableFrame openCategory(TestSession session,String ...destinationNames )
	{
		 return (ContentsTableFrame)PageNavigatorV4.openContentsTableView(session, destinationNames);
	}
	/**
	 * Opens category, finds content in table and moves this content to the destination folder.
	 * 
	 * @param session
	 * @param content
	 * @param destinationFolderName
	 * @return
	 */
	public ContentsTableFrame moveContent(TestSession session, Content<?> content,String ...destinationNames)
	{
		ContentsTableFrame contentTableFrame = (ContentsTableFrame)PageNavigatorV4.openContentsTableView(session, content.getParentNames());
		contentTableFrame.doMoveContent(content.getDisplayName(), destinationNames[0],destinationNames[1]);
		contentTableFrame.waituntilPageLoaded(2l);
		return contentTableFrame;
	}
	
	/**
	 * @param session
	 * @param destinationNames
	 */
	public void deleteCategory(TestSession session,String ...destinationNames )
	{
		openCategory(session, destinationNames);
		//TODO not implemented yet.
	}
}
