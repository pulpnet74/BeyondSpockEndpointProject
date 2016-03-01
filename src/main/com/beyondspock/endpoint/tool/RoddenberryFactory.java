/**
 * com.beyondspock.endpoint.tool package contains the HTTP server support classes
 */
package com.beyondspock.endpoint.tool;

/**
 * This class collects all the constants that shall be used in the source code
 * These constants are initialized, static, final and public to be reached from
 * everywhere but immutable
 * 
 * @author Fabio Riberto
 *
 */
public class RoddenberryFactory {

	/**
	 * The port to access the Endpoint
	 *
	 */
	public static final int DOMINION_PORT = 8081;

	/**
	 * Default value for the socket backlog tail
	 *
	 */
	public static final int DEFAULT_BACKLOG = 0;

	/**
	 * Life time of a session in milliseconds
	 *
	 */
	public static final int SESSION_LIFE = 10 * 60 * 1000; // 10min*60sec*1000msec
															// = 600000
															// msec/10min

	/**
	 * Length of a session key in bits
	 *
	 */
	public static final int SESSION_KEY_BIT_LENGTH = 32; // length of a session
															// key in bits

	/**
	 * USS Enterprise Chief communications officer Nyota Uhura birth year
	 *
	 */
	public static final int UHURA_BIRTH_YEAR = 2239; // Prime number

	/**
	 * Max elements of a top bid list per item
	 *
	 */
	public static final int MAX_BIDS_PER_ITEM = 15; // For memory reason,
														// every existing Top
														// bid list shall not
														// be greater than 15
														// elements

	/**
	 * Default number of thread for JMX
	 *
	 */
	public static final int DEFAULT_NUMBER_OF_THREADS = 10;

	/**
	 * GET method literal
	 *
	 */
	public static final String GET_METHOD = "GET";

	/**
	 * POST method literal
	 *
	 */
	public static final String POST_METHOD = "POST";

	/**
	 * Key to identify the type of content attribute for the message header
	 * purposes
	 *
	 */
	public static final String CONTENT_TYPE = "Content-Type";

	/**
	 * Key to identify the type of content for the message header purposes
	 *
	 */
	public static final String CONTENT_TEXT = "text/plain";

	/**
	 * Entry for the Endpoint main context
	 *
	 */
	public static final String MAIN_CONTEXT = "/";

	/**
	 * Key to identify the "parameters" attribute
	 *
	 */
	public static final String PARAM_ATTR_KEY = "parameters";

	/**
	 * Key to identify the "request" parameter
	 *
	 */
	public static final String REQ_PARAM = "request";

	/**
	 * Key to identify the "response" parameter
	 *
	 */
	public static final String RES_PARAM = "response";

	// Keys used in the actions
	/**
	 * Key to refer to "bid" literal in the action
	 *
	 */
	public static final String BID_KEY = "bid";

	/**
	 * Key to refer to "topBidList" literal in the action
	 *
	 */
	public static final String TOPBIDLIST_KEY = "topBidList";

	/**
	 * Key to refer to "login" literal in the action
	 *
	 */
	public static final String LOGIN_KEY = "login";
	/**
	 * Key to refer to "itemID" literal in the action
	 *
	 */
	public static final String ITEMID_KEY = "itemID";

	/**
	 * Key to refer to "sessionkey" literal in the action
	 *
	 */
	public static final String SESSIONKEY_KEY = "sessionkey";

	/**
	 * Key to refer to "userID" literal in the action
	 *
	 */
	public static final String USERID_KEY = "userID";

	/**
	 * Decimal URI filter
	 *
	 */
	public static final String DECIMAL_URI_FILTER = "/(\\d+)";

	/**
	 * Session key parameter pattern
	 *
	 */
	public static final String SESSIONKEY_PARAM_PATTERN = "\\?sessionkey=(.+)";

	/**
	 * Bid URI filter for URI validation
	 *
	 */
	public static final String BID_URI_FILTER = "/(\\d+)" + "/bid" + SESSIONKEY_PARAM_PATTERN;

	/**
	 * Top bid list URI filter for URI validation
	 *
	 */
	public static final String TOPBIDLIST_URI_FILTER = "/(\\d+)" + "/topBidList";

	/**
	 * Login URI filter for URI validation
	 *
	 */
	public static final String LOGIN_URI_FILTER = "/(\\d+)" + "/login";
}
