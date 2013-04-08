package com.enonic.autotests.exceptions;

/**
 * <p>
 * A runtime exception representing a failure to create new Content Repository
 * </p>
 */
public class ContentRepositoryException extends RuntimeException {

	private static final long serialVersionUID = -646053822968947970L;

	public ContentRepositoryException(String message) {
		super(message);

	}

	public ContentRepositoryException() {

	}

}
