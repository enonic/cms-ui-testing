package com.enonic.autotests.model.userstores;

import java.util.List;

public class User
{

	private String name;
	private String password;
	private String email;

	private List<String> groups;

	// preferences
	// events??

	public List<String> getGroups()
	{
		return groups;
	}

	public void setGroups(List<String> groups)
	{
		this.groups = groups;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}
}
