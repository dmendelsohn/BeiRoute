package com.purpleparrots.beiroute;

import java.util.Date;
import java.util.LinkedList;

import android.location.Location;

public class Route {
	
	private int id;
	
	//private static LinkedList<Route> routes;
	//private static int numRoutes = 0;
	
	private String startLoc;
	private String endLoc;
	private long startTime;
	private long duration;
	private LinkedList<Location> nodes;
	
	/*
	public static int createNewRoute() {
		if (routes == null) {
			routes = new LinkedList<Route>();
		}
			
		return numRoutes++;
	}
	
	public static void deleteRoute(int id) {
		routes.remove(id);
	}
	
	public static void deleteAllRoutes() {
		routes = null;
	}
	*/
	
	public Route() {
		setStartTime();
	}
	
	public void addLocFix(Location loc) {
		nodes.add(loc);
		setDuration();
	}
	public LinkedList<Location> getLocFixes() {
		return nodes;
	}
	
	public String getStartLoc() {
		return startLoc;
	}
	public void setStartLoc(String startLoc) {
		this.startLoc = startLoc;
	}
	
	public String getEndLoc() {
		return endLoc;
	}
	public void setEndLoc(String endLoc) {
		this.endLoc = endLoc;
	}
	
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime() {
		startTime = new Date().getTime();
	}
	
	public long getDuration() {
		return duration;
	}
	public void setDuration() {
		duration = new Date().getTime() - startTime;
	}
	
	
}
