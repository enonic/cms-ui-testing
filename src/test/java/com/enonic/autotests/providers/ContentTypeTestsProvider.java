package com.enonic.autotests.providers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.testng.annotations.DataProvider;

import com.enonic.autotests.testdata.contenttype.ContenTypeUtils;
import com.enonic.autotests.testdata.contenttype.ContentTypeTestData;
import com.enonic.autotests.testdata.contenttype.ContentTypeXml;

/**
 * Data Provider for {@link ContentTypeTests}
 *
 * 04.04.2013
 */
public class ContentTypeTestsProvider {

	@DataProvider(name = "createContentTypePositive")
	public static Object[][] createContentTypePositive() throws JAXBException {

		List<Object[]> casesParameters = new ArrayList<Object[]>();
		JAXBContext context = JAXBContext.newInstance(ContentTypeTestData.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		ContentTypeTestData testdata = (ContentTypeTestData) unmarshaller.unmarshal(new File(ContenTypeUtils.BASE_PATH_TO_RES+"test-data.xml"));
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
		ContentTypeTestData testdata = (ContentTypeTestData) unmarshaller.unmarshal(new File(ContenTypeUtils.BASE_PATH_TO_RES+
				"test-data-negative.xml"));
		List<ContentTypeXml> cases = testdata.getContentTypes();
		for (ContentTypeXml ctype : cases) {
			casesParameters.add(new Object[] { ctype

			});
		}
		return casesParameters.toArray(new Object[casesParameters.size()][]);
	}

}
