package com.enonic.autotests.providers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.testng.annotations.DataProvider;

import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.testdata.content.AbstractContentXml;
import com.enonic.autotests.testdata.content.ContentRepositoryTestData;
import com.enonic.autotests.testdata.content.ContentRepositoryXml;
import com.enonic.autotests.testdata.content.ContentTestData;
import com.enonic.autotests.testdata.contenttype.ContentConvertor;

/**
 * Provider for creating
 * 
 * 08.04.2013
 */
public class ContentRepositoryProvider
{

	private static final String CONTENT_REPO_TEST_DATA = "crepo-test-data.xml";
	private static final String CONTENT_REPO_DELETE_TEST_DATA = "crepo-delete.xml";
	private static final String CONTENT_REPO_TEST_DATA_NEG = "crepo-test-data-negative.xml";
	private static final String CONTENT_REPO_TEST_DATA_ALL_INPUTS = "cty-all-inputs-4-5-6.xml";
	private static final String ADD_CONTENT = "file-image-contents.xml";

	@DataProvider(name = "createContentRepositoryPositive")
	public static Object[][] createContentRepository() throws JAXBException
	{

		List<Object[]> casesParameters = new ArrayList<Object[]>();
		JAXBContext context = JAXBContext.newInstance(ContentRepositoryTestData.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream("test-data/contentrepository/" + CONTENT_REPO_TEST_DATA);
		if (in == null)
		{
			throw new TestFrameworkException("test data was not found!");
		}
		ContentRepositoryTestData testdata = (ContentRepositoryTestData) unmarshaller.unmarshal(in);
		List<ContentRepositoryXml> cases = testdata.getContentRepositories();
		for (ContentRepositoryXml ctype : cases)
		{
			casesParameters.add(new Object[] { ctype });
		}
		return casesParameters.toArray(new Object[casesParameters.size()][]);
	}

	@DataProvider(name = "deleteRepository")
	public static Object[][] deleteRepository() throws JAXBException
	{

		List<Object[]> casesParameters = new ArrayList<Object[]>();
		JAXBContext context = JAXBContext.newInstance(ContentRepositoryTestData.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream("test-data/contentrepository/" + CONTENT_REPO_DELETE_TEST_DATA);
		if (in == null)
		{
			throw new TestFrameworkException("test data was not found!");
		}
		ContentRepositoryTestData testdata = (ContentRepositoryTestData) unmarshaller.unmarshal(in);
		List<ContentRepositoryXml> repos = testdata.getContentRepositories();
		for (ContentRepositoryXml ctype : repos)
		{
			casesParameters.add(new Object[] { ctype });
		}
		return casesParameters.toArray(new Object[casesParameters.size()][]);
	}

	@DataProvider(name = "createContentRepositoryNegative")
	public static Object[][] createContentRepositoryNegative() throws JAXBException
	{

		List<Object[]> casesParameters = new ArrayList<Object[]>();
		JAXBContext context = JAXBContext.newInstance(ContentRepositoryTestData.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream("test-data/contentrepository/" + CONTENT_REPO_TEST_DATA_NEG);
		if (in == null)
		{
			throw new TestFrameworkException("test data was not found!");
		}
		ContentRepositoryTestData testdata = (ContentRepositoryTestData) unmarshaller.unmarshal(in);
		List<ContentRepositoryXml> cases = testdata.getContentRepositories();
		for (ContentRepositoryXml ctype : cases)
		{
			casesParameters.add(new Object[] { ctype });
		}
		try
		{
			in.close();
		} catch (IOException e)
		{
			// nothing to do.
		}
		return casesParameters.toArray(new Object[casesParameters.size()][]);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////
	@DataProvider(name = "addContent")
	public static Object[][] addContentToRepository() throws JAXBException
	{

		List<Object[]> casesParameters = new ArrayList<Object[]>();

		JAXBContext context = JAXBContext.newInstance(ContentTestData.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream("test-data/contentrepository/" + ADD_CONTENT);
		if (in == null)
		{
			throw new TestFrameworkException("test data was not found!");
		}
		ContentTestData testdata = (ContentTestData) unmarshaller.unmarshal(in);
		List<AbstractContentXml> cases = testdata.getContentList();
		for (AbstractContentXml ctype : cases)
		{
			casesParameters.add(new Object[] { ctype });
		}
		return casesParameters.toArray(new Object[casesParameters.size()][]);

	}

	public static void main(String[] args) throws JAXBException
	{
		JAXBContext context = JAXBContext.newInstance(ContentTestData.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream("test-data/contentrepository/" + ADD_CONTENT);
		if (in == null)
		{
			throw new TestFrameworkException("test data was not found!");
		}

		ContentTestData testdata = (ContentTestData) unmarshaller.unmarshal(in);
		List<AbstractContentXml> list = testdata.getContentList();
	}

}
