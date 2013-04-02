package com.enonic.autotests.exceptions;

/**
 * <p>
 * A runtime exception representing a failure to create new Content Type
 * </p>
 */
public class ContentTypeException extends RuntimeException {

	private static final long serialVersionUID = -646053826977947970L;

	public ContentTypeException(String message) {
		super(message);

	}

	public ContentTypeException() {

	}

}