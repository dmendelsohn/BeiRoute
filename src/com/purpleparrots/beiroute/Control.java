package com.purpleparrots.beiroute;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class Control {

	private static boolean hasBeenInitialized = false;

	private static TrackerService ts;
	private static AlarmService as;
	private static WakeLockService ws;

	private static int maxRouteId = 0;
	private static int workingRouteId = 0;
	private static Hashtable<Integer, AndroidSerializable> routeList;
	private static Route workingRoute;

	private static int maxAlarmId = 0;
	private static int workingAlarmId = 0;
	private static Hashtable<Integer, AndroidSerializable> alarmList;
	private static Alarm workingAlarm;

	static final int NOT_YET_RECORDED = 0;
	static final int RECORDING = 1;
	static final int RECORDED = 2;
	
	static final int NOT_FOLLOWING = 0;
	static final int FOLLOWING = 1;

	private static int newRouteState = NOT_YET_RECORDED;
	private static int followingState = NOT_FOLLOWING;

	public static int getNewRouteState() {
		return newRouteState;
	}
	
	public static int getFollowingState() {
		return followingState;
	}

	private static class WakeLockService extends Service {

		private PowerManager pm;
		private PowerManager.WakeLock wakelock;

		public void onCreate() {
			pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
					| PowerManager.ON_AFTER_RELEASE, "wakelock tag");
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
		if (hasBeenInitialized)
			return;
		ts = new TrackerService();
		ws = new WakeLockService();
		as = new AlarmService();
		// routeList = new Hashtable<Integer, Route>();
		// alarmList = new Hashtable<Integer, Alarm>();
		routeList = AndroidSerializable.deserializeTable("routes");
		alarmList = AndroidSerializable.deserializeTable("alarms");
		hasBeenInitialized = true;
		workingRoute = new Route();
	}

	/*
	 * public static void createRoute() { if (workingRoute == null) {
	 * workingRoute = new Route(); } }
	 */
	/*
	 * public int getWorkingRoute() { return workingRouteId; }
	 */
	public static void setWorkingRoute(int id) {
		workingRouteId = id;
		workingRoute = (Route) routeList.get(workingRouteId);
	}

	public static void startRecording() {
		// ws.acquire();
		ts.setWorkingRoute(workingRoute);
		// ts.startService(new Intent()); // TODO: information in intent
		workingRoute.setStartTime();
		newRouteState = RECORDING;
		Log.d("Control", "newRouteState = RECORDING");
	}

	public static long getElapsedTime() {
		if (workingRoute == null || workingRoute.getStartTime() == 0) {
			return 0;
		}
		workingRoute.updateDuration();
		return workingRoute.getDuration();
	}

	public static long stopRecording() {
		// ts.stopService(null);
		workingRoute.updateDuration();
		// ws.release();
		newRouteState = RECORDED;
		Log.d("Control", "newRouteState = RECORDED");
		return workingRoute.getDuration();
	}

	public static void saveRoute(String name, String startLoc, String endLoc) {
		workingRoute.setName(name);
		workingRoute.setStartLoc(startLoc);
		workingRoute.setEndLoc(endLoc);
		routeList.put(maxRouteId, workingRoute);
		workingRoute = new Route();
		maxRouteId++;
		newRouteState = NOT_YET_RECORDED;
		Log.d("Control", "newRouteState = NOT_YET_RECORDED");
		try {
			AndroidSerializable.serializeTable(routeList, "routes");
		} catch (IOException e) {
			// yeah idk
			e.printStackTrace();
		}
	}

	public static void deleteRoute() {
		for (int key : alarmList.keySet()) {
			if (((Alarm) alarmList.get(key)).getRoute().equals(workingRoute)) {
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

	public static Hashtable<Integer, String> getRoutes() {
		Hashtable<Integer, String> out = new Hashtable<Integer, String>();
		for (int key : routeList.keySet()) {
			out.put(key, ((Route) routeList.get(key)).getName());
		}
		return out;
	}

	/*
	 * Hashtable<Integer, Route> getRouteList() { return routeList; }
	 * 
	 * Route getRoute() { return workingRoute; }
	 */

	public static void setWorkingAlarm(int id) {
		workingAlarmId = id;
		workingAlarm = (Alarm) alarmList.get(workingAlarmId);
	}

	public static Hashtable<Integer, String> getAlarms() {
		Hashtable<Integer, String> out = new Hashtable<Integer, String>();
		for (int key : alarmList.keySet()) {
			out.put(key, ((Alarm) alarmList.get(key)).getName());
		}
		return out;
	}

	public static int saveAlarm(String name, GregorianCalendar time) {
		maxAlarmId--;
		alarmList.put(maxAlarmId, new Alarm(name, workingRoute, time, as));
		setWorkingAlarm(maxAlarmId);
		return workingAlarmId;
	}

	public static void saveAlarm(String name, int year, int month, int day,
			int hour, int minute) {
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
		Log.d("jb", "" + workingAlarm.getTime().get(GregorianCalendar.MONTH));
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
	 * Hashtable<Integer, GregorianCalendar> getAlarmList() { return alarmList;
	 * }
	 */
}
