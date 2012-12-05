package com.teamgrau.altourism;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import com.google.android.maps.MapActivity;

/**
 * User: thomaseichinger
 * Date: 12/5/12
 * Time: 12:54 PM
 */
public class AltourismLocationListener implements LocationListener {
    private MainActivity mainActivity;
    public AltourismLocationListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onLocationChanged(Location location) {
        //mainActivity.zoomToMyLocation((int)location.getLatitude(), (int)location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
