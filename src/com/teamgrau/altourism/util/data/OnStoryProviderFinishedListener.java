package com.teamgrau.altourism.util.data;

import com.teamgrau.altourism.util.data.model.POI;

import java.util.List;

/**
 * User: thomaseichinger
 * Date: 1/21/13
 * Time: 11:37 AM
 */
public interface OnStoryProviderFinishedListener {
    void onStoryProviderFinished ( List<POI> pois );
}
