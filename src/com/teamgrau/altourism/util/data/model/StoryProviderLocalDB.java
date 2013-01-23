package com.teamgrau.altourism.util.data.model;

import android.content.Context;
import android.location.Location;
import com.teamgrau.altourism.util.data.OnStoryProviderFinishedListener;
import com.teamgrau.altourism.util.data.StoryProvider;
import com.teamgrau.altourism.util.data.database.AltourismDBHelper;
import com.teamgrau.altourism.util.data.database.DBDefinition;

import java.util.List;

/**
 * User: simon
 */
public class StoryProviderLocalDB implements StoryProvider {

    AltourismDBHelper AlDBHelper;

    // define the columns to return on a POI Query
    String[] POIProjection = {
            DBDefinition.POI.COLUMN_NAME_Lat,
            DBDefinition.POI.COLUMN_NAME_Lng,
            DBDefinition.POI.COLUMN_NAME_Geschichte
    };


    public StoryProviderLocalDB(Context context){
        AlDBHelper = new AltourismDBHelper( context );
    }

    @Override
    public List<POI> listPOIs( Location position, double radius) {
        return null;
    }

    @Override
    public void listPOIs( Location position, double radius, OnStoryProviderFinishedListener l) {
    }

    @Override
    public POI getPOI( Location position) {
        return null;
    }
}
