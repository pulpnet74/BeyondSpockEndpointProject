/**
 * com.beyondspock.endpoint.tool package contains the HTTP server support classes
 */
package com.beyondspock.endpoint.tool;

import static com.beyondspock.endpoint.tool.RoddenberryFactory.GET_METHOD;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.TOPBIDLIST_KEY;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.TOPBIDLIST_URI_FILTER;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.ITEMID_KEY;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.LOGIN_KEY;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.LOGIN_URI_FILTER;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.MAIN_CONTEXT;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.PARAM_ATTR_KEY;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.POST_METHOD;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.REQ_PARAM;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.BID_KEY;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.BID_URI_FILTER;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.SESSIONKEY_KEY;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.USERID_KEY;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.beyondspock.exception.InvalidBodyException;
import com.beyondspock.exception.InvalidRequestException;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

/**
 * This class implement the server filter
 * 
 * @author Fabio Riberto
 * @extends Filter
 *
 */
public class BeyondSpockFilter extends Filter {

	@Override
	public String description() {
		String desc = "This filter is responsible to recognize and validate proper Bid, TopBidlist and Login"
				+ "URIs in order extract the data and deliver to the HttpExchange to routed to the controller."
				+ "It shall be able to recognize bad and invalid requests and inject the proper error status"
				+ "code for the response";
		return desc;
	}

