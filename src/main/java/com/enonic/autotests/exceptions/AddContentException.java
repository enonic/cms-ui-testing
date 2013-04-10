package com.enonic.autotests.exceptions;

/**
 * A runtime exception representing a failure to add to the Repository new Content. 
 *
 * 09.04.2013
 */
public class AddContentException extends RuntimeException {

	private static final long serialVersionUID = -646153826968947970L;

	public AddContentException(String message) {
		super(message);

	}

	public AddContentException() {

	}

}

