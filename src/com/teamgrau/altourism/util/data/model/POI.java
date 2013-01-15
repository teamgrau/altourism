package com.teamgrau.altourism.util.data.model;

import android.location.Location;

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

    public Location getPosition() {
        return position;
    }

    public String getTitle() {
        return title;
    }

    Location position;
	List<Story> geschichten;
	
	
	public POI( String title, Location position ) {
		this.title = title;
		this.position = position;
		geschichten = new ArrayList<Story>();
	}

    public void addStory(Story story){
        geschichten.add(story);
    }

}
