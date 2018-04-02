package com.contacts.resources;

/**
 * Represents REST API call execution result status.
 */
public class ResourceStatus {
	
	/** The message. */
	private String message;
	
	/**
	 * Instantiates a new resource status.
	 */
	public ResourceStatus() {
		
	}
	
	/**
	 * Instantiates a new resource status.
	 *
	 * @param message the message
	 */
	public ResourceStatus(String message) {
		this.message = message;
	}
	
	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return this.message;
	}
}
