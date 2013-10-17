package com.enonic.autotests.model;

/**
 * The sample 'Content Tab' for content that contains HTML area editor 
 *
 */
public class ContentWithEditorInfo implements IContentInfo<ContentWithEditorInfo>
{
	
	private String htmlareaText;

	@Override
	public ContentWithEditorInfo getInfo()
	{
		return this;
	}

	

	public String getHtmlareaText()
	{
		return htmlareaText;
	}

	public void setHtmlareaText(String htmlareaText)
	{
		this.htmlareaText = htmlareaText;
	}

}
