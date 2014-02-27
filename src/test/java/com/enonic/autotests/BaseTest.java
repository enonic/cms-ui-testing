package com.enonic.autotests;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.enonic.autotests.logger.Logger;
import com.enonic.autotests.model.ContentHandler;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.services.ContentService;
import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.services.RepositoryService;
import com.enonic.autotests.utils.BrowserUtils;

public class BaseTest
{
	protected Logger logger = Logger.getLogger();

	private ThreadLocal<TestSession> sessionRef = new ThreadLocal<TestSession>();
	
	protected ContentTypeService contentTypeService = new ContentTypeService();
	
	protected RepositoryService repositoryService = new RepositoryService();
	
	protected ContentService contentService = new ContentService();
	
	public String IMAGE_CONTENTTYPE_NAME = "Image";

	@BeforeClass(alwaysRun = true)
	public void readDesiredCapabilities(ITestContext context)
	{
		//logger.info("############### method readDesiredCapabilities started    ###################");
		TestSession testSession = new TestSession();

		String browser = (String) context.getCurrentXmlTest().getParameter("browser");
		if (browser == null)
		{
			throw new IllegalArgumentException("parameter browser was not specified! ");
		}
		logger.info("browser is  "+ browser );
		String browserVersion = (String) context.getCurrentXmlTest().getParameter("browserVersion");
		if (browserVersion == null)
		{
			logger.info("browserVersion was not specified! ");
		}else
		{
			logger.info("browser version  is:  "+ browserVersion );
		}
		String platform = (String) context.getCurrentXmlTest().getParameter("platform");
		if (platform == null)
		{
			throw new IllegalArgumentException("parameter platform was not specified! ");
		}
		logger.info("platform   is:  "+ platform );
		testSession.put(TestSession.BROWSER_NAME, browser);
		testSession.put(TestSession.BROWSER_VERSION, browserVersion);
		testSession.put(TestSession.PLATFORM, platform);
		String url = (String) context.getCurrentXmlTest().getParameter("base.url");
		if (url == null)
		{
			throw new IllegalArgumentException("parameter base url was not specified! ");
		}
		logger.info("base.url   is:  "+ url );
		testSession.put(TestSession.START_URL, url);
		String remoteParam = context.getCurrentXmlTest().getParameter("isRemote");
		if (remoteParam == null)
		{
			throw new IllegalArgumentException("parameter isRemote was not specified! ");
		}
		logger.info("isRemote   is:  "+ remoteParam );
		Boolean isRemote = Boolean.valueOf(remoteParam);
		if (isRemote != null)
		{
			testSession.put(TestSession.IS_REMOTE, isRemote);
			if (isRemote)
			{
				String hubUrl = (String) context.getCurrentXmlTest().getParameter("hub.url");
				if (hubUrl == null)
				{
					throw new IllegalArgumentException("parameter hubUrl was not specified! ");
				}
				logger.info("hubUrl   is:  "+ hubUrl );
				testSession.put(TestSession.HUB_URL, hubUrl);
			}
		}
		sessionRef.set(testSession);
		logger.info("TEST NAME::::::::::::::::::::::" + this.getClass().getCanonicalName());
		
	}

	@BeforeMethod
	public void openBrowser() throws IOException
	{
		BrowserUtils.createDriverAndOpenBrowser(getTestSession());

	}
	@AfterClass
	public void testInfo()
	{
		logger.info("end of test:"+ this.getClass().getName()+ sessionRef.get().getBrowserName());
	}

	@AfterTest
	public  void clearSession()
	{
		logger.info("end of test:"+ this.getClass().getName()+ "try to clear session "+sessionRef.get().getBrowserName());
		sessionRef.set(null);
	}

	@AfterMethod
	public void closeBrowser()
	{
		getTestSession().closeBrowser();

	}

	public TestSession getTestSession()
	{
		TestSession sess = sessionRef.get();
		if(sess == null)
		{
			logger.info("BaseTest: testsession is null!" );
		}
		return sess;
	}
	public WebDriver getSessionDriver()
	{
		return getTestSession().getDriver();
	}

	public void createImageCType()
	{
		logger.info("checks for the existance  of Content type, creates new content type if it does not exist");
		ContentType imagesType = new ContentType();
		imagesType.setName("Image");
		imagesType.setContentHandler(ContentHandler.IMAGES);
		imagesType.setDescription("content repository test");
		boolean isExist = contentTypeService.findContentType(getTestSession(), IMAGE_CONTENTTYPE_NAME);
		if (!isExist)
		{
			contentTypeService.createContentType(getTestSession(), imagesType);
			logger.info("New content type with 'Images' handler was created");
		} else
		{
			logger.info("Image content already exists");
		}
	}
}
