package com.enonic.autotests.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.enonic.autotests.TestSession;

public abstract class Page {// extends LoadableComponent<Page>{

	private TestSession session;

	public abstract String getTitle();

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

}
