package com.enonic.autotests.providers;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.testng.annotations.DataProvider;

import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.testdata.contenttype.ContentConvertor;
import com.enonic.autotests.testdata.contenttype.ContentRepositoryTestData;
import com.enonic.autotests.testdata.contenttype.ContentRepositoryXml;

/**
 * Provider for creating 
 *
 * 08.04.2013
 */
public class ContentRepositoryProvider {

	private static final String CONTENT_REPO_TEST_DATA = "crepo-test-data.xml";
	private static final String CONTENT_REPO_TEST_DATA_NEG = "crepo-test-data-negative.xml";

	@DataProvider(name = "createContentRepositoryPositive")
	public static Object[][] createContentRepository() throws JAXBException {

		List<Object[]> casesParameters = new ArrayList<Object[]>();
		JAXBContext context = JAXBContext.newInstance(ContentRepositoryTestData.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		// logger.info(message)
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream("contentrepository/" + CONTENT_REPO_TEST_DATA);
		if (in == null) {
			throw new TestFrameworkException("test data was not found!");
		}
		ContentRepositoryTestData testdata = (ContentRepositoryTestData) unmarshaller.unmarshal(in);
		List<ContentRepositoryXml> cases = testdata.getContentRepositories();
		for (ContentRepositoryXml ctype : cases) {
			casesParameters.add(new Object[] { ctype });
		}
		return casesParameters.toArray(new Object[casesParameters.size()][]);
	}
	@DataProvider(name = "createContentRepositoryNegative")
	public static Object[][] createContentRepositoryNegative() throws JAXBException {

		List<Object[]> casesParameters = new ArrayList<Object[]>();
		JAXBContext context = JAXBContext.newInstance(ContentRepositoryTestData.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream("contentrepository/" + CONTENT_REPO_TEST_DATA_NEG);
		if (in == null) {
			throw new TestFrameworkException("test data was not found!");
		}
		ContentRepositoryTestData testdata = (ContentRepositoryTestData) unmarshaller.unmarshal(in);
		List<ContentRepositoryXml> cases = testdata.getContentRepositories();
		for (ContentRepositoryXml ctype : cases) {
			casesParameters.add(new Object[] { ctype });
		}
		return casesParameters.toArray(new Object[casesParameters.size()][]);
	}
	//
}
