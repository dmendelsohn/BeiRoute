package com.purpleparrots.beiroute;

import java.util.Hashtable;

import android.content.Context;
import android.os.PowerManager;

public class Control {
	
	private TrackerService ts;
	private int maxRouteId = -1;
	private int workingRouteId = -1;
	private Hashtable<Integer, Route> routeList;
	private Route workingRoute;
	
	private PowerManager pm;
	private PowerManager.WakeLock wakelock;
	
	public Control() {
		ts = new TrackerService();
		routeList = new Hashtable<Integer, Route>();
		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "wakelock tag");
	}
	
	public int createRoute() {
		maxRouteId++;
		routeList.put(maxRouteId, new Route());
		setWorkingRoute(maxRouteId);
		return workingRouteId;
	}
	
	public int getWorkingRoute() {
		return workingRouteId;
	}
	
	public void setWorkingRoute(int id) {
		workingRouteId = id;
		workingRoute = routeList.get(workingRouteId);
	}
	
	public void startRecording() {
		ts.startService(null); // TODO: information in intent
	}
	
	public long stopRecording() {
		ts.stopService(null);
		
	}
	
	public void saveRoute(String name, String startLoc, String endLoc) {
		
	}
	
	public void deleteRoute() {
		
	}
	
	void setAlarm(Date date) {
		
	}

	String getRouteName() {
		
	}
	
	String getRouteStartLoc() {
		
	}
	
	String getRouteEndLoc() {
		
	}

	Hashtable getRouteList() {
		
	}
	
	Route getRoute() {
		
	}
}
