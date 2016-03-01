/**
 * com.beyondspock.endpoint package contains the server and its support classes
 */
package com.beyondspock.endpoint;

import static com.beyondspock.endpoint.tool.RoddenberryFactory.TOPBIDLIST_KEY;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.DOMINION_PORT;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.LOGIN_KEY;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.BID_KEY;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.SESSIONKEY_KEY;

import com.beyondspock.endpoint.tool.BeyondSpockFilter;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.beyondspock.endpoint.BeyondSpockServer;

/**
 * @author Fabio Riberto
 *
 */
public class BeyondSpockServerTest {

	HttpURLConnection connection;

	@Before
	public void init() throws Exception {
		connection = null;
		BeyondSpockServer.main(null);
	}

	@Test
	public void testValidLoginURIFormat() throws IOException {
		String protocol = "http";
		String host = "localhost";
		int port = DOMINION_PORT;
		String userIdValid = "995";
		String lReqValid = LOGIN_KEY;
		connection = loginRequest(protocol, host, port, userIdValid, lReqValid);
		Assert.assertEquals(null, HttpURLConnection.HTTP_OK, connection.getResponseCode());
		String sessionkeyValVal = getResponseText(connection);
		Assert.assertNotNull(null, sessionkeyValVal);
		System.out.println(sessionkeyValVal);
	}

	@Test
	public void testNotValidUserId_LoginURIFormat() throws IOException {
		String protocol = "http";
		String host = "localhost";
		int port = DOMINION_PORT;
		String userIdValid = "995";
		String userIdNotValid = "-2";
		String iReqValid = LOGIN_KEY;
		connection = loginRequest(protocol, host, port, userIdNotValid, iReqValid);
		Assert.assertEquals(
				"\n\"".concat(String.valueOf(userIdNotValid)).concat("\" ").concat("is a wrong user id\n")
						.concat("Valid user id is a 31 bit unsigned integer number (i.e.: ")
						.concat(String.valueOf(userIdValid)).concat(")\n"),
				HttpURLConnection.HTTP_NOT_FOUND, connection.getResponseCode());
	}

	@Test
	public void testNotValidLoginLiteral_LoginURIFormat() throws IOException {
		String protocol = "http";
		String host = "localhost";
		int port = DOMINION_PORT;
		String userIdValid = "995";
		String iReqNotValid = "margin";
		connection = loginRequest(protocol, host, port, userIdValid, iReqNotValid);
		Assert.assertEquals(
				"\n\"".concat(String.valueOf(iReqNotValid)).concat("\" ").concat("is a wrong login literal\n")
						.concat("Valid login literal is \"").concat(LOGIN_KEY).concat("\"\n"),
				HttpURLConnection.HTTP_NOT_FOUND, connection.getResponseCode());
	}

	@Test
	public void testValidBidURIFormat() throws IOException {
		String protocol = "http";
		String host = "localhost";
		int port = DOMINION_PORT;
		String userIdValid = "995";
		String lReqValid = LOGIN_KEY;
		String itemIdValid = "7";
		String bReqValid = BID_KEY;
		String skReqValid = SESSIONKEY_KEY;
		connection = loginRequest(protocol, host, port, userIdValid, lReqValid);
		String skValValid = getResponseText(connection);
		String bidValid = "2550";
		connection = bidRequest(protocol, host, port, itemIdValid, bReqValid, skReqValid, skValValid, bidValid);
		Assert.assertEquals(null, HttpURLConnection.HTTP_OK, connection.getResponseCode());
	}

	@Test
	public void testNotValidBidLiteral_BidURIFormat() throws IOException {
		String protocol = "http";
		String host = "localhost";
		int port = DOMINION_PORT;
		String userIdValid = "995";
		String lReqValid = LOGIN_KEY;
		String itemIdValid = "7";
		String bReqValid = BID_KEY;
		String bReqNotValid = "pig";
		String skReqValid = SESSIONKEY_KEY;
		connection = loginRequest(protocol, host, port, userIdValid, lReqValid);
		String skValValid = getResponseText(connection);
		String bidValid = "2550";
		connection = bidRequest(protocol, host, port, itemIdValid, bReqNotValid, skReqValid, skValValid, bidValid);
		Assert.assertEquals(
				"\n\"".concat(String.valueOf(bReqNotValid)).concat("\" ").concat("is a wrong bid literal\n")
						.concat("Valid bid literal is ").concat(String.valueOf(bReqValid)).concat(")\n"),
				HttpURLConnection.HTTP_NOT_FOUND, connection.getResponseCode());

	}

