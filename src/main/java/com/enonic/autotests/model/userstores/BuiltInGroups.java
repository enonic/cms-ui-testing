package com.enonic.autotests.model.userstores;

public enum BuiltInGroups
{
	ADMINISTRATORS("Administrators (built-in)"),CONTRIBUTORS("Contributors (built-in)"),DEVELOPERS("Developers (built-in)"),
	ENTERPRISE_ADMINISTRATORS("Enterprise Administrators (built-in)"),EXPERT_CONTRIBUTORS("Expert Contributors (built-in)");

	private String value;

	private BuiltInGroups( String value )
	{
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}
}
