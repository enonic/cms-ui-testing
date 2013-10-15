package com.enonic.autotests.services;

import java.util.List;

import org.openqa.selenium.WebElement;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.pages.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.pages.adminconsole.LeftMenuFrame;
import com.enonic.autotests.pages.adminconsole.content.AbstractContentTableView;
import com.enonic.autotests.pages.adminconsole.content.ContentsTableFrame;
import com.enonic.autotests.pages.adminconsole.content.CreateCategoryWizard;
import com.enonic.autotests.pages.adminconsole.content.RepositoriesListFrame;

/**
 * Repository Service
 *
 */
public class RepositoryService
{
	/**
	 * @param testSession
	 * @param repositoryName
	 * @return
	 */
	public int getRepositoryKey(TestSession testSession,String repositoryName)
	{
		PageNavigator.navgateToAdminConsole( testSession );
		LeftMenuFrame menu = new LeftMenuFrame(testSession);
		return menu.getRepositoryKey(repositoryName);
	}

	public int getCategoryKey(TestSession testSession,String catName, String ... parents )
	{
		PageNavigator.navgateToAdminConsole( testSession );
		LeftMenuFrame menu = new LeftMenuFrame(testSession);
		return menu.getCategoryKey(catName, parents);
	}

	public RepositoriesListFrame createContentRepository(TestSession testSession, ContentRepository repository)
	{
		PageNavigator.navgateToAdminConsole( testSession );
		LeftMenuFrame menu = new LeftMenuFrame(testSession);

		RepositoriesListFrame frame = menu.openRepositoriesTableFrame();
		frame.createContentRepository(repository);
		return frame;
	}

	/**
	 * Adds new Category(Folder)
	 * @param testSession
	 * @param newCategory
	 */
	public void addCategory(TestSession testSession, ContentCategory newCategory)
	{
		// 1. open admin-console, expand the "Content" folder, click by RepositoryName, open repository view and click by 'New-Category' button
		AbstractContentTableView repoview = PageNavigator.openContentsTableView( testSession, newCategory.getParentNames() );

		// 2. 'New-Category' button ->opened 'add category wizard'
		CreateCategoryWizard wizard = repoview.openAddCategoryWizard();
		// 3. populate all fields and click by 'Save' and 'Close' buttons
		wizard.doAddCategory(newCategory);
		repoview.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);

	}
	public void editCategory(TestSession testSession, ContentCategory categoryToEdit)
	{
		PageNavigator.navgateToAdminConsole( testSession );
		LeftMenuFrame menu = new LeftMenuFrame(testSession);
		PageNavigator.switchToFrame( testSession, AbstractAdminConsolePage.LEFT_FRAME_NAME );
		//ContentsTableFrame contentTableFrame = (ContentsTableFrame) PageNavigator.openContentsTableView( testSession, parentNames );
		WebElement result = menu.findCategoryInContentFolder(categoryToEdit.getName(), categoryToEdit.getParentNames());
		if (result != null)
		{
			//click by category and switch to main frame
			result.click();
			PageNavigator.switchToFrame( testSession, AbstractAdminConsolePage.MAIN_FRAME_NAME );
			
		} else
		{
			
		}

		
		ContentsTableFrame frame =new ContentsTableFrame(testSession);
		frame.doEditCategory(categoryToEdit);
		

	}


 public ContentsTableFrame findCategoryInContentAndOpen(TestSession testSession, ContentCategory category)
 {
	   LeftMenuFrame menu = new LeftMenuFrame(testSession);
		PageNavigator.switchToFrame( testSession, AbstractAdminConsolePage.LEFT_FRAME_NAME );
		WebElement result = menu.findCategoryInContentFolder(category.getName(), category.getParentNames());
		if(result == null)
		{
			throw new TestFrameworkException("category was not found!  " +category.getName());
		}
		result.click();
		PageNavigator.switchToFrame( testSession, AbstractAdminConsolePage.MAIN_FRAME_NAME );
		ContentsTableFrame frame =new ContentsTableFrame(testSession);
		
		return frame;
 }

	/**
	 * Finds category by Name and names of parent folders.
	 * 
	 * @param session
	 * @param categoryName
	 * @param parents
	 * @return true if category was found, otherwise false.
	 */
	public Boolean isCategoryPresent(TestSession session, String categoryName, String... parents)
	{
		PageNavigator.navgateToAdminConsole( session );
		LeftMenuFrame menu = new LeftMenuFrame(session);
		PageNavigator.switchToFrame( session, AbstractAdminConsolePage.LEFT_FRAME_NAME );
		WebElement result = menu.findCategoryInContentFolder(categoryName, parents);
		if (result != null)
		{
			return true;
		} else
		{
			return false;
		}

	}

	/**
	 * Finds a repository under the 'Content' Menu-item (from the LeftFrame), expand this repository and delete all content and categories. 
	 * <br>When repository is empty, 'Remove content repository' button appears, clicks by this button and delete empty Repository.
	 * 
	 * @param testSession
	 * @param repositoryName
	 */

	public void deleteRepository(TestSession session, String repositoryName)
	{
		PageNavigator.navgateToAdminConsole( session );
		LeftMenuFrame menu = new LeftMenuFrame(session);
		menu.doDeleteRepository(repositoryName);
	}
	/**
	 * gets names of all repositories. 
	 * @param session
	 * @return
	 */
	public List<String> getAllRepositoryNames(TestSession session)
	{
		PageNavigator.navgateToAdminConsole( session );
		LeftMenuFrame menu = new LeftMenuFrame(session);
		RepositoriesListFrame frame = menu.openRepositoriesTableFrame();
		return frame.getRepositoryNames();
		
	}

}
