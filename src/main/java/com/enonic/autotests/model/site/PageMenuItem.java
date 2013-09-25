package com.enonic.autotests.model.site;

/**
 *
 *
 */
public class PageMenuItem extends MenuItem
{
	private String pageTemplateName;

	public static class Builder
	{

		private String bDisplayNamename;
		private String bPageTemplateName;
		private String bmenuName;
		private boolean bShowInMenu;

		public Builder()
		{

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
		public Builder pageTemplateName(String  pageTemplateName )
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

}
