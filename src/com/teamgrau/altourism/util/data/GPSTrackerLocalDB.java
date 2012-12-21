package com.teamgrau.altourism.util.data;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Stores and retrieves GPS-locations in a local database
 *
 * User: simon
 */
public class GPSTrackerLocalDB implements GPSTracker{
    @Override
    public void addLocation(LatLng position) {

    }

    @Override
    public List<LatLng> getLocations(int n) {
        return null;
    }
}
