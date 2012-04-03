package com.purpleparrots.beiroute;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class AlarmService extends Service {

	private AlarmManager am;
	
	private class Notifier extends Service {
		
		private NotificationManager nm;
		CharSequence contentTitle = "My notification";
		CharSequence contentText = "Hello World!";
		
		public void onStartCommand() {
			nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			Notification notification = new Notification(R.drawable.ic_launcher, "hello", System.currentTimeMillis());
			Context context = getApplicationContext();
			Intent notificationIntent = new Intent(this, AlarmDetailActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
			nm.notify(0, notification);
		}
		
		@Override
		public IBinder onBind(Intent intent) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public void onCreate() {
		am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);		
		Intent intent = new Intent(this, Notifier.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
