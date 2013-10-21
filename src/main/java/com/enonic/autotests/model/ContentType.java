package com.enonic.autotests.model;

/**
 * Representation for 'Content Type'. Located in the  'Admin/Content types' frame
 * 
 */
public class ContentType 
{

	public static String DEFAULT_CONTENTHANDLER_NAME = "Custom content";
	/** Name of the content type */
	private String name;
	
	/** Content handler's name, 'Custom handler', by default */
	private ContentHandler contentHandler;

	/** description of the Content type */
	private String description;

	private String pathToCSS;
	
	private String configuration;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ContentHandler getContentHandler() {
		return contentHandler;
	}

	public void setContentHandler(ContentHandler contentHandler) {
		this.contentHandler = contentHandler;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPathToCSS() {
		return pathToCSS;
	}

	public void setPathToCSS(String pathToCSS) {
		this.pathToCSS = pathToCSS;
	}

	public String getConfiguration() {
		return configuration;
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}
	public static Builder with()
	{
		return new Builder();
	}
	public static class Builder
	{
		private String bName;
		
		/** Content handler's name, 'Custom handler', by default */
		private ContentHandler bContentHandler;

		/** description of the Content type */
		private String bDescription;

		private String bPathToCSS;
		
		private String bConfiguration;
		
	   public Builder()
	   {
		   
	   }
	   public Builder name(String name)
	   {
		   this.bName = name;
		   return this;
	   }
	   public Builder contentHandler(ContentHandler contentHandler)
	   {
		   this.bContentHandler = contentHandler;
		   return this;
	   }
	   public Builder pathToCSS(String path)
	   {
		   this.bPathToCSS = path;
		   return this;
	   }
	   public Builder description(String description)
	   {
		   this.bDescription = description;
		   return this;
	   }
	   public Builder configuration(String conf)
	   {
		   this.bConfiguration = conf;
		   return this;
	   }
	   public ContentType build()
	   {
		   ContentType ctype = new ContentType();
		   ctype.configuration = this.bConfiguration;
		   ctype.description = this.bDescription;
		   ctype.name = this.bName;
		   ctype.pathToCSS = this.bPathToCSS;
		   ctype.contentHandler = this.bContentHandler;
		   return ctype;
	   }
	}

}
