package com.booking;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;

import com.booking.exception.CabNotAvailableException;
import com.booking.model.Cab;
import com.booking.model.CabRequest;

/**
 * User : Shikha Date : 05/09/17 Version : v1
 */
public class CabBookingTest {

	@Test
	public void addCab() {

		Cab one = new Cab("KA01HB001", "pink", 120);
		ICabBookingSystem system = new CabBookingSystemImpl();
		system.addCab(one);
		system.addCab(one);
		Assert.assertEquals(system.getNumofCabsRunning(), 1);
	}

	@Test(expected = CabNotAvailableException.class)
	public void checkBooking() throws CabNotAvailableException {
		Cab one = new Cab("KA01HB001", "pink", 120);
		Cab two = new Cab("KA01HB002", "red", 200);
		Cab three = new Cab("KA01HB003", "pink", 160);
		Cab four = new Cab("KA01HB004", "white", 180);

		ICabBookingSystem system = new CabBookingSystemImpl();
		system.addCab(one);
		system.addCab(two);
		system.addCab(three);
		system.addCab(four);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, 10);
		Cab cab = system.requestForNearestCab(new CabRequest("BG001", "pink", 100, 156, cal.getTime()));
		Assert.assertEquals(cab.getId(), "KA01HB001");

		cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, 11);
		cab = system.requestForNearestCab(new CabRequest("BG002", "pink", 156, 122, cal.getTime()));
		Assert.assertEquals(cab.getId(), "KA01HB003");

		cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, 15);
		cab = system.requestForNearestCab(new CabRequest("BG003", "pink", 120, 175, cal.getTime()));
		Assert.assertEquals(cab.getId(), "KA01HB003");

		cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, 15);
		cab = system.requestForNearestCab(new CabRequest("BG004", "white", 140, 156, cal.getTime()));
		Assert.assertEquals(cab.getId(), "KA01HB004");

		cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, 15);
		cab = system.requestForNearestCab(new CabRequest("BG005", "red", 180, 156, cal.getTime()));
		Assert.assertEquals(cab.getId(), "KA01HB002");

		/* Exception:: Cab Not Available Exception */
		cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, 15);
		cab = system.requestForNearestCab(new CabRequest("BG005", "pink", 180, 156, cal.getTime()));
		//Assert.assertEquals(cab.getId(), "KA01HB001");

	}

}
