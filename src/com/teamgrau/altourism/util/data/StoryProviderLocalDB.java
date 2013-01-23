package com.teamgrau.altourism.util.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import com.teamgrau.altourism.util.data.OnStoryProviderFinishedListener;
import com.teamgrau.altourism.util.data.StoryProvider;
import com.teamgrau.altourism.util.data.database.AltourismDBHelper;
import com.teamgrau.altourism.util.data.database.DBDefinition;
import com.teamgrau.altourism.util.data.model.POI;
import com.teamgrau.altourism.util.data.model.Story;

import java.util.ArrayList;
import java.util.List;

/**
 * User: simon
 */
public class StoryProviderLocalDB implements StoryProvider {

    AltourismDBHelper AlDBHelper;

    // define the columns to return on a POI Query
    String[] POIProjection = {
        DBDefinition.POI.COLUMN_NAME_Geschichte
    };


    public StoryProviderLocalDB(Context context){
        AlDBHelper = new AltourismDBHelper( context );
    }

    @Override
    // Does currently not take radius into account but returns all POI that are stored
    public List<POI> listPOIs( Location position, double radius) {
        SQLiteDatabase db = AlDBHelper.getReadableDatabase();
        String sortOrder = DBDefinition.PositionEntry._ID + " ASC";
        Cursor c = db.query(
                DBDefinition.POI.TABLE_NAME,    // The table to query
                POIProjection,                  // The columns to return
                null,                                // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder,                                // The sort order
                null                                      // only return the n last points
        );

        c.moveToFirst();
        List<POI> list = new ArrayList<POI>();
        int n = c.getCount();
        String text;
        POI poi;
        Location l;
        double Lat;
        double Lng;
        for( int i = 1; i <= n; ++i ){
            Lat = c.getDouble( c.getColumnIndex( DBDefinition.POI.COLUMN_NAME_Lat ));
            Lng = c.getDouble( c.getColumnIndex( DBDefinition.POI.COLUMN_NAME_Lng ));
            text = c.getString( c.getColumnIndex( DBDefinition.POI.COLUMN_NAME_Geschichte ));
            l = new Location( "Thomas LocationProvider" );
            l.setLatitude( Lat );
            l.setLongitude( Lng );
            poi = new POI( "test Title", l);
            poi.addStory( new Story( text ));
            list.add(poi);
        }
        c.close();
        db.close();
        return list;
    }



    @Override
    public POI getPOI( Location position) {
        SQLiteDatabase db = AlDBHelper.getReadableDatabase();
        String sortOrder = DBDefinition.PositionEntry._ID + " ASC";
        String selection =
                DBDefinition.POI.COLUMN_NAME_Lat + " = " + position.getLatitude() + " AND " +
                DBDefinition.POI.COLUMN_NAME_Lng + " = " + position.getLongitude() ;

        Cursor c = db.query(
                DBDefinition.POI.TABLE_NAME,    // The table to query
                POIProjection,                  // The columns to return
                selection,                                // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder,                                // The sort order
                null                                      // only return the n last points
        );

        c.moveToFirst();
        POI poi = new POI("test Title", position);
        int n = c.getCount();
        String text;
        for( int i = 1; i <= n; ++i ){
            poi.addStory( new Story( c.getString( c.getColumnIndex( DBDefinition.POI.COLUMN_NAME_Geschichte ))));
            c.moveToNext();       // we can move 1 past the last entry w/o negative effects
        }
        c.close();
        db.close();
        return poi;
    }


    @Override
    public void listPOIs( Location position, double radius, OnStoryProviderFinishedListener l) {
        return;
    }
}