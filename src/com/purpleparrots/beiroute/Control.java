package com.purpleparrots.beiroute;

import java.util.GregorianCalendar;
import java.util.Hashtable;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;

public class Control {
	
	private static TrackerService ts;
	private static AlarmService as;
	private static WakeLockService ws;
	
	private static int maxRouteId = -1;
	private static int workingRouteId = -1;
	private static Hashtable<Integer, Route> routeList;
	private static Route workingRoute;
	
	private static int maxAlarmId = -1;
	private static int workingAlarmId = -1;
	private static Hashtable<Integer, Alarm> alarmList;
	private static Alarm workingAlarm;
	
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
		ts = new TrackerService();
		ws = new WakeLockService();
		as = new AlarmService();
		routeList = new Hashtable<Integer, Route>();
		alarmList = new Hashtable<Integer, Alarm>();
	}
	
	public static int createRoute() {
		maxRouteId++;
		routeList.put(maxRouteId, new Route());
		setWorkingRoute(maxRouteId);
		return workingRouteId;
	}
	/*
	public int getWorkingRoute() {
		return workingRouteId;
	}
	*/
	public static void setWorkingRoute(int id) {
		workingRouteId = id;
		workingRoute = routeList.get(workingRouteId);
	}
	
	public static void startRecording() {
		//ws.acquire();
		ts.setWorkingRoute(workingRoute);
		ts.startService(new Intent()); // TODO: information in intent
	}
	
	public static long stopRecording() {
		ts.stopService(null);
		workingRoute.setDuration();
		//ws.release();
		return workingRoute.getDuration();
	}
	
	public static void saveRoute(String name, String startLoc, String endLoc) {
		workingRoute.setName(name);
		workingRoute.setStartLoc(startLoc);
		workingRoute.setEndLoc(endLoc);
	}
	
	public static void deleteRoute() {
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
	
	public static Hashtable<String, Integer> getAlarms() {
		Hashtable<String, Integer> out = new Hashtable<String, Integer>();
		for (int key : alarmList.keySet()) {
			out.put(alarmList.get(key).getName(), key);
		}
		return out;
	}
	
	public static int saveAlarm(String name, GregorianCalendar time) {
		maxAlarmId++;
		alarmList.put(maxAlarmId, new Alarm(name, workingRoute, time, as));
		setWorkingAlarm(maxAlarmId);
		return workingAlarmId;
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
	
	public static GregorianCalendar getAlarmGregorian() {
		return workingAlarm.getTime();
	}
	
	/*
	Hashtable<Integer, GregorianCalendar> getAlarmList() {
		return alarmList;
	}
	*/
}
