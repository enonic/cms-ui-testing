package com.enonic.autotests.model.site;

import java.util.List;

/**
 * Model for 'Section' menu Item.
 * 
 */
public class SectionMenuItem extends MenuItem
{
	private boolean ordered;

	/** names of available contentTypes */
	private List<String> availableContentTypes;


	public List<String> getAvailableContentTypes()
	{
		return availableContentTypes;
	}

	public void setAvailableContentTypes(List<String> availableContentTypes)
	{
		this.availableContentTypes = availableContentTypes;
	}

	public boolean isOrdered()
	{
		return ordered;
	}

	public void setOrdered(boolean isOrdered)
	{
		this.ordered = isOrdered;
	}
	
}
