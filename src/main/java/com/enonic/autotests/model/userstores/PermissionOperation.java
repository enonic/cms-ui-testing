package com.enonic.autotests.model.userstores;

import com.enonic.autotests.model.site.PageMenuItem;
import com.enonic.autotests.model.site.PageMenuItem.Builder;

public class PermissionOperation
{
	private String name;

	private boolean allow;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isAllow()
	{
		return allow;
	}

	public void setAllow(boolean allow)
	{
		this.allow = allow;
	}

	public static Builder with()
	{
		return new Builder();
	}

	public static class Builder
	{

		private String bName;
		private boolean bAllow;

		public Builder()
		{

		}

		public Builder name(String name)
		{
			this.bName = name;
			return this;
		}

		public Builder allow(boolean allow)
		{
			this.bAllow = allow;
			return this;
		}

		public PermissionOperation build()
		{
			PermissionOperation operation = new PermissionOperation();
			operation.name = this.bName;
			operation.allow = this.bAllow;
			return operation;
		}
	}

}
