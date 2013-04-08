package com.enonic.autotests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.exceptions.ContentRepositoryException;
import com.enonic.autotests.exceptions.ContentTypeException;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.pages.v4.adminconsole.content.ContentRepositoriesFrame;
import com.enonic.autotests.pages.v4.adminconsole.contenttype.ContentTypesFrame;
import com.enonic.autotests.providers.ContentRepositoryProvider;
import com.enonic.autotests.providers.ContentTypeTestsProvider;
import com.enonic.autotests.testdata.contenttype.ContentConvertor;
import com.enonic.autotests.testdata.contenttype.ContentRepositoryXml;
import com.enonic.autotests.testdata.contenttype.ContentTypeXml;

public class ContentTypeTests extends BaseTest{

	@Test(description = "positive tests",dataProvider = "createContentTypePositive", dataProviderClass = ContentTypeTestsProvider.class)
	public void testCreateContentTypePositive( ContentTypeXml ctypeXML){
		logger.info(ctypeXML.getCaseInfo());
		ContentType ctype = ContentConvertor.convertXmlDataToContentType(ctypeXML);		
		adminConsoleServiceV4.createContentType(getTestSession(), ctype );
		ContentTypesFrame frame = new ContentTypesFrame(getTestSession());
		boolean isCreated = frame.verifyIsCreated(ctype.getName());
		Assert.assertTrue(isCreated,"new Content Type was not found on the Content Types page! ");
	}
	
	@Test(dependsOnMethods="testCreateContentTypePositive",description = "negative tests",expectedExceptions=ContentTypeException.class,dataProvider = "createContentTypeNegative", dataProviderClass = ContentTypeTestsProvider.class)
	public void createContentTypeNegative(ContentTypeXml ctypeXML){
		logger.info(ctypeXML.getCaseInfo());
		ContentType ctype = ContentConvertor.convertXmlDataToContentType(ctypeXML);		
		adminConsoleServiceV4.createContentType(getTestSession(), ctype );
		
	}
	
	@Test(description="create new Content Repository with new Content Type",dependsOnMethods="testCreateContentTypePositive",dataProvider = "createContentRepositoryPositive", dataProviderClass = ContentRepositoryProvider.class)
	public void createContentRepository(ContentRepositoryXml contentRepoXML){
		logger.info(contentRepoXML.getCaseInfo());
		ContentRepository cRepository = ContentConvertor.convertXmlDataToContentRepository(contentRepoXML);		
		adminConsoleServiceV4.createContentRepository(getTestSession(), cRepository);
		ContentRepositoriesFrame repositoryFrame = new ContentRepositoriesFrame(getTestSession());
		repositoryFrame.verifyIsPresentedInTable(cRepository.getName());
		
	}
	@Test(description="negative test",expectedExceptions = ContentRepositoryException.class,dependsOnMethods="testCreateContentTypePositive",dataProvider = "createContentRepositoryNegative", dataProviderClass = ContentRepositoryProvider.class)
	public void createContentRepositoryNegative(ContentRepositoryXml contentRepoXML){
		logger.info(contentRepoXML.getCaseInfo());
		ContentRepository cRepository = ContentConvertor.convertXmlDataToContentRepository(contentRepoXML);		
		adminConsoleServiceV4.createContentRepository(getTestSession(), cRepository);		
	}

}
