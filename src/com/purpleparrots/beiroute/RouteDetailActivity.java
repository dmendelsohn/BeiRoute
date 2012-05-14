package com.purpleparrots.beiroute;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class RouteDetailActivity extends MapActivity {
	TextView name, start, end, duration;
	MapView map;
	Handler mapUpdater;
	Context context;
	//getRouteName, getRouteStartLoc, getRouteEndLoc, getRouteDuration

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d("Dan's Log", "Created Route Detail Activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route);
        name = (TextView)findViewById(R.id.route_detail_name);
        start = (TextView)findViewById(R.id.route_detail_start);
        end = (TextView)findViewById(R.id.route_detail_end);
        duration = (TextView)findViewById(R.id.route_detail_duration);
        map = (MapView)findViewById(R.id.mapView1);
        name.setText(Control.getRouteName());
        //name.setText("Morning Commute");
        start.setText(Control.getRouteStartLoc());
        //start.setText("Baker");
        end.setText(Control.getRouteEndLoc());
        //end.setText("Stata");
        String stringDuration = longToString(Control.getRouteDuration());
        //String stringDuration = longToString(750000);
        duration.setText(stringDuration);
        
        context = this;
        mapUpdater = new Handler() {
        	public void handleMessage(Message msg) {
        		map.invalidate();
        		new Thread(new MapDynamicStuff()).start();
        	}
        };
        
        MapController mc = map.getController();
        mc.setCenter(Control.getRouteMapCenter());
        mc.setZoom(15);
        
        drawStaticMapOverlays();
        if (Control.getFollowingState() == Control.FOLLOWING) {
        	startService(new Intent(this, GpsFollowService.class));
        	Log.d("diag", "drawing dynamic");
        	//new Thread(new MapDynamicStuff()).start();
        	new MapDynamicStuff().run();
        } else {
        	Log.d("diag", "not drawing dynamic");
        }
    }
    
    public void onSet(View route) {
    	Log.d("Dan's Log", "Pushed set alarm button");
    	Intent i = new Intent(this, SetAlarmActivity.class);
    	startActivity(i);
    }
    
    public void onDelete(View route) {
    	Log.d("Dan's Log", "Deleted the route");
    	Control.deleteRoute();
    	Intent i = new Intent(this, MainActivity.class);
    	startActivity(i);
    }
    
    public void homeClick(View v) {
    	Log.d("Dan's Log", "Clicked home from route detail");
    	Intent i = new Intent(this, MainActivity.class);
    	startActivity(i);
    }
    
    public String longToString(long time) {
    	long totalsecs = time/1000;
    	long mins = totalsecs / 60;
    	long secs = totalsecs % 60;
    	String s = "";
    	s += mins;
    	s += " min, ";
    	s += secs;
    	s += " sec";
    	return s;
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void drawStaticMapOverlays() {
		GeoPoint pts[] = Control.getRouteGeoFixes();
		if (pts != null) {
        	if (pts.length > 2) {
        		GeoPoint midPts[] = new GeoPoint[pts.length - 2];
        		for (int i = 0; i < midPts.length; i++) {
        			midPts[i] = pts[i + 1];
        		}
        		MyItemizedOverlay DotIconOverlay = new MyItemizedOverlay(this.getResources().getDrawable(R.drawable.dot));
        		DotIconOverlay.addGeoPoints(midPts);
        		map.getOverlays().add(DotIconOverlay);
        	}
        	MyItemizedOverlay AIconOverlay = new MyItemizedOverlay(this.getResources().getDrawable(R.drawable.letter_a));
        	AIconOverlay.addOverlay(new OverlayItem(pts[0], "Title", "Snippet"));
        	MyItemizedOverlay BIconOverlay = new MyItemizedOverlay(this.getResources().getDrawable(R.drawable.letter_b));
        	BIconOverlay.addOverlay(new OverlayItem(pts[pts.length - 1], "Title", "Snippet"));
        	map.getOverlays().add(AIconOverlay);
        	map.getOverlays().add(BIconOverlay);
        }
	}
	
	MyItemizedOverlay oldIdeal = null;
	MyItemizedOverlay oldActual = null;

	private class MapDynamicStuff implements Runnable {
		
		public void run() {
			//while (true) {
			
				// Calculate ideal location
			
				long currentTime = System.currentTimeMillis();
				GeoPoint pts[] = Control.getRouteGeoFixes();
				long times[] = Control.getRouteRelativeTimes();
				for (int i = 0; i < times.length; i++) {
					times[i] += Control.getFollowingStartTime();
				}
				int lastFix = -1;
				while (lastFix < times.length - 1 && times[lastFix + 1] <= currentTime) {
					lastFix++;
				}
				int calcLat = 0;
				int calcLng = 0;
				if (lastFix == -1) {
					calcLat = pts[0].getLatitudeE6();
					calcLng = pts[0].getLongitudeE6();
				} else if (lastFix == pts.length - 1) {
					calcLat = pts[pts.length - 1].getLatitudeE6();
					calcLng = pts[pts.length - 1].getLongitudeE6();
				} else {
					int pt1Lat = pts[lastFix].getLatitudeE6();
					int pt1Lng = pts[lastFix].getLongitudeE6();
					int pt2Lat = pts[lastFix + 1].getLatitudeE6();
					int pt2Lng = pts[lastFix + 1].getLongitudeE6();
					double t = (currentTime - times[lastFix]) * 1.0 / (times[lastFix + 1] - times[lastFix]);
					calcLat = (int) (t*pt2Lat + (1-t)*pt1Lat);
					calcLng = (int) (t*pt2Lng + (1-t)*pt1Lng);
				}
				Log.d("diag", "" + calcLat + " " + calcLng);
				
				// Display ideal location and actual location
				
				MyItemizedOverlay IdealPointOverlay = new MyItemizedOverlay(context.getResources().getDrawable(R.drawable.symbol_crosshair));
		    	IdealPointOverlay.addOverlay(new OverlayItem(new GeoPoint(calcLat, calcLng), "Title", "Snippet"));
		    	MyItemizedOverlay ActualPointOverlay = new MyItemizedOverlay(context.getResources().getDrawable(R.drawable.jogging2));
		    	ActualPointOverlay.addOverlay(new OverlayItem(Control.getLastGpsFix(), "Title", "Snippet"));
		    	if (oldIdeal != null) {
		    		map.getOverlays().remove(oldIdeal);
		    	}
		    	if (oldActual != null) {
		    		map.getOverlays().remove(oldActual);
		    	}
		    	map.getOverlays().add(IdealPointOverlay);
		    	oldIdeal = IdealPointOverlay;
		    	map.getOverlays().add(ActualPointOverlay);
		    	oldActual = ActualPointOverlay;
		    	try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	//mapUpdater.sendMessage(new Message());
			//}
		}
	}
}