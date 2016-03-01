/**
 * com.beyondspock.endpoint package contains the server and its support classes
 */
package com.beyondspock.endpoint;

import static com.beyondspock.endpoint.tool.RoddenberryFactory.TOPBIDLIST_KEY;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.DOMINION_PORT;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.LOGIN_KEY;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.BID_KEY;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.SESSIONKEY_KEY;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.beyondspock.endpoint.BeyondSpockServer;

/**
 * @author Fabio Riberto
 *
 */
public class ManyRequestsStressTest {

	List<String> userIds = new ArrayList<>();
	List<String> sessionKeys = new ArrayList<>();
	List<String> items = new ArrayList<>();
	List<String> bids = new ArrayList<>();
	public static final int HUGE_NUM_OF_LOGINS = 201; // Only odd numbers are
														// chosen to be valid
														// user id
														// (HUGE_NUM_OF_LOGINS/2)
	public static final int HUGE_NUM_OF_ITEMS = 201;
	public static final int HUGE_NUM_OF_BIDS = HUGE_NUM_OF_ITEMS * 2;

	HttpURLConnection connection;
	Random rng;

	@Before
	public void init() throws Exception {
		BeyondSpockServer.main(null);
		for (int i = 1; i < HUGE_NUM_OF_LOGINS; i++) {
			if (i % 2 != 0)
				userIds.add(String.valueOf(i));
		}
		for (int i = 1; i < HUGE_NUM_OF_ITEMS; i++) {
			items.add(String.valueOf(i));
		}
		for (int i = 1; i < HUGE_NUM_OF_BIDS; i++) {
			bids.add(String.valueOf(i * 25));
		}
		rng = new Random();
	}

	@Test
	public void testEnormousNumberOfRequests() throws IOException {
		String protocol = "http";
		String host = "localhost";
		int port = DOMINION_PORT;
		String lReqValid = LOGIN_KEY;
		String bReqValid = BID_KEY;
		String tblReqValid = TOPBIDLIST_KEY;
		String skReqValid = SESSIONKEY_KEY;
		int totOfRequests = 0;
		// Logins
		for (String s : userIds) {
			connection = loginRequest(protocol, host, port, s, lReqValid);
			Assert.assertEquals(null, HttpURLConnection.HTTP_OK, connection.getResponseCode());
			sessionKeys.add(getResponseText(connection));
		}
		System.out.println("Number of Login Requests = ".concat(String.valueOf(userIds.size())));
		totOfRequests += userIds.size();
		// Store bids
		for (String s : sessionKeys) {
			Assert.assertNotNull(null, s);
			System.out.println(s);
			int i = 0;
			for (String s2 : items) {
				s2 = String.valueOf(rng.nextInt(items.size()));
				String s3 = bids.get(i).concat(String.valueOf(i * rng.nextInt(10)));
				connection = bidRequest(protocol, host, port, s2, bReqValid, skReqValid, s, s3);
				Assert.assertEquals(null, HttpURLConnection.HTTP_OK, connection.getResponseCode());
				i++;
			}
		}
		System.out.println("Number of Bid Requests = ".concat(String.valueOf(items.size() * sessionKeys.size())));
		totOfRequests += items.size() * sessionKeys.size();
		// Retrieve Top Bid Lists
		for (String s2 : items) {
			connection = topBidListRequest(protocol, host, port, s2, tblReqValid);
			Assert.assertEquals(null, HttpURLConnection.HTTP_OK, connection.getResponseCode());
			System.out.println("ITEM ".concat(s2).concat(": ").concat(getResponseText(connection)));
		}
		System.out.println("Number of Top Bid List Requests = ".concat(String.valueOf(items.size())));
		totOfRequests += items.size();
		System.out.println("Number of Requests = ".concat(String.valueOf(totOfRequests)));
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
