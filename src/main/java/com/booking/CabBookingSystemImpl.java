package com.booking;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.booking.constants.CabSystemConstants;
import com.booking.exception.CabNotAvailableException;
import com.booking.model.Cab;
import com.booking.model.CabRequest;

import cab.booking.util.CacheManager;
import cab.booking.util.CommonUtil;

/**
 * User : Shikha Date : 05/09/17 Version : v1
 */
public class CabBookingSystemImpl implements ICabBookingSystem {

	final static Logger logger = Logger.getLogger(CabBookingSystemImpl.class.getName());
	private static double NumResults = Double.MAX_VALUE;
	private static CacheManager cacheManager = CacheManager.getInstance();

	@Override
	public void addCab(Cab cab) {
		cacheManager.put(cab);
	}

	@Override
	public Cab requestForNearestCab(CabRequest request) throws CabNotAvailableException {

		return getNearestAvailableCab(request);
	}

	//check for nearest available cab which has got to serve this ride*/
	public Cab getNearestAvailableCab(CabRequest request) throws CabNotAvailableException {

		Cab selectedCab = null;
		double minDist = NumResults;
		String cabType = request.getCabType();
		for (Object cabObj : getAvailableCabsBasedOnType(cabType)) {
			Cab cab = (Cab) cabObj;
			
			//utility to calculate distance between two points*/
			CommonUtil calc = new CommonUtil(cab.getLocation(), request.getPickupLocation());
			double distanceBetween = calc.getDistance();
			
			//check for nearest available cab which has got to serve this ride*/
			if (distanceBetween < minDist && cab.gotTimeToServeThis(request)) {
				// double profit = cab.getCabProfit(request);
				selectedCab = cab;
				minDist = distanceBetween;
			}
		}
		if (selectedCab != null) {
			selectedCab.addJourney(request);
			System.out.println("Total Fare collected from customer: " + selectedCab.getFare(request));
			logger.log(Level.FINE, "Total Fare collected from customer: ", +selectedCab.getFare(request));
			return selectedCab;
		} else {
			throw new CabNotAvailableException("No Cab Available");
		}

	}

	/* Method to get Specific Pink Cabs*/
	private Set<Object> getAvailableCabsBasedOnType(String cabType) {

		Set<Object> availableCabs = cacheManager.getCache();
		if (!cabType.trim().isEmpty())
			return availableCabs;
		if (cabType == CabSystemConstants.PINK_CAB) {
			availableCabs.stream().filter(cab -> CabSystemConstants.PINK_CAB.equals(cab)).collect(Collectors.toList());
		}
		return availableCabs;
	}

	@Override
	public int getNumofCabsRunning() {
		return CacheManager.getInstance().getCache().size();
	}

	public Cab addCabs(String id, String type, Integer location) {
		Cab cab = new Cab(id, type, location);
		addCab(cab);
		return cab;
	}

}
