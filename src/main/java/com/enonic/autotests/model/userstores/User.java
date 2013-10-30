package com.enonic.autotests.model.userstores;

import java.util.List;

import com.enonic.autotests.model.userstores.PermissionOperation.Builder;

public class User
{

	private String name;
	private String password;
	private String email;

	private List<String> groups;

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

	public static Builder with()
	{
		return new Builder();
	}

	public static class Builder
	{
		private String bName;

		private String bPassword;

		private String bMail;

		public Builder()
		{

		}

		public Builder name(String name)
		{
			this.bName = name;
			return this;
		}

		public Builder password(String password)
		{
			this.bPassword = password;
			return this;
		}

		public Builder mail(String mail)
		{
			this.bMail = mail;
			return this;
		}

		public User build()
		{
			User user = new User();
			user.setEmail(this.bMail);
			user.setName(this.bName);
			user.setPassword(this.bPassword);
			return user;
		}

	}
}
