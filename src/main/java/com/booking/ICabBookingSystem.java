package com.booking;

import com.booking.exception.CabNotAvailableException;
import com.booking.model.Cab;
import com.booking.model.CabRequest;

/**
 * User : Shikha Date : 05/09/17 Version : v1
 */
public interface ICabBookingSystem {

	void addCab(Cab cab);

	Cab requestForNearestCab(CabRequest request) throws CabNotAvailableException;

	int getNumofCabsRunning();
}
