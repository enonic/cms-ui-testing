package com.enonic.autotests.general;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.BaseTest;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.pages.adminconsole.contenttype.ContentTypesFrame;
import com.enonic.autotests.providers.ContentTypeTestsProvider;
import com.enonic.autotests.testdata.contenttype.ContentConvertor;
import com.enonic.autotests.testdata.contenttype.ContentTypeXml;

public class ContentTypeTests extends BaseTest
{
	private final String CONTENTTYPE_CREATED_LIST = "ctypes_list";
	
	
	@Test(description = "positive tests: create new content type", dataProvider = "createContentTypePositive", dataProviderClass = ContentTypeTestsProvider.class)
	public void testCreateContentTypePositive(ContentTypeXml ctypeXML)
	{
		logger.info("###### STARTED "+ctypeXML.getCaseInfo()   +"######");
		String name = ctypeXML.getName() + Math.abs(new Random().nextInt());
		ContentType ctype = ContentConvertor.convertXmlDataToContentType(ctypeXML);
		ctype.setName(name);
		ContentTypesFrame frame = contentTypeService.createContentType(getTestSession(), ctype);

		boolean isCreated = frame.verifyIsPresent(ctype.getName());
		Assert.assertTrue(isCreated, "new Content Type was not found on the Content Types page! ");
		List<ContentType> ctypes =  (List<ContentType>)getTestSession().get(CONTENTTYPE_CREATED_LIST);
		if(ctypes == null)
		{
			//create java a empty Collection, this collection should contain all created content types, and these content types will be deleted in the 'deleteContentTypeTest'
			ctypes = new ArrayList<>();
			getTestSession().put(CONTENTTYPE_CREATED_LIST, ctypes);
		}
		ctypes.add(ctype);
		logger.info("$$$$ FINISHED: "+ctypeXML.getCaseInfo());
	}

	@Test(description = "negative tests: try to create new content type with wrong data", expectedExceptions = SaveOrUpdateException.class, dataProvider = "createContentTypeNegative", dataProviderClass = ContentTypeTestsProvider.class)
	public void createContentTypeNegative(ContentTypeXml ctypeXML)
	{
		logger.info("#### NEGATIVE TEST STARTED: " + ctypeXML.getCaseInfo());
		String name = ctypeXML.getName() + Math.abs(new Random().nextInt());
		ContentType ctype = ContentConvertor.convertXmlDataToContentType(ctypeXML);
		ctype.setName(name);
		contentTypeService.createContentType(getTestSession(), ctype);
		
	}
	
	@Test(dependsOnMethods = "testCreateContentTypePositive")
	public void deleteContentTypeTest()
	{
		logger.info("#### STARTED: delete content types: " );
		ContentTypesFrame frame = null;
		List<ContentType> ctypes =  (List<ContentType>)getTestSession().get(CONTENTTYPE_CREATED_LIST);
		for(ContentType ct:ctypes)
		{
			frame  = contentTypeService.deleteContentType(getTestSession(), ct.getName());
			boolean isPresent = frame.verifyIsPresent(ct.getName());
			if(!isPresent)
			{
				logger.info("content type:"+ ct.getName()+" was removed");
			}else{
				logger.info("ERROR during deletion of the content type:"+ ct.getName()+" was not removed");
				Assert.fail("The content type with name:" + ct.getName() + "was not deleted");
			}
		}
		logger.info("$$$$ FINISHED: deleteContentTypeTest");
		
	}

	
}
