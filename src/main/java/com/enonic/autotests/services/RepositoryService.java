package com.enonic.autotests.services;

import java.util.List;

import org.openqa.selenium.WebElement;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.pages.v4.adminconsole.LeftMenuFrame;
import com.enonic.autotests.pages.v4.adminconsole.content.AbstractContentTableView;
import com.enonic.autotests.pages.v4.adminconsole.content.ContentRepositoryViewFrame;
import com.enonic.autotests.pages.v4.adminconsole.content.CreateCategoryWizard;
import com.enonic.autotests.pages.v4.adminconsole.content.IAddContentToRepository;
import com.enonic.autotests.pages.v4.adminconsole.content.RepositoriesListFrame;

public class RepositoryService
{

	public RepositoriesListFrame createContentRepository(TestSession testSession, ContentRepository ctype)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame menu = new LeftMenuFrame(testSession);
	
		RepositoriesListFrame frame = menu.openRepositoriesTableFrame();
		frame.createContentRepository(ctype);
		return frame;
	}

	public <T> AbstractContentTableView addContent(TestSession testSession, ContentRepository cRepository, Content<T> content)//
	{
		AbstractContentTableView tableViewFrame = PageNavigatorV4.openContentsTableView(testSession, content.getParents());
		IAddContentToRepository<T> wizard = tableViewFrame.openAddContentWizardPage(cRepository);
		wizard.typeDataAndSave(content);
		tableViewFrame.waituntilPageLoaded(3l);
		return tableViewFrame;
		
		

	}
	
	/**
	 * @param testSession
	 * @param newCategory
	 */
	public void addCategory(TestSession testSession, ContentCategory newCategory)
	{
		//1. open admin-console, expand the "Content" folder, click by RepositoryName, open repository view and click by 'New-Category' button
		AbstractContentTableView repoview = PageNavigatorV4.openContentsTableView(testSession, newCategory.getParentNames());
		
		//2. 'New-Category' button ->opened 'add category wizard'
		CreateCategoryWizard wizard = repoview.openAddCategoryWizard();
		//3. populate all fields and click by 'Save' and 'Close' buttons 
		wizard = new CreateCategoryWizard(testSession);
		wizard.doAddCategory(newCategory);
		repoview.waituntilPageLoaded(1l);
		
	}
	
	public Boolean findCategoryByPath(TestSession session ,String categoryName,String ... parents)
	{
		PageNavigatorV4.navgateToAdminConsole(session);
		LeftMenuFrame menu = new LeftMenuFrame(session);
		PageNavigatorV4.switchToFrame(session,AbstractAdminConsolePage.LEFT_FRAME_NAME );
		WebElement result = menu.findCategoryInContentFolder(categoryName,parents);
		if( result!=null)
		{
			return true;
		}else{
			return false;
		}
		
	}
	
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
	 * Select a repository from the Menu in the LeftFrame, opens repository-view page, 
	 * <br>click by 'Remove content repository' button and  Delete Repository
	 * @param testSession
	 * @param repositoryName
	 * @return
	 */
	public RepositoriesListFrame deleteContentRepository(TestSession testSession, String repositoryName)
	{
		ContentRepositoryViewFrame frame = (ContentRepositoryViewFrame)PageNavigatorV4.openContentsTableView(testSession, repositoryName);
		frame.deleteContentRepository();
		return new RepositoriesListFrame(testSession);
	}

}
