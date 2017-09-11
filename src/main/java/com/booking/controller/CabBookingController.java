package com.booking.controller;

import static com.booking.util.JsonUtil.json;
import static com.booking.util.JsonUtil.toJson;
import static spark.Spark.after;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;

import java.util.Calendar;

import com.booking.CabBookingSystemImpl;
import com.booking.exception.CabNotAvailableException;
import com.booking.exception.ResponseError;
import com.booking.model.Cab;
import com.booking.model.CabRequest;

public class CabBookingController {

	public CabBookingController(final CabBookingSystemImpl cabService) {

		/* added to display the list of all cabs in front end */
		get("/cabs", (req, res) -> cabService.getNumofCabsRunning(), json());

		/* added to display the list of all nearestAvailableCabs */
		get("/cabs/", (req, res) -> {
			String bookingId = req.queryParams("bookingId");
			String type = req.queryParams("cabType");
			Integer pickupCode = Integer.parseInt(req.queryParams("pickupCode"));
			Integer dropCode = Integer.parseInt(req.queryParams("dropCode"));			
			Cab cab;
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, 10);
			try {
				cab = cabService
						.getNearestAvailableCab(new CabRequest(bookingId, type, pickupCode, dropCode, cal.getTime()));
			} catch (CabNotAvailableException e1) {
				return new ResponseError(e1.getMessage());
			}
			if (cab != null) {
				return cab;
			}
			res.status(400);
			return req;
		}, json());

		/* api to add cabs */
		post("/cabs", (req, res) -> cabService.addCabs(req.queryParams("id"), req.queryParams("type"),
				Integer.parseInt(req.queryParams("location"))), json());

		after((req, res) -> {
			res.type("application/json");
		});

		exception(IllegalArgumentException.class, (e, req, res) -> {
			res.status(400);
			res.body(toJson(new ResponseError(e)));
		});
	}
}