package com.enonic.autotests.model.userstores;

import java.util.List;

public class AclEntry
{
	public enum PrincipalType
	{
		
		USER, GROUP
	}
	
	public enum CategoryAvailableOperations
	{
		
		READ("right=read"), BROWSE("right=adminread"),CREATE("right=create"),APPROVE("right=approve"),
		ADMINISTRATE("right=administrate");
		private CategoryAvailableOperations(String value)
		{
			this.uiValue = value;
		}
		private String uiValue;
		
		public String getUiValue()
		{
			return uiValue;
		}
	}
	public enum ContentAvailableOperations
	{
		
		READ("right=read"), UPDATE("right=update"),DELETE("right=delete");
		
		private ContentAvailableOperations(String value)
		{
			this.uiValue = value;
		}
		private String uiValue;
		
		public String getUiValue()
		{
			return uiValue;
		}
	}

	private String principalName;

	private PrincipalType type;


	private List<PermissionOperation> permissions;

	// ------------------------------------------------------



	public List<PermissionOperation> getPermissions()
	{
		return permissions;
	}

	public void setPermissions(List<PermissionOperation> permissions)
	{
		this.permissions = permissions;
	}

	public String getPrincipalName()
	{
		return principalName;
	}

	public void setPrincipalName(String principalName)
	{
		this.principalName = principalName;
	}

	public PrincipalType getType()
	{
		return type;
	}

	public void setType(PrincipalType type)
	{
		this.type = type;
	}

}
