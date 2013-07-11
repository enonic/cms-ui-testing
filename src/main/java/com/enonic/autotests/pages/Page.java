package com.enonic.autotests.pages;

import org.openqa.selenium.support.PageFactory;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.logger.Logger;

public abstract class Page {// extends LoadableComponent<Page>{

	private TestSession session;
	
	private  Logger logger = Logger.getLogger();
	

	public Page(TestSession session) {
		this.session = session;
		PageFactory.initElements(session.getDriver(), this);
	}

	public TestSession getSession() {
		return session;
	}

	public void setSession(TestSession session) {
		this.session = session;
	}

	public Logger getLogger() {
		return logger;
	}


}
