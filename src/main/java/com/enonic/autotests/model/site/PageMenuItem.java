package com.enonic.autotests.model.site;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class for 'Page Menu Item'
 * 
 */
public class PageMenuItem extends MenuItem
{
	// Site: the name of 'page template'
	private String pageTemplateName;
	private List<PageMenuItemPortlet> portlets = new ArrayList<>();

	public enum Region
	{
		CENTER("center"), EAST("east"), NORTH("north"), WEST("west");
		private Region( String value )
		{
			this.value = value;
		}

		private String value;

		public String getValue()
		{
			return value;
		}
	}

	public static class PageMenuItemPortlet
	{
		private String portletName;

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((portletName == null) ? 0 : portletName.hashCode());
			result = prime * result + ((region == null) ? 0 : region.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PageMenuItemPortlet other = (PageMenuItemPortlet) obj;
			if (portletName == null)
			{
				if (other.portletName != null)
					return false;
			} else if (!portletName.equals(other.portletName))
				return false;
			if (region != other.region)
				return false;
			return true;
		}

		public String getPortletName()
		{
			return portletName;
		}

		public void setPortletName(String portletName)
		{
			this.portletName = portletName;
		}

		public Region getRegion()
		{
			return region;
		}

		public void setRegion(Region region)
		{
			this.region = region;
		}

		private Region region;
	}

	public static class Builder
	{

		private String bDisplayNamename;
		private String bPageTemplateName;
		private String bmenuName;
		private String bSiteName;
		private boolean bShowInMenu;

		public Builder()
		{

		}

		public Builder siteNmae(String siteName)
		{
			this.bSiteName = siteName;
			return this;
		}

		public Builder displayName(String displayName)
		{
			this.bDisplayNamename = displayName;
			return this;
		}

		public Builder menuName(String menuName)
		{
			this.bmenuName = menuName;
			return this;
		}

		public Builder showInMenu(boolean bShowInMenu)
		{
			this.bShowInMenu = bShowInMenu;
			return this;
		}

		public Builder pageTemplateName(String pageTemplateName)
		{
			this.bPageTemplateName = pageTemplateName;
			return this;
		}

		public PageMenuItem build()
		{
			PageMenuItem menuItem = new PageMenuItem();
			menuItem.displayName = bDisplayNamename;
			menuItem.menuName = bmenuName;
			menuItem.showInMenu = bShowInMenu;
			menuItem.pageTemplateName = bPageTemplateName;
			menuItem.siteName = bSiteName;
			return menuItem;
		}
	}

	public static Builder with()
	{
		return new Builder();
	}

	public String getPageTemplateName()
	{
		return pageTemplateName;
	}

	public void setPageTemplateName(String pageTemplateName)
	{
		this.pageTemplateName = pageTemplateName;
	}

	public List<PageMenuItemPortlet> getPortlets()
	{
		return portlets;
	}

	public void setPortlets(List<PageMenuItemPortlet> portlets)
	{
		this.portlets = portlets;
	}

	public PageMenuItem cloneItem()
	{
		PageMenuItem clon = new PageMenuItem();

		clon.setDisplayName(this.getDisplayName());
		clon.setMenuName(this.getMenuName());
		clon.setPageTemplateName(this.getPageTemplateName());
		clon.setPortlets(this.getPortlets());
		clon.setShowInMenu(this.isShowInMenu());
		clon.setSiteName(this.getSiteName());
		return clon;
	}
}
