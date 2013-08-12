package com.enonic.autotests.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.logger.Logger;

public abstract class Page
{

	private TestSession session;
	
	private  Logger logger = Logger.getLogger();
	
	public Page( TestSession session )
	{
		this.session = session;
		PageFactory.initElements(session.getDriver(), this);
	}

	public TestSession getSession()
	{
		return session;
	}

	public void setSession(TestSession session)
	{
		this.session = session;
	}

	public Logger getLogger()
	{
		return logger;
	}

	public WebElement findElement(By by)
	{
		return session.getDriver().findElement(by);
	}

	public List<WebElement> findElements(By by)
	{
		return session.getDriver().findElements(by);
	}
	
	public WebDriver getDriver()
	{
		return session.getDriver();
	}

}
