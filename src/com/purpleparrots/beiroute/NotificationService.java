package com.purpleparrots.beiroute;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class NotificationService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("diag", "Service started");
		int workingRouteId = intent.getIntExtra("workingRouteId", 0);
		int workingAlarmId = intent.getIntExtra("workingAlarmId", 0);
		long time = intent.getLongExtra("time", 0);
		createScheduledNotification(time, workingRouteId, workingAlarmId, 0);
		return 0;
	}
	
	private void createScheduledNotification(long time, int workingRouteId, int workingAlarmId, int blah) {
		Log.d("diag", "Creating scheduled notification...");
		AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(getBaseContext().ALARM_SERVICE);
		Intent intent = new Intent(this, TimeAlarm.class);
		intent.putExtra("workingRouteId", workingRouteId);
		intent.putExtra("workingAlarmId", workingAlarmId);
		intent.putExtra("blah", blah);
		PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, Intent.FLAG_GRANT_READ_URI_PERMISSION);
		am.set(AlarmManager.RTC_WAKEUP, time, pi);
		//am.set(AlarmManager.RTC_WAKEUP, 0, pi);
		Log.d("diag", "" + (time - System.currentTimeMillis()));
	}

}
