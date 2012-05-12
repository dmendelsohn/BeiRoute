package com.purpleparrots.beiroute;

//import java.util.Calendar;
import android.app.Activity;
import android.app.Dialog;
//import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
//import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.DatePicker;
//import android.app.DatePickerDialog;

public class SetAlarmActivity extends Activity {
	EditText nameField;
	
	/*private TextView mTimeDisplay;
    private Button mPickTime;
    private int mHour;
    private int mMinute;

    private TextView mDateDisplay;
    private Button mPickDate;
    private int mYear;
    private int mMonth;
    private int mDay;*/
    
    TimePicker timePicker;
    DatePicker datePicker;
    TextView topText;
    
    static final int REPEAT_DIALOG_ID = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.makealarm);
        
        topText = (TextView)findViewById(R.id.set_alarm_top_text);
        nameField = (EditText)findViewById(R.id.alarm_set_name_field);
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        datePicker = (DatePicker)findViewById(R.id.datePicker);
        
        String routeName = Control.getRouteName();
        topText.setText("Set Alert for : " + routeName);
        
        /*final Calendar c1 = Calendar.getInstance();
        mHour = c1.get(Calendar.HOUR_OF_DAY);
        mMinute = c1.get(Calendar.MINUTE);
        
        final Calendar c2 = Calendar.getInstance();
        mYear = c2.get(Calendar.YEAR);
        mMonth = c2.get(Calendar.MONTH);
        mDay = c2.get(Calendar.DAY_OF_MONTH);*/


        // display the current date (this method is below)
        //updateTimeDisplay();
        //updateDateDisplay();
    }
    
 /*// updates the time we display in the TextView
    private void updateTimeDisplay() {
        mTimeDisplay.setText(
            new StringBuilder()
                    .append(pad(mHour)).append(":")
                    .append(pad(mMinute)));
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
    
 // updates the date in the TextView
    private void updateDateDisplay() {
        mDateDisplay.setText(
            new StringBuilder()
                    // Month is 0 based so add 1
                    .append(mMonth + 1).append("-")
                    .append(mDay).append("-")
                    .append(mYear).append(" "));
    }*/
    
    /*private TimePickerDialog.OnTimeSetListener mTimeSetListener = 
    		new TimePickerDialog.OnTimeSetListener() {
    	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    		mHour = hourOfDay;
	        mMinute = minute;
		    updateTimeDisplay();
    	}
    	};
    
    	// the callback received when the user "sets" the date in the dialog
        private DatePickerDialog.OnDateSetListener mDateSetListener =
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, 
                                          int monthOfYear, int dayOfMonth) {
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                        updateDateDisplay();
                    }
                };
		        */
    
   @Override
   protected Dialog onCreateDialog(int id) {
	   switch (id) {
	   /*case TIME_DIALOG_ID:
		   	return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false);
	   case DATE_DIALOG_ID:
   			return new DatePickerDialog(this,
   					mDateSetListener,
   					mYear, mMonth, mDay);*/
	   case REPEAT_DIALOG_ID:
		   // what do?
	   }
	   return null;
   }
    
    public void onClick(View route) {
    	Log.d("Dan's Log", "Pushed set alarm button");
    	
        String name = nameField.getText().toString();
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
    	Control.saveAlarm(name, year-1900, month, day, hour, minute);
        Log.d("Dan's Log", "Saving alarm with data: " + name + ", " + hour + ", "+ minute + ", "
        		+ month + ", " + day + ", " + (year-1900));
        Intent i = new Intent(this, MainActivity.class);
    	startActivity(i);
    }
}