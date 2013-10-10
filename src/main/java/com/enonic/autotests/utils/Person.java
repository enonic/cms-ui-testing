package com.enonic.autotests.utils;

import java.util.List;

public class Person
{
	private String name;
	private List<UserEvent> events;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<UserEvent> getEvents()
	{
		return events;
	}

	public void setEvents(List<UserEvent> events)
	{
		this.events = events;
	}
	
//	public  List<UserEvent>getEventsFromUI(TestSession session)
//	{
//		List<UserEvent> userEvents = new ArrayList<>();
//		List<WebElement> eventNameElements = session.getDriver().findElements(By.xpath("//input[@name='event-name']"));
//		List<WebElement> eventValueElements = session.getDriver().findElements(By.xpath("//input[@name='dateevent-date']"));
//		UserEvent event;
//		for(int i=0; i<eventNameElements.size();i++)
//		{
//			event = new UserEvent();
//			event.setName(eventNameElements.get(i).getAttribute("value"));
//			event.setValue(eventValueElements.get(i).getAttribute("value"));
//			userEvents.add(event);
//		}
//		session.getDriver().findElement(By.xpath("//button[@name='closebtn']")).click();
//		ContentsTableFrame table = new ContentsTableFrame(session);
//		table.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
//		return userEvents;
//	}

}
