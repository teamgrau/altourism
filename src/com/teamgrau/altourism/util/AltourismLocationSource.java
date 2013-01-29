package com.teamgrau.altourism.util;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.google.android.gms.maps.LocationSource;

import java.util.LinkedList;
import java.util.List;

/**
 * User: thomaseichinger
 * Date: 1/16/13
 * Time: 3:21 PM
 */
public class AltourismLocationSource implements LocationSource, LocationListener {
    private final List<OnLocationChangedListener> mListeners;
    private final LocationManager mLocationManager;
    private final String mProvider;
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private Location mLastLocation;

    public AltourismLocationSource(Context ctx) {
        mListeners = new LinkedList<OnLocationChangedListener>();

        // Get the location manager
        mLocationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use default
        Criteria criteria = new Criteria();
        mProvider = mLocationManager.getBestProvider(criteria, true);
        mLocationManager.requestLocationUpdates(mProvider, 400, 1, this);
        mLastLocation = mLocationManager.getLastKnownLocation(mProvider);

        for (OnLocationChangedListener l : mListeners) {
            l.onLocationChanged(mLastLocation);
        }
    }

    public void stop() {
        mLocationManager.removeUpdates(this);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListeners.add(onLocationChangedListener);
    }

    @Override
    public void deactivate() {
        for (OnLocationChangedListener l : mListeners) {
            mListeners.remove(l);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (!isBetterLocation(location, mLastLocation)) return;

        for (OnLocationChangedListener l : mListeners) {
            l.onLocationChanged(location);
        }

        mLastLocation = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onProviderEnabled(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onProviderDisabled(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Determines whether one Location reading is better than the current Location fix
     *
     * @param location            The new Location that you want to evaluate
     * @param currentBestLocation The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
}
