package com.booking.exception;

/**
 * User : Shikha Date : 05/09/17 Version : v1
 */
public class CabNotAvailableException extends Exception {
	private static final long serialVersionUID = 1L;

	public CabNotAvailableException(String s) {
		super(s);
	}
}
