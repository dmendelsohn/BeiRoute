package com.purpleparrots.beiroute;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class AlarmDetailActivity extends Activity {
	//int route_ID;
	TextView name, route, hour, minute, month, day, year;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d("Dan's Log", "Created Alarm Detail Activity");
    	Log.d("Dan's Log", "database has month value stored as: " + Control.getAlarmMonth());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);
        name = (TextView)findViewById(R.id.alarm_detail_name_field);
        route = (TextView)findViewById(R.id.alarm_detail_route_field);
        hour = (TextView)findViewById(R.id.alarm_detail_hour_field);
        minute = (TextView)findViewById(R.id.alarm_detail_minute_field);
        month = (TextView)findViewById(R.id.alarm_detail_month_field);
        day = (TextView)findViewById(R.id.alarm_detail_day_field);
        year = (TextView)findViewById(R.id.alarm_detail_year_field);
        
        name.setText(Control.getAlarmName());
        //name.setText("*wake up*");
        route.setText(Control.getAlarmRouteName());
        //route.setText("AM Commute");
        hour.setText("" + Control.getAlarmHour());
        //hour.setText("9");
        minute.setText("" + Control.getAlarmMinute());
        //minute.setText("30");
        month.setText(((Integer)(Control.getAlarmMonth() + 1)).toString());
        //month.setText("3");
        day.setText(((Integer)(Control.getAlarmDay())).toString());
        //day.setText("28");
        year.setText(((Integer)(Control.getAlarmYear())).toString());
        //year.setText("2012");
    }
    
    public void onDelete(View alarm) {
    	Log.d("Dan's Log", "Deleted the alarm");
    	Control.deleteAlarm();
    	Intent i = new Intent(this, MainActivity.class);
    	startActivity(i);    
    }
    
    public void homeClick(View v) {
    	Log.d("Dan's Log", "Going home from Alarm Detail");
    	Intent i = new Intent(this, MainActivity.class);
    	startActivity(i);
    }
}