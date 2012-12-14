package com.teamgrau.altourism.util.data.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * POI models the data-object of an interesting point.
 * 
 * @author simon
 *
 */

public final class POI {
	
	String title;
	LatLng position;
	List<Geschichte> geschichten;
	
	
	public POI( String title, LatLng position ) {
		this.title = title;
		this.position = position;
		geschichten = new ArrayList<Geschichte>();
		
	}

}
