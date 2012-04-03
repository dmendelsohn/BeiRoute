package com.purpleparrots.beiroute;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class SetAlarmActivity extends Activity {
	EditText nameField, hourField, minuteField, monthField, dayField, yearField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.makealarm);
        
        nameField = (EditText)findViewById(R.id.alarm_set_name_field);
        hourField = (EditText)findViewById(R.id.alarm_set_hour_field);
        minuteField = (EditText)findViewById(R.id.alarm_set_minute_field);
        monthField = (EditText)findViewById(R.id.alarm_set_month_field);
        dayField = (EditText)findViewById(R.id.alarm_set_day_field);
        yearField = (EditText)findViewById(R.id.alarm_set_year_field);
    }
    
    public void onClick(View route) {
    	Log.d("Dan's Log", "Pushed set alarm button");
    	
        String name = nameField.getText().toString();
        int hour = Integer.parseInt(hourField.getText().toString());
        int minute = Integer.parseInt(minuteField.getText().toString());
        int month = Integer.parseInt(monthField.getText().toString());
        int day = Integer.parseInt(dayField.getText().toString());
        int year = Integer.parseInt(yearField.getText().toString());
        
        month -= 1;
        year -= 1900;

    	//Control.saveAlarm(name, year, month, day, hour, minute);
        Log.d("Dan's Log", "Saving alarm with data: " + name + ", " + hour + ", "+ minute + ", "
        		+ month + ", " + day + ", " + year);
        Intent i = new Intent(this, MainActivity.class);
    	startActivity(i);
    }
}