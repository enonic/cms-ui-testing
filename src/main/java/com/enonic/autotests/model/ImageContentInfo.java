package com.enonic.autotests.model;

public class ImageContentInfo implements ContentInfo<ImageContentInfo>
{

	private String displayName;

	private String description;

	private String comment;// unsaved draft

	private String pathToFile;

	private String photographerName;

	private String photographerEmail;

	private String copyright;
	private String keyWords;

	public String getDisplayName()
	{
		return displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getPathToFile()
	{
		return pathToFile;
	}

	public void setPathToFile(String pathToFile)
	{
		this.pathToFile = pathToFile;
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

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public void setPhotographerEmail(String photographerEmail)
	{
		this.photographerEmail = photographerEmail;
	}

	public String getCopyright()
	{
		return copyright;
	}

	public void setCopyright(String copyright)
	{
		this.copyright = copyright;
	}

	public String getKeyWords()
	{
		return keyWords;
	}

	public void setKeyWords(String keyWords)
	{
		this.keyWords = keyWords;
	}

	@Override
	public ImageContentInfo getContentTabInfo()
	{

		return this;
	}

}
