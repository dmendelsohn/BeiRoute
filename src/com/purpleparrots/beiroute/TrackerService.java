package com.purpleparrots.beiroute;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class TrackerService extends Service implements LocationListener {
	
	final String TAG = "TrackerService";
	LocationManager lm;
	final long interval = 15000;
	private Route workingRoute;
	
	public void onCreate() {
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		//String provider = lm.getBestProvider(new Criteria(), true);
    }

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		requestLocationUpdates();
		return START_STICKY;
	}

	public void onLocationChanged(Location location) {
		workingRoute.addLocFix(location);
	}

	public void onProviderDisabled(String provider) {
		Log.d(TAG, "Disabled: " + provider);
        requestLocationUpdates();
	}

	public void onProviderEnabled(String provider) {
		Log.d(TAG, "Enabled: " + provider);
		requestLocationUpdates();
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}
	
	private void requestLocationUpdates() {
		lm.requestLocationUpdates(lm.getBestProvider(new Criteria(), true), (long) interval, 0.0f, this);
	}

	public void setWorkingRoute(Route route) {
		workingRoute = route;
	}
}
