package com.purpleparrots.beiroute;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	Hashtable<Integer, String> routeHash;
	Hashtable<Integer, String> alarmHash;
	Button top_button;
	ProgressBar mRealProgress,mIdealProgress;
	ListView routeLv, alarmLv;
	TextView routeText, alarmText;
	int mRealProgressStatus = 0;
	int mIdealProgressStatus = 0;
	private Handler handler;
	private Handler realProgressHandler, idealProgressHandler;
	LinearLayout whole_screen, dynamicLayout;
	long currentTimeDisplayed;
	
	public static final int REAL_PROGRESS = 0;
	public static final int IDEAL_PROGRESS = 1;
	int counter1 = 0;
	int counter2 = 0;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d("Dan's Log", "Created Main Activity");
    	Control.initialize();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        whole_screen = (LinearLayout)findViewById(R.id.home_parent);
        dynamicLayout = (LinearLayout)findViewById(R.id.dynamic_layout);
        routeLv = (ListView)findViewById(R.id.route_listview);
        alarmLv = (ListView)findViewById(R.id.alarm_listview);
        routeText = (TextView)findViewById(R.id.route_text);
        alarmText = (TextView)findViewById(R.id.alarm_text);
        realProgressHandler = new Handler();
        idealProgressHandler = new Handler();
    }
    
    @Override
    protected void onResume() {
    	Log.d("diag mainActivity", "resuming mainActivity");
    	super.onResume();
    	dynamicLayout.removeAllViews();
   
    	int followState = 0;
    	if (Control.getFollowingState() == Control.FOLLOWING) {
    		makeRealProgressText();
    		makeRealProgressBar();
    		makeIdealProgressText();
    		makeIdealProgressBar();
    		Log.d("diag mainActivity", "about to start progress bar thread");
    		runProgressBars();
    		Log.d("diag mainActivity", "should have started thread by now");
    	}
    	else {
    		makeTopButton();
    	}
        routeHash = Control.getRoutes();
        //routeHash = dummyRouteHash();
        alarmHash = Control.getAlarms();
        //alarmHash = dummyAlarmHash();
        //makeTopText();
        fillRoutes();
        //makeMiddleText();
        fillAlarms();
    	
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
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
    		routeText.setText("You have no saved routes");
    		/*LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
  				  LinearLayout.LayoutParams.MATCH_PARENT,
  				  LinearLayout.LayoutParams.WRAP_CONTENT );
    		TextView tv = new TextView(this);
    		tv.setText("You have no saved routes");
    		routeLayout.addView(tv, p);*/
    	}
    	else {
    		routeText.setText("Saved routes:");
    		routeLv.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
    				Collections.list(routeHash.elements())));
    		/*
    		//Log.d("diag", routeHash.keySet().toString());    		
    		Enumeration<Integer> keys = routeHash.keys();
    		while(keys.hasMoreElements()) {
    			  int routeId = keys.nextElement();
    			  //Log.d("diag", routeHash.get(routeId).toString());
    			  
       			  LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
    					  LinearLayout.LayoutParams.MATCH_PARENT,
    					  LinearLayout.LayoutParams.WRAP_CONTENT );
       			  
       			  Button buttonView = new Button(this);
       			  buttonView.setText(routeHash.get(routeId));
    			  buttonView.setId(routeId);
       			  buttonView.setOnClickListener(this);
       			  routeLayout.addView(buttonView, p);
    		} */
    	}
    }
    
    public void fillAlarms() {
    	LinearLayout alarmLayout = (LinearLayout)findViewById(R.id.home_parent);
    	if (alarmHash.size() == 0) {
    		alarmText.setText("You have no saved alerts");
    		
    		/*LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
    				  LinearLayout.LayoutParams.MATCH_PARENT,
    				  LinearLayout.LayoutParams.WRAP_CONTENT );
      		TextView tv = new TextView(this);
      		tv.setText("You have no saved alarms");
      		alarmLayout.addView(tv, p);*/
    	}
    	else {
    		alarmText.setText("Saved alerts:");
    		alarmLv.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
    				Collections.list(alarmHash.elements())));
    		
    		Enumeration<Integer> keys = alarmHash.keys();
    		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
					  LinearLayout.LayoutParams.MATCH_PARENT,
					  LinearLayout.LayoutParams.WRAP_CONTENT );
    		while(keys.hasMoreElements()) {
    			  int alarmId = keys.nextElement();
    			  Button buttonView = new Button(this);
    			  buttonView.setId(alarmId);
       			  buttonView.setText(alarmHash.get(alarmId));	  
       			  buttonView.setOnClickListener(this);
       			  alarmLayout.addView(buttonView, p);
    		}
    	}
    }
    
    public void onClick(View v) {
    	if (routeHash.containsKey(v.getId())) {
    		Intent i = new Intent(this, RouteDetailActivity.class);
    		Control.setWorkingRoute(v.getId());
    		startActivity(i);
    	}
    	else if (alarmHash.containsKey(v.getId())) {
    		Intent i = new Intent(this, AlarmDetailActivity.class);
    		Control.setWorkingAlarm(v.getId());
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
    	LinearLayout layout = (LinearLayout)findViewById(R.id.dynamic_layout);
    	LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
				  LinearLayout.LayoutParams.MATCH_PARENT,
				  LinearLayout.LayoutParams.WRAP_CONTENT );
    	Button b = new Button(this);
    	layout.addView(b, p);
    	top_button = b;
    	
    	Log.d("Dan's Log", "Got to switch case");
        switch(Control.getNewRouteState()){
        case Control.NOT_YET_RECORDED:
        	top_button.setText("New Route");
        	break;
        case Control.RECORDING:
        	currentTimeDisplayed = Control.getElapsedTime();
        	updateTime();
        	handler = new Handler();
        	handler.removeCallbacks(updateTimeTask);
        	handler.postDelayed(updateTimeTask, 1000);
        	break;
        case Control.RECORDED:
        	top_button.setText("See Unsaved Route");
        	break;
        default:
        	break;
        }
        top_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent i = new Intent(MainActivity.this, NewRouteActivity.class);
            	//Control.createRoute();
            	//Log.d("jb", Control.getRouteName());
            	startActivity(i);
            }
        });
    }
    
    private void makeRealProgressText() {
    	LinearLayout layout = (LinearLayout)findViewById(R.id.dynamic_layout);
    	LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
				  LinearLayout.LayoutParams.MATCH_PARENT,
				  LinearLayout.LayoutParams.WRAP_CONTENT );
    	TextView tv = new TextView(this);
    	//tv.setText("Actual progress on " + Control.getRouteName());
    	tv.setText("Actual progress on HardcodedTestRoute");
    	tv.setTextSize(20);
    	layout.addView(tv, p);
    }
    
    private void makeIdealProgressText() {
    	LinearLayout layout = (LinearLayout)findViewById(R.id.dynamic_layout);
    	LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
				  LinearLayout.LayoutParams.MATCH_PARENT,
				  LinearLayout.LayoutParams.WRAP_CONTENT );
    	TextView tv = new TextView(this);
    	//tv.setText("Ideal progress on " + Control.getRouteName());
    	tv.setText("Ideal progress on HardcodedTestRoute");
    	tv.setTextSize(20);
    	layout.addView(tv, p);
    }
    
    private void makeRealProgressBar() {
    	LinearLayout layout = (LinearLayout)findViewById(R.id.dynamic_layout);
    	LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
				  LinearLayout.LayoutParams.MATCH_PARENT,
				  LinearLayout.LayoutParams.WRAP_CONTENT );
    	ProgressBar pb = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
    	layout.addView(pb, p);
    	mRealProgress = pb;
    }
    
    private void makeIdealProgressBar() {
    	LinearLayout layout = (LinearLayout)findViewById(R.id.dynamic_layout);
    	LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
				  LinearLayout.LayoutParams.MATCH_PARENT,
				  LinearLayout.LayoutParams.WRAP_CONTENT );
    	ProgressBar pb = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
    	layout.addView(pb, p);
    	mIdealProgress = pb;
    	
    	/*
    	// Start lengthy operation in a background thread
        new Thread(new Runnable() {
            public void run() {
                while (mIdealProgressStatus < 100) {
                	if (counter2 == 1000) {
                		mIdealProgressStatus += 1; //obviously change this to do something real
                		counter2 = 0;
                	}
                	else
                		counter2++;
                    //mIdealProgressStatus = 100*Control.getIdealProgress();
                    // Update the progress bar
                    idealProgressHandler.post(new Runnable() {
                        public void run() {
                            mIdealProgress.setProgress(mIdealProgressStatus);
                        }
                    });
                }
            }
        }).start();
        */
    }
    
    private void runProgressBars() {
    	new Thread(new Runnable() {
    		public void run() {
    			while (true) {
    				Log.d("diag mainActivity", "loooop");
    				mIdealProgressStatus = (int) (100 * Control.getIdealProgress());
    				mRealProgressStatus = (int) (100 * Control.getRealProgress());
    				mIdealProgress.setProgress(mIdealProgressStatus);
    				mRealProgress.setProgress(mRealProgressStatus);
    				try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    		}
    	});
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
    
    private Hashtable<Integer, String> dummyRouteHash() {
    	Hashtable<Integer,String> result = new Hashtable<Integer, String>();
    	for (int i = 0; i < 5; i++) {
    		result.put(1000 + i, "TestRoute # " + i);
    	}
    	return result;
    }
    
    private Hashtable<Integer, String> dummyAlarmHash() {
    	Hashtable<Integer,String> result = new Hashtable<Integer, String>();
    	for (int i = 0; i < 5; i++) {
    		result.put(2000 + i, "TestAlarm # " + i);
    	}
    	return result;
    }
    
}
