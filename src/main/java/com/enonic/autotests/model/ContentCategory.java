package com.enonic.autotests.model;

import java.util.List;

import com.enonic.autotests.model.userstores.AclEntry;
import com.enonic.autotests.model.userstores.PermissionOperation;

/**
 * Model class for 'Category' cms-object
 * 
 */
public class ContentCategory
{
	private String[] parentNames;	
	private String name;
	private String description;
	private String contentTypeName;
	private List<AclEntry> aclEntries;
	
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getContentTypeName()
	{
		return contentTypeName;
	}

	public void setContentTypeName(String contentTypeName)
	{
		this.contentTypeName = contentTypeName;
	}

	public String[] getParentNames()
	{
		return parentNames;
	}

	public void setParentNames(String[] parentNames)
	{
		this.parentNames = parentNames;
	}
	

	public List<AclEntry> getAclEntries()
	{
		return aclEntries;
	}

	public void setAclEntries(List<AclEntry> aclEntries)
	{
		this.aclEntries = aclEntries;
	}
	public static Builder with()
	{
		return new Builder();
	}
	public static class Builder{
		private String[] bParentNames;	
		private String bName;
		private String bDescription;
		private String bContentTypeName;
		private List<AclEntry>bAclEntries;
		
		public Builder parentNames(String[] pnames)
		{
			this.bParentNames = pnames;
			return this;
		}
		
		public Builder name(String name)
		{
			this.bName = name;
			return this;
		}
		public Builder description(String description)
		{
			this.bDescription = description;
			return this;
		}
		
		public Builder contentTypeName(String ctname)
		{
			this.bContentTypeName = ctname;
			return this;
		}
		public Builder aclEntries( List<AclEntry>aclEntries)
		{
			this.bAclEntries = aclEntries;
			return this;
		}
		
		public ContentCategory build()
		{
			ContentCategory cat = new ContentCategory();
			cat.name = this.bName;
			cat.description = this.bDescription;
			cat.contentTypeName = this.bContentTypeName;
			cat.parentNames = this.bParentNames;
			cat.aclEntries = this.bAclEntries;
			return cat;
		}
	}
}
