package com.purpleparrots.beiroute;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
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
		Log.d("diag", "started NotificationService.onCreate()");
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification test = new Notification(android.R.drawable.alert_light_frame, "This is a ticker!", System.currentTimeMillis());
		Intent notificationIntent = new Intent(this, MainActivity.class);
		PendingIntent pi = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		test.setLatestEventInfo(this, "Title", "Notification text", pi);
		nm.notify(0, test);
		Log.d("diag", "completed NotificationService.onCreate()");
		return 0;
	}

}
