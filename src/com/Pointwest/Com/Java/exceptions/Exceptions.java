package com.Pointwest.Com.Java.exceptions;

public class Exceptions extends Exception {

	private final String customMsg;
	
	public Exceptions(Exception exception, String message) {
		super(exception);
		if (message != null || !exception.getMessage().isEmpty()) {
			this.customMsg = new StringBuffer(message).append(" Reason: ").append(exception.getMessage()).toString();
		} else {
			this.customMsg = message;
		}
	}
	
	public Exceptions(String message) {
		this.customMsg = message;
	}
	
	public String getCustomMsg() {
		return customMsg;
	}
}
