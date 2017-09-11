package com.booking.spark;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.booking.Main;
import com.google.gson.Gson;

import spark.Spark;
import spark.utils.IOUtils;

public class CabBookingControllerIntegrationTest {

	@BeforeClass
	public static void beforeClass() {
		Main.main(null);
	}

	@AfterClass
	public static void afterClass() {
		Spark.stop();
	}

	@Test
	public void testCabConstruction() {
		
		TestResponse res = null;
		Map<String, String> json = new HashMap<String,String>();
		res = request("POST", "/cabs?id=DL01HB001&type=pink&location=120");
		json = res.json();
		assertEquals(200, res.status);
		Assert.assertEquals(json.get("id"), "DL01HB001");
		Assert.assertEquals(json.get("type"), "pink");
		Assert.assertEquals(json.get("location"), 120.0);
		assertNotNull(json.get("id"));
		
		res = request("POST", "/cabs?id=DL01HB002&type=red&location=200");
		json = res.json();
		assertEquals(200, res.status);
		Assert.assertEquals(json.get("id"), "DL01HB002");
		Assert.assertEquals(json.get("type"), "red");
		Assert.assertEquals(json.get("location"), 200.0);
		assertNotNull(json.get("id"));
	}

	@Test
	public void testGetAllRunningCabs() {

		/* add a cab */
		testCabConstruction();

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, 50);
		TestResponse result = request("GET", "/cabs");
		assertEquals(200, result.status);
		Assert.assertEquals(Integer.parseInt(result.body), 2);
	}
	
	@Test
	public void testNearestAvailableCabRequest() {

		/* add a cab */
		testCabConstruction();

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, 50);
		TestResponse result = request("GET", "/cabs/?bookingId=BR001&cabType=pink&pickupCode=100&dropCode=156");
		Map<String, String> jsonObj = result.json();
		assertEquals(200, result.status);
		Assert.assertEquals(jsonObj.get("id"), "DL01HB001");
	}

	private TestResponse request(String method, String path) {
		try {
			URL url = new URL("http://localhost:4567" + path);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method);
			connection.setDoOutput(true);
			connection.connect();
			String body = IOUtils.toString(connection.getInputStream());
			return new TestResponse(connection.getResponseCode(), body);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Sending request failed: " + e.getMessage());
			return null;
		}
	}

	private static class TestResponse {

		public final String body;
		public final int status;

		public TestResponse(int status, String body) {
			this.status = status;
			this.body = body;
		}

		@SuppressWarnings("unchecked")
		public Map<String, String> json() {
			return new Gson().fromJson(body, HashMap.class);
		}
	}
}