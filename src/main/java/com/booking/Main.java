package com.booking;

import com.booking.controller.CabBookingController;

public class Main {
	public static void main(String[] args) {
		new CabBookingController(new CabBookingSystemImpl());
	}
}