package com.purpleparrots.beiroute;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.purpleparrots.beiroute.Control;
import com.purpleparrots.beiroute.RouteDetailActivity;

public class TimeAlarm extends BroadcastReceiver {
	
	int i = 0;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("diag", "Received notification!");
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification test = new Notification(android.R.drawable.alert_light_frame, "This is a ticker!", System.currentTimeMillis());
		test.defaults |= Notification.DEFAULT_LIGHTS;
		test.defaults |= Notification.DEFAULT_SOUND;
		test.defaults |= Notification.DEFAULT_VIBRATE;
		Intent notificationIntent = new Intent(context, RouteDetailActivity.class);
		notificationIntent.putExtra("workingRouteId", intent.getIntExtra("workingRouteId", 0));
		notificationIntent.putExtra("workingAlarmId", intent.getIntExtra("workingAlarmId", 0));
		int blah = intent.getIntExtra("blah", 0);
		PendingIntent pi = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		test.setLatestEventInfo(context, "Title", "Notification text", pi);
		Control.setWorkingRoute(intent.getIntExtra("workingRouteId", blah));
		Control.setFollowingState(Control.FOLLOWING);
		Control.setupFollowingRoute(System.currentTimeMillis(), Control.getWorkingRoute().duration);
		Location start = Control.getWorkingRoute().getLocFixes().getFirst();
		//Control.setLastGpsFix(new GeoPoint((int) (start.getLatitude() * 1000000), (int) (start.getLongitude() * 1000000)));
		Control.setLastGpsFix(new GeoPoint(42358477, -71092208));
		nm.notify(i, test);
		i++;
	}
	
}