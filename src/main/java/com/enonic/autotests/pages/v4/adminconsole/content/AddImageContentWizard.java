package com.enonic.autotests.pages.v4.adminconsole.content;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.BaseAbstractContent;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;

public class AddImageContentWizard extends AbstractAdminConsolePage implements IAddContentToRepository {

	public AddImageContentWizard(TestSession session) {
		super(session);
	}

	@Override
	public <T extends BaseAbstractContent> void typeDataAndSave(T content) {
		
		
	}

}
