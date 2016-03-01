/**
 * com.beyondspock.exception package contains the custom exceptions
 */
package com.beyondspock.exception;

/**
 * InvalidRequestException is thrown if a URI doesn't contain a valid request
 *
 * @author Fabio Riberto
 */
public class InvalidRequestException extends Exception {

	private static final long serialVersionUID = -2324392236734997053L;

	/**
	 * InvalidRequestException constructor
	 */
	public InvalidRequestException() {
		super();
	}

	/**
	 * InvalidRequestException constructor with parameters
	 * 
	 * @param info
	 */
	public InvalidRequestException(String info) {
		super(info);
	}
}