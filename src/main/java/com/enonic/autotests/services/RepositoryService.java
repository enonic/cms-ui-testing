package com.enonic.autotests.services;

import java.util.List;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.model.ContentRepository;
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

	public <T> void addContent(TestSession testSession, ContentRepository cRepository, String categoryName, Content<T> content)//
	{
		AbstractContentTableView frame = PageNavigatorV4.openContentsTableView(testSession, cRepository.getName(), categoryName);
		IAddContentToRepository<T> wizard = frame.openAddContentWizardPage(cRepository);
		wizard.typeDataAndSave(content);
		frame.waituntilPageLoaded(3l);
		

	}
	
	public void addCategory(TestSession testSession, ContentCategory newCategory,String ...parentNames)
	{
		//1. open admin-console, expand the "Content" folder, click by RepositoryName, open repository view and click by 'New-Category' button
		AbstractContentTableView repoview = PageNavigatorV4.openContentsTableView(testSession, parentNames);
		
		//2. 'New-Category' button ->opened 'add category wizard'
		CreateCategoryWizard wizard = repoview.openAddCategoryWizard();
		//3. populate all fields and click by 'Save' and 'Close' buttons 
		wizard = new CreateCategoryWizard(testSession);
		wizard.doAddCategory(newCategory);
		repoview.waituntilPageLoaded(1l);
		
	}
	
	public <T> List<Content<T>> doSearchContentRelatedToRepository(TestSession testSession, String contentName,String repositoryName)
	{
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame menu = new LeftMenuFrame(testSession);
		RepositoriesListFrame frame = menu.openRepositoriesTableFrame();
		frame.doSearchContent(contentName,repositoryName);
		return null;
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
