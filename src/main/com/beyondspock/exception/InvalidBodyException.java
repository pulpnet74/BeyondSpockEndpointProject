/**
 * com.beyondspock.exception package contains the custom exceptions
 */
package com.beyondspock.exception;

/**
 * InvalidBodyException is thrown if a HTTP body doesn't contains expected info
 *
 * @author Fabio Riberto
 */
public class InvalidBodyException extends Exception {

	private static final long serialVersionUID = 8063374974962879148L;

	/**
	 * InvalidBodyException constructor
	 */
	public InvalidBodyException() {
		super();
	}

	/**
	 * InvalidBodyException constructor with parameters
	 *
	 * @param info
	 */
	public InvalidBodyException(String info) {
		super(info);
	}
}