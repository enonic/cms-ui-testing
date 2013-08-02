package com.enonic.autotests.testdata.content;

/**
 * Base class for all Contents.
 * 
 */
public class AbstractContentXml
{
	private String caseInfo;
	
	private String comments;// Unsaved draft

	private String contentHandler;

	private String displayName;
	
	private String categoryName;

	// ---- Getters and Setters:----------------------------

	public String getCategoryName()
	{
		return categoryName;
	}

	public void setCategoryName(String categoryName)
	{
		this.categoryName = categoryName;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}

	public String getContentHandler()
	{
		return contentHandler;
	}

	public void setContentHandler(String contentHandler)
	{
		this.contentHandler = contentHandler;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	public String getCaseInfo()
	{
		return caseInfo;
	}

	public void setCaseInfo(String caseInfo)
	{
		this.caseInfo = caseInfo;
	}

}
