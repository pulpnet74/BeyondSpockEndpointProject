/**
 * com.beyondspock.entity package contains the model objects
 */
package com.beyondspock.entity;

import static com.beyondspock.endpoint.tool.RoddenberryFactory.UHURA_BIRTH_YEAR;

import java.io.Serializable;

/**
 * Bid class represents the Bid model object
 * 
 * @author Fabio Riberto
 *
 */
public class Bid implements Comparable<Bid>, Serializable {

	private static final long serialVersionUID = 4429790886975390993L;

	/**
	 * User id that requests to post a bid
	 */
	private int userId;

	/**
	 * Posted bid
	 */
	private double bid;

	/**
	 * New Bid class
	 * 
	 * @param userId
	 * @param bid
	 */
	public Bid(int userId, double bid) {
		this.setUserId(userId);
		this.setBid(bid);
	}

	/**
	 * @return the bid
	 */
	public double getBid() {
		return bid;
	}

	/**
	 * @param bid
	 *            the bid to set
	 */
	public void setBid(double bid) {
		this.bid = bid;
	}
	
	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * This method compares 2 Bid objects to let the one having the highest
	 * bid value at the top of the Top Bid List (descending order)
	 * 
	 * @param bidToCompare
	 *            the bid to compare to
	 * @return result
	 */
	@Override
	public int compareTo(Bid bidToCompare) {
		// Switch to these commented lines in order to obtain an ascending Top
		// Bid List
		// int result = Double.compare(this.bid, bidToCompare.bid);
		// if ((result == 0) && (Integer.compare(this.userId,
		// bidToCompare.userId) != 0))
		int result = Double.compare(bidToCompare.bid, this.bid);
		if ((result == 0) && (Integer.compare(bidToCompare.userId, this.userId) != 0))
			result = 1;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		if (getUserId() != ((Bid) obj).userId)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = UHURA_BIRTH_YEAR; // prime number
		result += getUserId() + getBid() + 7;
		return result;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getUserId());
		sb.append("=");
		sb.append(getBid());
		return sb.toString();
	}

}
