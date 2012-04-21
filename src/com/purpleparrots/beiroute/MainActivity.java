package com.purpleparrots.beiroute;

import java.util.Enumeration;
import java.util.Hashtable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	Hashtable<String, Integer> routeHash;
	Hashtable<String, Integer> alarmHash;
	Button top_button;
	private Handler handler;
	long currentTimeDisplayed;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d("Dan's Log", "Created Main Activity");
    	Control.initialize();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Log.d("Dan's Log", "Got to switch case");
        switch(Control.getNewRouteState()){
        case 0:
        	makeTopButton();
        	top_button.setText("New Route");
        	top_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	Intent i = new Intent(MainActivity.this, NewRouteActivity.class);
                	Control.createRoute();
                	Log.d("jb", Control.getRouteName());
                	startActivity(i);
                }
            });
        	break;
        case 1:
        	makeTopButton();
        	currentTimeDisplayed = Control.getElapsedTime();
        	
        	updateTime();
        	handler = new Handler();
        	handler.removeCallbacks(updateTimeTask);
        	handler.postDelayed(updateTimeTask, 1000);
        	
        	top_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	Intent i = new Intent(MainActivity.this, NewRouteActivity.class);
                	Control.createRoute();
                	Log.d("jb", Control.getRouteName());
                	startActivity(i);
                }
        	});
        	break;
        case 2:
        	break;
        default:
        	break;
        }
        routeHash = Control.getRoutes();
        alarmHash = Control.getAlarms();
        makeTopText();
        fillRoutes();
        makeMiddleText();
        fillAlarms();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	//currentTimeDisplayed = Control.getElapsedTime();
    	//currentTimeDisplayed = 18000;
    	handler.removeCallbacks(updateTimeTask);
    	handler.postDelayed(updateTimeTask, 1000);
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	if (handler != null)
    		handler.removeCallbacks(updateTimeTask);
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	if ( handler != null )
    	handler.removeCallbacks(updateTimeTask);
    	handler = null;	
    }
    
    
    public void fillRoutes() {
    	LinearLayout routeLayout = (LinearLayout)findViewById(R.id.home_parent);
    	if (routeHash.size() == 0) {
    		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
  				  LinearLayout.LayoutParams.MATCH_PARENT,
  				  LinearLayout.LayoutParams.WRAP_CONTENT );
    		TextView tv = new TextView(this);
    		tv.setText("You have no saved routes");
    		routeLayout.addView(tv, p);
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
    		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
    				  LinearLayout.LayoutParams.MATCH_PARENT,
    				  LinearLayout.LayoutParams.WRAP_CONTENT );
      		TextView tv = new TextView(this);
      		tv.setText("You have no saved alarms");
      		alarmLayout.addView(tv, p);
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
    		Control.setWorkingRoute(routeHash.get( ((Button) v).getText().toString()));
    		startActivity(i);
    	}
    	else if (alarmHash.containsKey( ((Button) v).getText().toString() ) ) {
    		Intent i = new Intent(this, AlarmDetailActivity.class);
    		Control.setWorkingAlarm(alarmHash.get( ((Button) v).getText().toString()));
    		startActivity(i);
    	}
    }
    
    /*public void newRouteClick(View v) {
    	Intent i = new Intent(this, NewRouteActivity.class);
    	Control.createRoute();
    	Log.d("jb", Control.getRouteName());
    	startActivity(i);
    }*/
    
    public void makeTopText() {
    	LinearLayout layout = (LinearLayout)findViewById(R.id.home_parent);
    	LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
				  LinearLayout.LayoutParams.MATCH_PARENT,
				  LinearLayout.LayoutParams.WRAP_CONTENT );
    	TextView tv = new TextView(this);
    	tv.setText("Saved Routes");
    	tv.setTextSize(20);
    	layout.addView(tv, p);
    }
    
    public void makeMiddleText() {
    	LinearLayout layout = (LinearLayout)findViewById(R.id.home_parent);
    	LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
				  LinearLayout.LayoutParams.MATCH_PARENT,
				  LinearLayout.LayoutParams.WRAP_CONTENT );
    	TextView tv = new TextView(this);
    	tv.setText("Saved alarms");
    	tv.setTextSize(20);
    	layout.addView(tv, p);
    }
    
    public void makeTopButton() {
    	LinearLayout layout = (LinearLayout)findViewById(R.id.home_parent);
    	LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
				  LinearLayout.LayoutParams.MATCH_PARENT,
				  LinearLayout.LayoutParams.WRAP_CONTENT );
    	Button b = new Button(this);
    	layout.addView(b, p);
    	top_button = b;
    }
    
    private Runnable updateTimeTask = new Runnable() {
    	public void run() {
        currentTimeDisplayed += 1000;
    	updateTime();
    	handler.postDelayed(this, 1000);
    	}
    };

    private void updateTime() {
    	top_button.setText("Currently recording  " + Helper.millisToPaddedString(currentTimeDisplayed));
    }
    

    
}