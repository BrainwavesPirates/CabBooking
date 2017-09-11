package com.booking.util;

public class CommonUtil {
	private double dis;

	public CommonUtil(int x1, int y1) {
		dis = (Math.sqrt((x1) * (x1) + (y1) * (y1)));
	}

	public double getDistance() {
		return dis;
	}
}
