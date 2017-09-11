package com.booking;

import cab.booking.controller.CabBookingController;

public class Main {
	public static void main(String[] args) {
		new CabBookingController(new CabBookingSystemImpl());
	}
}