package com.enonic.autotests.services;

import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.pages.v4.adminconsole.LeftMenuFrame;
import com.enonic.autotests.pages.v4.adminconsole.content.AbstractContentTableView;
import com.enonic.autotests.pages.v4.adminconsole.content.ContentRepositoriesTableFrame;
import com.enonic.autotests.pages.v4.adminconsole.content.CreateCategoryWizard;
import com.enonic.autotests.pages.v4.adminconsole.content.RepositoriesListFrame;

/**
 * Repository Service
 *
 */
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

	/**
	 * @param testSession
	 * @param newCategory
	 */
	public void addCategory(TestSession testSession, ContentCategory newCategory)
	{
		// 1. open admin-console, expand the "Content" folder, click by
		// RepositoryName, open repository view and click by 'New-Category'
		// button
		AbstractContentTableView repoview = PageNavigatorV4.openContentsTableView(testSession, newCategory.getParentNames());

		// 2. 'New-Category' button ->opened 'add category wizard'
		CreateCategoryWizard wizard = repoview.openAddCategoryWizard();
		// 3. populate all fields and click by 'Save' and 'Close' buttons
		wizard = new CreateCategoryWizard(testSession);
		wizard.doAddCategory(newCategory);
		repoview.waituntilPageLoaded(1l);

	}

	/**
	 * Finds category by Name and names of parent folders.
	 * 
	 * @param session
	 * @param categoryName
	 * @param parents
	 * @return true if category was found, otherwise false.
	 */
	public Boolean findCategoryByPath(TestSession session, String categoryName, String... parents)
	{
		PageNavigatorV4.navgateToAdminConsole(session);
		LeftMenuFrame menu = new LeftMenuFrame(session);
		PageNavigatorV4.switchToFrame(session, AbstractAdminConsolePage.LEFT_FRAME_NAME);
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
	 * Select a repository from the Menu in the LeftFrame, opens repository-view
	 * page, <br>
	 * click by 'Remove content repository' button and Delete Repository
	 * 
	 * @param testSession
	 * @param repositoryName
	 * @return
	 */
	public RepositoriesListFrame deleteContentRepository(TestSession testSession, String repositoryName)
	{
		ContentRepositoriesTableFrame repositoriesTable = (ContentRepositoriesTableFrame) PageNavigatorV4.openContentsTableView(testSession,
				repositoryName);
		repositoriesTable.deleteContentRepository();
		return new RepositoriesListFrame(testSession);
	}

}
