package com.purpleparrots.beiroute;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewRouteActivity extends Activity {
	//int route_ID;
	int state;
	EditText nameField, startField, endField;
	Button recordButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d("Dan's Log", "Created New Route Activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newroute);
    	state = 0;
    	nameField = (EditText)findViewById(R.id.name_field);
    	startField = (EditText)findViewById(R.id.start_field);
    	endField = (EditText)findViewById(R.id.end_field);
    	recordButton = (Button)findViewById(R.id.record_button);
    	/*if (recordButton == null)
    		Log.d("Dan's Log", "button was null");*/
    	recordButton.setText("Begin Recording");
    }
    
    public void onClick(View route) {
    	Log.d("Dan's Log", "Pushed record/stop/save button");
    	if (state == 0) {
        	recordButton.setText("Stop Recording");
    		state++;
    		//Control.startRecording();
    	} else if (state == 1) {
        	recordButton.setText("Save Route");
    		state++;
    		//Control.stopRecording();
    	} else {
    		String name = nameField.getText().toString();
    		String startloc = startField.getText().toString();
    		String endloc = endField.getText().toString();
    		//Control.saveRoute(name, startloc, endloc);
    		Log.d("Dan's Log", "Saving with (name, startloc, endloc) = ( "+name+", "+startloc+", "+endloc+")");
    		Intent i = new Intent(this, RouteDetailActivity.class);
    		startActivity(i);
    	}

    }

}