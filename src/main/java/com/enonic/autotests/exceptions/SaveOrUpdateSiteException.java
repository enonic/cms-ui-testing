package com.enonic.autotests.exceptions;

/**
 * A runtime exception representing a failure to add new Site .
 *
 */
public class SaveOrUpdateSiteException extends RuntimeException
{

	private static final long serialVersionUID = -677052226778944470L;

	public SaveOrUpdateSiteException( String message )
	{
		super(message);

	}

	public SaveOrUpdateSiteException()
	{

	}

}
