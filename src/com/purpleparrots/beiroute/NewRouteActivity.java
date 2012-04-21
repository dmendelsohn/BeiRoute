package com.purpleparrots.beiroute;

//import java.text.SimpleDateFormat;
//import java.util.Date;
import java.util.Timer;
//import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NewRouteActivity extends Activity {
	//int route_ID;
	int state;
	EditText nameField, startField, endField;
	TextView timerDisplay;
	Button recordButton;
	long currentTimeDisplayed;
	Timer timer;
    private Handler handler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d("Dan's Log", "Created New Route Activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newroute);
    	state = 0;
    	timer = new Timer();
    	nameField = (EditText)findViewById(R.id.name_field);
    	startField = (EditText)findViewById(R.id.start_field);
    	endField = (EditText)findViewById(R.id.end_field);
    	recordButton = (Button)findViewById(R.id.record_button);
    	timerDisplay = (TextView)findViewById(R.id.newRouteTimerDisplay);

    	recordButton.setText("Begin Recording");
    	
    	//currentTimeDisplayed = Control.getElapsedTime();
    	currentTimeDisplayed = 0;
    	
    	updateTime();
    	handler = new Handler();

    	nameField.setText("Buffered Name");
    	startField.setText("Buffered Start");
    	endField.setText("Buffered End");
 	
    	
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	if (handler != null)
    		handler.removeCallbacks(updateTimeTask);
    	
    	String name = nameField.getText().toString();
		String startloc = startField.getText().toString();
		String endloc = endField.getText().toString();
    	Control.saveRoute(name, startloc, endloc);
		Log.d("Dan's Log", "Saving with (name, startloc, endloc) = ( "+name+", "+startloc+", "+endloc+")");
    }

    @Override
    protected void onResume() {
    	super.onResume();
    	//currentTimeDisplayed = Control.getElapsedTime();
    	//handler.removeCallbacks(updateTimeTask);
    	//handler.postDelayed(updateTimeTask, 1000);
    }

    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	if ( handler != null )
    	handler.removeCallbacks(updateTimeTask);
    	handler = null;	
    }
    
    
    public void onClick(View route) {
    	if (state == 0) {
        	recordButton.setText("Stop Recording");
    		state++;
    		Control.startRecording();
    		handler.removeCallbacks(updateTimeTask);
        	handler.postDelayed(updateTimeTask, 1000);

    	} else if (state == 1) {
        	recordButton.setText("Save Route");
    		state++;
    		Control.stopRecording();
            handler.removeCallbacks(updateTimeTask);
            handler = null;
    	} else {
    		String name = nameField.getText().toString();
    		String startloc = startField.getText().toString();
    		String endloc = endField.getText().toString();
    		Control.saveRoute(name, startloc, endloc);
    		Log.d("Dan's Log", "Saving with (name, startloc, endloc) = ( "+name+", "+startloc+", "+endloc+")");
    		Intent i = new Intent(this, RouteDetailActivity.class);
    		startActivity(i);
    	}

    }
    
    private Runnable updateTimeTask = new Runnable() {
    	public void run() {
        currentTimeDisplayed += 1000;
    	updateTime();
    	handler.postDelayed(this, 1000);
    	}
    };

    private void updateTime() {
    	timerDisplay.setText(Helper.millisToPaddedString(currentTimeDisplayed));
    }

}