	@Test
	public void testNotValidBid_BidURIFormat() throws IOException {
		String protocol = "http";
		String host = "localhost";
		int port = DOMINION_PORT;
		String userIdValid = "995";
		String lReqValid = LOGIN_KEY;
		String itemIdValid = "7";
		String bReqValid = BID_KEY;
		String skReqValid = SESSIONKEY_KEY;
		connection = loginRequest(protocol, host, port, userIdValid, lReqValid);
		String skValValid = getResponseText(connection);
		String bidValid = "2550";
		String bidNotValid = "-54";
		connection = bidRequest(protocol, host, port, itemIdValid, bReqValid, skReqValid, skValValid, bidNotValid);
		Assert.assertEquals(
				"\n\"".concat(String.valueOf(bidNotValid)).concat("\" ").concat("is a wrong bid\n")
						.concat("Valid bid is a double number (i.e.: ")
						.concat(String.valueOf(bidValid)).concat(")\n"),
				HttpURLConnection.HTTP_BAD_REQUEST, connection.getResponseCode());
	}

	@Test
	public void testValidTopBidListURIFormat() throws IOException {
		String protocol = "http";
		String host = "localhost";
		int port = DOMINION_PORT;
		String itemIdValid = "7";
		String tblReqValid = TOPBIDLIST_KEY;
		connection = topBidListRequest(protocol, host, port, itemIdValid, tblReqValid);
		Assert.assertEquals(null, HttpURLConnection.HTTP_OK, connection.getResponseCode());
		String returnedTopBidList = getResponseText(connection);
		Assert.assertNotNull(null, returnedTopBidList);
		System.out.println(returnedTopBidList);
	}

	@Test
	public void testNotValiditemId_TopBidListURIFormat() throws IOException {
		String protocol = "http";
		String host = "localhost";
		int port = DOMINION_PORT;
		String itemIdValid = "7";
		String itemNotValid = "-4";
		String tblReqValid = TOPBIDLIST_KEY;
		connection = topBidListRequest(protocol, host, port, itemNotValid, tblReqValid);
		Assert.assertEquals(
				"\n\"".concat(String.valueOf(itemNotValid)).concat("\" ").concat("is a wrong item id\n")
						.concat("Valid item id is a 31 bit unsigned integer number (i.e.: ")
						.concat(String.valueOf(itemIdValid)).concat(")\n"),
				HttpURLConnection.HTTP_NOT_FOUND, connection.getResponseCode());
	}

	@Test
	public void testNotValidTopBidListLiteral_TopBidListURIFormat() throws IOException {
		String protocol = "http";
		String host = "localhost";
		int port = DOMINION_PORT;
		String itemIdValid = "7";
		String tblReqNotValid = "rightbidchears";
		connection = topBidListRequest(protocol, host, port, itemIdValid, tblReqNotValid);
		Assert.assertEquals(
				"\n\"".concat(String.valueOf(tblReqNotValid)).concat("\" ")
						.concat("is a wrong top bid list literal\n").concat("Valid top bid list literal is \"")
						.concat(TOPBIDLIST_KEY).concat("\"\n"),
				HttpURLConnection.HTTP_NOT_FOUND, connection.getResponseCode());
	}

