package com.booking.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.booking.constants.CabSystemConstants;

/**
 * User : Shikha Date : 05/09/17 Version : v1
 */
public class Cab {

	private final String id;
	private String type;
	private int location;
	private Date availableFrom;
	private final List<CabRequest> requests;

	public Cab(String num, String type, int location) {
		this.id = num;
		this.type = type;
		this.location = location;
		Calendar cal = Calendar.getInstance();
		this.availableFrom = cal.getTime();
		this.requests = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public int getLocation() {
		return location;
	}

	public int hashCode() {
		return id.hashCode();
	}

	public boolean equals(Object that) {
		if (that instanceof Cab) {
			Cab _that = (Cab) that;
			return this.id.equals(_that.id);
		} else {
			return false;
		}
	}

	public List<CabRequest> getRequestsServed() {
		return requests;
	}

	public void addJourney(CabRequest request) {
		addJourneyMinutes(request.getPickupTime(), getTimeForDrive(request));
		this.location = request.getDropLocation();
		requests.add(request);

	}

	public Date getAvailableFrom() {
		return (Date) availableFrom.clone();
	}

	private void addJourneyMinutes(Date pickUpTime, int minutes) {
		final long ONE_MINUTE_IN_MILLIS = 60000;
		long curTimeInMs = pickUpTime.getTime();
		this.availableFrom = new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
	}

	public boolean gotTimeToServeThis(CabRequest request) {
		int timeToReach = getTimeToReach(request);
		Date currentTime = addMinutesToDate(timeToReach, this.getAvailableFrom());
		Date pickUpTime = request.getPickupTime();
		int spareTime = (int) (pickUpTime.getTime() - currentTime.getTime()) / (1000 * 60);
		return spareTime >= 15;
	}

	private int getTimeToReach(CabRequest request) {
		int distance = Math.abs(this.getLocation() - request.getPickupLocation())
				* CabSystemConstants.DISTANCE_BETWEEN_LOCATIONS;
		return distance * CabSystemConstants.TIME_FOR_KM;
	}

	private int getTimeForDrive(CabRequest request) {
		int distance = Math.abs(request.getPickupLocation() - request.getDropLocation())
				* CabSystemConstants.DISTANCE_BETWEEN_LOCATIONS;
		return distance * CabSystemConstants.TIME_FOR_KM;
	}

	private static Date addMinutesToDate(int minutes, Date beforeTime) {
		final long ONE_MINUTE_IN_MILLIS = 60000;
		long curTimeInMs = beforeTime.getTime();
		return new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
	}

	public double getFare(CabRequest request) {

		int travelDistance = Math.abs(request.getPickupLocation() - request.getDropLocation());
		double cabFare = CabSystemConstants.FARE;
		if (request.getCabType() == CabSystemConstants.PINK_CAB)
			cabFare = CabSystemConstants.PINK_CAB_FARE;
		double costCollected = travelDistance * cabFare;
		return costCollected;
	}

	public double getCabProfit(CabRequest request) {
		int distance = Math.abs(this.getLocation() - request.getPickupLocation())
				* CabSystemConstants.DISTANCE_BETWEEN_LOCATIONS;
		double costInCurred = distance * CabSystemConstants.COMPANY_COST;
		int travelDistance = Math.abs(request.getPickupLocation() - request.getDropLocation());
		costInCurred += travelDistance * CabSystemConstants.COMPANY_COST;
		double cabFare = CabSystemConstants.FARE;
		if (request.getCabType() == CabSystemConstants.PINK_CAB)
			cabFare = CabSystemConstants.PINK_CAB_FARE;
		double costCollected = travelDistance * cabFare;
		return ((costCollected - costInCurred) / costInCurred) * 100;
	}

}
