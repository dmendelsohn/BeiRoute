package com.purpleparrots.beiroute;

import java.util.GregorianCalendar;

public class Alarm {
	
	private String name;
	private GregorianCalendar time;
	
	public Alarm(String name, GregorianCalendar time) {
		this.name = name;
		this.time = time;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public GregorianCalendar getTime() {
		return time;
	}
	public void setTime(GregorianCalendar time) {
		this.time = time;
	}
}
