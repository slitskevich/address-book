package com.contacts.resources;

public class ResourceStatus {
	private String message;
	
	public ResourceStatus() {
		
	}
	
	public ResourceStatus(String message) {
		this.message = message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
