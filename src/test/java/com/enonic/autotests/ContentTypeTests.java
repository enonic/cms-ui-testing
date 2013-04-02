package com.enonic.autotests;

import java.io.IOException;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.enonic.autotests.model.ContentType;

public class ContentTypeTests extends BaseTest{
	
	@BeforeMethod
	public void openBrowser() throws IOException {
		TestUtils.getInstance().createDriverAndOpenBrowser(getTestSession());
		

	}

	@AfterMethod
	public void closeBrowser() {
		getTestSession().closeBrowser();
	
	}
	
	@Test(description="open 'Content Types' wizard page and create new custom content type")
	public void createCustomContentType(){
		ContentType ctype = new ContentType();
		ctype.setName("customct");
		ctype.setDescription("custom content type");
		ctype.setContentHandler(ContentType.DEFAULT_CONTENTHANDLER_NAME);
		ctype.setConfiguration("<contenttype> <indexparameters /></contenttype>");
		adminConsoleServiceV4.createContentType(getTestSession(), ctype );
	}
	
	
	@Test(description="open 'Content Types' wizard page and create content type with 'File' content handler")
	public void createFileContentType(){
		ContentType ctype = new ContentType();
		ctype.setName("filecontenttype");
		ctype.setDescription("description");
		ctype.setContentHandler("Files");
		
		adminConsoleServiceV4.createContentType(getTestSession(), ctype );
	}
}
