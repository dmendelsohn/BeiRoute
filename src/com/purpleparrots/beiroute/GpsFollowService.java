package com.purpleparrots.beiroute;

import com.google.android.maps.GeoPoint;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class GpsFollowService extends Service implements LocationListener {
	
	LocationManager lm;
	//final long interval = 15000;
	final long interval = 0;
	public void onCreate() {
		Log.d("diag_gpsfollow", "Creating TrackerService...");
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		Log.d("diag_gpsfollow", "Got location service");
		
		//String provider = lm.getBestProvider(new Criteria(), true);
    }

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("diag gpsfollow", "Starting...");
		requestLocationUpdates();
		Log.d("diag gpsfollow", "Submitted request for location lookups");
		return START_STICKY;
	}

	public void onLocationChanged(Location location) {
		if (Control.getNewRouteState() == Control.RECORDING) {
			Log.d("diag_gpsfollow", location.toString());
			Control.setLastGpsFix(new GeoPoint((int) (location.getLatitude() * 1000000), (int) (location.getLongitude() * 1000000)));
		}
	}

	public void onProviderDisabled(String provider) {
		Log.d("diag_gpsfollow", "Disabled: " + provider);
        requestLocationUpdates();
	}

	public void onProviderEnabled(String provider) {
		Log.d("diag_gpsfollow", "Enabled: " + provider);
		requestLocationUpdates();
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}
	
	private void requestLocationUpdates() {
		//lm.requestLocationUpdates(lm.getBestProvider(new Criteria(), true), (long) interval, 0.0f, this);
		lm.requestLocationUpdates(lm.GPS_PROVIDER, (long) interval, 0.0f, this);
	}
}
