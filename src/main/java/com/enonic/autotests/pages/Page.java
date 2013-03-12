package com.enonic.autotests.pages;

import org.openqa.selenium.WebDriver;

public abstract class Page {

	//TODO should be configured in properties file
	protected final long TIMEOUT = 10;
	
	private WebDriver driver;

	public abstract String getTitle();

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

}
