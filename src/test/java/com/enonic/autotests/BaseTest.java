package com.enonic.autotests;

import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;

public class BaseTest extends AbstractTestNGSpringContextTests{

	private TestSession testSession;



	@BeforeClass
	public void setupBeforeSuite(ITestContext context){
		testSession = new TestSession();
		String  browser = (String)context.getCurrentXmlTest().getParameter("browser");
		testSession.put(TestSession.BROWSER_NAME, browser);
		String  url = (String)context.getCurrentXmlTest().getParameter("base.url");
		testSession.put(TestSession.START_URL, url);
		Boolean isRemote = Boolean.valueOf( context.getCurrentXmlTest().getParameter("isRemote"));
		if(isRemote!=null){
			testSession.put(TestSession.IS_REMOTE, isRemote);
			String  habUrl = (String)context.getCurrentXmlTest().getParameter("hab.url");
			testSession.put(TestSession.HUB_URL, habUrl);
		}
		

	}

	
	public TestSession getTestSession() {
		return testSession;
	}

	public void setTestSession(TestSession testSession) {
		this.testSession = testSession;
	}
}
