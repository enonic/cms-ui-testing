package com.enonic.autotests.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.enonic.autotests.testdata.contenttype.ContentConvertor;

/**
 * This class used for Import content Tests
 *
 */
public class XmlReader
{
	public List<String> readNodeValue(String fileName, String childrenName, String childNodeName)
	{
		SAXBuilder builder = new SAXBuilder();
		URL dirURL = XmlReader.class.getClassLoader().getResource(fileName);
		File xmlFile = null;
		try
		{
			xmlFile = new File(dirURL.toURI());
		} catch (URISyntaxException e)
		{
			
		}
		List<String> vlaues = new ArrayList<>();
		try
		{

			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			List<Element> listNodes = rootNode.getChildren(childrenName);

			for(Element elPerson:listNodes)
			{
				
				vlaues.add(elPerson.getChildText(childNodeName));
			}

		} catch (IOException io)
		{
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex)
		{
			System.out.println(jdomex.getMessage());
		}
		return vlaues;
	}

	
	/**
	 * Importing into block groups:
	 * gets Persons with events.
	 * @return
	 */
	public List<Person> getPersons(String dataXmlFile)
	{
		List<Person>persons = new ArrayList<>();
		Person person = null;
		SAXBuilder builder = new SAXBuilder();
		URL dirURL = XmlReader.class.getClassLoader().getResource(dataXmlFile);
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
			UserEvent userevent = null;
			
			for(Element personElement:personsElements)
			{
				person = new Person();
				person.setName(personElement.getChildText("name"));
				List<UserEvent> userEvents = new ArrayList<>();
				if(personElement.getChildren("events").size()!=0)
				{
					List<Element> allEvents = personElement.getChildren("events").get(0).getChildren("event");
					for(Element el: allEvents)
					{
						userevent = new UserEvent();
						String name = el.getChildText("name");
						userevent.setName(name);
						System.out.println(name);
						String value = el.getChildText("date");
						userevent.setValue(value);
						System.out.println(value);
						userEvents.add(userevent);
					}
				}
				
				person.setEvents(userEvents);
				persons.add(person);
			}

		} catch (IOException io)
		{
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex)
		{
			System.out.println(jdomex.getMessage());
		}
		
		
		return persons;
	}
	
	
	public List<String> getPersonNamesFromCSV(String file)
	  {
			InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(file);
			List<String> names = new ArrayList<>();
			Scanner scanner = new Scanner(in);
			while (scanner.hasNextLine())
			{
				String lineFromCSV = scanner.nextLine();
				String delims = ";";
				String[] tokens = lineFromCSV.split(delims);
				names.add(tokens[1]);

			}
			scanner.close();
			return names;
	  }
}
