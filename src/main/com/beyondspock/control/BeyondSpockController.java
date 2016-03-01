/**
 * com.beyondspock.control package contains the controller files
 */
package com.beyondspock.control;

import com.beyondspock.entity.Bid;
import com.beyondspock.exception.InvalidSessionException;

/**
 * @author Fabio Riberto
 *
 *         BeyondSpockController class is in charge to route the requests to the
 *         proper service controller.
 */
public class BeyondSpockController {

	/**
	 * LoginController: a component that manage the login activities
	 */
	private final LoginController loginCtrl;

	/**
	 * BidController: a component that manage the bid storing and top
	 * bid list retrieving activities
	 */
	private final BidController bidCtrl;

	/**
	 * BeyondSpockController constructor
	 */
	public BeyondSpockController() {
		bidCtrl = new BidController();
		loginCtrl = new LoginController();
	}

	/**
	 * BeyondSpockController unique instance: this class delegates the operations to
	 * proper specialized controller
	 */
	public volatile static BeyondSpockController controlInstance;

	/**
	 * This method return the BeyondSpockController unique instance
	 * 
	 * @return BeyondSpockController unique instance: this class delegates the
	 *         operations to proper specialized controller
	 */
	public static BeyondSpockController getInstance() {
		// Out-of-order writes + Double-checked locking
		if (controlInstance == null)
			synchronized (BeyondSpockController.class) {
				BeyondSpockController inst = controlInstance;
				if (inst == null) {
					synchronized (BeyondSpockController.class) {
						inst = new BeyondSpockController();
						controlInstance = new BeyondSpockController();
					}
					controlInstance = inst;
				}
			}
		return controlInstance;
		// Singleton classic style
		// if (controlInstance == null) {
		// synchronized (BeyondSpockController.class) {
		// if (controlInstance == null) {
		// controlInstance = new BeyondSpockController();
		// }
		// }
		// }
		// return controlInstance;
	}

	/**
	 * Bid service: Check that the session is valid. Case session is valid
	 * then call BidController.storeABid() method to try to insert new bid
	 * for a particular item
	 *
	 * @param itemId
	 * @param bid
	 * @param sessionKey
	 * @throws InvalidSessionException
	 */
	public void bidService(int itemId, int bid, String sessionKey) throws InvalidSessionException {
		if (loginCtrl.isSessionActive(sessionKey)) {
			if (loginCtrl.getAliveLogins().get(sessionKey) != null)
				bidCtrl.storeABid(itemId,
						new Bid(loginCtrl.getAliveLogins().get(sessionKey).getUserId(), bid));
		} else
			throw new InvalidSessionException("The session key is not valid.");
	}

	/**
	 * This method returns the list of the bids for the requested item
	 * 
	 * @param itemId
	 *            item to retrieve the proper Top Bid List
	 * @return String that represent the list of the bids for the requested
	 *         item
	 */
	public String topBidListService(int itemId) {
		return bidCtrl.extractTopBidList(itemId).toString();
	}

	/**
	 * This method returns the session key when a user request to be logged in
	 * 
	 * @param userId
	 *            user that request to open a new session
	 * @return String that represent the session key
	 */
	public String loginService(int userId) {
		return loginCtrl.openSession(userId).getSessionKey();
	}

	/**
	 * Ensure that this instance shall not be cloned overriding the clone()
	 * Object inherited method while clone() method is called, a
	 * CloneNotSupportedException is thrown to indicate that this particular
	 * class is not clonable
	 *
	 * @throws CloneNotSupportedException
	 * @return Object
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}