package com.enonic.autotests.model.userstores;

import java.util.List;

public class AclEntry
{
	public enum PrincipalType
	{
		
		USER, GROUP
	}

	private String principalName;

	private PrincipalType type;

	private boolean allow;

	private List<String> operations;

	// ------------------------------------------------------

	public boolean isAllow()
	{
		return allow;
	}

	public void setAllow(boolean allow)
	{
		this.allow = allow;
	}

	public List<String> getOperations()
	{
		return operations;
	}

	public void setOperations(List<String> operations)
	{
		this.operations = operations;
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
