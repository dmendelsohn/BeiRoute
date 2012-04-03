package com.purpleparrots.beiroute;

import java.util.Enumeration;
import java.util.Hashtable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	Hashtable<String, Integer> routeHash;
	Hashtable<String, Integer> alarmHash;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //routeHash = Control.getRoutes();
        //alarmHash = Control.getHash();
        routeHash = new Hashtable<String,Integer>();
        routeHash.put("Morning commute1", 12345);
        routeHash.put("Morning commute2", 12345);
        routeHash.put("Morning commute3", 12345);
        routeHash.put("Morning commute4", 12345);
        //routeHash.put("Morning commute5", 12345);
        //routeHash.put("Morning commute6", 12345);
        //routeHash.put("Morning commute7", 12345);
        //routeHash.put("Morning commute8", 12345);
        //routeHash.put("Morning commute9", 12345);
        alarmHash = new Hashtable<String,Integer>();
        alarmHash.put("Wake up alarm1", 54321);
        alarmHash.put("Wake up alarm2", 54321);
        //alarmHash.put("Wake up alarm3", 54321);
        //alarmHash.put("Wake up alarm4", 54321);
        //alarmHash.put("Wake up alarm5", 54321);
        //alarmHash.put("Wake up alarm6", 54321);
        //alarmHash.put("Wake up alarm7", 54321);
        fillRoutes();
        makeMiddleText();
        fillAlarms();
    }
    
    public void fillRoutes() {
    	LinearLayout routeLayout = (LinearLayout)findViewById(R.id.home_parent);
    	if (routeHash.size() == 0) {
    	}
    	else {
    		Enumeration<String> keys = routeHash.keys();
    		while(keys.hasMoreElements()) {
    			  String name = keys.nextElement();
    			  
       			  LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
    					  LinearLayout.LayoutParams.MATCH_PARENT,
    					  LinearLayout.LayoutParams.WRAP_CONTENT );
       			  
       			  Button buttonView = new Button(this);
       			  buttonView.setText(name);
     					  
       			  buttonView.setOnClickListener(this);
       			  routeLayout.addView(buttonView, p);
    		}
    	}
    }
    
    public void fillAlarms() {
    	LinearLayout alarmLayout = (LinearLayout)findViewById(R.id.home_parent);
    	if (alarmHash.size() == 0) {
    	}
    	else {
    		Enumeration<String> keys = alarmHash.keys();
    		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
					  LinearLayout.LayoutParams.MATCH_PARENT,
					  LinearLayout.LayoutParams.WRAP_CONTENT );
    		while(keys.hasMoreElements()) {
    			  String name = keys.nextElement();
    			  Button buttonView = new Button(this);
       			  buttonView.setText(name);	  
       			  buttonView.setOnClickListener(this);
       			  alarmLayout.addView(buttonView, p);
    		}
    	}
    }
    
    public void onClick(View v) {
    	if (routeHash.containsKey( ((Button) v).getText().toString() ) ) {
    		Intent i = new Intent(this, RouteDetailActivity.class);
    		//Control.setAlarm(routeHash.get( ((Button) v).getText().toString()));
    		startActivity(i);
    	}
    	else if (alarmHash.containsKey( ((Button) v).getText().toString() ) ) {
    		Intent i = new Intent(this, AlarmDetailActivity.class);
    		//Control.setAlarm(alarmHash.get( ((Button) v).getText().toString()));
    		startActivity(i);
    	}
    }
    
    public void newRouteClick(View v) {
    	Intent i = new Intent(this, NewRouteActivity.class);
    	//Control.createRoute();
    	startActivity(i);
    }
    
    public void makeMiddleText() {
    	LinearLayout layout = (LinearLayout)findViewById(R.id.home_parent);
    	LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
				  LinearLayout.LayoutParams.MATCH_PARENT,
				  LinearLayout.LayoutParams.WRAP_CONTENT );
    	TextView tv = new TextView(this);
    	tv.setText("Saved alarms");
    	layout.addView(tv, p);
    }
    
}