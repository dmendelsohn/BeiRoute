package com.purpleparrots.beiroute;

import java.util.GregorianCalendar;
import java.util.Hashtable;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;

public class Control {
	
	private TrackerService ts;
	private AlarmService as;
	private WakeLockService ws;
	
	private int maxRouteId = -1;
	private int workingRouteId = -1;
	private Hashtable<Integer, Route> routeList;
	private Route workingRoute;
	
	private int maxAlarmId = -1;
	private int workingAlarmId = -1;
	private Hashtable<Integer, Alarm> alarmList;
	private Alarm workingAlarm;
	
	private class WakeLockService extends Service {

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
	
	public Control() {
		ts = new TrackerService();
		ws = new WakeLockService();
		as = new AlarmService();
		routeList = new Hashtable<Integer, Route>();
		alarmList = new Hashtable<Integer, Alarm>();
	}
	
	public int createRoute() {
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
	public void setWorkingRoute(int id) {
		workingRouteId = id;
		workingRoute = routeList.get(workingRouteId);
	}
	
	public void startRecording() {
		ws.acquire();
		ts.setWorkingRoute(workingRoute);
		ts.startService(new Intent()); // TODO: information in intent
	}
	
	public long stopRecording() {
		ts.stopService(null);
		workingRoute.setDuration();
		ws.release();
		return workingRoute.getDuration();
	}
	
	public void saveRoute(String name, String startLoc, String endLoc) {
		workingRoute.setName(name);
		workingRoute.setStartLoc(startLoc);
		workingRoute.setEndLoc(endLoc);
	}
	
	public void deleteRoute() {
		routeList.remove(workingRouteId);
	}

	String getRouteName() {
		return workingRoute.getName();
	}
	
	String getRouteStartLoc() {
		return workingRoute.getStartLoc();
	}
	
	String getRouteEndLoc() {
		return workingRoute.getEndLoc();
	}
	
	Long getRouteDuration() {
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
	
	public void setWorkingAlarm(int id) {
		workingAlarmId = id;
		workingAlarm = alarmList.get(workingAlarmId);
	}
	
	public Hashtable<String, Integer> getAlarms() {
		Hashtable<String, Integer> out = new Hashtable<String, Integer>();
		for (int key : alarmList.keySet()) {
			out.put(alarmList.get(key).getName(), key);
		}
		return out;
	}
	
	public int saveAlarm(String name, GregorianCalendar time) {
		maxAlarmId++;
		alarmList.put(maxAlarmId, new Alarm(name, workingRoute, time, as));
		setWorkingAlarm(maxAlarmId);
		return workingAlarmId;
	}
	
	public void saveAlarm(String name, int year, int month, int day, int hour, int minute) {
		saveAlarm(name, new GregorianCalendar(year, month, day, hour, minute));
	}
	
	public void deleteAlarm() {
		alarmList.remove(workingAlarmId);
	}
	
	public String getAlarmName() {
		return workingAlarm.getName();
	}
	
	public String getAlarmRouteName() {
		return workingAlarm.getRoute().getName();
	}
	
	public GregorianCalendar getAlarmGregorian() {
		return workingAlarm.getTime();
	}
	
	/*
	Hashtable<Integer, GregorianCalendar> getAlarmList() {
		return alarmList;
	}
	*/
}
