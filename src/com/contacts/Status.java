package com.contacts;

public class Status {
	private String message;
	
	public Status() {
		
	}
	
	public Status(String message) {
		this.message = message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
