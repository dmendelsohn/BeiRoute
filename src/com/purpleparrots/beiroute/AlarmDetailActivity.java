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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);
        name = (TextView)findViewById(R.id.alarm_detail_name_field);
        route = (TextView)findViewById(R.id.alarm_detail_route_field);
        hour = (TextView)findViewById(R.id.alarm_detail_hour_field);
        minute = (TextView)findViewById(R.id.alarm_detail_minute_field);
        month = (TextView)findViewById(R.id.alarm_detail_month_field);
        day = (TextView)findViewById(R.id.alarm_detail_day_field);
        year = (TextView)findViewById(R.id.alarm_detail_year_field);
        
        //name.setText(Control.getAlarmName());
        name.setText("*wake up*");
        //route.setText(Control.getAlarmRouteName());
        route.setText("AM Commute");
        //hour.setText((String)(Control.getAlarmHour()));
        hour.setText("9");
        //minute.setText((String)(Control.getAlarmMinute()));
        minute.setText("30");
        //month.setText((String)(Control.getAlarmMonth() + 1));
        month.setText("3");
        //day.setText((String)(Control.getAlarmDay()));
        day.setText("28");
        //year.setText((String)(Control.getAlarmYear() + 1900));
        year.setText("2012"); 
    }
    
    public void onDelete(View alarm) {
    	Log.d("Dan's Log", "Deleted the alarm");
    	//Control.deleteAlarm();
    	Intent i = new Intent(this, MainActivity.class);
    	startActivity(i);    	
    }
}