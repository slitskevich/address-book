package com.contacts.entity;

/**
 * Represents entity validation failure.
 */
public class ValidationException extends Exception {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new validation exception.
	 *
	 * @param message the message
	 */
	public ValidationException(String message) {
		super(message);
	}
}
