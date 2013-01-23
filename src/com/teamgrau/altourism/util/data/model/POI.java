package com.teamgrau.altourism.util.data.model;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * POI models the data-object of an interesting point.
 * A POI is uniquely identified by its coordinates.
 * 
 * @author simon
 *
 */

public final class POI {
	
	private String title;

    public Location getPosition() {
        return position;
    }

    public String getTitle() {
        return title;
    }

    private Location position;
	private List<Story> geschichten;
	
	
	public POI( String title, Location position ) {
		this.title = title;
		this.position = position;
		geschichten = new ArrayList<Story>();
	}

    public void addStory(Story story){
        geschichten.add(story);
    }

    public List<Story> getStories(int n){
        return geschichten.subList(0, n);
    }

    public List<Story> getStories( ){
        return geschichten.subList(0, geschichten.size());
    }

}
