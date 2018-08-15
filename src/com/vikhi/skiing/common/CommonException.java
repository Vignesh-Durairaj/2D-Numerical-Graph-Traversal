package com.vikhi.skiing.common;

public class CommonException extends Exception {

	/** Generated serial version UID */
	private static final long serialVersionUID = -2098928911782934549L;

	public CommonException() {
		super();
	}
	
	public CommonException(final String message) {
		super(message);
	}
	
	public CommonException(final Throwable cause) {
		super(cause);
	}
	
	public CommonException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
	public CommonException(final String message, final Throwable cause, 
								final boolean enableSuppression, final boolean writableStackTrace ) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
