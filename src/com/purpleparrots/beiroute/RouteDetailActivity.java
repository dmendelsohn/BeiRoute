package com.purpleparrots.beiroute;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class RouteDetailActivity extends Activity {
	TextView name, start, end, duration;
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
        
        name.setText(Control.getRouteName());
        //name.setText("Morning Commute");
        start.setText(Control.getRouteStartLoc());
        //start.setText("Baker");
        end.setText(Control.getRouteEndLoc());
        //end.setText("Stata");
        String stringDuration = longToString(Control.getRouteDuration());
        //String stringDuration = longToString(750000);
        duration.setText(stringDuration);
    }
    
    public void onSet(View route) {
    	Log.d("Dan's Log", "Pushed set alarm button");
    	Intent i = new Intent(this, SetAlarmActivity.class);
    	startActivity(i);
    }
    
    public void onDelete(View route) {
    	Log.d("Dan's Log", "Deleted the route");
    	//Control.deleteRoute();
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
}