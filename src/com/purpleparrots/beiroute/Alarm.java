package com.purpleparrots.beiroute;

import java.util.GregorianCalendar;

public class Alarm {
	
	private String name;
	private Route route;
	private GregorianCalendar time;
	private AlarmService as;
	
	public Alarm(String name, Route route, GregorianCalendar time, AlarmService as) {
		this.name = name;
		this.route = route;
		this.time = time;
		this.setAs(as);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Route getRoute() {
		return route;
	}
	public void setRoute(Route route) {
		this.route = route;
	}
	public GregorianCalendar getTime() {
		return time;
	}
	public void setTime(GregorianCalendar time) {
		this.time = time;
	}
	public AlarmService getAs() {
		return as;
	}
	public void setAs(AlarmService as) {
		this.as = as;
	}
}
