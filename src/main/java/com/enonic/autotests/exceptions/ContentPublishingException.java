package com.enonic.autotests.exceptions;

/**
 * A runtime exception representing a failure to publishing a Content. 
 *
 */
public class ContentPublishingException extends RuntimeException
{

	private static final long serialVersionUID = -646053822968757970L;

	public ContentPublishingException( String message )
	{
		super(message);

	}

	public ContentPublishingException()
	{

	}

}
