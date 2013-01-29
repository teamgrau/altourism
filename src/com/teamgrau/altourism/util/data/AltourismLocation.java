package com.teamgrau.altourism.util.data;

import android.location.Location;
import com.google.android.gms.maps.model.LatLng;

/**
 * User: thomaseichinger
 * Date: 1/22/13
 * Time: 11:18 AM
 */
public class AltourismLocation extends Location {
    AltourismLocation(String provider, double lat, double lng) {
        super(provider);
        super.setLatitude(lat);
        super.setLongitude(lng);
    }

    public LatLng toLatLng() {
        return new LatLng(super.getLatitude(), super.getLongitude());
    }
}