	@Override
	public void doFilter(HttpExchange exchange, Chain chainOfFilters) throws IOException {
		try {
			URI uri = exchange.getRequestURI();
			String uriStr = uri.toString();
			List<String> patterns = new ArrayList<String>();
			patterns.add(BID_URI_FILTER);
			patterns.add(TOPBIDLIST_URI_FILTER);
			patterns.add(LOGIN_URI_FILTER);
			Map<String, String> dataMap = null;
			for (String pattern : patterns) {
				if (uriStr.matches(pattern)) {
					switch (pattern) {
					case BID_URI_FILTER:
						// System.out.println(BID_URI_FILTER + " pattern
						// matched!");
						dataMap = exctractBidDataMap(exchange);
						break;
					case TOPBIDLIST_URI_FILTER:
						// System.out.println(TOPBIDLIST_URI_FILTER + "
						// pattern matched!");
						dataMap = exctractTopBidListDataMap(exchange);
						break;
					case LOGIN_URI_FILTER:
						// System.out.println(LOGIN_URI_FILTER + " pattern
						// matched!");
						dataMap = exctractLoginDataMap(exchange);
						break;
					default:
						break;
					}
				} else if (patterns.iterator().hasNext())
					continue;
				else
					throw new InvalidRequestException("Not valid request: the URI doesn't match any valid pattern\n"
							.concat("Check the URI format"));
			}
			if (!(dataMap == null))
				exchange.setAttribute(PARAM_ATTR_KEY, dataMap);
			else
				throw new InvalidRequestException(
						"Not valid request: the URI doesn't contain a valid request\n".concat("Check the URI format"));
			chainOfFilters.doFilter(exchange);
		} catch (InvalidRequestException e) {
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, e.getMessage().length());
			OutputStream os = exchange.getResponseBody();
			os.write(e.getMessage().getBytes());
			os.close();
		} catch (Exception e) {
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage().length());
			OutputStream os = exchange.getResponseBody();
			os.write(e.getMessage().getBytes());
			os.close();
		} finally {

		}
	}

	/**
	 * This method exctractBidDataMap extracts the data from the bid request
	 * URI
	 *
	 * @param exchange
	 * @return dataMap
	 * @throws InvalidRequestException
	 * @throws InvalidBodyException
	 * @throws IOException
	 */
	private static Map<String, String> exctractBidDataMap(HttpExchange exchange)
			throws InvalidRequestException, InvalidBodyException, IOException {
		String reqMethod = exchange.getRequestMethod();
		Map<String, String> dataMap = new HashMap<>();
		String bid, itemId, sessionKey;
		try { // Open resources
			InputStreamReader isr = new InputStreamReader(
					exchange.getRequestBody()/* , "utf-8" */);
			BufferedReader br = new BufferedReader(isr);
			try { // Consume resources
				bid = br.readLine();
			} catch (IOException e) {
				throw new InvalidBodyException(
						"Not valid body: The body is empty\n" + "A minimum of one number expected");
			} finally { // Close resources
				br.close();
				isr.close();
			}
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		}
		if (reqMethod.equalsIgnoreCase(POST_METHOD)) {
			if (bid.isEmpty())
				throw new InvalidBodyException(
						"Not valid body: The body of the POST message is blank\n" + "Minimum of one character expected");
			if (!isDoubleUnsignedEncoded(bid))
				throw new InvalidBodyException("Not valid body: the POST body=" + bid
						+ " contains one or more special characters\n" + "Alphanumeric body expected");
			itemId = exchange.getRequestURI().toString().split(BID_KEY)[0].substring(1,
					exchange.getRequestURI().toString().split(BID_KEY)[0].length() - 1);
			sessionKey = exchange.getRequestURI().toString().split(SESSIONKEY_KEY + "=")[1];
		} else
			throw new InvalidRequestException("The " + reqMethod + " is invalid\n" + POST_METHOD + " method expected");
		dataMap.put(REQ_PARAM, BID_KEY);
		dataMap.put(BID_KEY, bid);
		dataMap.put(ITEMID_KEY, itemId);
		dataMap.put(SESSIONKEY_KEY, sessionKey);
		return dataMap;
	}

	/**
	 * This method exctractTopBidListDataMap extracts the data from the top bid
	 * limit request URI
	 *
	 * @param exchange
	 * @return dataMap
	 * @throws InvalidRequestException
	 * @throws InvalidBodyException
	 * @throws IOException
	 */
	private static Map<String, String> exctractTopBidListDataMap(HttpExchange exchange)
			throws InvalidRequestException, InvalidBodyException, IOException {
		String reqMethod = exchange.getRequestMethod();
		Map<String, String> dataMap = new HashMap<>();
		String itemId;
		if (reqMethod.equalsIgnoreCase(GET_METHOD)) {
			itemId = exchange.getRequestURI().toString().split(MAIN_CONTEXT)[1];
		} else
			throw new InvalidRequestException("The " + reqMethod + " is invalid\n" + GET_METHOD + " mehod expected");
		dataMap.put(REQ_PARAM, TOPBIDLIST_KEY);
		dataMap.put(ITEMID_KEY, itemId);
		return dataMap;
	}

	/**
	 * This method exctractLoginDataMap extracts the data from the login request
	 * URI
	 *
	 * @param exchange
	 * @return dataMap
	 * @throws InvalidRequestException
	 * @throws InvalidBodyException
	 * @throws IOException
	 */
	private static Map<String, String> exctractLoginDataMap(HttpExchange exchange)
			throws InvalidRequestException, InvalidBodyException, IOException {
		String reqMethod = exchange.getRequestMethod();
		Map<String, String> dataMap = new HashMap<>();
		String userId;
		if (reqMethod.equalsIgnoreCase(GET_METHOD)) {
			userId = exchange.getRequestURI().toString().split(MAIN_CONTEXT)[1];
		} else
			throw new InvalidRequestException("The " + reqMethod + " is not valid\n" + GET_METHOD + " mehod expected");
		dataMap.put(REQ_PARAM, LOGIN_KEY);
		dataMap.put(USERID_KEY, userId);
		return dataMap;
	}

	/**
	 * This method check that the body content contains only [0...9] and/or 0x002E = "." characters
	 *
	 * @param body
	 *            content
	 * @return true if the body content contains only [0...9] and/or 0x002E = "." characters
	 * @return false if the body content contains one or more special characters or two or more "." characters
	 */
	public static boolean isDoubleUnsignedEncoded(String body) {
		int i = 0;
		for (char character : body.toCharArray()) {
			if (!Character.isDigit(Integer.valueOf(character)))
				if (character == 0x002E) {	// 0x002E = "."
					if (i==1)
						return false;
					i++;
				}
				else return false;
		}
		return true;
	}
}
