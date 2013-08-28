package com.enonic.autotests.contentimport;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.pages.v4.adminconsole.content.ContentsTableFrame;
import com.enonic.autotests.testdata.contenttype.ContentConvertor;
import com.enonic.autotests.utils.UserEvent;

public class ImportUtils
{

	/**
	 * Reads name from CSV formatted file. Name located in the second position.
	 * 
	 * <import mode="csv" name="person-import-csv" separator=";"> <mapping
	 * dest="person-no" src="1" /> <mapping dest="name" src="2" /> <mapping
	 * dest="date-of-birth" src="4" /> </import>
	 */
	public static List<String> getPersonNamesFromCSV(String file)
	{
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(file);
		List<String> names = new ArrayList<>();
		Scanner scanner = new Scanner(in);
		while (scanner.hasNextLine())
		{
			String lineFromCSV = scanner.nextLine();
			String delims = ";";
			String[] tokens = lineFromCSV.split(delims);
			// name has second position in the csv-record!
			names.add(tokens[1]);

		}
		scanner.close();
		return names;
	}
	/**
	 * Clicks by content and gets all person's events from UI.
	 * @param driver
	 * @param table
	 * @param contentName
	 * @return
	 */
	public static  List<UserEvent>getEventsFromUI(WebDriver driver,ContentsTableFrame table, String contentName)
	{
		table.clickByNameAndOpenInfo(contentName);
		List<UserEvent> userEvents = new ArrayList<>();
		List<WebElement> eventNameElements = driver.findElements(By.xpath("//input[@name='event-name']"));
		List<WebElement> dateElements = driver.findElements(By.xpath("//input[@name='dateevent-date']"));
		UserEvent event;
		for(int i=0; i<eventNameElements.size();i++)
		{
			event = new UserEvent();
			event.setName(eventNameElements.get(i).getAttribute("value"));
			String dateFormatted = converDateFormat(dateElements.get(i).getAttribute("value"));
			event.setValue(dateFormatted);
			userEvents.add(event);
		}
		driver.findElement(By.xpath("//button[@name='closebtn']")).click();
		table.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return userEvents;
	}
	/**
	 * Gets names of child persons
	 * @param driver
	 * @param table
	 * @param contentName
	 * @return
	 */
	public static  List<String> getChildPersons(WebDriver driver, ContentsTableFrame table, String contentName)
	{
		table.clickByNameAndOpenInfo(contentName);
		List<String> childList = new ArrayList<>();
		String childrenXpath = "//span[@class='relatedcontentsclass']";
		List<WebElement> children = driver.findElements(By.xpath(childrenXpath));
		for(WebElement ch:children)
		{
			childList.add(ch.getText());
		}
		return childList;
	}
	
	/**
	 * @param driver
	 * @param table
	 * @param contentName
	 * @return
	 */
	public static boolean  isImageLinkPresent(WebDriver driver, ContentsTableFrame table, String contentName)
	{
		table.clickByNameAndOpenInfo(contentName);
		String imageLinkXpath = "//a[@name='image_link']";
		List<WebElement> elems = driver.findElements(By.xpath(imageLinkXpath));
		if(elems.size() >0){
			return true;
		}else 
		{
			return false;
		}
		
	}
	/**
	 * Verify is present a KEY of resource on the page.
	 * @param driver
	 * @param table
	 * @param contentName
	 * @param key
	 * @return
	 */
	public static boolean checkContentKey(WebDriver driver, ContentsTableFrame table, String contentName, String key)
	{
		table.clickByNameAndOpenInfo(contentName);
		String imageXpath = String.format("//img[contains(@src,'image/%s/label')]",key);
		List<WebElement> elems = driver.findElements(By.xpath(imageXpath));
		if(elems.size() == 0)
		{
			return false;
		}
		return true;
	}

	public static String  converDateFormat(String date)
	{
		DateFormat dfRequired = new SimpleDateFormat("dd-MM-yyyy");  
		SimpleDateFormat parse = new SimpleDateFormat("dd.MM.yyyy");
		Date parsed =null;
		try
		{
			parsed = parse.parse(date);
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dfRequired.format(parsed); 
	}

}
