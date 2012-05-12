package com.purpleparrots.beiroute;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MyItemizedOverlay extends ItemizedOverlay {

	private ArrayList<OverlayItem> myOverlays = new ArrayList<OverlayItem>();
	private Context myContext;
	
	public MyItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		// TODO Auto-generated constructor stub
	}
	
	// For handling touches on markers
	/*
	public MyItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		myContext = context;
	}
	*/
	
	public void addOverlay(OverlayItem overlay) {
		myOverlays.add(overlay);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return myOverlays.get(i);
	}
	
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return myOverlays.size();
	}
	
	public void addGeoPoints(GeoPoint[] pts) {
		for (int i = 0; i < pts.length; i++) {
			addOverlay(new OverlayItem(pts[i], "Title", "Snippet"));
		}
	}
	
	// For handling touches on markers
	/*
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = myOverlays.get(index);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(myContext);
	  dialog.setTitle(item.getTitle());
	  dialog.setMessage(item.getSnippet());
	  dialog.show();
	  return true;
	}
	*/

}
