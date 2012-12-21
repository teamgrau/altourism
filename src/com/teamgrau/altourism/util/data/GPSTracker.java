package com.teamgrau.altourism.util.data;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Interface for a GPSTracker storing and returning GPS-Positions
 *
 * @author Simon
 */

public interface GPSTracker {

    // stores the given position
    void addLocation(LatLng position);

    // returns the last n saved positions
    List<LatLng> getLocations(int n);
}
