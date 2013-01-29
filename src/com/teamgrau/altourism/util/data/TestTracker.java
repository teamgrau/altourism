package com.teamgrau.altourism.util.data;

import android.location.Location;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.LinkedList;
import java.util.List;

/**
 * User: thomaseichinger
 * Date: 1/8/13
 * Time: 1:30 PM
 */
public class TestTracker implements GPSTracker {
    @Override
    public void addLocation(Location position) {
        return;
    }

    @Override
    public List<Location> getLocations(int n) {
        return null;
    }

    @Override
    public List<Location> getLocations(LatLngBounds b) {
        final String THOMAS_LOCATION_PROVIDER = "Thomas LocationProvider";
        LinkedList<Location> ll = new LinkedList<Location>();
        Location l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.513609);
        l.setLongitude(13.392119);
        ll.add(l);
        l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.517396);
        l.setLongitude(52.517396);
        ll.add(l);
        l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.519772);
        l.setLongitude(13.398385);
        ll.add(l);
        l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.518806);
        l.setLongitude(13.400531);
        ll.add(l);
        l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.524159);
        l.setLongitude(13.402376);
        ll.add(l);
        l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.522201);
        l.setLongitude(13.412848);
        ll.add(l);
        return ll;
    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