	@Test
	public void testCompletePoolOfRequests() throws IOException {
		String protocol = "http";
		String host = "localhost";
		int port = DOMINION_PORT;
		String userIdValid = "995";
		String lReqValid = LOGIN_KEY;
		String itemIdValid = "7";
		String bReqValid = BID_KEY;
		String skReqValid = SESSIONKEY_KEY;
		String tblReqValid = TOPBIDLIST_KEY;
		connection = loginRequest(protocol, host, port, userIdValid, lReqValid);
		Assert.assertEquals(null, HttpURLConnection.HTTP_OK, connection.getResponseCode());
		String skValValid = getResponseText(connection);
		String bidValid = "2550";
		connection = bidRequest(protocol, host, port, itemIdValid, bReqValid, skReqValid, skValValid, bidValid);
		Assert.assertEquals(null, HttpURLConnection.HTTP_OK, connection.getResponseCode());
		connection = topBidListRequest(protocol, host, port, itemIdValid, tblReqValid);
		String tblVal = getResponseText(connection).replace("[", "[\n").replace("]", "\n]");
		Assert.assertEquals(null, HttpURLConnection.HTTP_OK, connection.getResponseCode());
		Assert.assertNotNull(null, tblVal);
		Assert.assertEquals(
				"\n\"".concat(String.valueOf(tblVal)).concat("\" ").concat("is a wrong Top Bid List sequence\n")
						.concat("Valid Top Bid List sequence is a JSON array of {“userID”:“bid”} (i.e.: ")
						.concat(String.valueOf("[ {\"995\": \"2550\"} ]")).concat("[ {\"995\": \"2550\"}, {\"34\": \"3400.45\"} ]").concat(")\n"),
				"[\n\t{\"995\": \"2550.0\"}\n]", tblVal);
		itemIdValid = "2";
		connection = topBidListRequest(protocol, host, port, itemIdValid, tblReqValid);
		tblVal = getResponseText(connection);//.replace("[", "[\n").replace("]", "\n]");
		Assert.assertEquals(null, HttpURLConnection.HTTP_OK, connection.getResponseCode());
		Assert.assertEquals(
				"\n\"".concat(String.valueOf(tblVal)).concat("\" ").concat("is a wrong Top Bid List sequence\n")
						.concat("Valid Top Bid List sequence is a JSON array of {“userID”: “bid”} or a empy text (i.e.: ")
						.concat(String.valueOf("<empty_text>")).concat(")\n"),
				"", tblVal);
	}

	@Test
	public void testIsDoubleUnsigned() throws IOException {
		Assert.assertEquals(null, true, BeyondSpockFilter.isDoubleUnsignedEncoded("90854678"));
		Assert.assertEquals(null, true, BeyondSpockFilter.isDoubleUnsignedEncoded("3.1"));
		Assert.assertEquals(null, false, BeyondSpockFilter.isDoubleUnsignedEncoded("33247uh"));
		Assert.assertEquals(null, false, BeyondSpockFilter.isDoubleUnsignedEncoded("3.1.54"));
		Assert.assertEquals(null, false, BeyondSpockFilter.isDoubleUnsignedEncoded("-43.8"));
	}
	
	private HttpURLConnection loginRequest(final String protocol, final String host, final int port,
			final String userId, final String iReq) throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append(protocol);
		sb.append("://");
		sb.append(host);
		sb.append(":");
		sb.append(port);
		sb.append("/");
		sb.append(userId);
		sb.append("/");
		sb.append(iReq);
		URL url = new URL(sb.toString());
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.disconnect();
		return connection;
	}

	private HttpURLConnection bidRequest(final String protocol, final String host, final int port,
			final String itemId, final String bidReq, final String sessionKeyAttr, final String sessionKeyVal,
			final String bid) throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append(protocol);
		sb.append("://");
		sb.append(host);
		sb.append(":");
		sb.append(port);
		sb.append("/");
		sb.append(itemId);
		sb.append("/");
		sb.append(bidReq);
		sb.append("?");
		sb.append(sessionKeyAttr);
		sb.append("=");
		sb.append(sessionKeyVal);
		URL url = new URL(sb.toString());
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
		dos.writeBytes(bid);
		dos.flush();
		dos.close();
		connection.disconnect();
		return connection;
	}

	private HttpURLConnection topBidListRequest(final String protocol, final String host, final int port,
			final String itemId, final String tblReq) throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append(protocol);
		sb.append("://");
		sb.append(host);
		sb.append(":");
		sb.append(port);
		sb.append("/");
		sb.append(itemId);
		sb.append("/");
		sb.append(tblReq);
		URL url = new URL(sb.toString());
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.disconnect();
		return connection;
	}

	private String getResponseText(final HttpURLConnection connection) throws IOException {
		String response;
		BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
		StringBuilder sb = new StringBuilder();
		while ((response = br.readLine()) != null) {
			sb.append(response);
		}
		return sb.toString();
	}
}
