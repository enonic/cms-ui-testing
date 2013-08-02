package com.enonic.autotests.model;

/**
 * Base class for all types of content.
 * 
 * 12.04.2013
 */
public class Content<T>
{
	private ContentHandler contentHandler;

	private String[] parentNames;
	
	private String displayName;

	private IContentInfo<T> contentTab;

	private ContentProperties propertiesTab;

	private Publishing publishingTab;
	
	public IContentInfo<T> getContentTab()
	{
		return contentTab;
	}

	public void setContentTab(IContentInfo<T> contentTab)
	{
		this.contentTab = contentTab;
	}

	public ContentProperties getPropertiesTab()
	{
		return propertiesTab;
	}

	public void setPropertiesTab(ContentProperties propertiesTab)
	{
		this.propertiesTab = propertiesTab;
	}

	public Publishing getPublishingTab()
	{
		return publishingTab;
	}

	public void setPublishingTab(Publishing publishingTab)
	{
		this.publishingTab = publishingTab;
	}
	
	public static Content<FileContentInfo> buildFileContent()
	{
		Content<FileContentInfo> fileContent = new Content<>();
		return fileContent;
	}
	
	public String[] getParentNames()
	{
		return parentNames;
	}

	public void setParentNames(String[] parentsNames)
	{
		this.parentNames = parentsNames;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	public ContentHandler getContentHandler()
	{
		return contentHandler;
	}

	public void setContentHandler(ContentHandler contentHandler)
	{
		this.contentHandler = contentHandler;
	}

	public String buildContentNameWithPath()
	{
		StringBuilder sb = new StringBuilder();
		if(parentNames != null)
		{
			sb.append("/");
			for (String s : parentNames)
			{
				sb.append(s).append("/");
			}
		}
		

		sb.append(displayName);
		return sb.toString();
	}

}
