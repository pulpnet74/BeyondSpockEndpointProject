/**
 * com.beyondspock.control package contains the controller files
 * 
 */
package com.beyondspock.control;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.beyondspock.entity.TopBidList;
import com.beyondspock.entity.Bid;

/**
 * @author Fabio Riberto
 *
 *         BidController class is in charge to process the bid and the top
 *         bid list requests
 * 
 */
public class BidController {

	/**
	 * Thread-safe java.util.Map to store all the Top Bid Lists for the
	 * existing items where "Integer" is the item and TopBidList is the
	 * list for this particular item
	 * 
	 */
	private ConcurrentMap<Integer, TopBidList> allItemsTopBidListMap;

	/**
	 * BidController instantiation
	 * 
	 */
	public BidController() {
		allItemsTopBidListMap = new ConcurrentHashMap<>();
	}

	/**
	 * This method stores a bid: if the item is not present, add a new item
	 * and enter the posted bid to the new list
	 * 
	 */
	public synchronized void storeABid(final Integer item, final Bid bid) {
		TopBidList tbl = allItemsTopBidListMap.get(item);
		if (tbl != null)
			tbl.insert(bid);
		else { // put a new element if the item doesn't exist
			tbl = new TopBidList();
			allItemsTopBidListMap.putIfAbsent(item, tbl);
			tbl.insert(bid);
		}
	}

	/**
	 * This method extracts a List for a particular item id. It shall return
	 * empty list (as required) if the item id is not present in the map of
	 * lists
	 * 
	 */
	public TopBidList extractTopBidList(final int item) {
		if (allItemsTopBidListMap.containsKey(item))
			return allItemsTopBidListMap.get(item);
		else
			return new TopBidList();
	}
}
