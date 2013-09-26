package com.enonic.autotests.model.site;

import java.util.HashMap;
import java.util.Map;

/**
 * Model class for 'Site'-cms-object.
 *
 */
public class Site
{
	public enum AllowedPageTypes
	{

		SECTION("section page"), URL("URL page"), LABEL("label page");
		private String name;

		public String getName()
		{
			return name;
		}

		private final static Map<String, AllowedPageTypes> map = new HashMap<String, AllowedPageTypes>();
		static
		{
			for (AllowedPageTypes page : values())
			{
				map.put(page.name, page);
			}
		}

		private AllowedPageTypes( String name )
		{
			this.name = name;
		}

		public static AllowedPageTypes findByValue(String value)
		{
			return value != null ? map.get(value) : null;
		}
		
	}
		
	private String pathToPublicResources;
	
	private String pathToInternalResources;
	private STKResource deviceClassification;

	private String dispalyName;
	
	private String language;
	
	private String statisticsUrl;
	
	//Allowed page types: 1)label 	2)URL  3) section 	
	private AllowedPageTypes [] allowedPageTypes;
	
	public AllowedPageTypes[] getAllowedPageTypes()
	{
		return allowedPageTypes;
	}

	public void setAllowedPageTypes(AllowedPageTypes[] allowedPageTypes)
	{
		this.allowedPageTypes = allowedPageTypes;
	}

	public String getDispalyName()
	{
		return dispalyName;
	}

	public void setDispalyName(String dispalyName)
	{
		this.dispalyName = dispalyName;
	}

	public String getLanguage()
	{
		return language;
	}

	public void setLanguage(String language)
	{
		this.language = language;
	}

	public String getStatisticsUrl()
	{
		return statisticsUrl;
	}

	public void setStatisticsUrl(String statisticsUrl)
	{
		this.statisticsUrl = statisticsUrl;
	}

	public String getPathToPublicResources()
	{
		return pathToPublicResources;
	}

	public void setPathToPublicResources(String pathToPublicResources)
	{
		this.pathToPublicResources = pathToPublicResources;
	}

	public String getPathToInternalResources()
	{
		return pathToInternalResources;
	}

	public void setPathToInternalResources(String pathToInternalResources)
	{
		this.pathToInternalResources = pathToInternalResources;
	}

	public STKResource getDeviceClassification()
	{
		return deviceClassification;
	}

	public void setDeviceClassification(STKResource deviceClassification)
	{
		this.deviceClassification = deviceClassification;
	}
}
