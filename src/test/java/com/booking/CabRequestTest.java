package com.booking;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;

import com.booking.model.CabRequest;

/**
 * User : Shikha Date : 05/09/17 Version : v1
 */
public class CabRequestTest {

	@Test
	public void testCabRequestConstruction() {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, 10);
		CabRequest request = new CabRequest("BG1010", "pink", 101, 123, cal.getTime());

		Assert.assertEquals(request.getBookingId(), "BG1010");
		Assert.assertEquals(request.getPickupLocation(), 101);
		Assert.assertEquals(request.getDropLocation(), 123);
		Assert.assertEquals(request.getPickupTime(), cal.getTime());

	}
}
