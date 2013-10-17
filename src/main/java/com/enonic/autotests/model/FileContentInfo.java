package com.enonic.autotests.model;

/**
 * Info for 'Content Tab'
 *
 */
public class FileContentInfo implements IContentInfo<FileContentInfo>
{

	private String displayName;	

	/** description for File-content */
	private String description;

	/** absolute pathname string    */
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

	public String getDisplayName()
	{
		return displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	@Override
	public FileContentInfo getInfo()
	{
		return this;
	}

}
