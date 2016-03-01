/**
 * com.beyondspock.exception package contains the custom exceptions
 */
package com.beyondspock.exception;

/**
 * InvalidSessionException is thrown if a sent session key is not valid
 * 
 * @author Fabio Riberto
 *
 */
public class InvalidSessionException extends Exception {

	private static final long serialVersionUID = -1646021453758524271L;

	/**
	 * InvalidSessionException constructor
	 *
	 */
	public InvalidSessionException() {
		super();
	}

	/**
	 * InvalidSessionException constructor with parameters
	 *
	 * @param info
	 */
	public InvalidSessionException(String info) {
		super(info);
	}
}
