package com.enonic.autotests.testdata.content;

public class ImageContentXml extends AbstractContentXml
{
	private String pathToFile;
	
	private String description;
	
	private String photographerName;
	
	private String photographerEmail;
	
	private String copyright;
	

	public String getCopyright()
	{
		return copyright;
	}

	public void setCopyright(String copyright)
	{
		this.copyright = copyright;
	}

	public String getPhotographerName()
	{
		return photographerName;
	}

	public void setPhotographerName(String photographerName)
	{
		this.photographerName = photographerName;
	}

	public String getPhotographerEmail()
	{
		return photographerEmail;
	}

	public void setPhotographerEmail(String photographerEmail)
	{
		this.photographerEmail = photographerEmail;
	}

	public String getPathToFile()
	{
		return pathToFile;
	}

	public void setPathToFile(String pathToFile)
	{
		this.pathToFile = pathToFile;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
}
