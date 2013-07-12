package com.enonic.autotests.model;

/**
 * Base class for all types of content.
 * 
 * 12.04.2013
 */
public class Content<T>
{

	private String [] parents;
	

	private ContentInfo<T> contentTab;

	private ContentProperties propertiesTab;

	private Publishing publishingTab;
	

	
	public ContentInfo<T> getContentTab()
	{
		return contentTab;
	}

	public void setContentTab(ContentInfo<T> contentTab)
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
	
	public String[] getParents()
	{
		return parents;
	}

	public void setParents(String[] parents)
	{
		this.parents = parents;
	}

}
