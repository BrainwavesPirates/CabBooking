package com.booking.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CacheManager {

	private static CacheManager instance;
	private static Object monitor = new Object();
	private Set<Object> availableCabs = Collections.synchronizedSet(new HashSet<Object>());

	private CacheManager() {
	}

	public void put(Object value) {
		availableCabs.add(value);
	}

	public Object get(Object value) {
		return availableCabs.contains(value);
	}
	
	public Set<Object> getCache() {
		return availableCabs;
	}

	public void clear() {
		availableCabs.clear();
	}

	public static synchronized CacheManager getInstance() {
		if (instance == null) {
			synchronized (monitor) {
				if (instance == null) {
					instance = new CacheManager();
				}
			}
		}
		return instance;
	}

}