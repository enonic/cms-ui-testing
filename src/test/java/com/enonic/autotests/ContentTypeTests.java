package com.enonic.autotests;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.services.IAdminConsoleService;


@ContextConfiguration(locations = { "/test-applicationContext.xml" })
public class ContentTypeTests extends BaseTest{

	@Autowired
	IAdminConsoleService adminConsoleServiceV4;
	
	@BeforeMethod
	public void openBrowser() throws IOException {
		TestUtils.getInstance().createDriverAndOpenBrowser(getTestSession());
		

	}

	@AfterMethod
	public void closeBrowser() {
		getTestSession().closeBrowser();
	
	}
	
	@Test(description="open cretae 'Content Types' page and create content types")
	public void createFileContentType(){
		ContentType ctype = new ContentType();
		ctype.setName("file content type");
		ctype.setDescription("description");
		ctype.setContentHandler("Files");
		adminConsoleServiceV4.createContentType(getTestSession(), ctype );
	}
}
