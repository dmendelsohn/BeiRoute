package com.purpleparrots.beiroute;

public class Helper {
	public static String millisToPaddedString(long millis){
		long hours = millis/(3600*1000);
    	long minutes = (millis%(3600*1000)/(60*1000));
    	long seconds = (millis)%(60*1000)/1000;
    	return String.valueOf(hours) + ":" + pad(minutes) + ":" + pad(seconds);
    	
	}
	
	public static String pad(long number){
    	if (number >= 10) {
    		return String.valueOf(number);
    	}
    	else {
    		return "0" + String.valueOf(number);
    	}
    }
}
