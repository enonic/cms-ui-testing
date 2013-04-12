package com.enonic.autotests.providers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.testng.annotations.DataProvider;

import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.model.CustomContent.InputModel;
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
	private static final String CONTENT_REPO_DELETE_TEST_DATA = "crepo-delete.xml";
	private static final String CONTENT_REPO_TEST_DATA_NEG = "crepo-test-data-negative.xml";
	private static final String CONTENT_REPO_TEST_DATA_ALL_INPUTS = "cty-all-inputs-4-5-6.xml";

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
	@DataProvider(name = "deleteRepository")
	public static Object[][] deleteRepository() throws JAXBException {

		List<Object[]> casesParameters = new ArrayList<Object[]>();
		JAXBContext context = JAXBContext.newInstance(ContentRepositoryTestData.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		// logger.info(message)
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream("contentrepository/" + CONTENT_REPO_DELETE_TEST_DATA);
		if (in == null) {
			throw new TestFrameworkException("test data was not found!");
		}
		ContentRepositoryTestData testdata = (ContentRepositoryTestData) unmarshaller.unmarshal(in);
		List<ContentRepositoryXml> repos = testdata.getContentRepositories();
		for (ContentRepositoryXml ctype : repos) {
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
		try {
			in.close();
		} catch (IOException e) {
			//nothing to do.
		}
		return casesParameters.toArray(new Object[casesParameters.size()][]);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	@DataProvider(name = "addContentToRepository")
	public static Object[][] addContentToRepository() throws JAXBException {
		List<Object[]> casesParameters = new ArrayList<Object[]>();
		

		InputStream in1 = ContentConvertor.class.getClassLoader().getResourceAsStream("contenttype/" + CONTENT_REPO_TEST_DATA_ALL_INPUTS);
		if (in1 == null) {
			throw new TestFrameworkException("test data was not found!");
		}
		InputHandler handler = null;
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = factory.newSAXParser();
			handler = new InputHandler();
			saxParser.parse(in1, handler);
			
		} catch (Throwable t) {
			throw new TestFrameworkException("Error during parsing test-dtata!");
		}
		
		JAXBContext context = JAXBContext.newInstance(ContentRepositoryTestData.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		// logger.info(message)
		InputStream in2 = ContentConvertor.class.getClassLoader().getResourceAsStream("contentrepository/" + CONTENT_REPO_TEST_DATA);
		if (in2 == null) {
			throw new TestFrameworkException("test data was not found!");
		}
		ContentRepositoryTestData testdata = (ContentRepositoryTestData) unmarshaller.unmarshal(in2);
		List<ContentRepositoryXml> repositories = testdata.getContentRepositories();
		
			//casesParameters.add(new Object[] { handler.getInputs(),repositories });
			casesParameters.add(new Object[] { repositories });
		
		return casesParameters.toArray(new Object[casesParameters.size()][]);
	}
	
}
