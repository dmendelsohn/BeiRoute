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
	
	private static GpsRecordService grs;
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
	
	private static long followingStartTime = 0;
	private static long followingDuration = 0;
	private static GeoPoint lastGpsFix;
	
	static final int NOT_YET_RECORDED = 0;
	static final int RECORDING = 1;
	static final int RECORDED = 2;
	
	static final int NOT_FOLLOWING = 0;
	static final int FOLLOWING = 1;
	
	private static int newRouteState = NOT_YET_RECORDED;
	private static int followingState = NOT_FOLLOWING;
	
	private static boolean isFirstTimeFlag = false;
	
	public static boolean isFirstTime() {
		return isFirstTimeFlag;
	}
	
	public static void markFirstTime() {
		isFirstTimeFlag = true;
	}
	
	public static int getNewRouteState() {
		return newRouteState;
	}
	
	public static int getFollowingState() {
		return followingState;
	}
	public static void setFollowingState(int state) {
		followingState = state;
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
		grs = new GpsRecordService();
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
		workingRoute.startTime = 1000000000000l;
		workingRoute.duration = 300000;
		Location l = new Location("test");
		l.setLatitude(42.355);
		l.setLongitude(-71.09);
		l.setTime(1000000000000l);
		workingRoute.addLocFix(l);
		l = new Location("test");
		l.setLatitude(42.356);
		l.setLongitude(-71.091);
		l.setTime(1000000007500l);
		workingRoute.addLocFix(l);
		l = new Location("test");
		l.setLatitude(42.357);
		l.setLongitude(-71.092);
		l.setTime(1000000015000l);
		workingRoute.addLocFix(l);
		l = new Location("test");
		l.setLatitude(42.358);
		l.setLongitude(-71.093);
		l.setTime(1000000022500l);
		workingRoute.addLocFix(l);
		l = new Location("test");
		l.setLatitude(42.359);
		l.setLongitude(-71.094);
		l.setTime(1000000030000l);
		workingRoute.addLocFix(l);
		workingRoute.duration = 30000;
		saveRoute("Monday morning", "Maseeh", "32-124");
		workOnNewRoute();
		workingRoute.duration = 300000;
		saveRoute("W/F morning", "Maseeh", "26-100");
		workOnNewRoute();
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
		grs.setWorkingRoute(workingRoute);
		workingRoute.setStartTime();
		Log.d("diag", "Starting TrackerService");
		context.startService(new Intent(context, GpsRecordService.class));
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
	
	public static void discardRoute() {
		newRoute = new Route();
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
	
	public static GeoPoint[] getRouteGeoFixes() {
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
	
	public static long[] getRouteRelativeTimes() {
		LinkedList<Location> ll = workingRoute.getLocFixes();
		if (ll.isEmpty()) {
			return null;
		}
		Iterator<Location> li = ll.iterator();
		long[] timestamps = new long[ll.size()];
		for (int i = 0; i < timestamps.length; i++) {
			Location next = li.next();
			timestamps[i] = next.getTime() - workingRoute.startTime;
		}
		return timestamps;
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
	
	public static void saveAlarm(String name, GregorianCalendar time, Context context) {
		maxAlarmId--;
		alarmList.put(maxAlarmId, new Alarm(name, workingRoute, time, as));
		setWorkingAlarm(maxAlarmId);
		/*
        Intent i = new Intent(this, MainActivity.class);
    	startActivity(i);
    	*/
        //startActivity(new Intent(android.provider.AlarmClock.ACTION_SET_ALARM));
		Intent intent = new Intent(context, NotificationService.class);
        intent.putExtra("workingRouteId", workingRouteId);
        intent.putExtra("workingAlarmId", workingAlarmId);
        intent.putExtra("time", time.getTimeInMillis());
		context.startService(intent);
	}
	
	public static void saveAlarm(String name, int year, int month, int day, int hour, int minute, Context context) {
		saveAlarm(name, new GregorianCalendar(year, month, day, hour, minute), context);
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
	
	public static void setupFollowingRoute(long startTime, long duration) {
		followingStartTime = startTime;
		followingDuration = duration;
	}
	
	public static long getFollowingStartTime() {
		return followingStartTime;
	}
	public static long getFollowingDuration() {
		return followingDuration;
	}

	public static GeoPoint getLastGpsFix() {
		return lastGpsFix;
	}

	public static void setLastGpsFix(GeoPoint lastGpsFix) {
		Control.lastGpsFix = lastGpsFix;
	}

	public static double getIdealProgress() {
		if (followingState == NOT_FOLLOWING) {
			return 0;
		} else {
			Log.d("diag", "" + ((System.currentTimeMillis() - followingStartTime) * 1.0 / followingDuration));
			return (System.currentTimeMillis() - followingStartTime) * 1.0 / followingDuration;
		}
	}
	
	public static void stopFollowing() {
		
	}
	

	public static double getRealProgress() {
		return 0;
	}
}
