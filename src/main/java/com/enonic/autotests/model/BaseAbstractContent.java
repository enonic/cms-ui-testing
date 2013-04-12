package com.enonic.autotests.model;

/**
 * Base class for all types of content.
 *
 * 12.04.2013
 */
public abstract class BaseAbstractContent {
	
	private ContentProperties properties;
	
	private Publishing publishing;

	public ContentProperties getProperties() {
		return properties;
	}

	public void setProperties(ContentProperties properties) {
		this.properties = properties;
	}

	public Publishing getPublishing() {
		return publishing;
	}

	public void setPublishing(Publishing publishing) {
		this.publishing = publishing;
	}

}
