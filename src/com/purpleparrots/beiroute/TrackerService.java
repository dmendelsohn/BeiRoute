package com.purpleparrots.beiroute;

import java.util.LinkedList;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class TrackerService extends Service implements LocationListener {
	
	final String TAG = "TrackerService";
	LocationManager lm;
	final long interval = 15000;
	
	public void onCreate() {
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		//String provider = lm.getBestProvider(new Criteria(), true);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, (long) interval, 0.0f, this);
    }
	
	public void setLocationList(LinkedList<Location> list) {
		recordedLocations = list;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		return 0; ///////
	}

	@Override
	public void onLocationChanged(Location location) {
		recordedLocations.add(location);
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.d(TAG, "Disabled: " + provider);
        requestLocationUpdates();
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d(TAG, "Enabled: " + provider);
		requestLocationUpdates();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	public void requestLocationUpdates() {
		lm.requestLocationUpdates(lm.getBestProvider(new Criteria(), true), (long) interval, 0.0f, this);
	}
}
