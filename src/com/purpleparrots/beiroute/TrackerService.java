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
		Log.d("diag", "Creating TrackerService...");
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		Log.d("diag", "Got location service");
		
		//String provider = lm.getBestProvider(new Criteria(), true);
    }

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		workingRoute = Control.getWorkingRoute();
		Log.d("diag", "Starting...");
		requestLocationUpdates();
		Log.d("diag", "Submitted request for location lookups");
		return START_STICKY;
	}

	public void onLocationChanged(Location location) {
		if (Control.getNewRouteState() == Control.RECORDING) {
			workingRoute.addLocFix(location);
			Log.d("diag", location.toString());
		}
	}

	public void onProviderDisabled(String provider) {
		Log.d("diag", "Disabled: " + provider);
        requestLocationUpdates();
	}

	public void onProviderEnabled(String provider) {
		Log.d("diag", "Enabled: " + provider);
		requestLocationUpdates();
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}
	
	private void requestLocationUpdates() {
		//lm.requestLocationUpdates(lm.getBestProvider(new Criteria(), true), (long) interval, 0.0f, this);
		lm.requestLocationUpdates(lm.GPS_PROVIDER, (long) interval, 0.0f, this);
	}

	public void setWorkingRoute(Route route) {
		workingRoute = route;
	}
}
