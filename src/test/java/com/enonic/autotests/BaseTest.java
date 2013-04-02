package com.enonic.autotests;

import java.io.IOException;

import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.enonic.autotests.services.IAdminConsoleService;
import com.enonic.autotests.services.v4.AdminConsoleServiceImplV4;

public class BaseTest {

	protected IAdminConsoleService adminConsoleServiceV4 = new AdminConsoleServiceImplV4();
	private TestSession testSession;

	@BeforeClass
	public void readDesiredCapabilities(ITestContext context) {
		testSession = new TestSession();
		String browser = (String) context.getCurrentXmlTest().getParameter("browser");
		testSession.put(TestSession.BROWSER_NAME, browser);
		String url = (String) context.getCurrentXmlTest().getParameter("base.url");
		testSession.put(TestSession.START_URL, url);
		Boolean isRemote = Boolean.valueOf(context.getCurrentXmlTest().getParameter("isRemote"));
		if (isRemote != null) {
			testSession.put(TestSession.IS_REMOTE, isRemote);
			if (isRemote) {
				String hubUrl = (String) context.getCurrentXmlTest().getParameter("hub.url");
				testSession.put(TestSession.HUB_URL, hubUrl);
			}
		}

	}
	@BeforeMethod
	public void openBrowser() throws IOException {
		TestUtils.getInstance().createDriverAndOpenBrowser(getTestSession());
		
		

	}

	@AfterMethod
	public void closeBrowser() {
		getTestSession().closeBrowser();
	
	}

	public TestSession getTestSession() {
		return testSession;
	}

}
