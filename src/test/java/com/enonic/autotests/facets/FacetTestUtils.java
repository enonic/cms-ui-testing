package com.enonic.autotests.facets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.util.IteratorIterable;
import org.testng.Assert;
import org.xml.sax.InputSource;

import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.utils.XmlReader;

public class FacetTestUtils
{
	/**
	 * Gets list of {@link Term}instances from 'preview datasource' output.
	 * 
	 * @param xmlString
	 * @return
	 */
	public static List<Term> getTermsFromPreview(String xmlString)
	{
		SAXBuilder builder = new SAXBuilder();
		List<Term> terms = new ArrayList<>();
		InputSource is = new InputSource(new StringReader(xmlString));
		Document document = null;
		try
		{
			document = (Document) builder.build(is);
		} catch (JDOMException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		Element rootNode = document.getRootElement();
		IteratorIterable<Element> itr = rootNode.getDescendants(new ElementFilter("term"));
		Term term = null;
		while (itr.hasNext())
		{
			term = new Term();
			Element element = (Element) itr.next();
			try
			{
				term.setHits(element.getAttribute("hits").getIntValue());
				term.setValue(element.getValue());
				terms.add(term);
			} catch (DataConversionException e)
			{
				Assert.fail("DataConversionException occured");
			}
		}

		return terms;
	}

	/**
	 * Gets list of {@link Range}instances from 'preview datasource' output.
	 * 
	 * @param xmlString
	 * @return
	 */
	public static List<Range> getRangesFromPreview(String xmlString)
	{
		SAXBuilder builder = new SAXBuilder();
		List<Range> ranges = new ArrayList<>();
		InputSource is = new InputSource(new StringReader(xmlString));
		Document document = null;
		try
		{
			document = (Document) builder.build(is);
		} catch (JDOMException e)
		{
			Assert.fail("JDOMException occured"+ e.getMessage());
			
		} catch (IOException e)
		{
			Assert.fail("IOException occured"+ e.getMessage());
		}
		Element rootNode = document.getRootElement();
		IteratorIterable<Element> itr = rootNode.getDescendants(new ElementFilter("range"));
		Range range = null;
		while (itr.hasNext())
		{
			range = new Range();
			Element element = (Element) itr.next();
			try
			{
				range.setHits(element.getAttribute("hits").getIntValue());

				if (element.getAttribute("from") != null)
				{
					range.setFrom(element.getAttribute("from").getFloatValue());
				}

				range.setTo(element.getAttribute("to").getFloatValue());
				ranges.add(range);
			} catch (DataConversionException e)
			{
				Assert.fail("DataConversionException occured");
			}
		}

		return ranges;
	}

	/**
	 * Parse xml content and return list of {@link Histogram} instances.
	 * 
	 * @param xmlString
	 * @return
	 */
	public static List<Histogram> getHistogramFromPreview(String xmlString)
	{
		SAXBuilder builder = new SAXBuilder();
		List<Histogram> histograms = new ArrayList<>();
		InputSource is = new InputSource(new StringReader(xmlString));
		Document document = null;
		try
		{
			document = (Document) builder.build(is);
		} catch (JDOMException e)
		{
			Assert.fail("JDOMException occured"+ e.getMessage());
			
		} catch (IOException e)
		{
			Assert.fail("IOException occured"+ e.getMessage());
		}
		Element rootNode = document.getRootElement();
		IteratorIterable<Element> itr = rootNode.getDescendants(new ElementFilter("interval"));
		Histogram histogram = null;
		while (itr.hasNext())
		{
			histogram = new Histogram();
			Element element = (Element) itr.next();
			try
			{
				histogram.setHits(element.getAttribute("hits").getIntValue());

				histogram.setValue(Float.valueOf(element.getText()));
				histograms.add(histogram);
			} catch (DataConversionException e)
			{
				Assert.fail("DataConversionException occured");
			}
		}

		return histograms;
	}

	/**
	 * Parse xml content and return list of {@link DateHistogram} instances.
	 * 
	 * @param xmlString
	 * @return
	 */
	public static List<DateHistogram> getDateHistogramFromPreview(String xmlString)
	{
		SAXBuilder builder = new SAXBuilder();
		List<DateHistogram> histograms = new ArrayList<>();
		InputSource is = new InputSource(new StringReader(xmlString));
		Document document = null;
		try
		{
			document = (Document) builder.build(is);
		} catch (JDOMException e)
		{
			Assert.fail("JDOMException occured"+ e.getMessage());
			
		} catch (IOException e)
		{
			Assert.fail("IOException occured"+ e.getMessage());
		}
		Element rootNode = document.getRootElement();
		IteratorIterable<Element> itr = rootNode.getDescendants(new ElementFilter("interval"));
		DateHistogram histogram = null;
		while (itr.hasNext())
		{
			histogram = new DateHistogram();
			Element element = (Element) itr.next();
			try
			{
				histogram.setHits(element.getAttribute("hits").getIntValue());

				histogram.setDate(element.getText());
				histograms.add(histogram);
			} catch (DataConversionException e)
			{
				Assert.fail("DataConversionException occured");
			}
		}

		return histograms;
	}

	/**
	 * Parse XML file and gets list of persons.
	 * 
	 * @param file
	 *            xml -file name.
	 * @return
	 */
	public static List<Person> getPersons(String file)
	{
		List<Person> persons = new ArrayList<>();
		Person person = null;
		SAXBuilder builder = new SAXBuilder();
		URL dirURL = XmlReader.class.getClassLoader().getResource(file);
		File xmlFile = null;
		try
		{
			xmlFile = new File(dirURL.toURI());
		} catch (URISyntaxException e)
		{
			Assert.fail("URISyntaxException occured" + e.getMessage());
		}
		try
		{

			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();

			List<Element> personsElements = rootNode.getChildren("person");

			for (Element personElement : personsElements)
			{
				person = new Person();
				person.setFirstname(personElement.getChildText("firstname"));
				person.setLastname(personElement.getChildText("lastname"));
				person.setBalance(Integer.valueOf(personElement.getChildText("balance")));
				person.setBirthdate(personElement.getChildText("birthdate"));
				person.setNationality(personElement.getChildText("nationality"));
				person.setGender(personElement.getChildText("gender"));

				persons.add(person);
			}

		} catch (IOException io)
		{
			Assert.fail("IOException occured" + io.getMessage());
			
		} catch (JDOMException jdomex)
		{
			Assert.fail("JDOMException occured" + jdomex.getMessage());
		}

		return persons;
	}



	/**
	 * @param dataXmlFile
	 * @param lastname
	 * @return
	 */
	public static List<Person> getPersonsWithLasttname(String dataXmlFile, String lastname)
	{
		List<Person> allpersons = getPersons(dataXmlFile);
		List<Person> filtered = new ArrayList<>();
		for (Person p : allpersons)
		{
			if (p.getLastname().equalsIgnoreCase(lastname))
			{
				filtered.add(p);
			}
		}
		return filtered;
	}

	/**
	 * @param dataXmlFile
	 * @param range
	 * @return
	 */
	public static List<Person> getPersonsByRange(String dataXmlFile, Range range)
	{
		Float from = range.getFrom();
		Float to = range.getTo();
		List<Person> allpersons = getPersons(dataXmlFile);
		List<Person> filtered = new ArrayList<>();
		for (Person p : allpersons)
		{
			if (p.getBalance() >= from && (to != null && p.getBalance() < to))
			{
				filtered.add(p);
			}
		}
		return filtered;
	}

	/**
	 * @param dataXmlFile
	 * @param balance
	 * @return
	 */
	public static List<Person> getPersonsByBalance(String dataXmlFile, Float balance)
	{

		List<Person> allpersons = getPersons(dataXmlFile);
		List<Person> filtered = new ArrayList<>();
		for (Person p : allpersons)
		{
			if (p.getBalance() == balance)
			{
				filtered.add(p);
			}
		}
		return filtered;
	}

	/**
	 * @param dataXmlFile
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static List<Person> getPersonsByBirthYear(String dataXmlFile, String date) throws ParseException
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = dateFormat.parse(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d1);
		int year = cal.get(Calendar.YEAR);

		List<Person> allpersons = getPersons(dataXmlFile);
		List<Person> filtered = new ArrayList<>();
		DateFormat dateF = new SimpleDateFormat("yyyy-MM-dd");
		for (Person p : allpersons)
		{

			Date d2 = dateF.parse(p.getBirthdate());
			cal.setTime(d2);
			int yy = cal.get(Calendar.YEAR);
			if (yy == year)
			{
				filtered.add(p);
			}
		}
		return filtered;
	}

	/**
	 * @param categoryKey
	 * @param fileCfg
	 * @return
	 */
	public static String buildDataSourceString(int categoryKey, String fileCfg)
	{
		InputStream in = FacetTestUtils.class.getClassLoader().getResourceAsStream(fileCfg);
		String datasource = TestUtils.getInstance().readConfiguration(in);
		int index = datasource.indexOf("categoryKeys\">");
		StringBuffer sb = new StringBuffer(datasource);
		sb.insert(index + 14, categoryKey);
		return sb.toString();
	}

}
