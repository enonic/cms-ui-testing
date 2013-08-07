package com.enonic.autotests.model;

import java.util.List;

/**
 * Model for 'Section'-cms-object.
 * 
 */
public class Section
{

	private String menuName;

	private String displayName;

	private boolean showInMenu;

	/** names of available contentTypes */
	private List<String> availableContentTypes;

	// Getters and setters:::::::::::::::

	public String getMenuName()
	{
		return menuName;
	}

	public void setMenuName(String menuName)
	{
		this.menuName = menuName;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	public boolean isShowInMenu()
	{
		return showInMenu;
	}

	public void setShowInMenu(boolean showInMenu)
	{
		this.showInMenu = showInMenu;
	}

	public List<String> getAvailableContentTypes()
	{
		return availableContentTypes;
	}

	public void setAvailableContentTypes(List<String> availableContentTypes)
	{
		this.availableContentTypes = availableContentTypes;
	}
}
