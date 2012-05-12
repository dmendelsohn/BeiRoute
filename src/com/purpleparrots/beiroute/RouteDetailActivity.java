package com.purpleparrots.beiroute;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class RouteDetailActivity extends MapActivity {
	TextView name, start, end, duration;
	MapView map;
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
        
        MapController mc = map.getController();
        mc.setCenter(Control.getRouteMapCenter());
        mc.setZoom(15);
        
        GeoPoint pts[] = Control.getRouteLocFixes();
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
}