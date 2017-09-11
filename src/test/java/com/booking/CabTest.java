package com.booking;

import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.booking.model.Cab;
import com.booking.model.CabRequest;

/**
 * User : Shikha Date : 05/09/17 Version : v1
 */
public class CabTest {

	@Test
	public void testCabConstruction() {
		Cab cab = new Cab("KA01HB001", "pink", 120);
		Assert.assertEquals(cab.getId(), "KA01HB001");
		Assert.assertEquals(cab.getType(), "pink");
		Assert.assertEquals(cab.getLocation(), 120);
	}

	@Test
	public void addCabRequest() {

		Cab cab = new Cab("KA01HB001", "pink", 120);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, 1);
		CabRequest request = new CabRequest("BG1010", "pink", 125, 123, cal.getTime());
		cab.addJourney(request);

		Calendar afterRequest = Calendar.getInstance();
		afterRequest.setTime(cal.getTime());
		afterRequest.add(Calendar.MINUTE, 8);
		Assert.assertEquals(cab.getLocation(), 123);
		Assert.assertEquals(cab.getAvailableFrom(), afterRequest.getTime());
		List<CabRequest> req = cab.getRequestsServed();
		Assert.assertEquals(req.get(0).getBookingId(), "BG1010");
	}
}
