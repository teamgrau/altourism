package com.teamgrau.altourism.util.data;

import android.location.Location;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

/**
 * Interface for a GPSTracker storing and returning GPS-Positions
 *
 * @author Simon
 */

public interface GPSTracker {

    // stores the given position
    void addLocation(Location position);

    // returns the last n saved positions starting with the very last position recorded
    List<Location> getLocations(int n);

    List<Location> getLocations(LatLngBounds b);
}
