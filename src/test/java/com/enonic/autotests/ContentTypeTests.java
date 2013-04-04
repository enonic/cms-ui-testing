package com.enonic.autotests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.exceptions.ContentTypeException;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.pages.v4.adminconsole.contenttype.ContentTypesFrame;
import com.enonic.autotests.providers.ContentTypeTestsProvider;
import com.enonic.autotests.testdata.contenttype.ContenTypeUtils;
import com.enonic.autotests.testdata.contenttype.ContentTypeXml;

public class ContentTypeTests extends BaseTest{
	
	
	

	@Test(description = "positive tests",dataProvider = "createContentTypePositive", dataProviderClass = ContentTypeTestsProvider.class)
	public void createContentTypePositive( ContentTypeXml ctypeXML){
		logger.info(ctypeXML.getCaseInfo());
		ContentType ctype = ContenTypeUtils.convertToModel(ctypeXML);		
		adminConsoleServiceV4.createContentType(getTestSession(), ctype );
		ContentTypesFrame frame = new ContentTypesFrame(getTestSession());
		boolean isCreated = frame.verifyIsCreated(ctype.getName());
		Assert.assertTrue(isCreated,"new Content Type was not found on the Content Types page! ");
	}
	
	@Test(dependsOnMethods="createContentTypePositive",description = "negative tests",expectedExceptions=ContentTypeException.class,dataProvider = "createContentTypeNegative", dataProviderClass = ContentTypeTestsProvider.class)
	public void createContentTypeNegative(ContentTypeXml ctypeXML){
		logger.info(ctypeXML.getCaseInfo());
		ContentType ctype = ContenTypeUtils.convertToModel(ctypeXML);		
		adminConsoleServiceV4.createContentType(getTestSession(), ctype );
		
	}

}
