package com.enonic.autotests.facets;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
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

import com.enonic.autotests.utils.XmlReader;

public class FacetTestUtils
{
	/**
	 * Gets list of {@link Term}instances from 'preview datasource' output.
	 * 
	 * @param xmlString
	 * @return
	 */
	public static List<Term> getTerms(String xmlString)
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
	public static List<Range> getRanges(String xmlString)
	{
		SAXBuilder builder = new SAXBuilder();
		List<Range> ranges = new ArrayList<>();
		InputSource is = new InputSource(new StringReader(xmlString));
		// Document document = (Document) builder.build(xmlFile);
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
		IteratorIterable<Element> itr = rootNode.getDescendants(new ElementFilter("range"));
		Range range = null;
		while (itr.hasNext())
		{
			range = new Range();
			Element element = (Element) itr.next();
			try
			{
				range.setHits(element.getAttribute("hits").getIntValue());

				range.setFrom(element.getAttribute("from").getIntValue());
				range.setTo(element.getAttribute("to").getIntValue());
			} catch (DataConversionException e)
			{
				Assert.fail("DataConversionException occured");
			}
		}

		return ranges;
	}


	/**
	 * Parse XML file and  gets list of persons.
	 *
	 * @param file xml -file name.
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
			Assert.fail("IOException occured"+ io.getMessage());
		} catch (JDOMException jdomex)
		{
			Assert.fail("JDOMException occured"+ jdomex.getMessage());
		}

		return persons;
	}

	// getPersons("test-data/facets-ctypes/persons-to-import.xml");
	public static List<Person> getPersonsWithFirstname(String dataXmlFile, String firstname)
	{
		List<Person> allpersons = getPersons(dataXmlFile);
		List<Person> filtered = new ArrayList<>();
		for (Person p : allpersons)
		{
			if (p.getFirstname().equals(firstname))
			{
				filtered.add(p);
			}
		}
		return filtered;
	}

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

	public static List<Person> getPersonsWithBalance(String dataXmlFile, int from, int to)
	{
		List<Person> allpersons = getPersons(dataXmlFile);
		List<Person> filtered = new ArrayList<>();
		for (Person p : allpersons)
		{
			if (p.getBalance() > from && p.getBalance() < to)
			{
				filtered.add(p);
			}
		}
		return filtered;
	}

}
