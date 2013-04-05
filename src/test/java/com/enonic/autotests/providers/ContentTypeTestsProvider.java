package com.enonic.autotests.providers;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.testng.annotations.DataProvider;

import com.enonic.autotests.ContentTypeTests;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.logger.Logger;
import com.enonic.autotests.testdata.contenttype.ContenTypeUtils;
import com.enonic.autotests.testdata.contenttype.ContentTypeTestData;
import com.enonic.autotests.testdata.contenttype.ContentTypeXml;

/**
 * Data Provider for {@link ContentTypeTests}
 *
 * 04.04.2013
 */
public class ContentTypeTestsProvider {
	private static   Logger logger = Logger.getLogger();
	private static final String POSITIVE_TEST_DATA_FILE_NAME = "test-data.xml";
	private static final String NEGATIVE_TEST_DATA_FILE_NAME = "test-data-negative.xml";

	@DataProvider(name = "createContentTypePositive")
	public static Object[][] createContentTypePositive() throws JAXBException {

		List<Object[]> casesParameters = new ArrayList<Object[]>();
		JAXBContext context = JAXBContext.newInstance(ContentTypeTestData.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		//logger.info(message)
		InputStream in = ContenTypeUtils.class.getClassLoader().getResourceAsStream("contenttype/" +POSITIVE_TEST_DATA_FILE_NAME);
		String fs  =System.getProperty("file.separator");
		//InputStream in = ContenTypeUtils.class.getClassLoader().getResourceAsStream("contenttype"+fs+POSITIVE_TEST_DATA_FILE_NAME);
		if(in == null){
			throw new TestFrameworkException("test data was not found!");
		}
		ContentTypeTestData testdata = (ContentTypeTestData) unmarshaller.unmarshal(in);
		List<ContentTypeXml> cases = testdata.getContentTypes();
		for (ContentTypeXml ctype : cases) {
			casesParameters.add(new Object[] { ctype

			});
		}
		return casesParameters.toArray(new Object[casesParameters.size()][]);
	}

	@DataProvider(name = "createContentTypeNegative")
	public static Object[][] createContentTypeNegative() throws JAXBException {

		List<Object[]> casesParameters = new ArrayList<Object[]>();
		JAXBContext context = JAXBContext.newInstance(ContentTypeTestData.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		//InputStream in = ContenTypeUtils.class.getClassLoader().getResourceAsStream("contenttype/"  + NEGATIVE_TEST_DATA_FILE_NAME);
		InputStream in = ContenTypeUtils.class.getClassLoader().getResourceAsStream("contenttype/"+NEGATIVE_TEST_DATA_FILE_NAME);
		ContentTypeTestData testdata = (ContentTypeTestData) unmarshaller.unmarshal(in);
		List<ContentTypeXml> cases = testdata.getContentTypes();
		for (ContentTypeXml ctype : cases) {
			casesParameters.add(new Object[] { ctype

			});
		}
		return casesParameters.toArray(new Object[casesParameters.size()][]);
	}

}
