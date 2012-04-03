package com.purpleparrots.beiroute;

import java.util.Date;
import java.util.LinkedList;

import android.location.Location;

public class Route {
	
	private String name;
	private String startLoc;
	private String endLoc;
	private long startTime;
	private long duration;
	private LinkedList<Location> nodes;
	
	public Route() {
		setStartTime();
	}
	
	public void addLocFix(Location loc) {
		nodes.add(loc);
		setDuration();
	}
	public LinkedList<Location> getLocFixes() {
		return nodes;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getStartLoc() {
		return startLoc;
	}
	public void setStartLoc(String startLoc) {
		this.startLoc = startLoc;
	}
	
	public String getEndLoc() {
		return endLoc;
	}
	public void setEndLoc(String endLoc) {
		this.endLoc = endLoc;
	}
	
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime() {
		startTime = new Date().getTime();
	}
	
	public long getDuration() {
		return duration;
	}
	public void setDuration() {
		duration = new Date().getTime() - startTime;
	}
	
	
}
