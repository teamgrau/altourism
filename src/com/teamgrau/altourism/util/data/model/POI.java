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

    public LatLng getPosition() {
        return position;
    }

    public String getTitle() {
        return title;
    }

    LatLng position;
	List<Story> geschichten;
	
	
	public POI( String title, LatLng position ) {
		this.title = title;
		this.position = position;
		geschichten = new ArrayList<Story>();
	}

}
