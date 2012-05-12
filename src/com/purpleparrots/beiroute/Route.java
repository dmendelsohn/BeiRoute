package com.purpleparrots.beiroute;

import java.util.Date;
import java.util.LinkedList;

import com.google.android.maps.GeoPoint;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Route extends AndroidSerializable {
	
	private String name;
	private String startLoc;
	private String endLoc;
	public long startTime; // should really be private
	public long duration; // should really be private
	private LinkedList<Location> nodes;
	private GeoPoint mapCenter;
	
	public Route() {
		name = "New Route";
		startTime = 0;
		mapCenter = new GeoPoint(42355000, -71090000);
		nodes = new LinkedList<Location>();
	}
	
	public void addLocFix(Location loc) {
		nodes.add(loc);
		Log.d("diag", "size of linked list: " + nodes.size());
		updateDuration();
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
	public void updateDuration() {
		duration = new Date().getTime() - startTime;
	}
	
	public GeoPoint getMapCenter() {
		return mapCenter;
	}
}
