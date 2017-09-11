package com.booking.model;

import java.util.Date;

/**
 * User : Shikha Date : 05/09/17 Version : v1
 */
public class CabRequest {

	private final String bookingId;
	private final String cabType;

	private final int pickupLocation;
	private final int dropLocation;
	private final Date pickupTime;

	public CabRequest(String bookingId, String cabType, int pickupCode, int dropCode, Date pickupTime) {
		this.bookingId = bookingId;
		this.cabType = cabType;
		this.pickupLocation = pickupCode;
		this.dropLocation = dropCode;
		this.pickupTime = pickupTime;
	}

	public String getBookingId() {
		return bookingId;
	}

	public String getCabType() {
		return cabType;
	}

	public int getPickupLocation() {
		return pickupLocation;
	}

	public int getDropLocation() {
		return dropLocation;
	}

	public Date getPickupTime() {
		return (Date) pickupTime.clone();
	}
}
