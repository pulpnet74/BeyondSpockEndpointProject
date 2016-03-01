/**
 * com.beyondspock.entity package contains the model objects
 */
package com.beyondspock.entity;

import static com.beyondspock.endpoint.tool.RoddenberryFactory.MAX_BIDS_PER_ITEM;

import java.io.Serializable;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * TopBidList class represents the top bid list model object
 * 
 * @author Fabio Riberto
 *
 */
public class TopBidList implements Serializable {

	private static final long serialVersionUID = -3612212885917193176L;
	
	/**
	 * List of users bids for a particular item. Since a sorted container is
	 * needed, a ConcurrentSkipListSet has been chose.
	 */
	private ConcurrentSkipListSet<Bid> topBidLists;

	/**
	 * New TopBidList class
	 * 
	 */
	public TopBidList() {
		this.topBidLists = new ConcurrentSkipListSet<>();
	}

	/**
	 * This method return a top bid list
	 * 
	 * @return ConcurrentSkipListSet<Bid>
	 */
	public ConcurrentSkipListSet<Bid> getTopBidList() {
		return topBidLists;
	}

	/**
	 * This method set a top bid list
	 * 
	 * @param topBidList
	 *            the topBidList to set
	 */
	public void setTopBidList(ConcurrentSkipListSet<Bid> topBidList) {
		this.topBidLists = topBidList;
	}

	/**
	 * Insert a new posted bid for a particular item if there doesn't exist
	 * one higher (for that user)
	 *
	 * @param bid
	 */
	public void insert(Bid bid) { // Here
										// BeyondSpockFilter.exctractBidDataMap()
										// has yet verified that the posted
										// bid has num_of_chars > 0
		Bid lastValidBid = null;
		for (Bid bidIteration : topBidLists) { // Verify that particular
														// bid for a user
														// isn't member of top
														// bid list for a
														// requested item
			if (bidIteration.getUserId() == bid.getUserId())
				lastValidBid = bidIteration;
		}
		if (lastValidBid != null) { // If it is, test if the existing one is
										// bigger or smaller than the newer
			if (lastValidBid.getBid() >= bid.getBid()) {
				return;
			} else
				topBidLists.remove(lastValidBid);
		} else {
			topBidLists.add(bid); // Here lastValidBid is null OR it has
										// been removed! That's why the new
										// bid is inserted
			cutTopBidList(); // Here BeyondSpockFilter.exctractBidDataMap() has
								// yet verified that the posted bid has
								// num_of_chars > 0
		}
	}

	/**
	 * Cut the smaller value of the Top Bid list if it becomes bigger than
	 * MAX_BIDS_PER_ITEM
	 *
	 */
	private void cutTopBidList() {
		if (topBidLists.size() > MAX_BIDS_PER_ITEM) { // Check that the
															// max size of the
															// top bid list
															// hasn't been
															// reached.
			topBidLists.pollLast();
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		String parseTopBidList = getTopBidList().toString().trim();
		if (parseTopBidList.equals("[]"))
			parseTopBidList = "";
		else
			parseTopBidList = parseTopBidList.replace("[", "[\n\t{\"").replace("]", "\"}\n]")
											   	   .replace(", ", "\"},\n\t{\"").replaceAll("=", "\": \"");
		return sb.append(parseTopBidList).toString();
	}
}
