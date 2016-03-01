/**
 * com.beyondspock.control package contains the controller files
 */
package com.beyondspock.control;

import static com.beyondspock.endpoint.tool.RoddenberryFactory.MAX_BIDS_PER_ITEM;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.SESSION_LIFE;

import java.util.concurrent.ConcurrentSkipListSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.beyondspock.control.LoginController;
import com.beyondspock.control.BidController;
import com.beyondspock.entity.TopBidList;
import com.beyondspock.entity.Bid;

/**
 * @author Fabio Riberto
 * @RunWith MockitoJUnitRunner
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class BidControllerTest {

	public static final int USS_HERA_FACTOR = 300;
	int timeWarp;
	String sessionKey;
	BidController scon;
	int userId[] = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73 }; // 21
																										// user
																										// ids
	double bidValue[] = { 250.3745554, 500.5, 750, 1000.548, 1250, 1500, 1750.21, 2000, 2250.9, 2500, 2750.111, 3000.3, 3250, 3500.30998, 3750, 4000,
			4250, 4500, 4750, 5000, 5250 }; // 21 bids
	int itemId[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21 }; // 21
																									// items
	ConcurrentSkipListSet<Bid> bidArrayList;

	@InjectMocks
	private final LoginController lcon = new LoginController();

	@Mock
	private LoginController lconMock;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		timeWarp = (int) SESSION_LIFE / USS_HERA_FACTOR; // Wait only 2 seconds
														// instead 10 minutes!
		sessionKey = "";
		scon = new BidController();
		bidArrayList = new ConcurrentSkipListSet<Bid>();
	}

	@Test
	public void testStoreOneBid() throws Exception {
		TopBidList topBidList = scon.extractTopBidList(itemId[0]);
		StringBuffer sb = new StringBuffer();
		Assert.assertEquals("Valid filled TopBidList", new ConcurrentSkipListSet<>(),
				topBidList.getTopBidList());
		Bid bidObj = new Bid(userId[0], bidValue[0]);
		scon.storeABid(itemId[0], bidObj);
		topBidList = scon.extractTopBidList(itemId[0]);
		sb.append("[\n\t{\"");
		sb.append(String.valueOf(userId[0]));
		sb.append("\": \"");
		sb.append(String.valueOf(bidValue[0]));
		sb.append("\"}\n]");
		System.out.println("testStoreOneBid:");
		System.out.println(sb.toString());
		Assert.assertEquals("TopBid not valid", sb.toString(), topBidList.toString());
	}

	@Test
	public void testStoreTwoBidsSameUser() throws Exception {
		TopBidList topBidList = scon.extractTopBidList(itemId[1]);
		StringBuffer sb = new StringBuffer();
		Assert.assertEquals("Valid filled TopBidList", new ConcurrentSkipListSet<>(),
				topBidList.getTopBidList());
		Bid bidObjLower = new Bid(userId[0], bidValue[0]);
		Bid bidObjHigher = new Bid(userId[0], bidValue[1]);
		scon.storeABid(itemId[1], bidObjHigher);
		scon.storeABid(itemId[1], bidObjLower);
		topBidList = scon.extractTopBidList(itemId[1]);
		sb.append("[\n\t{\"");
		sb.append(String.valueOf(userId[0]));
		sb.append("\": \"");
		sb.append(String.valueOf(bidValue[1]));
		sb.append("\"}\n]");
		System.out.println("testStoreTwoBidsSameUser:");
		System.out.println(sb.toString());
		Assert.assertEquals("TopBid not valid", sb.toString(), topBidList.toString());
	}

	@Test
	public void testStoreTwoBidsDifferentUsers() throws Exception {
		TopBidList topBidList = scon.extractTopBidList(itemId[2]);
		StringBuffer sb = new StringBuffer();
		Assert.assertEquals("Valid filled TopBidList", new ConcurrentSkipListSet<>(),
				topBidList.getTopBidList());
		Bid bidObjHigher = new Bid(userId[1], bidValue[2]);
		Bid bidObjLower = new Bid(userId[2], bidValue[1]);
		scon.storeABid(itemId[2], bidObjLower); // Insert the lower before
														// to test the
														// comparator
		scon.storeABid(itemId[2], bidObjHigher);
		topBidList = scon.extractTopBidList(itemId[2]);
		sb.append("[\n\t{\"");
		sb.append(String.valueOf(userId[1]));
		sb.append("\": \"");
		sb.append(String.valueOf(bidObjHigher.getBid()));
		sb.append("\"},\n\t{\"");
		sb.append(String.valueOf(userId[2]));
		sb.append("\": \"");
		sb.append(String.valueOf(bidObjLower.getBid()));
		sb.append("\"}\n]");
		System.out.println("testStoreTwoBidsDifferentUsers:");
		System.out.println(sb.toString());
		Assert.assertEquals("topBidList not valid", sb.toString(), topBidList.toString());
	}

	@Test
	public void testStoreOneBidDifferentUsers() throws Exception {
		TopBidList topBidList = scon.extractTopBidList(itemId[3]);
		StringBuffer sb = new StringBuffer();
		Assert.assertEquals("Valid filled topBidList", new ConcurrentSkipListSet<>(),
				topBidList.getTopBidList());
		Bid bidObjMario = new Bid(userId[3], bidValue[3]); // Mario id =
																	// 7
		Bid bidObjLuigi = new Bid(userId[4], bidValue[3]); // Luigi id =
																	// 11
		scon.storeABid(itemId[3], bidObjMario);
		scon.storeABid(itemId[3], bidObjLuigi);
		topBidList = scon.extractTopBidList(itemId[3]);
		sb.append("[\n\t{\"");
		sb.append(String.valueOf(userId[3]));
		sb.append("\": \"");
		sb.append(String.valueOf(bidObjMario.getBid()));
		sb.append("\"},\n\t{\"");
		sb.append(String.valueOf(userId[4]));
		sb.append("\": \"");
		sb.append(String.valueOf(bidObjLuigi.getBid()));
		sb.append("\"}\n]");
		System.out.println("testStoreOneBidDifferentUsers (first store):");
		System.out.println(sb.toString());
		Assert.assertEquals("TopBid not valid", sb.toString(), topBidList.toString());
		topBidList = scon.extractTopBidList(itemId[4]);
		Assert.assertEquals("Valid filled TopBidList", new ConcurrentSkipListSet<>(),
				topBidList.getTopBidList());
		scon.storeABid(itemId[4], bidObjLuigi);
		scon.storeABid(itemId[4], bidObjMario);
		topBidList = scon.extractTopBidList(itemId[4]);
		sb = new StringBuffer();
		sb.append("[\n\t{\"");
		sb.append(String.valueOf(userId[4]));
		sb.append("\": \"");
		sb.append(String.valueOf(bidObjLuigi.getBid()));
		sb.append("\"},\n\t{\"");
		sb.append(String.valueOf(userId[3]));
		sb.append("\": \"");
		sb.append(String.valueOf(bidObjMario.getBid()));
		sb.append("\"}\n]");
		System.out.println("testStoreOneBidDifferentUsers (second store):");
		System.out.println(sb.toString());
		Assert.assertEquals("TopBid not valid", sb.toString(), topBidList.toString());
	}

	@Test
	public void testMaxBidsPerItem() throws Exception {
		TopBidList topBidList = scon.extractTopBidList(itemId[5]);
		Assert.assertEquals("Valid filled TopBidList", new ConcurrentSkipListSet<>(),
				topBidList.getTopBidList());
		for (int i = 0; i < MAX_BIDS_PER_ITEM; i++) {
			bidArrayList.add(new Bid(userId[i], bidValue[i]));
		}
		for (Bid bidIteration : bidArrayList) { // Verify that particular
														// bid for a user
														// isn't member of top
														// bid list for a
														// requested item
			scon.storeABid(itemId[5], bidIteration);
		}
		topBidList = scon.extractTopBidList(itemId[5]);
		StringBuffer sb = new StringBuffer();
		sb.append("[\n\t{\"");
		for (int i = MAX_BIDS_PER_ITEM; i > 1; i--) {
			sb.append(String.valueOf(userId[i - 1]));
			sb.append("\": \"");
			sb.append(String.valueOf(bidValue[i - 1]));
			sb.append("\"},\n\t{\"");
		}
		sb.append(String.valueOf(userId[0]));
		sb.append("\": \"");
		sb.append(String.valueOf(bidValue[0]));
		sb.append("\"}\n]");
		System.out.println("testMaxBidsPerItem (first test):");
		System.out.println(sb.toString());
		Assert.assertEquals("TopBid not valid", sb.toString(), topBidList.toString());
		bidArrayList.add(new Bid(userId[MAX_BIDS_PER_ITEM], bidValue[MAX_BIDS_PER_ITEM]));
		scon.storeABid(itemId[5], new Bid(userId[MAX_BIDS_PER_ITEM], bidValue[MAX_BIDS_PER_ITEM]));
		topBidList = scon.extractTopBidList(itemId[5]);
		StringBuffer sb2 = new StringBuffer();
		sb2.append("[\n\t{\"");
		for (int i = MAX_BIDS_PER_ITEM; i > 1; i--) {
			sb2.append(String.valueOf(userId[i]));
			sb2.append("\": \"");
			sb2.append(String.valueOf(bidValue[i]));
			sb2.append("\"},\n\t{\"");
		}
		sb2.append(String.valueOf(userId[1]));
		sb2.append("\": \"");
		sb2.append(String.valueOf(bidValue[1]));
		sb2.append("\"}\n]");
		System.out.println("testMaxBidsPerItem (second test):");
		System.out.println(sb2.toString());
		Assert.assertEquals("TopBid not valid", sb2.toString(), topBidList.toString());
	}

}
