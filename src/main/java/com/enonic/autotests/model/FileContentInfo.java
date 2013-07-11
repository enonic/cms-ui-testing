package com.enonic.autotests.model;

/**
 * Info for 'Content Tab'
 *
 */
public class FileContentInfo implements ContentInfo<FileContentInfo>
{

	private String description;

	private String pathToFile;

	private String comment;

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public String getPathToFile()
	{
		return pathToFile;
	}

	public void setPathToFile(String pathToFile)
	{
		this.pathToFile = pathToFile;
	}

	@Override
	public FileContentInfo getContentTabInfo()
	{
		return this;
	}

}