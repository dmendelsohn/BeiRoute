package com.purpleparrots.beiroute;

import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import com.google.android.maps.GeoPoint;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class Control {
	
	private static boolean hasBeenInitialized = false;
	
	private static TrackerService ts;
	private static AlarmService as;
	//private static WakeLockService ws;
	
	private static int maxRouteId = 0;
	private static int workingRouteId = 0;
	private static Hashtable<Integer, Route> routeList;
	private static Route workingRoute;
	private static Route newRoute;
	// workingRoute either points to the same route as newRoute
	// (if it represents the new route not yet added to the route hash)
	// or points to one of the routes already in the hash, in which case
	// workingRouteId will be the key.
	
	private static int maxAlarmId = 0;
	private static int workingAlarmId = 0;
	private static Hashtable<Integer, Alarm> alarmList;
	private static Alarm workingAlarm;
	
	static final int NOT_YET_RECORDED = 0;
	static final int RECORDING = 1;
	static final int RECORDED = 2;
	
	private static int newRouteState = NOT_YET_RECORDED;
	
	public static int getNewRouteState() {
		return newRouteState;
	}

	private static class WakeLockService extends Service {

		private PowerManager pm;
		private PowerManager.WakeLock wakelock;
		
		public void onCreate() {
			pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
	        wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "wakelock tag");
		}
		public void acquire() {
			wakelock.acquire();
		}
		public void release() {
			wakelock.release();
		}
		public void onDestroy() {
			release();
		}
		@Override
		public IBinder onBind(Intent intent) {
			return null;
		}
		
	}
	
	public static void initialize() {
		if (hasBeenInitialized) return;
		ts = new TrackerService();
		//ws = new WakeLockService();
		as = new AlarmService();
		routeList = new Hashtable<Integer, Route>();
		alarmList = new Hashtable<Integer, Alarm>();
		workingRoute = new Route();
		newRoute = workingRoute;
		hasBeenInitialized = true;
		populateForDemo(routeList, alarmList);
	}
	
	private static void populateForDemo(Hashtable<Integer, Route> routeList, Hashtable<Integer, Alarm> alarmList) {
		workingRoute.duration = 300000;
		Location l = new Location("test");
		l.setLatitude(42.355);
		l.setLongitude(-71.09);
		workingRoute.addLocFix(l);
		l = new Location("test");
		l.setLatitude(42.356);
		l.setLongitude(-71.091);
		workingRoute.addLocFix(l);
		l = new Location("test");
		l.setLatitude(42.357);
		l.setLongitude(-71.092);
		workingRoute.addLocFix(l);
		l = new Location("test");
		l.setLatitude(42.358);
		l.setLongitude(-71.093);
		workingRoute.addLocFix(l);
		l = new Location("test");
		l.setLatitude(42.359);
		l.setLongitude(-71.094);
		workingRoute.addLocFix(l);
		saveRoute("Monday morning", "Maseeh", "32-124");
		workingRoute.duration = 300000;
		saveRoute("W/F morning", "Maseeh", "26-100");
		workingRoute.duration = 300000;
		saveRoute("House to Campus", "House", "77 Mass Ave");
	}
	
	public static Route getWorkingRoute() {
		return workingRoute;
	}
	
	public static void setWorkingRoute(int id) {
		Log.d("diag", "workingRoute set to " + id);
		workingRouteId = id;
		workingRoute = routeList.get(workingRouteId);
	}
	
	public static void workOnNewRoute() {
		workingRoute = newRoute;
	}
	
	public static void startRecording(Context context) {
		ts.setWorkingRoute(workingRoute);
		workingRoute.setStartTime();
		Log.d("diag", "Starting TrackerService");
		context.startService(new Intent(context, TrackerService.class));
		Log.d("diag", "Started TrackerService");
		newRouteState = RECORDING;
		Log.d("diag", "newRouteState = RECORDING");
	}
	
	public static long getElapsedTime() {
		if (newRouteState == NOT_YET_RECORDED) {
			return 0;
		}
		if (workingRoute == null || workingRoute.getStartTime() == 0) {
			return 0;
		}
		workingRoute.updateDuration();
		return workingRoute.getDuration();
	}
	
	public static long stopRecording() {
		workingRoute.updateDuration();
		newRouteState = RECORDED;
		Log.d("diag", "newRouteState = RECORDED");
		return workingRoute.getDuration();
	}
	
	public static void saveRoute(String name, String startLoc, String endLoc) {
		newRoute.setName(name);
		newRoute.setStartLoc(startLoc);
		newRoute.setEndLoc(endLoc);
		routeList.put(maxRouteId, newRoute);
		newRoute = new Route();
		maxRouteId++;
		//setWorkingRoute(maxRouteId);
		newRouteState = NOT_YET_RECORDED;
		Log.d("diag", "newRouteState = NOT_YET_RECORDED");
	}
	
	public static void deleteRoute() {
		for (int key : alarmList.keySet()) {
			if (alarmList.get(key).getRoute().equals(workingRoute)) {
				alarmList.remove(key);
			}
		}
		routeList.remove(workingRouteId);
	}

	public static String getRouteName() {
		return workingRoute.getName();
	}
	
	public static String getRouteStartLoc() {
		return workingRoute.getStartLoc();
	}
	
	public static String getRouteEndLoc() {
		return workingRoute.getEndLoc();
	}
	
	public static long getRouteDuration() {
		return workingRoute.getDuration();
	}
	
	public static GeoPoint[] getRouteLocFixes() {
		LinkedList<Location> ll = workingRoute.getLocFixes();
		if (ll.isEmpty()) {
			return null;
		}
		Iterator<Location> li = ll.iterator();
		GeoPoint[] points = new GeoPoint[ll.size()];
		Log.d("diag", ((Integer) ll.size()).toString());
		for (int i = 0; i < points.length; i++) {
			Location next = li.next();
			points[i] = new GeoPoint((int) (next.getLatitude()*1000000), (int) (next.getLongitude()*1000000));
		}
		return points;
	}
	
	public static GeoPoint getRouteMapCenter() {
		return workingRoute.getMapCenter();
	}
	
	public static Hashtable<Integer, String> getRoutes() {
		Hashtable<Integer, String> out = new Hashtable<Integer, String>();
		for (int key : routeList.keySet()) {
			out.put(key, routeList.get(key).getName());
		}
		return out;
	}

	/*
	Hashtable<Integer, Route> getRouteList() {
		return routeList;
	}
	
	Route getRoute() {
		return workingRoute;
	}
	*/
	
	public static void setWorkingAlarm(int id) {
		workingAlarmId = id;
		workingAlarm = alarmList.get(workingAlarmId);
	}
	
	public static Hashtable<Integer, String> getAlarms() {
		Hashtable<Integer, String> out = new Hashtable<Integer, String>();
		for (int key : alarmList.keySet()) {
			out.put(key, alarmList.get(key).getName());
		}
		return out;
	}
	
	public static void saveAlarm(String name, GregorianCalendar time) {
		maxAlarmId--;
		alarmList.put(maxAlarmId, new Alarm(name, workingRoute, time, as));
		setWorkingAlarm(maxAlarmId);
		/*
        Intent i = new Intent(this, MainActivity.class);
    	startActivity(i);
    	*/
        //Log.d("diag", "about to start service");
        //startActivity(new Intent(android.provider.AlarmClock.ACTION_SET_ALARM));
        //startService(new Intent(this, NotificationService.class));
        //Log.d("diag", "just finished startService");
	}
	
	public static void saveAlarm(String name, int year, int month, int day, int hour, int minute) {
		saveAlarm(name, new GregorianCalendar(year, month, day, hour, minute));
	}
	
	public static void deleteAlarm() {
		alarmList.remove(workingAlarmId);
	}
	
	public static String getAlarmName() {
		return workingAlarm.getName();
	}
	
	public static String getAlarmRouteName() {
		return workingAlarm.getRoute().getName();
	}
	
	public static int getAlarmYear() {
		return workingAlarm.getTime().get(GregorianCalendar.YEAR);
	}
	public static int getAlarmMonth() {
		return workingAlarm.getTime().get(GregorianCalendar.MONTH);
	}
	public static int getAlarmDay() {
		return workingAlarm.getTime().get(GregorianCalendar.DATE);
	}
	public static int getAlarmHour() {
		return workingAlarm.getTime().get(GregorianCalendar.HOUR);
	}
	public static int getAlarmMinute() {
		return workingAlarm.getTime().get(GregorianCalendar.MINUTE);
	}
	
	public static GregorianCalendar getAlarmGregorian() {
		return workingAlarm.getTime();
	}
	
	/*
	Hashtable<Integer, GregorianCalendar> getAlarmList() {
		return alarmList;
	}
	*/
}
