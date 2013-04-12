package com.enonic.autotests.services.v4;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.BaseAbstractContent;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.pages.v4.adminconsole.LeftMenuFrame;
import com.enonic.autotests.pages.v4.adminconsole.content.ContentRepositoryFrame;
import com.enonic.autotests.pages.v4.adminconsole.content.IAddContentToRepository;
import com.enonic.autotests.pages.v4.adminconsole.content.RepositoriesListFrame;
import com.enonic.autotests.pages.v4.adminconsole.contenttype.ContentTypesFrame;
import com.enonic.autotests.services.IAdminConsoleService;


public class AdminConsoleServiceImplV4 implements IAdminConsoleService {
	
	@SuppressWarnings("unchecked")
	@Override
	public ContentTypesFrame createContentType(TestSession testSession, ContentType ctype) {
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame menu = new LeftMenuFrame(testSession);
		ContentTypesFrame frame = menu.openContentTypesFrame(testSession);
		frame.createContentType(ctype);
		return new ContentTypesFrame(testSession);

	}
	@SuppressWarnings("unchecked")
	@Override
	public RepositoriesListFrame createContentRepository(TestSession testSession, ContentRepository ctype) {
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame menu = new LeftMenuFrame(testSession);
		RepositoriesListFrame frame = menu.openContentFrame(testSession);
		frame.createContentRepository(ctype);
		return  new RepositoriesListFrame(testSession);
	}


	
	@Override
	public void addContentToRepository(TestSession testSession,ContentRepository cRepository,BaseAbstractContent content) {//AbstractContent content
		//cRepository = new ContentRepository();
		//cRepository.setName("repo-files");
		ContentRepositoryFrame frame = PageNavigatorV4.openContentRepositoryDashboard(testSession, cRepository.getName());
		IAddContentToRepository wizard = frame.openAddContentWizardPage( cRepository);
		wizard.typeDataAndSave(content);
		System.out.println("ok");		

	}


	@Override
	public void editContentType(TestSession testSession, String name) {
		PageNavigatorV4.navgateToAdminConsole(testSession);
		//TODO add implementation.

	}

	@SuppressWarnings("unchecked")
	@Override
	public RepositoriesListFrame deleteContentType(TestSession testSession, String name) {
		ContentRepositoryFrame frame = PageNavigatorV4.openContentRepositoryDashboard(testSession, name);
		frame.deleteContentRepository(name);
		return  new RepositoriesListFrame(testSession);
	}

}
