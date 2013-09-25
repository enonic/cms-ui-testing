package com.enonic.autotests.model.site;


public class MenuItem
{
	 /** site's name, where section will be located. */
	protected String siteName;
	
	protected String displayName;
	
	protected String menuName;
	
	protected boolean showInMenu;
	
	
	public String getDisplayName()
	{
		return displayName;
	}
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	public String getMenuName()
	{
		return menuName;
	}
	public void setMenuName(String menuName)
	{
		this.menuName = menuName;
	}
	public boolean isShowInMenu()
	{
		return showInMenu;
	}
	public void setShowInMenu(boolean showInMenu)
	{
		this.showInMenu = showInMenu;
	}
	public String getSiteName()
	{
		return siteName;
	}

	public void setSiteName(String siteName)
	{
		this.siteName = siteName;
	}
	
}
