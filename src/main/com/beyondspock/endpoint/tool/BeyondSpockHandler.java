/**
 * com.beyondspock.endpoint.tool package contains the HTTP server support classes
 */
package com.beyondspock.endpoint.tool;

import static com.beyondspock.endpoint.tool.RoddenberryFactory.CONTENT_TEXT;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.CONTENT_TYPE;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.TOPBIDLIST_KEY;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.ITEMID_KEY;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.LOGIN_KEY;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.PARAM_ATTR_KEY;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.REQ_PARAM;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.BID_KEY;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.SESSIONKEY_KEY;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.USERID_KEY;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;

import com.beyondspock.control.BeyondSpockController;
import com.beyondspock.exception.InvalidRequestException;
import com.beyondspock.exception.InvalidSessionException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * This class implement the server handler
 * 
 * @author Fabio Riberto
 * @extends HttpHandler
 */
public class BeyondSpockHandler implements HttpHandler {
	/**
	 * Controller for routing the requests and store the data
	 */
	private final BeyondSpockController controller;

	/**
	 * BeyondSpockHandler new instance
	 *
	 * @param controller
	 */
	public BeyondSpockHandler(BeyondSpockController controller) {
		this.controller = controller;
	}

	/**
	 * This method handle the external requests. It communicates to the
	 * controller which service shall process the request
	 * 
	 * @param exchange
	 * @throws IOException
	 */
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		@SuppressWarnings("unchecked")
		Map<String, String> params = (Map<String, String>) exchange.getAttribute(PARAM_ATTR_KEY); // Filter
																									// the
																									// "parameters"
																									// attribute
		String reqAction = params.get(REQ_PARAM); // Select the HTTP
													// encapsulated request
		String msg = "";
		int connCode = HttpURLConnection.HTTP_OK;
		try {
			if (reqAction.equals(BID_KEY)) { // Take actions to store a bid
												// for a particular item
				handleBid(params); // It isn't requested any Response body
			} else if (reqAction.equals(TOPBIDLIST_KEY)) { // Take actions to
																// retrieve the
																// topbidlist
																// for a
																// particular
																// item
				msg = handleTopBidList(params); // Top Bid List in CVS
													// format to "msg" String
			} else if (reqAction.equals(LOGIN_KEY)) { // Take actions to login a
														// user
				msg = handleLogin(params); // Session key to "msg" String
			} else {
				throw new InvalidRequestException("Invalid request: the URI doesn't contain a valid request.");
			}
		} catch (InvalidRequestException | InvalidSessionException e) {
			msg = e.getMessage();
			connCode = HttpURLConnection.HTTP_NOT_FOUND;
		} finally {
			exchange.getResponseHeaders().add(CONTENT_TYPE, CONTENT_TEXT);
			exchange.sendResponseHeaders(connCode, msg.length());
			OutputStream os = exchange.getResponseBody();
			os.write(msg.getBytes()); // Consume resources
			os.close(); // Release resources
		}
	}

	/**
	 * This method format the bid request to be interpreted by the controller
	 * 
	 * @param dataMap
	 * @throws InvalidSessionException
	 * @throws NumberFormatException
	 * 
	 */
	private void handleBid(final Map<String, String> dataMap) throws NumberFormatException, InvalidSessionException {
		controller.bidService(Integer.parseInt(dataMap.get(ITEMID_KEY)), Integer.parseInt(dataMap.get(BID_KEY)),
				dataMap.get(SESSIONKEY_KEY));
	}

	/**
	 * This method format the top bid list request to be interpreted by the
	 * controller
	 * 
	 * @param dataMap
	 * @return String the top bid list for a requested item as a string
	 * 
	 */
	private String handleTopBidList(final Map<String, String> dataMap) {
		return controller.topBidListService(Integer.parseInt(dataMap.get(ITEMID_KEY)));
	}

	/**
	 * This method format the login request to be interpreted by the controller
	 * 
	 * @param dataMap
	 * @return String the generated session key as a string
	 * 
	 */
	private String handleLogin(final Map<String, String> dataMap) {
		return controller.loginService(Integer.parseInt(dataMap.get(USERID_KEY)));
	}
}
